/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.ao3;

import at.yawk.fiction.HtmlText;
import at.yawk.fiction.Pageable;
import at.yawk.fiction.impl.PageParser;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author yawkat
 */
class SearchPageParser extends PageParser<Pageable.Page<Ao3Story>> {
    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormat.forPattern("dd MMM yyyy");

    @Override
    protected Pageable.Page<Ao3Story> create() {
        return new Pageable.Page<>();
    }

    @Override
    protected void parse(Element root, Pageable.Page<Ao3Story> target) throws Exception {
        List<Ao3Story> stories = new ArrayList<>();

        for (Element element : root.select("#main > .work.index > li")) {
            Ao3Story story = new Ao3Story();
            story.setId(parseIntLenient(element.attr("id")));
            story.setUri(URI.create("https://archiveofourown.org/works/" + story.getId()));
            Elements titleAndAuthor = element.select("> div > h4 > a");
            story.setTitle(titleAndAuthor.first().text());
            story.setAuthor(new Ao3Author());
            story.getAuthor().setName(titleAndAuthor.get(1).text());
            story.setUpdateTime(LocalDate.parse(element.select(".datetime").first().text(), DATE_PATTERN)
                                        .toDateTimeAtStartOfDay()
                                        .toInstant());

            story.setRequiredTags(map(element.select(".required-tags > li .text"), Element::text));
            story.setWarnings(map(element.select(".tags > .warnings"), Element::text));
            story.setRelationships(map(element.select(".tags > .relationships"), Element::text));
            story.setCharacters(map(element.select(".tags > .characters"), Element::text));
            story.setFreeforms(map(element.select(".tags > .freeforms"), Element::text));

            Element summary = element.select(".summary").first();
            if (summary != null) {
                HtmlText description = new HtmlText();
                description.setHtml(summary.html());
                story.setDescription(description);
            }

            story.setLanguage(element.select(".stats dd.language").text());
            story.setWords(parseIntLenient(element.select(".stats dd.words").first().text()));
            Element chapters = element.select(".stats dd.chapters").first();
            if (chapters != null) {
                String[] parts = chapters.text().split("/");
                List<Ao3Chapter> chapterList;
                if (story.getChapters() == null) {
                    story.setChapters(chapterList = new ArrayList<>());
                } else {
                    //noinspection unchecked
                    chapterList = (List<Ao3Chapter>) story.getChapters();
                }
                int chapterCount = parseIntLenient(parts[0]);
                for (int i = 0; i < chapterCount; i++) {
                    while (chapterList.size() <= i) { chapterList.add(new Ao3Chapter()); }
                }
                story.setChapterGoal(parts[1].equals("?") ? null : parseIntLenient(parts[1]));
            } else {
                story.setChapterGoal(null);
            }
            Element comments = element.select(".stats dd.comments").first();
            if (comments != null) {
                story.setCommentCount(parseIntLenient(comments.text()));
            } else {
                story.setCommentCount(0);
            }
            Element kudos = element.select(".stats dd.kudos").first();
            if (kudos != null) {
                story.setKudoCount(parseIntLenient(kudos.text()));
            } else {
                story.setKudoCount(0);
            }
            Element bookmarks = element.select(".stats dd.bookmarks").first();
            if (bookmarks != null) {
                story.setBookmarkCount(parseIntLenient(bookmarks.text()));
            } else {
                story.setBookmarkCount(0);
            }
            Element hits = element.select(".stats dd.hits").first();
            if (hits != null) {
                story.setHitCount(parseIntLenient(hits.text()));
            } else {
                story.setHitCount(0);
            }

            stories.add(story);
        }

        target.setPageCount(1);
        target.setEntries(stories);
        for (Element pageLink : root.select(".pagination > li")) {
            if (!pageLink.hasClass("next")) {
                target.setPageCount(parseIntLenient(pageLink.text()));
            } else if (!pageLink.select(".disabled").isEmpty()) {
                target.setLast(true);
            }
        }
        if (target.getPageCount() == 1) { target.setLast(true); }
    }

    private <I, O> List<O> map(List<I> list, Function<I, O> function) {
        return new ArrayList<>(Lists.transform(list, function));
    }
}
