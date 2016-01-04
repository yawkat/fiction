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
internal object ShelvesParser : PageParser<List<FimShelf>>() {
    override fun parse(root: Element): List<FimShelf> {
        return root.select(".bookshelves").first().select("> li > a").map {
            FimShelf(
                    id = parseIntLenient(extractGroup(it.attr("href"), ".*/bookshelf/(\\d+)/.*")),
                    name = it.ownText()
            )
        }
    }
}
