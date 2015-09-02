package at.yawk.fiction;

import java.net.URI;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import org.joda.time.Instant;

/**
 * @author yawkat
 */
@Value
@Wither
@Builder
public class Story implements Metadatable {
    String title;
    Image image;
    URI uri;
    FormattedText description;
    List<? extends Chapter> chapters;
    Author author;
    Instant publishTime;
    Instant updateTime;

    Metadata metadata;
}
