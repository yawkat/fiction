package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.HtmlText;
import at.yawk.fiction.Image;
import at.yawk.fiction.impl.PageParser;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * @author yawkat
 */
class StoryParser extends PageParser<FimStory> {
    @Override
    protected FimStory create() {
        return new FimStory();
    }

    @Override
    protected void parse(Element root, FimStory target) throws Exception {
        // only applicable outside a search when the root is a html element
        Element storyContainer = root.select(".story_container").first();

        Elements contentBox = storyContainer.select(".story_content_box");
        target.setId(parseIntLenient(contentBox.attr("id")));
        Element storyName = contentBox.select(".story_name").first();
        target.setTitle(storyName.text());
        target.setUri(URI.create(storyName.absUrl("href")));

        FimAuthor author = new FimAuthor();
        Elements authorLink = contentBox.select(".author a");
        String authorHref = authorLink.attr("href");
        author.setId(authorHref.substring(authorHref.lastIndexOf('/') + 1));
        author.setName(authorLink.text());
        target.setAuthor(author);

        Element descriptionElement = storyContainer.select(".description").first();
        Element storyImage = descriptionElement.select(".story_image").first();
        if (storyImage != null) {
            Image image = new Image();
            image.setImageUrl(URI.create(storyImage.absUrl("data-src")));
            image.setThumbnailUrl(URI.create(storyImage.absUrl("src")));
            target.setImage(image);
        }

        Element separator = descriptionElement.select(">hr").first();
        if (separator != null) {
            StringBuilder descriptionHtml = new StringBuilder();
            Node e = separator;
            while ((e = e.nextSibling()) != null) {
                descriptionHtml.append(e.outerHtml());
            }

            HtmlText html = new HtmlText();
            html.setHtml(descriptionHtml.toString());
            target.setDescription(html);
        }

        List<FimTag> tags = new ArrayList<>();

        for (Element categoryElement : descriptionElement.select("> .story_category")) {
            FimTag tag = new FimTag();
            tag.setId(extractGroup(categoryElement.attr("href"), ".*tags\\[\\]=(.+)"));
            tag.setName(categoryElement.text());
            tag.setIcon(null);
            tags.add(tag);
        }

        for (Element characterIcon : root.select(".extra_story_data .inner_data").first().select("> .character_icon")) {
            FimTag tag = new FimTag();
            // we can't find out id here :(
            tag.setName(characterIcon.attr("title"));
            tag.setIcon(new URL(characterIcon.select("img").first().absUrl("src")));
            tags.add(tag);
        }

        target.setTags(tags);

        Element statusElement = storyContainer.select(".chapters .bottom > span").first();
        switch (statusElement.attr("title")) {
        case "Complete":
            target.setStatus(FimStatus.COMPLETED);
            break;
        case "Incomplete":
            target.setStatus(FimStatus.INCOMPLETE);
            break;
        case "On Hiatus":
            target.setStatus(FimStatus.ON_HIATUS);
            break;
        case "Cancelled":
            target.setStatus(FimStatus.CANCELLED);
            break;
        }

        List<FimChapter> chapters = new ArrayList<>();
        for (Element chapterContainer : storyContainer.select(".chapters .chapter_container")) {
            if (chapterContainer.hasClass("chapter_expander")) { continue; }

            Element chapterLink = chapterContainer.select(".chapter_link").first();

            FimChapter chapter = new FimChapter();
            chapter.setId(parseIntLenient(extractGroup(
                    chapterContainer.select(".download_container a").first().attr("href"), ".*chapter=(\\d+)")));
            chapter.setIndex(parseIntLenient(extractGroup(
                    chapterLink.attr("href"), ".*/story/\\d+/(\\d+)(:?/.*)?")) - 1);
            chapter.setName(chapterLink.text());
            chapter.setWordCount(parseIntLenient(chapterContainer.select(".word_count").first().ownText()));

            Element chapterReadIcon = chapterContainer.select(".chapter-read-icon").first();
            if (chapterReadIcon != null) {
                chapter.setRead(chapterReadIcon.hasClass("chapter-read"));
            } else {
                chapter.setRead(null);
            }

            chapters.add(chapter);
        }
        target.setChapters(chapters);

        // todo: parse first published + last modified
    }
}
