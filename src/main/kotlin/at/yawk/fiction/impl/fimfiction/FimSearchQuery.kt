/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fimfiction

import at.yawk.fiction.DefaultParameterDeserializer
import at.yawk.fiction.SearchQuery
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.net.URI
import java.net.URLEncoder

private class Deserializer : DefaultParameterDeserializer<FimSearchQuery>(::FimSearchQuery)

/**
 * @author yawkat
 */
@JsonDeserialize(using = Deserializer::class)
data class FimSearchQuery(
        val includedTags: Set<FimTag>? = null,
        val excludedTags: Set<FimTag>? = null,
        val order: FimOrder? = null,
        val publishTime: FimTimeRange? = null,
        val status: FimStatus? = null,
        val minWords: Int? = null,
        val maxWords: Int? = null,
        val shelf: FimShelf? = null,
        val unread: Boolean? = null
) : SearchQuery {
    internal fun build(page: Int): URI {
        val uriBuilder = StringBuilder("https://www.fimfiction.net/")

        if (shelf == null) {
            uriBuilder.append("stories?")
        } else {
            uriBuilder.append("bookshelf/").append(shelf.id).append('?')
        }

        if (order != null) {
            uriBuilder.append("order=")
            when (order) {
                FimOrder.RELEVANCE -> uriBuilder.append("relevance")
                FimOrder.APPROVED -> uriBuilder.append("latest")
                FimOrder.HEAT -> uriBuilder.append("heat")
                FimOrder.UPDATED -> uriBuilder.append("updated")
                FimOrder.RATING -> uriBuilder.append("top")
                FimOrder.VIEWS -> uriBuilder.append("views")
                FimOrder.WORDS -> uriBuilder.append("words")
                FimOrder.COMMENTS -> uriBuilder.append("comments")
            }
            uriBuilder.append('&')
        }

        if (publishTime != null) {
            uriBuilder.append("published_timeframe").append(publishTime.ordinal + 1).append('&')
        }

        if (status != null) {
            uriBuilder.append("status").append(status.name.toLowerCase().replace('_', '-')).append('&')
        }

        if (minWords != null) {
            uriBuilder.append("minimum_words").append(minWords).append('&')
        }

        if (maxWords != null) {
            uriBuilder.append("maximum_words").append(maxWords).append('&')
        }

        if (includedTags != null) {
            for (includedTag in includedTags) {
                uriBuilder.append("tags[]=").append(URLEncoder.encode(includedTag.id, "UTF-8")).append('&')
            }
        }

        if (excludedTags != null) {
            for (excludedTag in excludedTags) {
                uriBuilder.append("tags[]=-").append(URLEncoder.encode(excludedTag.id, "UTF-8")).append('&')
            }
        }

        if (unread != null) {
            uriBuilder.append("unread=").append(if (unread) '1' else '0').append('&')
        }

        uriBuilder.append("page=").append(page + 1)
        return URI.create(uriBuilder.toString())
    }
}
