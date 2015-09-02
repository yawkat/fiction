package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.*;
import at.yawk.fiction.impl.PageParser;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yawkat
 */
class ChapterPageParser extends PageParser<StoryChapterPair> {
    @SuppressWarnings("unchecked")
    @Override
    protected StoryChapterPair parse(Element root) throws Exception {
        Element profileTop = root.select("#profile_top").first();

        if (profileTop == null) { throw new NotFoundException(); }

        Story.StoryBuilder storyBuilder = Story.builder();
        storyBuilder.title(profileTop.select("> b.xcontrast_txt").text());
        // todo: author, tags

        @Nullable Element select = root.select("#chap_select").first();
        int chapterCount = 1;
        Elements chapterOptions = null;
        if (select != null) {
            chapterOptions = select.select("option");
            chapterCount = chapterOptions.size();
        }
        List<Chapter> chapters = new ArrayList<>();
        storyBuilder.chapters(chapters);

        for (int i = 0; i < chapterCount; i++) {
            Chapter.ChapterBuilder chapterBuilder = Chapter.builder();
            chapterBuilder.metadata(new FfnChapterMetadata(i));
            if (select != null) {
                String name = chapterOptions.get(i).text();
                chapterBuilder.name(name.substring(name.indexOf(' ') + 1)); // remove leading enumeration
            }
            chapters.add(chapterBuilder.build());
        }

        Chapter.ChapterBuilder currentChapter = Chapter.builder();
        currentChapter.text(new HtmlText(root.select("#storytext").html()));

        return new StoryChapterPair(storyBuilder.build(), currentChapter.build());
    }
}
