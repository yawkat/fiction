/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction

import com.fasterxml.jackson.annotation.JsonProperty
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

data class HtmlText(@JsonProperty("html") val html: String) : FormattedText
data class RawText(@JsonProperty("text") val text: String) : FormattedText