/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.impl.PageParser
import org.jsoup.nodes.Element
import java.net.URL

/**
 * @author yawkat
 */
internal object TagsParser : PageParser<List<FimTag>>() {
    override fun parse(root: Element): List<FimTag> {
        return root.select("ul.tags-dropdown").first().select(">li").map {
            val iconElement = it.select(".icon img").first()
            FimTag(
                    name = it.attr("data-name"),
                    id = it.attr("data-tag"),
                    icon = if (iconElement == null) null else URL(iconElement.absUrl("src"))
            )
        }
    }
}
