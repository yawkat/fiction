/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction;

import java.util.Collection;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * @author yawkat
 */
@Value
@Wither
@Builder
public class FfnSubCategory {
    FfnCategory category;
    boolean crossover;

    String name;
    int id;
    int estimatedStoryCount;
    Collection<Character> characters;
    Collection<FfnGenre> genres;
    Collection<FfnLanguage> languages;
    Collection<FfnWorld> worlds;
}
