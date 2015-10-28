package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.impl.PageParserProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * @author yawkat
 */
public class FimFictionProviderTestLogin {
    private FimFictionProvider provider;
    private CloseableHttpClient client;

    @BeforeMethod
    public void setUp() throws Exception {
        String username = System.getProperty("username");
        String password = System.getProperty("password");
        if (username == null || password == null) {
            throw new SkipException("username and password system properties not defined");
        }

        client = HttpClientBuilder.create().build();
        provider = new FimFictionProvider(new PageParserProvider(), client, new ObjectMapper());
        provider.loginOnce(username, password);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        client.close();
        client = null;
        provider = null;
    }

    @Test
    public void testFetchStory() throws Exception {
        FimStory story = new FimStory();
        story.setId(55373);

        provider.fetchStory(story);

        assertEquals(story.getChapters().get(0).getRead(), Boolean.TRUE);
        assertEquals(story.getChapters().get(120).getRead(), Boolean.FALSE);
    }

    @Test
    public void testFetchShelves() throws Exception {
        List<FimShelf> shelves = provider.fetchShelves();
        FimShelf favorites = shelves.stream()
                .filter(s -> s.getName().equals("Favourites"))
                .findAny().get();
        assertNotEquals(favorites.getId(), 0);
    }

    @Test
    public void testToggleRead() throws Exception {
        FimStory story = new FimStory();
        story.setId(55373);
        provider.fetchStory(story);

        FimChapter chapter = (FimChapter) story.getChapters().get(109);
        boolean oldRead = chapter.getRead();
        provider.toggleRead(chapter);
        assertNotEquals(chapter.getRead(), oldRead);
        provider.toggleRead(chapter);
        assertEquals(chapter.getRead().booleanValue(), oldRead);
    }

    @Test
    public void testFetchStoryShelves() throws Exception {
        FimStory story = new FimStory();
        story.setId(55373);
        Map<FimShelf, Boolean> result = provider.fetchStoryShelves(story);
        assertEquals(result, ImmutableMap.of(
                createShelf(517612, "Tracking"), false,
                createShelf(321007, "Read It Later"), false,
                createShelf(124401, "Favourites"), true
        ));
    }

    private static FimShelf createShelf(int id, String name) {
        FimShelf shelf = new FimShelf();
        shelf.setId(id);
        shelf.setName(name);
        return shelf;
    }
}
