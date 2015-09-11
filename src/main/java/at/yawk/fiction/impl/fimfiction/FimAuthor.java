package at.yawk.fiction.impl.fimfiction;

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
public class FimAuthor extends Author {
    private String id;
}
