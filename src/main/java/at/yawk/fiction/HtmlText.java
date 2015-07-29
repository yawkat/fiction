package at.yawk.fiction;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * @author yawkat
 */
@Data
public class HtmlText implements FormattedText {
    private String html;

    public String toRawText() {
        return Jsoup.clean(html, Whitelist.none());
    }
}
