/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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
