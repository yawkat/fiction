package at.yawk.fiction;

import lombok.Value;

/**
 * @author yawkat
 */
@Value
public class StoryChapterPair {
    private final Story story;
    private final Chapter chapter;
}
