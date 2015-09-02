package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.Metadata;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * @author yawkat
 */
@Value
@Wither
@Builder
public class FfnAuthorMetadata implements Metadata {
    int id;
}
