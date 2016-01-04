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