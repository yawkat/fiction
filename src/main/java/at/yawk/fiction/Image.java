package at.yawk.fiction;

import java.net.URI;
import javax.annotation.Nullable;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * @author yawkat
 */
@Value
@Wither
@Builder
public class Image {
    @Nullable URI imageUrl;
    @Nullable URI thumbnailUrl;
}
