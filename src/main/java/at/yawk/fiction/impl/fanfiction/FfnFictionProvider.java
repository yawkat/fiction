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
    public void fetchStory(Story story) throws Exception {
        Chapter chapter;
        if (story.getChapters() != null && !story.getChapters().isEmpty()) {
            chapter = story.getChapters().get(0);
        } else {
            chapter = new FfnChapter();
            ((FfnChapter) chapter).setIndex(0);
        }
        fetchChapter(story, chapter);
    }

    @Override
    public void fetchChapter(Story story, Chapter chapter) throws Exception {
        pageParserProvider.getParser(ChapterPageParser.class).request(httpClient)
                .get("https://www.fanfiction.net/s/" + ((FfnStory) story).getId() + '/' +
                     (((FfnChapter) chapter).getIndex() + 1))
                .target(new ChapterPageParser.StoryChapterPair(story, chapter))
                .send();
    }

    @Override
    public FfnSearchQuery createQuery() {
        return new FfnSearchQuery();
    }

    public List<FfnSubCategory> fetchSubCategories(FfnCategory category) throws Exception {
        List<FfnSubCategory> subCategories = pageParserProvider.getParser(SubCategoryPageParser.class)
                .parse(httpClient, new HttpGet("https://www.fanfiction.net/" + category.getId()));
        for (FfnSubCategory subCategory : subCategories) {
            subCategory.setCategory(category);
        }
        return subCategories;
    }

    @Override
    public Pageable<FfnStory> search(SearchQuery query) {
        // fail early
        FfnSearchQuery ffnSearchQuery = (FfnSearchQuery) query;
        return page -> {
            URI uri = ffnSearchQuery.build(page);
            Pageable.Page<FfnStory> o = pageParserProvider.getParser(SearchPageParser.class)
                    .request(httpClient).get(uri).send();
            o.setLast(o.getPageCount() <= page - 1);
            return o;
        };
    }
}
