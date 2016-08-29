/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl.ao3;

import at.yawk.fiction.Chapter;
import at.yawk.fiction.FictionProvider;
import at.yawk.fiction.Pageable;
import at.yawk.fiction.SearchQuery;
import at.yawk.fiction.Story;
import at.yawk.fiction.impl.PageParserProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author yawkat
 */
@RequiredArgsConstructor
public class Ao3FictionProvider implements FictionProvider {
    private final PageParserProvider pageParserProvider;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public List<Ao3Tag> suggestTags(String query) throws IOException {
        return suggestTags0(query, "https://archiveofourown.org/autocomplete/tag?term=");
    }

    public List<Ao3Tag> suggestFandoms(String query) throws IOException {
        return suggestTags0(query, "https://archiveofourown.org/autocomplete/fandom?term=");
    }

    private List<Ao3Tag> suggestTags0(String query, String baseUrl) throws IOException {
        HttpGet get = new HttpGet(baseUrl + URLEncoder.encode(query, "UTF-8"));
        get.addHeader("Accept", "application/json");
        HttpResponse response = httpClient.execute(get);
        return objectMapper.readValue(
                response.getEntity().getContent(),
                CollectionType.construct(List.class, objectMapper.constructType(Ao3Tag.class))
        );
    }

    @Override
    public void fetchStory(Story story) throws Exception {
        pageParserProvider.getParser(ChapterPageParser.class).request(httpClient)
                .target((Ao3Story) story)
                .get("https://archiveofourown.org/works/" + ((Ao3Story) story).getId() +
                     "?view_full_work=true&view_adult=true")
                .send();
    }

    @Override
    public void fetchChapter(Story story, Chapter chapter) throws Exception {
        fetchStory(story); // loads all chapters
    }

    @Override
    public SearchQuery createQuery() {
        return new Ao3SearchQuery();
    }

    @Override
    @SneakyThrows(UnsupportedEncodingException.class)
    public Pageable<Ao3Story> search(SearchQuery _query) {
        Ao3SearchQuery query = (Ao3SearchQuery) _query;

        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("utf8", "\u2713"));
        if (query.getOrder() != null) {
            parameters.add(new BasicNameValuePair("work_search[sort_column]", query.getOrder().getId()));
        }
        if (query.getOrderDirection() != null) {
            parameters.add(new BasicNameValuePair("work_search[sort_direction]",
                                                  query.getOrderDirection() == OrderDirection.ASCENDING ?
                                                          "asc" :
                                                          "desc"));
        }
        if (query.getTitle() != null) {
            parameters.add(new BasicNameValuePair("work_search[title]", query.getTitle()));
        }
        if (query.getFreeText() != null) {
            parameters.add(new BasicNameValuePair("work_search[query]", query.getFreeText()));
        }
        if (query.getAuthor() != null) {
            parameters.add(new BasicNameValuePair("work_search[creator]", query.getAuthor()));
        }
        if (query.getPeriod() != null) {
            parameters.add(new BasicNameValuePair("work_search[revised_at]", query.getFreeText()));
        }
        if (query.isComplete()) {
            parameters.add(new BasicNameValuePair("work_search[complete]", "1"));
        }
        if (query.isSingleChapter()) {
            parameters.add(new BasicNameValuePair("work_search[single_chapter]", "1"));
        }
        if (query.getRating() != null) {
            parameters.add(new BasicNameValuePair("work_search[rating_ids]",
                                                  String.valueOf(query.getRating().getId())));
        }
        if (query.getFandoms() != null) {
            parameters.add(new BasicNameValuePair("work_search[fandom_names]", Joiner.on(",")
                    .join(Collections2.transform(query.getFandoms(), Ao3Tag::getId))));
        }
        if (query.getTags() != null) {
            parameters.add(new BasicNameValuePair("work_search[other_tag_names]", Joiner.on(",")
                    .join(Collections2.transform(query.getTags(), Ao3Tag::getId))));
        }
        if (query.getWordCount() != null) {
            parameters.add(new BasicNameValuePair("work_search[word_count]", query.getWordCount().toQueryString()));
        }
        if (query.getHits() != null) {
            parameters.add(new BasicNameValuePair("work_search[hits]", query.getHits().toQueryString()));
        }
        if (query.getKudos() != null) {
            parameters.add(new BasicNameValuePair("work_search[kudos_count]", query.getKudos().toQueryString()));
        }
        if (query.getComments() != null) {
            parameters.add(new BasicNameValuePair("work_search[comments_count]", query.getComments().toQueryString()));
        }
        if (query.getBookmarks() != null) {
            parameters.add(new BasicNameValuePair("work_search[bookmarks_count]",
                                                  query.getBookmarks().toQueryString()));
        }
        StringBuilder urlBuilder = new StringBuilder("https://archiveofourown.org/works/search?");
        for (NameValuePair parameter : parameters) {
            urlBuilder.append(URLEncoder.encode(parameter.getName(), "utf-8"));
            urlBuilder.append('=');
            urlBuilder.append(URLEncoder.encode(parameter.getValue(), "utf-8"));
            urlBuilder.append('&');
        }
        urlBuilder.append("page=");
        String url = urlBuilder.toString();
        return page -> {
            System.out.println(url + (page + 1));
            return pageParserProvider.getParser(SearchPageParser.class)
                    .request(httpClient)
                    .get(url + (page + 1))
                    .send();
        };
    }
}
