package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.impl.ParseTest;
import org.testng.annotations.Test;

/**
 * @author yawkat
 */
public class ChapterPageParserTest {
    @Test
    public void test() throws Exception {
        ParseTest.print(ParseTest.parse(ChapterPageParser.class,
                                        "https://www.fanfiction.net/s/11106521/1").getStory());
    }
}