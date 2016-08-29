/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.ao3;

import at.yawk.fiction.Story;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author yawkat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Ao3Story extends Story {
    int id;

    List<String> requiredTags;
    List<String> warnings;
    List<String> relationships;
    List<String> characters;
    List<String> freeforms;

    String language;
    int words;
    Integer chapterGoal;
    int commentCount;
    int kudoCount;
    int bookmarkCount;
    int hitCount;
}
