/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.Story
import at.yawk.fiction.impl.ParseTest
import com.google.common.collect.ImmutableMap
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotEquals
import org.testng.SkipException
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * @author yawkat
 */
class FimFictionProviderTestLogin {
    private lateinit var provider: FimFictionProvider
    private lateinit var client: CloseableHttpClient

    @BeforeMethod
    fun setUp() {
        val username = System.getProperty("username")
        val password = System.getProperty("password")
        if (username == null || password == null) {
            throw SkipException("username and password system properties not defined")
        }

        client = HttpClientBuilder.create().build()
        provider = FimFictionProvider(client, ParseTest.makeObjectMapper())
        provider.loginOnce(username, password)
    }

    @AfterMethod
    fun tearDown() {
        client.close()
    }

    @Test
    fun testFetchStory() {
        val story = provider.fetchStory(Story(providerData = FimStoryProviderData(
                id = 55373
        )))

        assertEquals(story.chapters!![0].read, java.lang.Boolean.TRUE)
        assertEquals(story.chapters!![120].read, java.lang.Boolean.FALSE)
    }

    @Test
    fun testFetchShelves() {
        val shelves = provider.fetchShelves()
        val favorites = shelves.first({ s -> s.name == "Favourites" })
        assertNotEquals(favorites.id, 0)
    }

    @Test
    fun testToggleRead() {
        val story = provider.fetchStory(Story(providerData = FimStoryProviderData(
                id = 55373
        )))

        val oldRead = story.chapters!![109].read
        var updated = provider.toggleRead(story.chapters!![109])
        assertNotEquals(updated.read, oldRead)
        updated = provider.toggleRead(story.chapters!![109])
        assertEquals(updated.read, oldRead)
    }

    @Test
    fun testFetchStoryShelves() {
        val result = provider.fetchStoryShelves(Story(providerData = FimStoryProviderData(
                id = 55373
        )))
        assertEquals(result, ImmutableMap.of(
                FimShelf(517612, "Tracking"), false,
                FimShelf(321007, "Read It Later"), false,
                FimShelf(124401, "Favourites"), true))
    }

    @Test
    @Throws(Exception::class)
    fun testSetStoryShelf() {
        val shelf = FimShelf(321007, "Read It Later")
        val story = Story(providerData = FimStoryProviderData(
                id = 55373
        ))

        val initialState = provider.fetchStoryShelves(story)[shelf]!!
        // flip the state
        provider.setStoryShelf(story, shelf, !initialState)
        assertEquals(provider.fetchStoryShelves(story)[shelf], !initialState)
        // this should do nothing
        provider.setStoryShelf(story, shelf, !initialState)
        assertEquals(provider.fetchStoryShelves(story)[shelf], !initialState)
        // ... and flip it back again
        provider.setStoryShelf(story, shelf, initialState)
        assertEquals(provider.fetchStoryShelves(story)[shelf], initialState)
    }
}
