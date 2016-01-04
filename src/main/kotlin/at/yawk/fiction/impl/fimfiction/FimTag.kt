package at.yawk.fiction.impl.fimfiction

import java.net.URL

/**
 * @author yawkat
 */
class FimTag(
        val id: String? = null,
        val name: String? = null,
        // for whatever reason, some icons have spaces in their url, which URI does not support. We use URL for that reason.
        val icon: URL? = null
)
