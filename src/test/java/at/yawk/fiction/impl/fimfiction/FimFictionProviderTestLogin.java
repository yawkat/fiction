package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.impl.PageParserProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

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
}
