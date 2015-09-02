package at.yawk.fiction;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * @author yawkat
 */
@Value
@Wither
@Builder
public class HtmlText implements FormattedText {
    private final String html;

    public String toRawText() {
        return Jsoup.clean(html, Whitelist.none());
    }
}
