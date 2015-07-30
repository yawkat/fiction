package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.Chapter;
import at.yawk.fiction.impl.PageParser;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.joda.time.Instant;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yawkat
 */
class TagParser extends PageParser<FfnStory> {
    @Override
    protected FfnStory create() {
        return new FfnStory();
    }

    @Override
    protected void parse(Element root, FfnStory target) throws Exception {
        Elements timeElements = root.select("span");
        List<String> items = split(root.text(), " - ");

        target.setGenres(Lists.transform(Arrays.asList(items.remove(2).split("/")), name -> {
            FfnGenre genre = new FfnGenre();
            genre.setName(name);
            return genre;
        }));

        target.setFavorites(parseIntLenient(removeMatch(items, "Favs: (\\d+)")));
        target.setFollows(parseIntLenient(removeMatch(items, "Follows: (\\d+)")));
        target.setWords(parseIntLenient(removeMatch(items, "Words: (\\d+)")));

        removeMatch(items, "Updated: (.*)");
        removeMatch(items, "Published: (.*)");
        target.setUpdateTime(new Instant(Long.parseLong(timeElements.first().attr("data-xutime")) * 1000));
        target.setPublishTime(new Instant(Long.parseLong(timeElements.last().attr("data-xutime")) * 1000));

        int chapterCount = Integer.parseInt(removeMatch(items, "Chapters: (\\d+)"));
        List<Chapter> chapters = new ArrayList<>(chapterCount);
        for (int i = 0; i < chapterCount; i++) {
            FfnChapter chapter = new FfnChapter();
            chapter.setIndex(i);
            chapters.add(chapter);
        }
        target.setChapters(chapters);
    }
}
