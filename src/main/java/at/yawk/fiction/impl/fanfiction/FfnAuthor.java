package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.Author;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author yawkat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FfnAuthor extends Author {
    int id;
}
