/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.Mergeable

/**
 * @author yawkat
 */
data class FfnGenre(
        val id: Int? = null,
        val name: String? = null
) : Mergeable<FfnGenre> {
    override fun merge(other: FfnGenre): FfnGenre {
        return FfnGenre(
                id = id ?: other.id,
                name = name ?: other.name
        )
    }
}