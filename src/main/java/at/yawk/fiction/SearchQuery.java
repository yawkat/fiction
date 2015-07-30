package at.yawk.fiction;

import at.yawk.fiction.impl.fanfiction.FfnSearchQuery;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(FfnSearchQuery.class)
})
public abstract class SearchQuery {}
