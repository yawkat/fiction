/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction

import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * @author yawkat
 */
data class Chapter(
        val name: String? = null,
        val text: FormattedText? = null,
        /**
         * Whether this chapter is read according to account data of this user. `null` means unknown or not logged
         * in.
         */
        val read: Boolean? = null,
        val providerData: ChapterProviderData
) : Mergeable<Chapter> {
    override fun merge(other: Chapter): Chapter {
        return Chapter(
                name = name ?: other.name,
                text = text ?: other.text,
                read = read ?: other.read,
                providerData = providerData.merge(other.providerData)
        )
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
interface ChapterProviderData : Mergeable<ChapterProviderData>, ProviderData