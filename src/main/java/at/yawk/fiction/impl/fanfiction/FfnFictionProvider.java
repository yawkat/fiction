package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.*;
import at.yawk.fiction.impl.PageParserProvider;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/**
 * @author yawkat
 */
@RequiredArgsConstructor
public class FfnFictionProvider implements FictionProvider {
    private final PageParserProvider pageParserProvider;
    private final HttpClient httpClient;

    @Override
    public void fetchStory(Story story) {

    }

    @Override
    public void fetchChapter(Story story, Chapter chapter) {

    }

    @Override
    public FfnSearchQuery createQuery() {
        return new FfnSearchQuery();
    }

    @Override
    public Pageable<FfnStory> search(SearchQuery query) {
        // fail early
        FfnSearchQuery ffnSearchQuery = (FfnSearchQuery) query;
        return page -> {
            URI uri = ffnSearchQuery.build(page);
            Pageable.Page<FfnStory> o = pageParserProvider.getParser(SearchPageParser.class)
                    .parse(httpClient, new HttpGet(uri));
            o.setLast(o.getPageCount() <= page - 1);
            return o;
        };
    }
}
