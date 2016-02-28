/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.PropertyMetadata
import com.fasterxml.jackson.databind.PropertyName
import com.fasterxml.jackson.databind.deser.CreatorProperty
import java.util.*
import kotlin.reflect.KCallable
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.javaType

/**
 * `open` for easy use in [JsonDeserialize.using]
 *
 * @author yawkat
 */
open class DefaultParameterDeserializer<T : Any>(private val constructor: KCallable<T>) : JsonDeserializer<T>() {
    private val parameters = constructor.parameters.map { Pair(it.name, it) }.toMap()

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        // if START_OBJECT, skip to first field
        if (p.currentToken == JsonToken.START_OBJECT) p.nextFieldName() ?: throw ctxt.wrongTokenException(p, JsonToken.FIELD_NAME, null)

        val parameterValues = HashMap<KParameter, Any>()
        while (p.currentToken == JsonToken.FIELD_NAME) {
            val name = p.currentName!!
            val parameter = parameters[name]
            p.nextToken()

            if (parameter == null) {
                if (!ctxt.handleUnknownProperty(p, this, constructor.returnType.javaClass, name)) {
                    ctxt.handleUnknownProperty(p, this, constructor.returnType.javaClass, name)
                    p.skipChildren()
                }
                continue
            }

            val type = ctxt.typeFactory.constructType(parameter.type.javaType)
            val valueDeserializer = ctxt.findContextualValueDeserializer(type, CreatorProperty(
                    PropertyName.construct(parameter.name),
                    type, null, null, null, null, -1, null,
                    if (parameter.isOptional) PropertyMetadata.STD_OPTIONAL else PropertyMetadata.STD_REQUIRED
            ))

            val value = valueDeserializer.deserialize(p, ctxt)
            parameterValues[parameter] = value

            p.nextToken()
        }

        if (p.currentToken != JsonToken.END_OBJECT) throw ctxt.wrongTokenException(p, JsonToken.END_OBJECT, null)

        return constructor.callBy(parameterValues)
    }
}

