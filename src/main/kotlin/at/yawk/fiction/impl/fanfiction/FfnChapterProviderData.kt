/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.ChapterProviderData

/**
 * @author yawkat
 */
data class FfnChapterProviderData(val index: Int? = null) : ChapterProviderData {
    override fun merge(other: ChapterProviderData): ChapterProviderData {
        if (other !is FfnChapterProviderData) throw ClassCastException()
        return FfnChapterProviderData(index = index ?: other.index)
    }
}