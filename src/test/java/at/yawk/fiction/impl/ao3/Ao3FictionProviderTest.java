/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.ao3;

import at.yawk.fiction.Chapter;
import at.yawk.fiction.HtmlText;
import at.yawk.fiction.Pageable;
import at.yawk.fiction.impl.PageParserProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author yawkat
 */
public class Ao3FictionProviderTest {
    private Ao3FictionProvider fictionProvider;

    @BeforeTest
    public void init() {
        fictionProvider = new Ao3FictionProvider(
                new PageParserProvider(),
                HttpClientBuilder.create().build(),
                new ObjectMapper()
        );
    }

    @Test
    public void testSuggestTags() throws IOException {
        String query = "rain";
        List<Ao3Tag> suggestions = fictionProvider.suggestTags(query);
        assertTrue(suggestions.size() > 5);
        for (Ao3Tag suggestion : suggestions) {
            assertTrue(suggestion.getName().toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US)));
        }
    }

    @Test
    public void testSuggestFandoms() throws IOException {
        String query = "Mass";
        List<Ao3Tag> suggestions = fictionProvider.suggestFandoms(query);
        assertTrue(!suggestions.isEmpty());
        for (Ao3Tag suggestion : suggestions) {
            assertTrue(suggestion.getName().toLowerCase(Locale.US).contains(query.toLowerCase(Locale.US)),
                       suggestion.toString());
        }
    }

    @Test
    public void testSearch() throws Exception {
        Ao3SearchQuery query = new Ao3SearchQuery();
        query.setFandoms(Collections.singleton(new Ao3Tag("Mass Effect", "Mass Effect")));
        query.setOrder(Ao3SearchOrder.HITS);

        Pageable<Ao3Story> search = fictionProvider.search(query);
        Pageable.Page<Ao3Story> firstPage = search.getPage(0);
        Pageable.Page<Ao3Story> secondPage = search.getPage(1);
        assertFalse(firstPage.isLast());
        assertFalse(secondPage.isLast());
        assertNotEquals(firstPage.getEntries(), secondPage.getEntries());
        assertEquals(firstPage.getEntries().size(), secondPage.getEntries().size());
    }

    @Test
    public void testEmptySearch() throws Exception {
        Ao3SearchQuery query = new Ao3SearchQuery();
        query.setTitle("xyzzzzz");
        Pageable<Ao3Story> pageable = fictionProvider.search(query);
        Pageable.Page<Ao3Story> page = pageable.getPage(0);
        assertTrue(page.getEntries().isEmpty());
        assertTrue(page.isLast());
        assertEquals(1, page.getPageCount());
    }

    @Test
    public void testSinglePageSearch() throws Exception {
        Ao3SearchQuery query = new Ao3SearchQuery();
        query.setTitle("One Day in the Life of Maribel Hearn");
        Pageable<Ao3Story> pageable = fictionProvider.search(query);
        Pageable.Page<Ao3Story> page = pageable.getPage(0);
        assertFalse(page.getEntries().isEmpty());
        assertTrue(page.isLast());
        assertEquals(1, page.getPageCount());
    }

    @Test
    public void testGetStory() throws Exception {
        Ao3Story story = new Ao3Story();
        story.setId(1859244);
        fictionProvider.fetchStory(story);

        assertFalse(story.getChapters().isEmpty());
        for (Chapter chapter : story.getChapters()) {
            assertNotNull(chapter.getText());
            assertFalse(((HtmlText) chapter.getText()).toRawText().trim().isEmpty());
        }
    }

    @Test
    public void testGetSingleChapterStory() throws Exception {
        Ao3Story story = new Ao3Story();
        story.setId(421810);
        fictionProvider.fetchStory(story);
        assertEquals(1, story.getChapters().size());
        for (Chapter chapter : story.getChapters()) {
            assertNotNull(chapter.getText());
            assertFalse(((HtmlText) chapter.getText()).toRawText().trim().isEmpty());
        }
        System.out.println(story);
    }
}