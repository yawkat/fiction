package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.HtmlText;
import at.yawk.fiction.impl.PageParser;
import org.jsoup.nodes.Element;

/**
 * @author yawkat
 */
class ChapterParser extends PageParser<FimChapter> {
    @Override
    protected FimChapter create() {
        return new FimChapter();
    }

    @Override
    protected void parse(Element root, FimChapter target) throws Exception {
        target.setName(root.select("h3").first().text());
        HtmlText html = new HtmlText();
        html.setHtml(root.select("body").first().html());
        target.setText(html);
    }
}
