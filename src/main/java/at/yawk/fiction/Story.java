package at.yawk.fiction;

import at.yawk.fiction.impl.ao3.Ao3Story;
import at.yawk.fiction.impl.fanfiction.FfnStory;
import at.yawk.fiction.impl.fimfiction.FimStory;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.net.URI;
import java.util.List;
import lombok.Data;
import org.joda.time.Instant;

/**
 * @author yawkat
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(FfnStory.class),
        @JsonSubTypes.Type(FimStory.class),
        @JsonSubTypes.Type(Ao3Story.class),
})
public abstract class Story {
    String title;
    Image image;
    URI uri;
    FormattedText description;
    List<? extends Chapter> chapters;
    Author author;
    Instant publishTime;
    Instant updateTime;
}
