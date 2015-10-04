package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.Image;
import at.yawk.fiction.Pageable;
import at.yawk.fiction.RawText;
import at.yawk.fiction.impl.PageParser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yawkat
 */
class SearchPageParser extends PageParser<Pageable.Page<FfnStory>> {
    @Override
    protected void parse(Element root, Pageable.Page<FfnStory> page) throws Exception {
        List<FfnStory> stories = new ArrayList<>();

        Elements storyContainers = root.select(".z-list");
        for (Element storyContainer : storyContainers) {
            Element stitle = storyContainer.select("a.stitle").first();

            FfnStory story = new FfnStory();
            story.setId(Integer.parseInt(extractGroup(stitle.attr("href"), "/s/(\\d+)/.*")));
            story.setTitle(stitle.text());
            Element image = stitle.select("img").first();
            if (image.hasAttr("data-original")) {
                story.setImage(new Image(new URI(image.absUrl("data-original"))));
            }
            story.setUri(new URI("https://fanfiction.net/s/" + story.getId() + "/1"));

            for (Element link : storyContainer.select("> a")) {
                String authorIdString = extractGroup(link.attr("href"), "/u/(\\d+)/.*");
                if (authorIdString != null) {
                    FfnAuthor author = new FfnAuthor();
                    author.setId(Integer.parseInt(authorIdString));
                    author.setName(link.text());
                    story.setAuthor(author);
                    break;
                }
            }

            Element descriptionTag = storyContainer.select(".z-indent").first();

            RawText description = new RawText();
            description.setText(descriptionTag.ownText());
            story.setDescription(description);

            getParser(TagParser.class).parse(descriptionTag.select(".xgray").first(), story);

            stories.add(story);
        }

        page.setEntries(stories);

        int maxPage = 0;
        for (Element pager : root.select("#content_wrapper_inner center a")) {
            maxPage = Math.max(maxPage, Integer.parseInt(extractGroup(pager.attr("href"), ".*p=(\\d+)")));
        }
        page.setPageCount(maxPage);
    }

    @Override
    protected Pageable.Page<FfnStory> create() {
        return new Pageable.Page<>();
    }
}
