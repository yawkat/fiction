package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.Chapter
import at.yawk.fiction.Story
import at.yawk.fiction.impl.PageParser
import org.joda.time.Instant
import org.jsoup.nodes.Element
import java.util.*

/**
 * @author yawkat
 */
internal object TagParser : PageParser<Story>() {
    override fun parse(root: Element): Story {
        val timeElements = root.select("span")
        val items = split(root.text(), " - ")

        val genres: List<FfnGenre>
        if (!items[2].startsWith("Chapters:")) {
            genres = items.removeAt(2).split("/").map { FfnGenre(name = it) }
        } else {
            genres = emptyList()
        }

        val favorites = parseIntLenient(removeMatch(items, "Favs: ([\\d,]+)"))
        val follows = parseIntLenient(removeMatch(items, "Follows: ([\\d,]+)"))
        val words = parseIntLenient(removeMatch(items, "Words: ([\\d,]+)"))

        removeMatch(items, "Updated: (.*)")
        removeMatch(items, "Published: (.*)")
        val updateTime = Instant(timeElements.first().attr("data-xutime").toLong() * 1000)
        val publishTime = Instant(timeElements.last().attr("data-xutime").toLong() * 1000)

        val chapterCount = parseIntLenient(removeMatch(items, "Chapters: ([\\d,]+)"))
        val chapters = ArrayList<Chapter>(chapterCount)
        for (i in 0..chapterCount - 1) {
            chapters.add(Chapter(providerData = FfnChapterProviderData(index = i)))
        }
        return Story(
                updateTime = updateTime,
                publishTime = publishTime,
                chapters = chapters,
                providerData = FfnStoryProviderData(
                        genres = genres,
                        favorites = favorites,
                        follows = follows,
                        words = words
                )
        )
    }
}
