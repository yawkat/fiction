package at.yawk.fiction.impl.fanfiction

enum class FfnSearchOrder(
        val displayName: String,
        val id: Int
) {
    UPDATE_DATE("Update Date", 1),
    PUBLISH_DATE("Publish Date", 2),
    REVIEWS("Reviews", 3),
    FAVORITES("Favorites", 4),
    FOLLOWS("Follows", 5),
}
