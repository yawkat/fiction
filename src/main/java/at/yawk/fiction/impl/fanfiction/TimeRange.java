/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction;

import java.util.concurrent.TimeUnit;
import lombok.Getter;

/**
* @author yawkat
*/
@Getter
public enum TimeRange {
    ONE_DAY(1, "24 hours", 1),
    ONE_WEEK(2, "1 week", 7),
    ONE_MONTH(3, "1 month", 30),
    SIX_MONTH(4, "6 months", 365 / 2),
    ONE_YEAR(5, "1 year", 365);

    private final int id;
    private final String label;
    private final long durationSeconds;

    TimeRange(int id, String label, int days) {
        this.id = id;
        this.label = label;
        this.durationSeconds = TimeUnit.DAYS.toSeconds(days);
    }
}
