package at.yawk.fiction.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.jsoup.Jsoup;

/**
 * @author yawkat
 */
public class ParseTest {
    public static <T> T parse(Class<? extends PageParser<T>> parser, String uri) throws Exception {
        return parse(parser, new URI(uri));
    }

    public static <T> T parse(Class<? extends PageParser<T>> parser, URI uri) throws Exception {
        try (InputStream in = uri.toURL().openStream()) {
            T target = new PageParserProvider().getParser(parser).create();
            new PageParserProvider().getParser(parser).parse(Jsoup.parse(in,
                                                                         StandardCharsets.UTF_8.name(),
                                                                         uri.toString()), target);
            return target;
        }
    }

    public static String toJson(Object o) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        mapper.findAndRegisterModules();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    }

    public static void print(Object o) throws Exception {
        System.out.println(toJson(o));
    }
}
