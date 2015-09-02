package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.SearchQuery;
import com.google.common.base.Function;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * @author yawkat
 */
@Value
@Wither
@Builder
public class FfnSearchQuery implements SearchQuery {
    FfnSubCategory category;

    FfnSearchOrder order = FfnSearchOrder.UPDATE_DATE;
    List<FfnGenre> genresIncluded = Collections.emptyList();
    List<FfnGenre> genresExcluded = Collections.emptyList();
    FfnRating minRating = null;
    FfnRating maxRating = null;
    List<FfnWorld> worldsIncluded = Collections.emptyList();
    List<FfnWorld> worldsExcluded = Collections.emptyList();
    List<FfnCharacter> charactersIncluded = Collections.emptyList();
    List<FfnCharacter> charactersExcluded = Collections.emptyList();
    TimeRange timeRange = null;
    FfnLanguage language = null;
    FfnStatus status = null;
    int words = 0;
    boolean wordsIsMinimum = true;
    boolean pairingIncluded = false;
    boolean pairingExcluded = false;

    URI build(int page) {
        StringBuilder builder = new StringBuilder("https://www.fanfiction.net/");
        builder.append(category.getCategory().getId()).append('/');
        try {
            builder.append(URLEncoder.encode(category.getName().replace(' ', '-'), "UTF-8")).append("/?");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        if (order != null) {
            builder.append("srt=").append(order.getId()).append('&');
        }
        for (int i = 0; i < genresIncluded.size(); i++) {
            FfnGenre genre = genresIncluded.get(i);
            builder.append('g').append((char) ('a' + i)).append('=').append(genre.getId()).append('&');
        }
        if (minRating != null || maxRating != null) {
            builder.append("r=").append(minRating == null ? 0 : minRating.getId());
            if (minRating != maxRating) {
                builder.append('0').append(maxRating == null ? 0 : maxRating.getId());
            }
            builder.append('&');
        }
        if (words != 0 || !wordsIsMinimum) {
            builder.append("len=");
            int thousands = words / 1000;
            builder.append(thousands);
            if (!wordsIsMinimum) { builder.append('1'); }
            builder.append('&');
        }
        if (language != null) {
            builder.append("lan=").append(language.getId()).append('&');
        }
        if (status != null) {
            builder.append("s=").append(status.getId()).append('&');
        }
        if (timeRange != null) {
            builder.append("t=").append(timeRange.getId()).append('&');
        }
        if (pairingIncluded) {
            builder.append("pm=1&");
        }
        if (pairingExcluded) {
            builder.append("_pm=1&");
        }
        appendItemList(builder, "g", genresIncluded, FfnGenre::getId);
        appendItemList(builder, "_g", genresExcluded, FfnGenre::getId);
        appendItemList(builder, "c", charactersIncluded, FfnCharacter::getId);
        appendItemList(builder, "_c", charactersExcluded, FfnCharacter::getId);
        appendItemList(builder, "v", worldsIncluded, FfnWorld::getId);
        appendItemList(builder, "_v", worldsExcluded, FfnWorld::getId);

        builder.append("p=").append(page + 1);
        return URI.create(builder.toString());
    }

    private static <T> void appendItemList(StringBuilder builder, String tag, List<T> l, Function<T, Integer> map) {
        for (int i = 0; i < l.size(); i++) {
            T item = l.get(i);
            builder.append(tag).append((char) ('a' + i)).append('=').append(map.apply(item)).append('&');
        }
    }
}
