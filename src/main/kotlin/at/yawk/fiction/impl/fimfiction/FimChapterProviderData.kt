/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.ChapterProviderData

/**
 * @author yawkat
 */
data class FimChapterProviderData(val id: Int? = null, val index: Int? = null, val wordCount: Int? = null) : ChapterProviderData {
    override fun merge(other: ChapterProviderData): ChapterProviderData {
        if (other !is FimChapterProviderData) throw ClassCastException()
        return FimChapterProviderData(
                id = id ?: other.id,
                index = index ?: other.index,
                wordCount = wordCount ?: other.wordCount
        )
    }
}
