package at.yawk.fiction.impl.fanfiction;

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
public class FfnFictionProvider implements FictionProvider {
    private final PageParserProvider pageParserProvider;
    private final HttpClient httpClient;

    @Override
    public void fetchStory(Story story) {
        // todo
    }

    @Override
    public void fetchChapter(Story story, Chapter chapter) {
        // todo
    }

    @Override
    public FfnSearchQuery createQuery() {
        return new FfnSearchQuery();
    }

    public List<FfnSubCategory> fetchSubCategories(FfnCategory category) throws Exception {
        return pageParserProvider.getParser(SubCategoryPageParser.class)
                .parse(httpClient, new HttpGet("https://www.fanfiction.net/" + category.getId()));
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
