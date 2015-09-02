package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.*;
import at.yawk.fiction.impl.PageParser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yawkat
 */
class SearchPageParser extends PageParser<Pageable.Page<Story>> {
    @Override
    protected Pageable.Page<Story> parse(Element root) throws Exception {
        List<Story> stories = new ArrayList<>();

        Elements storyContainers = root.select(".z-list");
        for (Element storyContainer : storyContainers) {
            Element stitle = storyContainer.select("a.stitle").first();

            Story.StoryBuilder storyBuilder = Story.builder();
            FfnStoryMetadata.FfnStoryMetadataBuilder metadataBuilder = FfnStoryMetadata.builder();

            int id = Integer.parseInt(extractGroup(stitle.attr("href"), "/s/(\\d+)/.*"));
            metadataBuilder.id(id);

            storyBuilder.title(stitle.text());
            storyBuilder.image(new Image(new URI(stitle.select("img").attr("data-original")), null));
            storyBuilder.uri(new URI("https://fanfiction.net/s/" + id + "/1"));

            for (Element link : storyContainers.select("> a")) {
                String authorIdString = extractGroup(link.attr("href"), "/u/(\\d+)/.*");
                if (authorIdString != null) {
                    storyBuilder.author(
                            Author.builder()
                                    .name(link.text())
                                    .metadata(new FfnAuthorMetadata(Integer.parseInt(authorIdString)))
                                    .build()
                    );
                    break;
                }
            }

            Element descriptionTag = storyContainer.select(".z-indent").first();
            storyBuilder.description(new RawText(descriptionTag.ownText()));

            getParser(TagParser.class).parse(descriptionTag.select(".xgray").first());

            storyBuilder.metadata(metadataBuilder.build());
            stories.add(storyBuilder.build());
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
