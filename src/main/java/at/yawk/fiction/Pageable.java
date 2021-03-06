package at.yawk.fiction;

import java.util.List;
import lombok.Data;

/**
 * @author yawkat
 */
public interface Pageable<T> {
    /**
     * Get the page of the given index. Indexes start at 0.
     */
    Page<T> getPage(int i) throws Exception;

    @Data
    final class Page<T> {
        List<T> entries;
        /**
         * The expected total page count or <code>-1</code> if unknown.
         */
        int pageCount = -1;
        /**
         * <code>true</code> if this is the last page, <code>false</code> if there are more following pages or
         * we don't know.
         */
        boolean last = false;
    }
}
