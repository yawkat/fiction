package at.yawk.fiction;

import at.yawk.fiction.impl.fanfiction.FfnSearchQuery;
import at.yawk.fiction.impl.fimfiction.FimSearchQuery;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(FfnSearchQuery.class),
        @JsonSubTypes.Type(FimSearchQuery.class),
})
public abstract class SearchQuery {}
