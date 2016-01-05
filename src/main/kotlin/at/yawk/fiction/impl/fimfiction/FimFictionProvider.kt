/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.*
import at.yawk.fiction.impl.PageParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.*

/**
 * @author yawkat
 */
class FimFictionProvider(
        val httpClient: HttpClient,
        val objectMapper: ObjectMapper
) : FictionProvider {
    override val providedTypes = listOf(
            FimAuthorProviderData::class.java,
            FimChapterProviderData::class.java,
            FimStoryProviderData::class.java,
            FimSearchQuery::class.java
    )

    /**
     * Authentication to use for auto login. HttpClient must save cookies for this to work!
     */
    var defaultAuthentication: FimAuthentication? = null

    /**
     * Login once, without saving credentials. HttpClient must save cookies for this to work!
     */
    fun loginOnce(username: String, password: String) {
        loginOnce(FimAuthentication(username, password))
    }

    /**
     * Login once, without saving credentials. HttpClient must save cookies for this to work!
     */
    fun loginOnce(authentication: FimAuthentication) {
        val request = HttpPost("https://www.fimfiction.net/ajax/login.php")
        request.entity = UrlEncodedFormEntity(Arrays.asList(
                BasicNameValuePair("username", authentication.username),
                BasicNameValuePair("password", authentication.password),
                BasicNameValuePair("secure", "true"),
                BasicNameValuePair("keep_logged_in", if (authentication.isRemember) "1" else "0"),
                BasicNameValuePair("otp", authentication.otp)))

        val response = httpClient.execute(request)
        val responseNode = objectMapper.readTree(EntityUtils.toByteArray(response.entity))
        if (responseNode.has("error")) {
            throw FimAuthenticationException(responseNode.get("error").toString())
        }
    }

    fun fetchTags(): List<FimTag> {
        val request = TagsParser.request(httpClient).get("https://www.fimfiction.net/stories")
        decorateHtmlRequest(request)
        return request.send()
    }

    /**
     * Get a list of all shelves of this user. Return value is undefined if the user isn't logged in.
     */
    fun fetchShelves(): List<FimShelf> {
        val request = ShelvesParser.request(httpClient).get("https://www.fimfiction.net/")
        decorateHtmlRequest(request)
        return request.send()
    }

    /**
     * Toggle this chapters read status and return the changes. *This does not make use of the
     * old [Chapter.read] value, if it is out of date this action may yield unwanted results!*
     */
    fun toggleRead(chapter: Chapter): Chapter {
        val request = HttpPost("https://www.fimfiction.net/ajax/toggle_read.php")
        request.entity = UrlEncodedFormEntity(listOf(BasicNameValuePair("chapter",
                (chapter.providerData as FimChapterProviderData).id!!.toString())))

        val response = httpClient.execute(request)
        val responseNode = objectMapper.readTree(EntityUtils.toByteArray(response.entity))
        return Chapter(read = responseNode.get("read").asBoolean(), providerData = FimChapterProviderData())
    }

    override fun fetchStory(story: Story): Story {
        val id = (story.providerData as FimStoryProviderData).id!!
        val request = StoryParser.request(httpClient)
                .get("https://www.fimfiction.net/story/$id")
        decorateHtmlRequest(request)
        return request.send()
    }

    override fun fetchChapter(story: Story, chapter: Chapter): Pair<Story, Chapter> {
        val id = (chapter.providerData as FimChapterProviderData).id!!
        val downloaded = ChapterParser.request(httpClient).get("https://www.fimfiction.net/download_chapter.php?html&chapter=$id")
                .send()
        return Pair(story, downloaded)
    }

    /**
     * @return A map containing all shelves of this user as keys, and whether this story is part of those shelves as
     * * values.
     */
    fun fetchStoryShelves(story: Story): Map<FimShelf, Boolean> {
        val uri = "https://www.fimfiction.net/ajax/bookshelves/popup_list.php?story=" + (story.providerData as FimStoryProviderData).id!!

        val response = httpClient.execute(HttpGet(uri))

        var tree: JsonNode = response.entity.content.use { objectMapper.readTree(it) }

        val contentHtml = tree.get("content").asText()

        return StoryShelfParser.parse(Jsoup.parse(contentHtml, uri))
    }

    /**
     * Add or remove a story from a shelf.

     * @param add whether this story should be in the shelf after this call.
     */
    fun setStoryShelf(story: Story, shelf: FimShelf, add: Boolean) {
        val request = HttpPost("https://www.fimfiction.net/ajax/bookshelf_items/post.php")
        request.entity = UrlEncodedFormEntity(Arrays.asList(
                BasicNameValuePair("bookshelf", shelf.id!!.toString()),
                BasicNameValuePair("story", (story.providerData as FimStoryProviderData).id!!.toString()),
                BasicNameValuePair("task", if (add) "add" else "remove")))
        // we just assume this works
        //noinspection resource
        httpClient.execute(request).entity.content.close()
    }

    override fun search(query: SearchQuery): Pageable<Story> {
        val fim = query as FimSearchQuery
        return object : Pageable<Story> {
            override fun getPage(page: Int): Pageable.Page<Story> {
                val request = SearchPageParser.request(httpClient).get(fim.build(page))
                decorateHtmlRequest(request)
                val p = request.send()
                return p.copy(last = p.entries.isEmpty())
            }
        }
    }

    private fun decorateHtmlRequest(request: PageParser.RequestBuilder<*>) {
        request.validator(object : PageParser.DocumentValidator {
            override fun validate(requestBuilder: PageParser.RequestBuilder<*>, root: Element): Boolean {
                val defaultAuthentication = defaultAuthentication

                // confirm that we are logged in
                if (defaultAuthentication != null && root.getElementById("private-message-drop-down") == null) {

                    loginOnce(defaultAuthentication)
                    // resend request - we should have session cookies now
                    return false
                }
                return true
            }
        })
    }
}
