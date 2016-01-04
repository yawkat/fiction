/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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