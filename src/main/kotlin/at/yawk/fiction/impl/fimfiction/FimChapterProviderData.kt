package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.ChapterProviderData

/**
 * @author yawkat
 */
data class FimChapterProviderData(val id: Int? = null, val index: Int? = null, val wordCount: Int? = null) : ChapterProviderData {
    override fun merge(other: ChapterProviderData): ChapterProviderData {
        if (other !is FimChapterProviderData) throw ClassCastException()
        return FimChapterProviderData(
                id = id ?: other.id,
                index = index ?: other.index,
                wordCount = wordCount ?: other.wordCount
        )
    }
}
