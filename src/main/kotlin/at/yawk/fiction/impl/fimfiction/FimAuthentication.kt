/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

/**
 * @author yawkat
 */
data class FimAuthentication(
        val username: String,
        val password: String,
        val otp: String,
        val isRemember: Boolean
) {
    constructor(username: String, password: String) : this(username, password, "", true)
}
