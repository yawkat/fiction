/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

import java.net.URL

/**
 * @author yawkat
 */
class FimTag(
        val id: String? = null,
        val name: String? = null,
        // for whatever reason, some icons have spaces in their url, which URI does not support. We use URL for that reason.
        val icon: URL? = null
)
