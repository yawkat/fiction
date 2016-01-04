/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction

import java.util.concurrent.TimeUnit

/**
 * @author yawkat
 */
enum class TimeRange(
        val id: Int,
        val label: String,
        days: Int
) {
    ONE_DAY(1, "24 hours", 1),
    ONE_WEEK(2, "1 week", 7),
    ONE_MONTH(3, "1 month", 30),
    SIX_MONTH(4, "6 months", 365 / 2),
    ONE_YEAR(5, "1 year", 365);

    val durationSeconds = TimeUnit.DAYS.toSeconds(days.toLong())
}
