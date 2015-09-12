package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.impl.PageParser;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;

/**
 * @author yawkat
 */
class ShelvesParser extends PageParser<List<FimShelf>> {
    @Override
    protected List<FimShelf> create() {
        return new ArrayList<>();
    }

    @Override
    protected void parse(Element root, List<FimShelf> target) throws Exception {
        for (Element element : root.select(".bookshelves").first().select("> li > a")) {
            FimShelf shelf = new FimShelf();
            shelf.setId(parseIntLenient(extractGroup(element.attr("href"), ".*/bookshelf/(\\d+)/.*")));
            shelf.setName(element.ownText());

            target.add(shelf);
        }
    }
}
