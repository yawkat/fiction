package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.impl.PageParser;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jsoup.nodes.Element;

/**
 * @author yawkat
 */
class StoryShelfParser extends PageParser<Map<FimShelf,Boolean>> {
    @Override
    protected Map<FimShelf, Boolean> create() {
        return new LinkedHashMap<>();
    }

    @Override
    protected void parse(Element root, Map<FimShelf, Boolean> target) throws Exception {
        for (Element shelfLink : root.select("#bookshelves-popup-list li > a")) {
            FimShelf shelf = new FimShelf();
            shelf.setId(Integer.parseInt(shelfLink.attr("data-bookshelf")));
            shelf.setName(shelfLink.select(".name").text());
            boolean selected = shelfLink.hasClass("selected");

            target.put(shelf, selected);
        }
    }
}
