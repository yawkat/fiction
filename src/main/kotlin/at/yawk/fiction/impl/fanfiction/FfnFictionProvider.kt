/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.*
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet

/**
 * @author yawkat
 */
class FfnFictionProvider(private val httpClient: HttpClient) : FictionProvider {
    override val providedTypes = listOf(
            FfnAuthorProviderData::class.java,
            FfnChapterProviderData::class.java,
            FfnStoryProviderData::class.java,
            FfnSearchQuery::class.java
    )

    override fun fetchStory(story: Story): Story {
        val chapter: Chapter
        if (story.chapters != null && !story.chapters.isEmpty()) {
            chapter = story.chapters[0]
        } else {
            chapter = Chapter(providerData = FfnChapterProviderData(index = 0))
        }
        return fetchChapter(story, chapter).first
    }

    override fun fetchChapter(story: Story, chapter: Chapter): Pair<Story, Chapter> {
        val id = (story.providerData as FfnStoryProviderData).id
        val i = (chapter.providerData as FfnChapterProviderData).index!! + 1
        return ChapterPageParser.request(httpClient)
                .get("https://www.fanfiction.net/s/$id/$i")
                .send()
    }

    fun fetchSubCategories(category: FfnCategory): List<FfnSubCategory> {
        val subCategories = SubCategoryPageParser.fetch(httpClient, HttpGet("https://www.fanfiction.net/" + category.id))
        return subCategories.map { it.copy(category = category) }
    }

    override fun search(query: SearchQuery): Pageable<Story> {
        // fail early
        val ffnSearchQuery = query as FfnSearchQuery
        return object : Pageable<Story> {
            override fun getPage(page: Int): Pageable.Page<Story> {
                val uri = ffnSearchQuery.build(page)
                val o = SearchPageParser.request(httpClient).get(uri).send()
                return o.copy(last = o.pageCount!! <= page - 1)
            }
        }
    }
}
