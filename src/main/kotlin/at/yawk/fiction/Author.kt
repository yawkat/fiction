package at.yawk.fiction

import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * @author yawkat
 */
data class Author(
        val name: String? = null,
        val providerData: AuthorProviderData
) : Mergeable<Author> {
    override fun merge(other: Author): Author {
        return Author(
                name = name ?: other.name,
                providerData = providerData.merge(other.providerData)
        )
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
interface AuthorProviderData : Mergeable<AuthorProviderData>, ProviderData