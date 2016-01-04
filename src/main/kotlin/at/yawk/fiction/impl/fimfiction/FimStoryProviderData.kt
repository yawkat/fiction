/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.StoryProviderData

/**
 * @author yawkat
 */
class FimStoryProviderData(
        val id: Int? = null,
        val tags: List<FimTag>? = null,
        val status: FimStatus? = null
) : StoryProviderData {

    override fun merge(other: StoryProviderData): StoryProviderData {
        if (other !is FimStoryProviderData) throw ClassCastException()
        return FimStoryProviderData(
                id = id ?: other.id,
                tags = tags ?: other.tags,
                status = status ?: other.status
        )
    }
}
