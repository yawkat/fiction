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
