package at.yawk.fiction.impl.fimfiction;

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
public class FimChapter extends Chapter {
    private int index;
    private int wordCount;
}
