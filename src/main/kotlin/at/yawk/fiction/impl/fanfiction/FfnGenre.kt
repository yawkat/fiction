package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.Mergeable

/**
 * @author yawkat
 */
data class FfnGenre(
        val id: Int? = null,
        val name: String? = null
) : Mergeable<FfnGenre> {
    override fun merge(other: FfnGenre): FfnGenre {
        return FfnGenre(
                id = id ?: other.id,
                name = name ?: other.name
        )
    }
}