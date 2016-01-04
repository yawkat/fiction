package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.AuthorProviderData

/**
 * @author yawkat
 */
class FfnAuthorProviderData(
        val id: Int? = null
) : AuthorProviderData {
    override fun merge(other: AuthorProviderData): AuthorProviderData {
        if (other !is FfnAuthorProviderData) throw ClassCastException()
        return FfnAuthorProviderData(id = id ?: other.id)
    }
}
