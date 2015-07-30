package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.impl.ParseTest;
import org.testng.annotations.Test;

/**
 * @author yawkat
 */
public class SearchPageParserTest {
    @Test
    public void test() throws Exception {
        ParseTest.print(ParseTest.parse(SearchPageParser.class,
                                        "https://www.fanfiction.net/game/Elder-Scroll-series/?&srt=5&r=10&p=8"));
    }
}