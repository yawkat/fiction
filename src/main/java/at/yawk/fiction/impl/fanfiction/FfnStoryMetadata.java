package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.Metadata;
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
public class FfnStoryMetadata implements Metadata {
    int id;
    List<FfnGenre> genres;
    int favorites;
    int follows;
    int words;
}
