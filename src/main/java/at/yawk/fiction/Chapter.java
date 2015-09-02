package at.yawk.fiction;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * @author yawkat
 */
@Value
@Wither
@Builder
public class Chapter implements Metadatable {
    String name;
    FormattedText text;

    Metadata metadata;
}
