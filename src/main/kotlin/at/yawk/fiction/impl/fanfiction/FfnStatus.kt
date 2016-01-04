/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction

/**
 * @author yawkat
 */
enum class FfnStatus(
        val id: Int,
        val displayName: String
) {
    IN_PROGRESS(1, "In-Progress"),
    COMPLETE(2, "Complete"),
}
