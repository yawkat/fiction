package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.impl.PageParser
import org.jsoup.nodes.Element

/**
 * @author yawkat
 */
internal object SubCategoryPageParser : PageParser<List<FfnSubCategory>>() {
    override fun parse(root: Element): List<FfnSubCategory> {
        return root.select("#list_output tr td > div").map {
            FfnSubCategory(
                    name = it.select("a").text(),
                    estimatedStoryCount = parseUnitedDouble(extractGroup(
                            it.select("span").text(), "\\((\\d+(\\.\\d+)?[KM]?)\\)") ?: "")
            )
        }.toList()
    }

    private fun parseUnitedDouble(text: String): Int {
        val unit = text[text.length - 1]
        if (Character.isDigit(unit)) {
            return Integer.parseInt(text)
        } else {
            val mul: Int
            when (unit) {
                'K' -> mul = 1000
                'M' -> mul = 1000000
                else -> throw UnsupportedOperationException("Unsupported unit '$unit'")
            }
            return Math.round(java.lang.Float.parseFloat(text.substring(0, text.length - 1)) * mul)
        }
    }
}
