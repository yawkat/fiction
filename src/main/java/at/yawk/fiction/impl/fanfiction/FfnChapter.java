package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.Chapter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author yawkat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FfnChapter extends Chapter {
    /**
     * Starts at 0
     */
    int index;
}
