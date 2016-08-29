/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.ao3;

import at.yawk.fiction.HtmlText;
import at.yawk.fiction.impl.PageParser;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Loads *all* chapters of a story
 *
 * @author yawkat
 */
class ChapterPageParser extends PageParser<Ao3Story> {
    @Override
    protected Ao3Story create() {
        return new Ao3Story();
    }

    @Override
    protected void parse(Element root, Ao3Story target) throws Exception {
        @SuppressWarnings("unchecked")
        List<Ao3Chapter> chapters = (List<Ao3Chapter>) target.getChapters();
        if (chapters == null) {
            target.setChapters(chapters = new ArrayList<>());
        }
        Elements chaptersList = root.select("#chapters");
        // single-chapter pages have no .chapter elements but rather a single work text
        Elements chaptersListChildren = chaptersList.select("> .chapter");
        if (!chaptersListChildren.isEmpty()) {
            chaptersList = chaptersListChildren;
        }
        for (Element element : chaptersList) {
            int chapterIndex = parseIntLenient(element.id()) - 1;
            if (chapterIndex == -1) { chapterIndex = 0; }
            while (chapters.size() <= chapterIndex) {
                if (chapters.getClass() != ArrayList.class) {
                    target.setChapters(chapters = new ArrayList<>(chapters));
                }
                chapters.add(new Ao3Chapter());
            }

            Ao3Chapter chapter = chapters.get(chapterIndex);
            List<Element> children = new ArrayList<>(element.children());

            // in single-chapter texts this may be the "work text" h3, in which case the match will fail and we'll
            // just ignore it
            Element title = children.remove(0).select("> h3.title").first();
            if (title != null) {
                chapter.setId(parseIntLenient(extractGroup(title.select("a").attr("href"), ".*/(\\d+)")));
                chapter.setName(extractGroup(title.ownText(), "\\s*: (.*)\\s*"));
            }

            // this approach should lead to only one copy, excluding the outerHtml calls
            @SuppressWarnings("NonConstantStringShouldBeStringBuffer")
            String html = "";
            for (Element child : children) {
                // landmarks are invisible
                child.select(".landmark").remove();

                // tiny optimization to avoid copying...
                if (html.isEmpty()) {
                    html = child.outerHtml();
                } else {
                    // concat only does one copy
                    //noinspection CallToStringConcatCanBeReplacedByOperator
                    html = html.concat(child.outerHtml());
                }
            }
            HtmlText text = new HtmlText();
            text.setHtml(html);
            chapter.setText(text);
        }
    }
}
