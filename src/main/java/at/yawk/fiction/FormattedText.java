package at.yawk.fiction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author yawkat
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(HtmlText.class),
        @JsonSubTypes.Type(RawText.class),
})
public interface FormattedText {}
