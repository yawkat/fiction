/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.impl.PageParser
import org.jsoup.nodes.Element

/**
 * @author yawkat
 */
internal object StoryShelfParser : PageParser<Map<FimShelf, Boolean>>() {
    override fun parse(root: Element): Map<FimShelf, Boolean> {
        return root.select("#bookshelves-popup-list li > a").map {
            Pair(
                    FimShelf(
                            id = Integer.parseInt(it.attr("data-bookshelf")),
                            name = it.select(".name").text()
                    ),
                    it.hasClass("selected")
            )
        }.toMap()
    }
}
