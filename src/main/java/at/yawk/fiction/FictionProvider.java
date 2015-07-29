package at.yawk.fiction;

/**
 * @author yawkat
 */
public interface FictionProvider {
    void fetchStory(Story story);

    void fetchChapter(Story story, Chapter chapter);

    SearchQuery createQuery();

    Pageable<? extends Story> search(SearchQuery query);
}
