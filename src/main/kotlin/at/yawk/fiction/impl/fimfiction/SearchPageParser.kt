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
