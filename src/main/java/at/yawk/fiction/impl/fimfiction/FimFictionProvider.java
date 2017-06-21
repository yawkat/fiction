package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.*;
import at.yawk.fiction.impl.PageParser;
import at.yawk.fiction.impl.PageParserProvider;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

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
     * Secret signing key set during login.
     */
    @Nullable
    private SecretKey signingKey = null;

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
        HttpPost request = new HttpPost("https://www.fimfiction.net/ajax/login");
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
        signingKey = new SecretKeySpec(parseBase64(responseNode.get("signing_key").asText()), "HmacSHA256");
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

    public void setRead(Chapter chapter, boolean read) throws Exception {
        String path = "/ajax/chapters/" + ((FimChapter) chapter).getId() + "/read";
        String url = "https://www.fimfiction.net" + path;
        HttpUriRequest request;
        if (read) {
            request = new HttpPost(url);
        } else {
            // HttpDelete does not support entities - so we do this hack
            request = new HttpEntityEnclosingRequestBase() {
                { setURI(URI.create(url)); }

                @Override
                public String getMethod() {
                    return "DELETE";
                }
            };
        }
        Signature signature = sign(path, "");
        String data = "&signature=" + URLEncoder.encode(signature.getHmac(), "UTF-8") +
                      "&signature_nonce=" + URLEncoder.encode(signature.getNonce(), "UTF-8") +
                      "&signature_timestamp=" + signature.getTime();
        ((HttpEntityEnclosingRequestBase) request).setEntity(new StringEntity(data,
                                                                              ContentType.APPLICATION_FORM_URLENCODED));

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
                .get("https://www.fimfiction.net/download_chapter.php?html&chapter=" + ((FimChapter) chapter).getId())
                .target((FimChapter) chapter).send();
    }

    /**
     * @return A map containing all shelves of this user as keys, and whether this story is part of those shelves as
     * values.
     */
    public Map<FimShelf, Boolean> fetchStoryShelves(Story story) throws Exception {
        String uri = "https://www.fimfiction.net/ajax/bookshelves/add-story-popup?story=" + ((FimStory) story).getId();

        HttpResponse response = httpClient.execute(new HttpGet(uri));

        JsonNode tree;
        try (InputStream in = response.getEntity().getContent()) {
            tree = objectMapper.readTree(in);
        }

        String contentHtml = tree.get("content").asText();

        StoryShelfParser parser = pageParserProvider.getParser(StoryShelfParser.class);
        Map<FimShelf, Boolean> result = parser.create();
        parser.parse(Jsoup.parse(contentHtml, uri), result);
        return result;
    }

    /**
     * Add or remove a story from a shelf.
     *
     * @param add whether this story should be in the shelf after this call.
     */
    public void setStoryShelf(Story story, FimShelf shelf, boolean add) throws Exception {
        HttpUriRequest request;
        if (add) {
            request = new HttpPost("https://www.fimfiction.net/ajax/bookshelves/" + shelf.getId() + "/items");
            ((HttpPost) request).setEntity(new UrlEncodedFormEntity(Collections.singletonList(
                    new BasicNameValuePair("story", String.valueOf(((FimStory) story).getId()))
            )));
        } else {
            request = new HttpDelete("https://www.fimfiction.net/ajax/bookshelves/" + shelf.getId() + "/items/" +
                                     ((FimStory) story).getId());
        }
        // we just assume this works
        //noinspection resource
        httpClient.execute(request)
                .getEntity().getContent().close();
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
                root.select(".drop-down-private-messages").isEmpty()) {

                loginOnce(defaultAuthentication);
                // resend request - we should have session cookies now
                return false;
            }
            return true;
        });
    }

    @SneakyThrows
    private Signature sign(String url, String body) {
        byte[] nonceBytes = new byte[32];
        ThreadLocalRandom.current().nextBytes(nonceBytes); // i don't really care about security here
        String nonce = printHex(nonceBytes);
        long time = System.currentTimeMillis() / 1000;

        String payload = nonce + '|' + time + '|' + printBase64(url.getBytes()) + '|' + body;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] hmac = mac.doFinal(payload.getBytes());
        return new Signature(printBase64(hmac), nonce, time);
    }

    private static String printHex(byte[] hex) {
        return new String(Hex.encodeHex(hex));
    }

    private String printBase64(byte[] bytes) {
        return Base64Variants.MIME.encode(bytes);
    }

    private byte[] parseBase64(String string) {
        return Base64Variants.MIME.decode(string);
    }

    @Value
    private static class Signature {
        String hmac;
        String nonce;
        long time;
    }
}
