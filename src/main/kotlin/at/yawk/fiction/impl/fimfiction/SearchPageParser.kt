/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.Pageable
import at.yawk.fiction.Story
import at.yawk.fiction.impl.PageParser
import org.jsoup.nodes.Element

/**
 * @author yawkat
 */
internal object SearchPageParser : PageParser<Pageable.Page<Story>>() {
    override fun parse(root: Element): Pageable.Page<Story> {
        return Pageable.Page(
                entries = root.select(".story_container").map { StoryParser.parse(it) }.toList(),
                pageCount = null,
                last = false
        )
    }
}
