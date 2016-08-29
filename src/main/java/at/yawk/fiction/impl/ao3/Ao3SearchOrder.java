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
public enum Ao3SearchOrder {
    BEST_MATCH("Best Match", ""),
    AUTHOR("Author", "authors_to_sort_on"),
    TITLE("Title", "title_to_sort_on"),
    DATE_POSTED("Date Posted", "created_at"),
    DATE_UPDATED("Date Updated", "revised_at"),
    WORD_COUNT("Word Count", "word_count"),
    HITS("Hits", "hits"),
    KUDOS("Kudos", "kudos_count"),
    COMMENTS("Comments", "comments_count"),
    BOOKMARKS("Bookmarks", "bookmarks_count");

    private final String name;
    private final String id;
}
