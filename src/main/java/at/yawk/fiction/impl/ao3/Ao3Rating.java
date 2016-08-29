/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.ao3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yawkat
 */
@Getter
@RequiredArgsConstructor
public enum Ao3Rating {
    NOT_RATED("Not Rated", 9),
    GENERAL("General Audiences", 10),
    TEEN_PLUS("Teen And Up Audiences", 11),
    MATURE("Mature", 12),
    EXPLICIT("Explicit", 13);

    private final String name;
    private final int id;
}
