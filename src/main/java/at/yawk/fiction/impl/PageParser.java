package at.yawk.fiction.impl;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        HttpResponse response = client.execute(request);
        Header contentEncoding = response.getEntity().getContentEncoding();
        Charset charset = Charsets.UTF_8;
        if (contentEncoding != null) {
            charset = Charset.forName(contentEncoding.getValue());
        }
        try (InputStream in = response.getEntity().getContent()) {
            return parse(in, charset, request.getURI());
        }
    }

    public final T parse(InputStream input, Charset charset, URI baseUri) throws Exception {
        T target = create();
        parse(Jsoup.parse(input, charset.name(), baseUri.toString()), target);
        return target;
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
}
