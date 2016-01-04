package at.yawk.fiction

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * @author yawkat
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@JsonSubTypes(
        JsonSubTypes.Type(HtmlText::class),
        JsonSubTypes.Type(RawText::class)
)
interface FormattedText

data class HtmlText(val html: String) : FormattedText
data class RawText(val text: String) : FormattedText