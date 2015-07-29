package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.Story;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author yawkat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FfnStory extends Story {
    int id;
}
