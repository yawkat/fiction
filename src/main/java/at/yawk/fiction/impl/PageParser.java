package at.yawk.fiction.impl;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
        T target = create();
        parse(client, request, target);
        return target;
    }

    public final void parse(HttpClient client, HttpUriRequest request, T target) throws Exception {
        HttpResponse response = client.execute(request);
        Header contentEncoding = response.getEntity().getContentEncoding();
        Charset charset = Charsets.UTF_8;
        if (contentEncoding != null) {
            charset = Charset.forName(contentEncoding.getValue());
        }
        try (InputStream in = response.getEntity().getContent()) {
            parse(Jsoup.parse(in, charset.name(), request.getURI().toString()), target);
        }
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
}
