package at.yawk.fiction

/**
 * @author yawkat
 */
interface FictionProvider {
    val providedTypes: List<Class<out ProviderData>>

    fun fetchStory(story: Story): Story

    fun fetchChapter(story: Story, chapter: Chapter): Pair<Story, Chapter>

    fun search(query: SearchQuery): Pageable<out Story>
}
