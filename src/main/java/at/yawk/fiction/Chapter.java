package at.yawk.fiction;

import at.yawk.fiction.impl.fanfiction.FfnChapter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(FfnChapter.class)
})
public abstract class Chapter {
    String name;
    FormattedText text;
}
