package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.Pageable;
import at.yawk.fiction.impl.PageParserProvider;
import at.yawk.fiction.impl.ParseTest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author yawkat
 */
public class FfnFictionProviderTest {
    private CloseableHttpClient client;
    private FfnFictionProvider provider;

    @BeforeClass
    public void setUp() throws Exception {
        client = HttpClientBuilder.create().build();
        provider = new FfnFictionProvider(new PageParserProvider(), client);
    }

    @AfterClass
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testSearch() throws Exception {
        FfnSearchQuery query = provider.createQuery();
        FfnSubCategory category = new FfnSubCategory();
        category.setCategory(FfnCategory.GAMES);
        category.setName("Elder Scroll series");
        query.setCategory(category);
        Pageable<FfnStory> search = provider.search(query);
        ParseTest.print(search.getPage(0));
    }

    @Test
    public void testSubCategories() throws Exception {
        ParseTest.print(provider.fetchSubCategories(FfnCategory.GAMES));
    }
}