/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction

import java.net.URI

/**
 * @author yawkat
 */
data class Image(
        val imageUrl: URI?,
        val thumbnailUrl: URI?,
        /**
         * The HTTP referer required to fetch these images. Note that the header is called `Referer`, this is a
         * misspelling in the HTTP standard.
         */
        @SuppressWarnings("SpellCheckingInspection")
        val referer: URI? = null
) : Mergeable<Image> {
    override fun merge(other: Image): Image {
        return Image(
                imageUrl = imageUrl ?: other.imageUrl,
                thumbnailUrl = thumbnailUrl ?: other.thumbnailUrl,
                referer = referer ?: other.referer
        )
    }
}