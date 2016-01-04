package at.yawk.fiction

import com.fasterxml.jackson.databind.module.SimpleModule

/**
 * @author yawkat
 */
class ProviderManager(val providers: List<FictionProvider>) {
    private val providersByDataType = providers
            .flatMap { provider -> provider.providedTypes.map { provided -> Pair(provided, provider) } }
            .toMap()

    fun getProvider(story: Story) = getProvider(story.providerData)
    fun getProvider(author: Author) = getProvider(author.providerData)
    fun getProvider(chapter: Chapter) = getProvider(chapter.providerData)

    fun getProvider(providerData: ProviderData) =
            providersByDataType[providerData.javaClass] ?: throw UnknownProviderException()

    inner class JacksonModule : SimpleModule() {
        init {
            registerSubtypes(*providersByDataType.keys.toTypedArray())
        }
    }

    class UnknownProviderException : Exception()
}