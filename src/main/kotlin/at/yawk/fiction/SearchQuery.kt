package at.yawk.fiction

import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * @author yawkat
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
interface SearchQuery