/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.Chapter
import at.yawk.fiction.Image
import at.yawk.fiction.Story
import at.yawk.fiction.impl.ParseTest
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotEquals
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.net.URI

/**
 * @author yawkat
 */
class FimFictionProviderTest {
    private lateinit var provider: FimFictionProvider
    private lateinit var client: CloseableHttpClient

    @BeforeMethod
    fun setUp() {
        client = HttpClientBuilder.create().build()
        provider = FimFictionProvider(client, ParseTest.makeObjectMapper())
    }

    @AfterMethod
    fun tearDown() {
        client.close()
    }

    @Test
    fun testFetchTags() {
        val sol = provider.fetchTags().first({ it.id == "g:slice_of_life" })
        assertEquals(sol.name, "Slice of Life")
    }

    @Test
    fun testFetchChapter() {
        val story = Story(providerData = FimStoryProviderData(
                id = 55373
        ))
        val chapter = Chapter(providerData = FimChapterProviderData(
                id = 839155,
                index = 0
        ))

        provider.fetchChapter(story, chapter)
    }

    @Test
    fun testFetchStory() {
        val story = provider.fetchStory(Story(providerData = FimStoryProviderData(
                id = 55373
        )))

        assertEquals(story.title, "Rebirth of the Damned")
        assertEquals(story.author!!.name, "Borsuq")
        assertEquals(story.image, Image(URI.create(
                "https://cdn-img.fimfiction.net/story/ihgz-1432450586-55373-medium"), null))
    }

    @Test
    fun testSearch() {
        val query = FimSearchQuery(
                excludedTags = emptySet(),
                maxWords = 1857,
                order = FimOrder.APPROVED
        )
        assertNotEquals(provider.search(query).getPage(1),
                provider.search(query).getPage(2))
    }
}