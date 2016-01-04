package at.yawk.fiction.impl.fanfiction

import at.yawk.fiction.impl.ParseTest
import org.testng.annotations.Test

/**
 * @author yawkat
 */
class ChapterPageParserTest {
    @Test
    fun test() {
        ParseTest.print(ParseTest.parse(ChapterPageParser, "https://www.fanfiction.net/s/7031899/1").first)
    }
}