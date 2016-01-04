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
