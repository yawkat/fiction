package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.impl.PageParser
import org.jsoup.nodes.Element

/**
 * @author yawkat
 */
internal object StoryShelfParser : PageParser<Map<FimShelf, Boolean>>() {
    override fun parse(root: Element): Map<FimShelf, Boolean> {
        return root.select("#bookshelves-popup-list li > a").toMap(
                {
                    FimShelf(
                            id = Integer.parseInt(it.attr("data-bookshelf")),
                            name = it.select(".name").text()
                    )
                },
                { it.hasClass("selected") }
        )
    }
}
