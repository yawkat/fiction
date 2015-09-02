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
public class Author implements Metadatable {
    String name;
    Metadata metadata;
}
