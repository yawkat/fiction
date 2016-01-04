package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.SearchQuery
import java.io.UnsupportedEncodingException
import java.net.URI
import java.net.URLEncoder

/**
 * @author yawkat
 */
data class FfnSearchQuery(
        val category: FfnSubCategory? = null,

        val order: FfnSearchOrder? = FfnSearchOrder.UPDATE_DATE,
        val genresIncluded: List<FfnGenre> = emptyList(),
        val genresExcluded: List<FfnGenre> = emptyList(),
        val minRating: FfnRating? = null,
        val maxRating: FfnRating? = null,
        val worldsIncluded: List<FfnWorld> = emptyList(),
        val worldsExcluded: List<FfnWorld> = emptyList(),
        val charactersIncluded: List<FfnCharacter> = emptyList(),
        val charactersExcluded: List<FfnCharacter> = emptyList(),
        val timeRange: TimeRange? = null,
        val language: FfnLanguage? = null,
        val status: FfnStatus? = null,
        val words: Int? = null,
        val wordsIsMinimum: Boolean = true,
        val pairingIncluded: Boolean = false,
        val pairingExcluded: Boolean = false
) : SearchQuery {

    internal fun build(page: Int): URI {
        if (category == null) throw IllegalStateException()

        val builder = StringBuilder("https://www.fanfiction.net/")
        builder.append(category.id).append('/')
        try {
            builder.append(URLEncoder.encode(category.name!!.replace(' ', '-'), "UTF-8")).append("/?")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException(e)
        }

        if (order != null) {
            builder.append("srt=").append(order.id).append('&')
        }
        for (i in genresIncluded.indices) {
            val genre = genresIncluded[i]
            builder.append('g').append(('a' + i).toChar()).append('=').append(genre.id).append('&')
        }
        if (minRating != null || maxRating != null) {
            builder.append("r=").append(if (minRating == null) 0 else minRating.id)
            if (minRating !== maxRating) {
                builder.append('0').append(if (maxRating == null) 0 else maxRating.id)
            }
            builder.append('&')
        }
        if (words != null || !wordsIsMinimum) {
            builder.append("len=")
            val thousands = (words ?: 0) / 1000
            builder.append(thousands)
            if (!wordsIsMinimum) {
                builder.append('1')
            }
            builder.append('&')
        }
        if (language != null) {
            builder.append("lan=").append(language.id).append('&')
        }
        if (status != null) {
            builder.append("s=").append(status.id).append('&')
        }
        if (timeRange != null) {
            builder.append("t=").append(timeRange.id).append('&')
        }
        if (pairingIncluded) {
            builder.append("pm=1&")
        }
        if (pairingExcluded) {
            builder.append("_pm=1&")
        }
        appendItemList(builder, "g", genresIncluded, { it.id!! })
        appendItemList(builder, "_g", genresExcluded, { it.id!! })
        appendItemList(builder, "c", charactersIncluded, { it.id })
        appendItemList(builder, "_c", charactersExcluded, { it.id })
        appendItemList(builder, "v", worldsIncluded, { it.id })
        appendItemList(builder, "_v", worldsExcluded, { it.id })

        builder.append("p=").append(page + 1)
        return URI.create(builder.toString())
    }

    private fun <T> appendItemList(builder: StringBuilder, tag: String, l: List<T>, map: (T) -> Int) {
        for (i in l.indices) {
            builder.append(tag).append(('a' + i).toChar())
                    .append('=').append(map(l[i])).append('&')
        }
    }
}
