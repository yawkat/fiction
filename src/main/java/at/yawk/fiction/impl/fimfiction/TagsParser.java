package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.impl.PageParser;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;

/**
 * @author yawkat
 */
class TagsParser extends PageParser<List<FimTag>> {
    @Override
    protected List<FimTag> create() {
        return new ArrayList<>();
    }

    @Override
    protected void parse(Element root, List<FimTag> target) throws Exception {
        for (Element element : root.select("ul.tags-dropdown").first().select(">li")) {
            FimTag tag = new FimTag();
            tag.setName(element.attr("data-name"));
            tag.setId(element.attr("data-tag"));

            Element icon = element.select(".icon img").first();
            if (icon != null) {
                tag.setIcon(new URL(icon.absUrl("src")));
            }
            target.add(tag);
        }
    }
}
