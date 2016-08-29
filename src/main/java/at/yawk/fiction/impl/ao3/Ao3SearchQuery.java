/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.ao3;

import at.yawk.fiction.SearchQuery;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * @author yawkat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Ao3SearchQuery extends SearchQuery {
    Ao3SearchOrder order;
    OrderDirection orderDirection;
    String title;
    String freeText;
    String author;
    String period;
    boolean complete;
    boolean singleChapter;
    Ao3Rating rating;
    Set<Ao3Tag> fandoms;
    Set<Ao3Tag> tags;
    Range wordCount;
    Range hits;
    Range kudos;
    Range comments;
    Range bookmarks;

    @Value
    public static final class Range {
        Integer min;
        Integer max;

        @JsonCreator
        public Range(@JsonProperty("min") Integer min, @JsonProperty("max") Integer max) {
            this.min = min;
            this.max = max;
        }

        @Nullable
        String toQueryString() {
            if (min == null) {
                if (max == null) {
                    return null;
                } else {
                    return "<" + (max + 1);
                }
            } else {
                if (max == null) {
                    return ">" + (min - 1);
                } else {
                    return min + "-" + max;
                }
            }
        }
    }
}
