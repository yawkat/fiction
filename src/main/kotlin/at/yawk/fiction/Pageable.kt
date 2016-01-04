package at.yawk.fiction

/**
 * @author yawkat
 */
interface Pageable<T> {
    /**
     * Get the page of the given index. Indexes start at 0.
     */
    fun getPage(page: Int): Page<T>

    data class Page<T>(
            val entries: List<T>,
            /**
             * The expected total page count or `null` if unknown.
             */
            val pageCount: Int?,
            /**
             * `true` if this is the last page, `false` if there are more following pages or
             * we don't know.
             */
            val last: Boolean
    )
}
