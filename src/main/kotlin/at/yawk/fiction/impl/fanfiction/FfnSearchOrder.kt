/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction

enum class FfnSearchOrder(
        val displayName: String,
        val id: Int
) {
    UPDATE_DATE("Update Date", 1),
    PUBLISH_DATE("Publish Date", 2),
    REVIEWS("Reviews", 3),
    FAVORITES("Favorites", 4),
    FOLLOWS("Follows", 5),
}
