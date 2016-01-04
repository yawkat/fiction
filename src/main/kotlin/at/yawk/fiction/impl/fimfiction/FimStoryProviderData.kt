package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.StoryProviderData

/**
 * @author yawkat
 */
class FimStoryProviderData(
        val id: Int? = null,
        val tags: List<FimTag>? = null,
        val status: FimStatus? = null
) : StoryProviderData {

    override fun merge(other: StoryProviderData): StoryProviderData {
        if (other !is FimStoryProviderData) throw ClassCastException()
        return FimStoryProviderData(
                id = id ?: other.id,
                tags = tags ?: other.tags,
                status = status ?: other.status
        )
    }
}
