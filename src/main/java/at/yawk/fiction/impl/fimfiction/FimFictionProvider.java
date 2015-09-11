package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.*;
import at.yawk.fiction.impl.PageParserProvider;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * @author yawkat
 */
@RequiredArgsConstructor
public class FimFictionProvider implements FictionProvider {
    private final PageParserProvider pageParserProvider;
    private final HttpClient httpClient;

    @Getter @Setter
    private String sessionToken;

    public List<FimTag> fetchTags() throws Exception {
        HttpGet request = new HttpGet(URI.create("https://www.fimfiction.net/stories"));
        decorateRequest(request);
        return pageParserProvider.getParser(TagsParser.class).parse(httpClient, request);
    }

    @Override
    public void fetchStory(Story story) throws Exception {
        HttpGet request = new HttpGet(URI.create("https://www.fimfiction.net/story/" + ((FimStory) story).getId()));
        decorateRequest(request);
        pageParserProvider.getParser(StoryParser.class).parse(httpClient, request, (FimStory) story);
    }

    @Override
    public void fetchChapter(Story story, Chapter chapter) throws Exception {
        HttpGet request = new HttpGet(URI.create(
                "https://www.fimfiction.net/story/" + ((FimStory) story).getId() + '/' +
                (((FimChapter) chapter).getIndex() + 1)
        ));
        decorateRequest(request);
        pageParserProvider.getParser(ChapterParser.class).parse(httpClient, request, (FimChapter) chapter);
    }

    @Override
    public FimSearchQuery createQuery() {
        return new FimSearchQuery();
    }

    @Override
    public Pageable<FimStory> search(SearchQuery query) {
        FimSearchQuery fim = (FimSearchQuery) query;
        return page -> {
            HttpGet request = new HttpGet(fim.build(page));
            decorateRequest(request);
            Pageable.Page<FimStory> p = pageParserProvider.getParser(SearchPageParser.class).parse(httpClient, request);
            p.setLast(p.getEntries().isEmpty());
            return p;
        };
    }

    private void decorateRequest(HttpUriRequest request) throws Exception {
        request.addHeader("Cookie", "view_mature=true; session_token=" + URLEncoder.encode(
                sessionToken == null ? "" : sessionToken, "UTF-8"));
    }
}
