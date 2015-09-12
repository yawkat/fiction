package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.*;
import at.yawk.fiction.impl.PageParser;
import at.yawk.fiction.impl.PageParserProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author yawkat
 */
@RequiredArgsConstructor
public class FimFictionProvider implements FictionProvider {
    private final PageParserProvider pageParserProvider;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Authentication to use for auto login. HttpClient must save cookies for this to work!
     */
    @Setter @Nullable
    private FimAuthentication defaultAuthentication = null;

    /**
     * Login once, without saving credentials. HttpClient must save cookies for this to work!
     */
    public void loginOnce(String username, String password)
            throws IOException, FimAuthenticationException {
        loginOnce(new FimAuthentication(username, password));
    }

    /**
     * Login once, without saving credentials. HttpClient must save cookies for this to work!
     */
    public void loginOnce(FimAuthentication authentication)
            throws IOException, FimAuthenticationException {
        HttpPost request = new HttpPost("https://www.fimfiction.net/ajax/login.php");
        request.setEntity(new UrlEncodedFormEntity(Arrays.asList(
                new BasicNameValuePair("username", authentication.getUsername()),
                new BasicNameValuePair("password", authentication.getPassword()),
                new BasicNameValuePair("secure", "true"),
                new BasicNameValuePair("keep_logged_in", authentication.isRemember() ? "1" : "0"),
                new BasicNameValuePair("otp", authentication.getOtp())
        )));

        HttpResponse response = httpClient.execute(request);
        JsonNode responseNode = objectMapper.readTree(EntityUtils.toByteArray(response.getEntity()));
        if (responseNode.has("error")) {
            throw new FimAuthenticationException(responseNode.get("error").toString());
        }
    }

    public List<FimTag> fetchTags() throws Exception {
        PageParser<List<FimTag>>.RequestBuilder request = pageParserProvider.getParser(TagsParser.class)
                .request(httpClient)
                .get("https://www.fimfiction.net/stories");
        decorateHtmlRequest(request);
        return request.send();
    }

    /**
     * Get a list of all shelves of this user. Return value is undefined if the user isn't logged in.
     */
    public List<FimShelf> fetchShelves() throws Exception {
        PageParser<List<FimShelf>>.RequestBuilder request = pageParserProvider.getParser(ShelvesParser.class)
                .request(httpClient)
                .get("https://www.fimfiction.net/");
        decorateHtmlRequest(request);
        return request.send();
    }

    public void toggleRead(Chapter chapter) throws Exception {
        HttpPost request = new HttpPost("https://www.fimfiction.net/ajax/toggle_read.php");
        request.setEntity(new UrlEncodedFormEntity(Collections.singletonList(
                new BasicNameValuePair("chapter", String.valueOf(((FimChapter) chapter).getId()))
        )));

        HttpResponse response = httpClient.execute(request);
        JsonNode responseNode = objectMapper.readTree(EntityUtils.toByteArray(response.getEntity()));
        chapter.setRead(responseNode.get("read").asBoolean());
    }

    @Override
    public void fetchStory(Story story) throws Exception {
        PageParser.RequestBuilder request = pageParserProvider.getParser(StoryParser.class).request(httpClient)
                .get("https://www.fimfiction.net/story/" + ((FimStory) story).getId())
                .target((FimStory) story);
        decorateHtmlRequest(request);
        request.send();
    }

    @Override
    public void fetchChapter(Story story, Chapter chapter) throws Exception {
        pageParserProvider.getParser(ChapterParser.class).request(httpClient)
                .get("http://www.fimfiction.net/download_chapter.php?html&chapter=" + ((FimChapter) chapter).getId())
                .target((FimChapter) chapter).send();
    }

    @Override
    public FimSearchQuery createQuery() {
        return new FimSearchQuery();
    }

    @Override
    public Pageable<FimStory> search(SearchQuery query) {
        FimSearchQuery fim = (FimSearchQuery) query;
        return page -> {
            PageParser<Pageable.Page<FimStory>>.RequestBuilder request =
                    pageParserProvider.getParser(SearchPageParser.class).request(httpClient)
                    .get(fim.build(page));
            decorateHtmlRequest(request);
            Pageable.Page<FimStory> p = request.send();
            p.setLast(p.getEntries().isEmpty());
            return p;
        };
    }

    private void decorateHtmlRequest(PageParser.RequestBuilder request) throws Exception {
        request.validator((requestBuilder, root) -> {
            FimAuthentication defaultAuthentication = this.defaultAuthentication;

            // confirm that we are logged in
            if (defaultAuthentication != null &&
                root.getElementById("private-message-drop-down") == null) {

                loginOnce(defaultAuthentication);
                // resend request - we should have session cookies now
                return false;
            }
            return true;
        });
    }
}
