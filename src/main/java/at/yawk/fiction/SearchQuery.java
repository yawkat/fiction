package at.yawk.fiction;

import at.yawk.fiction.impl.fanfiction.FfnSearchQuery;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
@JsonSubTypes({
        @JsonSubTypes.Type(FfnSearchQuery.class)
})
public abstract class SearchQuery {}
