package at.yawk.fiction;

/**
 * @author yawkat
 */
public interface FictionProvider {
    void fetchStory(Story story) throws Exception;

    void fetchChapter(Story story, Chapter chapter) throws Exception;

    SearchQuery createQuery();

    Pageable<? extends Story> search(SearchQuery query);
}
