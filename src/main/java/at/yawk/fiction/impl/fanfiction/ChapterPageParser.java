package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.Chapter;
import at.yawk.fiction.HtmlText;
import at.yawk.fiction.NotFoundException;
import at.yawk.fiction.Story;
import at.yawk.fiction.impl.PageParser;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Value;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yawkat
 */
class ChapterPageParser extends PageParser<ChapterPageParser.StoryChapterPair> {
    @Override
    protected StoryChapterPair create() {
        return new StoryChapterPair(new FfnStory(), new FfnChapter());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void parse(Element root, StoryChapterPair target) throws Exception {
        Element profileTop = root.select("#profile_top").first();

        if (profileTop == null) { throw new NotFoundException(); }

        target.story.setTitle(profileTop.select("> b.xcontrast_txt").text());
        // todo: author, tags

        @Nullable Element select = root.select("#chap_select").first();
        int chapterCount = 1;
        Elements chapterOptions = null;
        if (select != null) {
            chapterOptions = select.select("option");
            chapterCount = chapterOptions.size();
        }
        List<FfnChapter> chapters = (List<FfnChapter>) target.story.getChapters();
        if (chapters == null) {
            chapters = new ArrayList<>();
            target.story.setChapters(chapters);
        }
        // remove trailing chapters
        if (chapterCount < chapters.size()) {
            chapters.subList(chapterCount, chapters.size()).clear();
        }

        for (int i = 0; i < chapterCount; i++) {
            FfnChapter chapter;
            if (i < chapters.size()) {
                chapter = chapters.get(i);
            } else {
                chapter = new FfnChapter();
                chapters.add(chapter);
            }

            chapter.setIndex(i);
            if (select != null) {
                String name = chapterOptions.get(i).text();
                chapter.setName(name.substring(name.indexOf(' ') + 1)); // remove leading enumeration
            }
        }

        HtmlText text = new HtmlText();
        text.setHtml(root.select("#storytext").html());
        target.chapter.setText(text);
    }

    @Value
    public static class StoryChapterPair {
        Story story;
        Chapter chapter;
    }
}
