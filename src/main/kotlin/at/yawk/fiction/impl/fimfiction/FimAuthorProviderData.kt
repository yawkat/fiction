package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.AuthorProviderData

/**
 * @author yawkat
 */
class FimAuthorProviderData(val id: String? = null) : AuthorProviderData {
    override fun merge(other: AuthorProviderData): AuthorProviderData {
        if (other !is FimAuthorProviderData) throw ClassCastException()
        return FimAuthorProviderData(id = id ?: other.id)
    }
}
