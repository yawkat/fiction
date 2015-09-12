package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.SearchQuery;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;

/**
 * @author yawkat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FimSearchQuery extends SearchQuery {
    @Nullable private Set<FimTag> includedTags;
    @Nullable private Set<FimTag> excludedTags;
    @Nullable private FimOrder order;
    @Nullable private FimTimeRange publishTime;
    @Nullable private FimStatus status;
    @Nullable private Integer minWords;
    @Nullable private Integer maxWords;
    @Nullable private FimShelf shelf;

    @SneakyThrows(UnsupportedEncodingException.class)
    URI build(int page) {
        StringBuilder uriBuilder = new StringBuilder("https://www.fimfiction.net/");

        if (shelf == null) {
            uriBuilder.append("stories?");
        } else {
            uriBuilder.append("bookshelf/").append(shelf.getId()).append('?');
        }

        if (order != null) {
            uriBuilder.append("order=");
            switch (order) {
            case RELEVANCE:
                uriBuilder.append("relevance");
                break;
            case APPROVED:
                uriBuilder.append("latest");
                break;
            case HEAT:
                uriBuilder.append("heat");
                break;
            case UPDATED:
                uriBuilder.append("updated");
                break;
            case RATING:
                uriBuilder.append("top");
                break;
            case VIEWS:
                uriBuilder.append("views");
                break;
            case WORDS:
                uriBuilder.append("words");
                break;
            case COMMENTS:
                uriBuilder.append("comments");
                break;
            }
            uriBuilder.append('&');
        }

        if (publishTime != null) {
            uriBuilder.append("published_timeframe").append(publishTime.ordinal() + 1).append('&');
        }

        if (status != null) {
            uriBuilder.append("status").append(status.name().toLowerCase().replace('_', '-')).append('&');
        }

        if (minWords != null) {
            uriBuilder.append("minimum_words").append(minWords).append('&');
        }

        if (maxWords != null) {
            uriBuilder.append("maximum_words").append(maxWords).append('&');
        }

        if (includedTags != null) {
            for (FimTag includedTag : includedTags) {
                uriBuilder.append("tags[]=").append(URLEncoder.encode(includedTag.getId(), "UTF-8")).append('&');
            }
        }

        if (excludedTags != null) {
            for (FimTag excludedTag : excludedTags) {
                uriBuilder.append("tags[]=-").append(URLEncoder.encode(excludedTag.getId(), "UTF-8")).append('&');
            }
        }

        uriBuilder.append("page=").append(page + 1);
        return URI.create(uriBuilder.toString());
    }
}
