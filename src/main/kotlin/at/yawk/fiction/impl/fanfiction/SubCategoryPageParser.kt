/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.impl.PageParser
import org.jsoup.nodes.Element

/**
 * @author yawkat
 */
internal class SubCategoryPageParser(val category: FfnCategory) : PageParser<List<FfnSubCategory>>() {
    override fun parse(root: Element): List<FfnSubCategory> {
        return root.select("#list_output tr td > div").map {
            FfnSubCategory(
                    category = category,
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
