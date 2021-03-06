package at.yawk.fiction;

import at.yawk.fiction.impl.ao3.Ao3Author;
import at.yawk.fiction.impl.fanfiction.FfnAuthor;
import at.yawk.fiction.impl.fimfiction.FimAuthor;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(FfnAuthor.class),
        @JsonSubTypes.Type(FimAuthor.class),
        @JsonSubTypes.Type(Ao3Author.class),
})
public abstract class Author {
    String name;
}
