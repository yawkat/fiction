/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction

/**
 * @author yawkat
 */
interface Pageable<T> {
    /**
     * Get the page of the given index. Indexes start at 0.
     */
    fun getPage(page: Int): Page<T>

    data class Page<T>(
            val entries: List<T>,
            /**
             * The expected total page count or `null` if unknown.
             */
            val pageCount: Int?,
            /**
             * `true` if this is the last page, `false` if there are more following pages or
             * we don't know.
             */
            val last: Boolean
    )
}
