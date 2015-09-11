package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.*;
import at.yawk.fiction.impl.PageParserProvider;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/**
 * @author yawkat
 */
@RequiredArgsConstructor
public class FimFictionProvider implements FictionProvider {
    private final PageParserProvider pageParserProvider;
    private final HttpClient httpClient;

    public List<FimTag> fetchTags() throws Exception {
        return pageParserProvider.getParser(TagsParser.class)
                .parse(httpClient, new HttpGet(URI.create("https://www.fimfiction.net/stories")));
    }

    @Override
    public void fetchStory(Story story) throws Exception {
        FimStory fim = (FimStory) story;
        pageParserProvider.getParser(StoryParser.class)
                .parse(httpClient, new HttpGet(URI.create("https://www.fimfiction.net/story/" + fim.getId())), fim);
    }

    @Override
    public void fetchChapter(Story story, Chapter chapter) throws Exception {
        pageParserProvider.getParser(ChapterParser.class).parse(httpClient, new HttpGet(URI.create(
                "https://www.fimfiction.net/story/" + ((FimStory) story).getId() + '/' +
                (((FimChapter) chapter).getIndex() + 1)
        )), (FimChapter) chapter);
    }

    @Override
    public FimSearchQuery createQuery() {
        return new FimSearchQuery();
    }

    @Override
    public Pageable<FimStory> search(SearchQuery query) {
        FimSearchQuery fim = (FimSearchQuery) query;
        return page -> {
            Pageable.Page<FimStory> p = pageParserProvider.getParser(SearchPageParser.class)
                    .parse(httpClient, new HttpGet(fim.build(page)));
            p.setLast(p.getEntries().isEmpty());
            return p;
        };
    }
}
