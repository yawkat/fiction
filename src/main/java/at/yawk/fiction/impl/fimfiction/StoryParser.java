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

        target.setId(Integer.parseInt(root.select(".story_container").attr("data-story")));
        Element storyName = root.select(".story_name").first();
        target.setTitle(storyName.text());
        target.setUri(URI.create(storyName.absUrl("href")));

        FimAuthor author = new FimAuthor();
        Element authorLink = root.select(".author a").first();
        String authorHref = authorLink.attr("href");
        author.setId(extractGroup(authorHref, "/user/(\\d)+/.*"));
        author.setName(authorLink.text());
        target.setAuthor(author);

        Element descriptionElement = storyContainer.select(".description").first();
        Element storyImage = descriptionElement.select(".story_container__story_image img").first();
        if (storyImage != null) {
            Image image = new Image();
            if (storyImage.hasAttr("data-src")) {
                image.setImageUrl(URI.create(storyImage.absUrl("data-src")));
            }
            if (storyImage.hasAttr("src")) {
                image.setThumbnailUrl(URI.create(storyImage.absUrl("src")));
            }
            target.setImage(image);
        }

        Element descriptionText = descriptionElement.select("> .description-text").first();
        if (descriptionText != null) {
            HtmlText html = new HtmlText();
            html.setHtml(descriptionText.html());
            target.setDescription(html);
        }

        List<FimTag> tags = new ArrayList<>();

        for (Element tagElement : storyContainer.select(".story-tags a")) {
            FimTag tag = new FimTag();
            tag.setId(tagElement.attr("data-tag"));
            tag.setName(tagElement.text());
            tag.setIcon(null);
            tags.add(tag);
        }

        target.setTags(tags);

        Elements footer = storyContainer.select(".chapters-footer");
        if (footer.select(".completed-status-incomplete") != null) {
            target.setStatus(FimStatus.INCOMPLETE);
        } else if (footer.select(".completed-status-complete") != null) {
            target.setStatus(FimStatus.COMPLETED);
        } else if (footer.select(".completed-status-hiatus") != null) {
            target.setStatus(FimStatus.ON_HIATUS);
        } else if (footer.select(".completed-status-cancelled") != null) {
            target.setStatus(FimStatus.CANCELLED);
        }

        List<FimChapter> chapters = new ArrayList<>();
        for (Element chapterContainer : storyContainer.select(".chapters > li > div")) {
            if (chapterContainer.hasClass("chapter_expander")) { continue; }

            Element chapterLink = chapterContainer.select(".chapter-title").first();

            FimChapter chapter = new FimChapter();
            chapter.setId(parseIntLenient(extractGroup(
                    chapterContainer.select(".word_count .drop-down a").first().attr("href"), "/chapters/download/(\\d+)/txt")));
            chapter.setIndex(parseIntLenient(extractGroup(
                    chapterLink.attr("href"), ".*/story/\\d+/[^/]*/(\\d+)(:?/.*)?")) - 1);
            chapter.setName(chapterLink.text());
            chapter.setWordCount(parseIntLenient(chapterContainer.select(".word_count .word-count-number").first().text()));

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
