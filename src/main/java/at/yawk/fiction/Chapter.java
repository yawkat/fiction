package at.yawk.fiction;

import at.yawk.fiction.impl.fanfiction.FfnChapter;
import at.yawk.fiction.impl.fimfiction.FimChapter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(FfnChapter.class),
        @JsonSubTypes.Type(FimChapter.class),
})
public abstract class Chapter {
    String name;
    FormattedText text;
    /**
     * Whether this chapter is read according to account data of this user. {@code null} means unknown or not logged
     * in.
     */
    Boolean read;
}
