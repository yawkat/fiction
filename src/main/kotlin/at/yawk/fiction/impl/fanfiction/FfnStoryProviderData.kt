package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.StoryProviderData

/**
 * @author yawkat
 */
data class FfnStoryProviderData(
        val id: Int? = null,
        val genres: List<FfnGenre>? = null,
        val favorites: Int? = null,
        val follows: Int? = null,
        val words: Int? = null
) : StoryProviderData {
    override fun merge(other: StoryProviderData): StoryProviderData {
        if (other !is FfnStoryProviderData) throw ClassCastException()
        return FfnStoryProviderData(
                id = id ?: other.id,
                genres = genres ?: other.genres,
                favorites = favorites ?: other.favorites,
                follows = follows ?: other.follows,
                words = words ?: other.words
        )
    }
}