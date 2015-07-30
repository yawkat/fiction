package at.yawk.fiction;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
public class RawText implements FormattedText {
    private String text;
}
