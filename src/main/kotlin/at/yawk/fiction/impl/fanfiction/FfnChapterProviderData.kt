package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.ChapterProviderData

/**
 * @author yawkat
 */
data class FfnChapterProviderData(val index: Int? = null) : ChapterProviderData {
    override fun merge(other: ChapterProviderData): ChapterProviderData {
        if (other !is FfnChapterProviderData) throw ClassCastException()
        return FfnChapterProviderData(index = index ?: other.index)
    }
}