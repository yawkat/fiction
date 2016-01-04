/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.Mergeable

/**
 * @author yawkat
 */
data class FimShelf(
        val id: Int? = null,
        val name: String? = null
) : Mergeable<FimShelf> {
    override fun merge(other: FimShelf): FimShelf {
        return FimShelf(
                id = id ?: other.id,
                name = name ?: other.name
        )
    }
}
