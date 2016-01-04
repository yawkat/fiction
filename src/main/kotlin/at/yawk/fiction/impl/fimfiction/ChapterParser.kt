package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.Chapter
import at.yawk.fiction.HtmlText
import at.yawk.fiction.impl.PageParser
import org.jsoup.nodes.Element

/**
 * @author yawkat
 */
internal object ChapterParser : PageParser<Chapter>() {
    override fun parse(root: Element): Chapter {
        return Chapter(
                text = HtmlText(root.select("body").first().html()),
                providerData = FimChapterProviderData()
        )
    }
}
