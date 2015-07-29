package at.yawk.fiction.impl.fanfiction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FfnSearchOrder {
    UPDATE_DATE("Update Date", 1),
    PUBLISH_DATE("Publish Date", 2),
    REVIEWS("Reviews", 3),
    FAVORITES("Favorites", 4),
    FOLLOWS("Follows", 5);

    private final String name;
    private final int id;
}
