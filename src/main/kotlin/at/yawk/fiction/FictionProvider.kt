/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction

/**
 * @author yawkat
 */
interface FictionProvider {
    val providedTypes: List<Class<out ProviderData>>

    fun fetchStory(story: Story): Story

    fun fetchChapter(story: Story, chapter: Chapter): Pair<Story, Chapter>

    fun search(query: SearchQuery): Pageable<out Story>
}
