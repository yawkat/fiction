package at.yawk.fiction.impl;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.intellij.lang.annotations.RegExp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * @author yawkat
 */
public abstract class PageParser<T> {
    PageParserProvider provider;

    public final T parse(HttpClient client, HttpUriRequest request) throws Exception {
        return request(client).request(request).send();
    }

    public final void parse(HttpClient client, HttpUriRequest request, T target) throws Exception {
        request(client).request(request).target(target).send();
    }

    protected abstract T create();

    protected abstract void parse(Element root, T target) throws Exception;

    protected String extractGroup(String haystack, @RegExp String regex) throws IOException {
        Matcher matcher = Pattern.compile(regex).matcher(haystack);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    protected String removeMatch(Collection<String> haystack, @RegExp String regex) {
        Pattern pattern = Pattern.compile(regex);
        for (Iterator<String> iterator = haystack.iterator(); iterator.hasNext(); ) {
            Matcher matcher = pattern.matcher(iterator.next());
            if (matcher.matches()) {
                iterator.remove();
                return matcher.group(1);
            }
        }
        return null;
    }

    protected List<String> split(String haystack, String separator) {
        return new ArrayList<>(Splitter.on(separator).trimResults().splitToList(haystack));
    }

    protected <P extends PageParser<?>> P getParser(Class<P> type) {
        return provider.getParser(type);
    }

    /**
     * Parse the given integer string, ignoring invalid characters.
     *
     * @return the parsed integer or 0 if the input was <code>null</code>.
     */
    protected int parseIntLenient(@Nullable String s) {
        int val = 0;
        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    val = val * 10 + (c - '0');
                }
            }
        }
        return val;
    }

    // REQUEST API

    public final RequestBuilder request(HttpClient client) {
        return new RequestBuilder(client);
    }

    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public class RequestBuilder {
        private final HttpClient client;
        private HttpUriRequest request;
        private T value;
        private Element root;

        public RequestBuilder request(HttpUriRequest request) {
            this.request = request;
            return this;
        }

        public RequestBuilder get(URI uri) {
            return request(new HttpGet(uri));
        }

        public RequestBuilder get(String uri) {
            return request(new HttpGet(uri));
        }

        public RequestBuilder cookies(String key, String value) {
            return cookies(Collections.singletonList(key), Collections.singletonList(value));
        }

        public RequestBuilder cookies(String key1, String value1, String key2, String value2) {
            return cookies(Arrays.asList(key1, key2), Arrays.asList(value1, value2));
        }

        @SneakyThrows(UnsupportedEncodingException.class)
        public RequestBuilder cookies(List<String> keys, List<String> values) {
            assert keys.size() == values.size();
            StringBuilder cookieString = new StringBuilder();
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = values.get(i);
                if (i != 0) { cookieString.append(';'); }
                cookieString.append(URLEncoder.encode(key, "UTF-8"))
                        .append('=')
                        .append(URLEncoder.encode(value, "UTF-8"));
            }
            return header("Cookie", cookieString.toString());
        }

        public RequestBuilder header(String key, String value) {
            request.addHeader(key, value);
            return this;
        }

        public RequestBuilder target(T value) {
            this.value = value;
            return this;
        }

        private void requestRoot() throws IOException {
            HttpResponse response = client.execute(request);
            Header contentEncoding = response.getEntity().getContentEncoding();
            Charset charset = Charsets.UTF_8;
            if (contentEncoding != null) {
                charset = Charset.forName(contentEncoding.getValue());
            }
            try (InputStream in = response.getEntity().getContent()) {
                root = Jsoup.parse(in, charset.name(), request.getURI().toString());
            }
        }

        public T send() throws Exception {
            if (root == null) { requestRoot(); }
            if (value == null) { value = create(); }
            parse(root, value);
            return value;
        }
    }
}
