package at.yawk.fiction;

import com.fasterxml.jackson.annotation.JsonSubTypes;

/**
 * @author yawkat
 */
@JsonSubTypes({
        @JsonSubTypes.Type(HtmlText.class),
        @JsonSubTypes.Type(RawText.class),
})
public interface FormattedText {}
