package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.Image;
import at.yawk.fiction.impl.PageParserProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Collections;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * @author yawkat
 */
public class FimFictionProviderTest {
    private FimFictionProvider provider;
    private CloseableHttpClient client;

    @BeforeMethod
    public void setUp() throws Exception {
        client = HttpClientBuilder.create().build();
        provider = new FimFictionProvider(new PageParserProvider(), client, new ObjectMapper());
    }

    @AfterMethod
    public void tearDown() throws Exception {
        client.close();
        client = null;
        provider = null;
    }

    @Test
    public void testFetchTags() throws Exception {
        FimTag sol = provider.fetchTags().stream().filter(t -> t.getId().equals("g:slice_of_life")).findAny().get();
        assertEquals(sol.getName(), "Slice of Life");
    }

    @Test
    public void testFetchChapter() throws Exception {
        FimStory story = new FimStory();
        story.setId(55373);
        FimChapter chapter = new FimChapter();
        chapter.setId(839155);
        chapter.setIndex(0);

        provider.fetchChapter(story, chapter);
    }

    @Test
    public void testFetchStory() throws Exception {
        FimStory story = new FimStory();
        story.setId(55373);

        provider.fetchStory(story);

        assertEquals(story.getTitle(), "Rebirth of the Damned");
        assertEquals(story.getAuthor().getName(), "Borsuq");
        assertEquals(story.getImage(), new Image(URI.create(
                "https://cdn-img.fimfiction.net/story/ihgz-1432450586-55373-medium"), null));
    }

    @Test
    public void testSearch() throws Exception {
        FimSearchQuery query = new FimSearchQuery();
        query.setExcludedTags(Collections.emptySet());
        query.setMaxWords(1857);
        query.setOrder(FimOrder.APPROVED);

        assertNotEquals(provider.search(query).getPage(1),
                        provider.search(query).getPage(2));
    }
}