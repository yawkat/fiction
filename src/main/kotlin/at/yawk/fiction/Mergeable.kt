package at.yawk.fiction

fun <T : Mergeable<T>> T?.merge(b: T?): T? =
        if (b == null) this else this?.merge(b)

/**
 * @author yawkat
 */
interface Mergeable<T : Mergeable<T>> {
    companion object {
        fun <T : Mergeable<T>> merge(a: List<T>?, b: List<T>?): List<T>? {
            if (a == null) return b
            if (b == null) return a
            if (a.size > b.size) {
                return a.mapIndexed { i, t -> if (i < b.size) t.merge(b[i]) else t }
            } else {
                return b.mapIndexed { i, t -> if (i < a.size) t.merge(a[i]) else t }
            }
        }
    }

    /**
     * Merge the values of this object with the given object. values present in both objects will use the value in this object.
     */
    fun merge(other: T): T
}
