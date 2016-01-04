package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.*
import at.yawk.fiction.impl.PageParser
import org.jsoup.nodes.Element
import java.net.URI
import java.util.*

/**
 * @author yawkat
 */
internal object SearchPageParser : PageParser<Pageable.Page<Story>>() {
    // yes, Referrer is misspelled in HTTP.
    private val FANFICTION_REFERER = URI.create("https://www.fanfiction.net")

    override fun parse(root: Element): Pageable.Page<Story> {
        val stories = ArrayList<Story>()

        val storyContainers = root.select(".z-list")
        for (storyContainer in storyContainers) {
            val stitle = storyContainer.select("a.stitle").first()

            val id = Integer.parseInt(extractGroup(stitle.attr("href"), "/s/(\\d+)/.*"))
            val title = stitle.text()
            val imageTag = stitle.select("img").first()

            var image: Image? = null
            if (imageTag.hasAttr("data-original")) {
                val imageUrl = imageTag.absUrl("data-original")
                if (imageUrl.endsWith("/75/")) {
                    image = Image(
                            // full-size image
                            imageUrl = URI(imageUrl.substring(0, imageUrl.length - 4) + "/180/"),
                            // thumbnail
                            thumbnailUrl = URI(imageUrl),
                            referer = FANFICTION_REFERER)
                }
            }

            var author: Author? = null
            for (link in storyContainer.select("> a")) {
                val authorIdString = extractGroup(link.attr("href"), "/u/(\\d+)/.*")
                if (authorIdString != null) {
                    author = Author(
                            name = link.text(),
                            providerData = FfnAuthorProviderData(
                                    id = authorIdString.toInt()
                            )
                    )
                    break
                }
            }

            val descriptionTag = storyContainer.select(".z-indent").first()

            val description = RawText(descriptionTag.ownText())

            var story = Story(
                    title = title,
                    uri = URI("https://fanfiction.net/s/$id/1"),
                    image = image,
                    description = description,
                    author = author,
                    providerData = FfnStoryProviderData(
                            id = id
                    )
            ).merge(TagParser.parse(descriptionTag.select(".xgray").first()))
            stories.add(story)
        }


        var maxPage = 0
        for (pager in root.select("#content_wrapper_inner center a")) {
            maxPage = Math.max(maxPage, Integer.parseInt(extractGroup(pager.attr("href"), ".*p=(\\d+)")))
        }

        return Pageable.Page(stories, maxPage, false)
    }
}
