package at.yawk.fiction;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * @author yawkat
 */
@Value
@Wither
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
public class RawText implements FormattedText {
    private final String text;
}
