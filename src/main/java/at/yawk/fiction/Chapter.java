package at.yawk.fiction;

import at.yawk.fiction.impl.fanfiction.FfnChapter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
@JsonSubTypes({
        @JsonSubTypes.Type(FfnChapter.class)
})
public abstract class Chapter {
    String name;
    FormattedText text;
}
