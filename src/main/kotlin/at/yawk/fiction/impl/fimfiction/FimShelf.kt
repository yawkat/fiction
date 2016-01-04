package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.Mergeable

/**
 * @author yawkat
 */
data class FimShelf(
        val id: Int? = null,
        val name: String? = null
) : Mergeable<FimShelf> {
    override fun merge(other: FimShelf): FimShelf {
        return FimShelf(
                id = id ?: other.id,
                name = name ?: other.name
        )
    }
}
