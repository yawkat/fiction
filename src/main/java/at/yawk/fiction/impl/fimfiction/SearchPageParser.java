package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.Pageable;
import at.yawk.fiction.impl.PageParser;
import java.util.ArrayList;
import org.jsoup.nodes.Element;

/**
 * @author yawkat
 */
class SearchPageParser extends PageParser<Pageable.Page<FimStory>> {
    @Override
    protected Pageable.Page<FimStory> create() {
        return new Pageable.Page<>();
    }

    @Override
    protected void parse(Element root, Pageable.Page<FimStory> target) throws Exception {
        target.setEntries(new ArrayList<>());

        for (Element storyContainer : root.select(".story_container")) {
            FimStory story = new FimStory();
            getParser(StoryParser.class).parse(storyContainer, story);
            target.getEntries().add(story);
        }
    }
}
