package at.yawk.fiction.impl

import java.util.*

/**
 * @author yawkat
 */
class ListTransformer<T>(val emptyFactory: () -> T) {
    private var transformers = ArrayList<(T) -> T>()

    fun addTransformer(transformer: (T) -> T) {
        transformers.add(transformer)
    }

    fun apply(list: List<T>?): List<T> {
        return transformers.mapIndexed { i, transformer -> transformer(list?.getOrNull(i) ?: emptyFactory()) }
    }
}