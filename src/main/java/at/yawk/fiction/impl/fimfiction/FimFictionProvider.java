package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.*;
import at.yawk.fiction.impl.PageParser;
import at.yawk.fiction.impl.PageParserProvider;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.http.client.HttpClient;

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
        PageParser.RequestBuilder request = pageParserProvider.getParser(TagsParser.class).request(httpClient)
                .get("https://www.fimfiction.net/stories");
        decorateRequest(request);
        return request
                .send();
    }

    @Override
    public void fetchStory(Story story) throws Exception {
        PageParser.RequestBuilder request = pageParserProvider.getParser(StoryParser.class).request(httpClient)
                .get("https://www.fimfiction.net/story/" + ((FimStory) story).getId())
                .target((FimStory) story);
        decorateRequest(request);
        request.send();
    }

    @Override
    public void fetchChapter(Story story, Chapter chapter) throws Exception {
        PageParser.RequestBuilder request = pageParserProvider.getParser(ChapterParser.class).request(httpClient)
                .get("https://www.fimfiction.net/story/" + ((FimStory) story).getId() + '/' +
                     (((FimChapter) chapter).getIndex() + 1))
                .target((FimChapter) chapter);
        decorateRequest(request);
        request.send();
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
            decorateRequest(request);
            Pageable.Page<FimStory> p = request.send();
            p.setLast(p.getEntries().isEmpty());
            return p;
        };
    }

    private void decorateRequest(PageParser.RequestBuilder request) throws Exception {
        request.cookies("view_mature", "true",
                        "session_token", sessionToken == null ? "" : sessionToken);
    }
}
