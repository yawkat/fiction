package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.*
import at.yawk.fiction.impl.PageParser
import org.jsoup.nodes.Element
import java.net.URI
import java.net.URL
import java.util.*

/**
 * @author yawkat
 */
internal object StoryParser : PageParser<Story>() {
    override fun parse(root: Element): Story {
        // only applicable outside a search when the root is a html element
        val storyContainer = root.select(".story_container").first()

        val contentBox = storyContainer.select(".story_content_box")
        val id = parseIntLenient(contentBox.attr("id"))
        val storyName = contentBox.select(".story_name").first()
        val title = storyName.text()
        val uri = URI.create(storyName.absUrl("href"))

        val authorLink = contentBox.select(".author a")
        val authorHref = authorLink.attr("href")
        val author = Author(
                name = authorLink.text(),
                providerData = FimAuthorProviderData(id = authorHref.substring(authorHref.lastIndexOf('/') + 1))
        )

        val descriptionElement = storyContainer.select(".description").first()
        val storyImage = descriptionElement.select(".story_image img").first()
        var image: Image? = null
        if (storyImage != null) {
            var imageUri: URI? = null
            if (storyImage.hasAttr("data-src")) {
                imageUri = URI.create(storyImage.absUrl("data-src"))
            }
            var thumbnailUri: URI? = null
            if (storyImage.hasAttr("src")) {
                thumbnailUri = URI.create(storyImage.absUrl("src"))
            }
            if (imageUri != null || thumbnailUri != null) {
                image = Image(
                        imageUrl = imageUri ?: thumbnailUri,
                        thumbnailUrl = thumbnailUri
                )
            }
        }

        var description: HtmlText? = null

        val separator = descriptionElement.select(">hr").first()
        if (separator != null) {
            val descriptionHtml = StringBuilder()
            var e = separator.nextSibling()
            while (e != null) {
                descriptionHtml.append(e.outerHtml())
                e = e.nextSibling()
            }

            description = HtmlText(descriptionHtml.toString())
        }

        val tags = ArrayList<FimTag>()

        for (categoryElement in descriptionElement.select("> .story_category")) {
            tags.add(FimTag(
                    id = extractGroup(categoryElement.attr("href"), ".*tags\\[\\]=(.+)")!!,
                    name = categoryElement.text()
            ))
        }

        for (characterIcon in root.select(".extra_story_data .inner_data").first().select("> .character_icon")) {
            tags.add(FimTag(
                    // we can't find out id here :(
                    name = characterIcon.attr("title"),
                    icon = URL(characterIcon.select("img").first().absUrl("src"))
            ))
        }

        val statusElement = storyContainer.select(".chapters .bottom > span").first()
        val status = when (statusElement.attr("title")) {
            "Complete" -> FimStatus.COMPLETED
            "Incomplete" -> FimStatus.INCOMPLETE
            "On Hiatus" -> FimStatus.ON_HIATUS
            "Cancelled" -> FimStatus.CANCELLED
            else -> null
        }

        val chapters = ArrayList<Chapter>()
        for (chapterContainer in storyContainer.select(".chapters .chapter_container")) {
            if (chapterContainer.hasClass("chapter_expander")) {
                continue
            }

            val chapterLink = chapterContainer.select(".chapter_link").first()

            val id = parseIntLenient(extractGroup(
                    chapterContainer.select(".download_container a").first().attr("href"), ".*chapter=(\\d+)"))
            val index = parseIntLenient(extractGroup(
                    chapterLink.attr("href"), ".*/story/\\d+/(\\d+)(:?/.*)?")) - 1
            val name = chapterLink.text()
            val wordCount = parseIntLenient(chapterContainer.select(".word_count").first().ownText())

            val chapterReadIcon = chapterContainer.select(".chapter-read-icon").first()
            val read = chapterReadIcon?.hasClass("chapter-read")

            chapters.add(Chapter(
                    name = name,
                    read = read,
                    providerData = FimChapterProviderData(
                            id = id,
                            index = index,
                            wordCount = wordCount
                    )
            ))
        }

        // todo: parse first published + last modified

        return Story(
                title = title,
                author = author,
                chapters = chapters,
                description = description,
                image = image,
                uri = uri,
                providerData = FimStoryProviderData(
                        id = id,
                        status = status,
                        tags = tags
                )
        )
    }
}
