/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.impl.ParseTest
import org.testng.annotations.Test

/**
 * @author yawkat
 */
class SearchPageParserTest {
    @Test
    fun test() {
        ParseTest.print(ParseTest.parse(SearchPageParser, "https://www.fanfiction.net/game/Elder-Scroll-series/?&srt=5&r=10&p=8"))
    }
}