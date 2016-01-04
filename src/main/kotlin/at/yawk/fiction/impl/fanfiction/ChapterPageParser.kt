/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.Chapter
import at.yawk.fiction.HtmlText
import at.yawk.fiction.NotFoundException
import at.yawk.fiction.Story
import at.yawk.fiction.impl.PageParser
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*

/**
 * @author yawkat
 */
internal object ChapterPageParser : PageParser<Pair<Story, Chapter>>() {
    override fun parse(root: Element): Pair<Story, Chapter> {
        val profileTop = root.select("#profile_top").first() ?: throw NotFoundException()

        val title = profileTop.select("> b.xcontrast_txt").text()
        // todo: author, tags

        val select = root.select("#chap_select").first()
        var chapterCount = 1
        var chapterOptions: Elements? = null
        if (select != null) {
            chapterOptions = select.select("option")
            chapterCount = chapterOptions!!.size
        }

        val chapters = ArrayList<Chapter>()
        for (i in 0..chapterCount - 1) {
            var name: String? = null
            if (chapterOptions != null) {
                val text = chapterOptions[i].text()
                name = text.substring(text.indexOf(' ') + 1)
            }
            chapters.add(Chapter(
                    name = name,
                    providerData = FfnChapterProviderData(index = i)
            ))
        }

        val text = HtmlText(root.select("#storytext").html())
        return Pair(Story(
                title = title,
                chapters = chapters,
                providerData = FfnStoryProviderData()
        ), Chapter(
                text = text,
                providerData = FfnChapterProviderData()
        ))
    }
}
