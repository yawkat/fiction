package at.yawk.fiction

import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * @author yawkat
 */
data class Chapter(
        val name: String? = null,
        val text: FormattedText? = null,
        /**
         * Whether this chapter is read according to account data of this user. `null` means unknown or not logged
         * in.
         */
        val read: Boolean? = null,
        val providerData: ChapterProviderData
) : Mergeable<Chapter> {
    override fun merge(other: Chapter): Chapter {
        return Chapter(
                name = name ?: other.name,
                text = text ?: other.text,
                read = read ?: other.read,
                providerData = providerData.merge(other.providerData)
        )
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
interface ChapterProviderData : Mergeable<ChapterProviderData>, ProviderData