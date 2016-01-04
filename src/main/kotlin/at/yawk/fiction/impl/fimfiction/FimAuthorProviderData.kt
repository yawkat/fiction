/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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
