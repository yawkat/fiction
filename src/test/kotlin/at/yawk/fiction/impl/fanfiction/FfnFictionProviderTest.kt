package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.impl.ParseTest
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

/**
 * @author yawkat
 */
class FfnFictionProviderTest {
    private lateinit var client: CloseableHttpClient
    private lateinit var provider: FfnFictionProvider

    @BeforeClass
    fun setUp() {
        client = HttpClientBuilder.create().build()
        provider = FfnFictionProvider(client)
    }

    @AfterClass
    fun tearDown() {
        client.close()
    }

    @Test
    fun testSearch() {
        val query = FfnSearchQuery(
                category = FfnSubCategory(
                        category = FfnCategory.GAMES,
                        name = "Elder Scroll series"
                )
        )
        val search = provider.search(query)
        ParseTest.print(search.getPage(0))
    }

    @Test
    fun testSubCategories() {
        ParseTest.print(provider.fetchSubCategories(FfnCategory.GAMES))
    }
}