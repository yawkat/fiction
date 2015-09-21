package at.yawk.fiction.impl.fimfiction;

import at.yawk.fiction.Story;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author yawkat
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FimStory extends Story {
    private int id;
    private List<FimTag> tags;
    private FimStatus status;
}
