/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction

/**
 * @author yawkat
 */
enum class FfnRating(
        val displayName: String,
        val id: Int
) {
    CHILDREN("K", 1),
    CHILDREN_PLUS("K+", 2),
    TEEN("T", 3),
    MATURE("M", 4);

    companion object {
        fun forName(name: String): FfnRating? {
            return values().firstOrNull { it.displayName == name }
        }
    }
}
