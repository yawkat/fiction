package at.yawk.fiction

import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.joda.time.Instant
import java.net.URI

/**
 * @author yawkat
 */
data class Story(
        val title: String? = null,
        val image: Image? = null,
        val uri: URI? = null,
        val description: FormattedText? = null,
        val chapters: List<Chapter>? = null,
        val author: Author? = null,
        val publishTime: Instant? = null,
        val updateTime: Instant? = null,
        val providerData: StoryProviderData
) : Mergeable<Story> {
    override fun merge(other: Story): Story {
        return Story(
                title = title ?: other.title,
                image = image ?: other.image,
                uri = uri ?: other.uri,
                description = description ?: other.description,
                chapters = Mergeable.merge(chapters, other.chapters),
                author = author ?: other.author,
                publishTime = publishTime ?: other.publishTime,
                updateTime = updateTime ?: other.updateTime,
                providerData = providerData.merge(other.providerData)
        )
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
interface StoryProviderData : Mergeable<StoryProviderData>, ProviderData