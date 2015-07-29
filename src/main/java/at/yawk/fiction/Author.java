package at.yawk.fiction;

import at.yawk.fiction.impl.fanfiction.FfnAuthor;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
@JsonSubTypes({
        @JsonSubTypes.Type(FfnAuthor.class)
})
public abstract class Author {
    String name;
}
