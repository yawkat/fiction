/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction.impl

import com.google.common.base.Charsets
import com.google.common.base.Splitter
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.intellij.lang.annotations.RegExp
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*
import java.util.regex.Pattern

/**
 * @author yawkat
 */
abstract class PageParser<T> {
    fun fetch(client: HttpClient, request: HttpUriRequest): T {
        return request(client).request(request).send()
    }

    public abstract fun parse(root: Element): T

    protected fun extractGroup(haystack: String, @RegExp regex: String): String? {
        val matcher = Pattern.compile(regex).matcher(haystack)
        if (matcher.matches()) {
            return matcher.group(1)
        } else {
            return null
        }
    }

    protected fun removeMatch(haystack: MutableCollection<String>, @RegExp regex: String): String? {
        val pattern = Pattern.compile(regex)
        val iterator = haystack.iterator()
        while (iterator.hasNext()) {
            val matcher = pattern.matcher(iterator.next())
            if (matcher.matches()) {
                iterator.remove()
                return matcher.group(1)
            }
        }
        return null
    }

    protected fun split(haystack: String, separator: String): MutableList<String> {
        return ArrayList(Splitter.on(separator).trimResults().splitToList(haystack))
    }

    /**
     * Parse the given integer string, ignoring invalid characters.

     * @return the parsed integer or null if [s] was `null`.
     */
    protected fun parseIntLenient(s: String?): Int {
        if (s == null) return 0
        var result = 0
        for (i in 0..s.length - 1) {
            val c = s[i]
            if (c >= '0' && c <= '9') {
                result = result * 10 + (c - '0')
            }
        }
        return result
    }

    // REQUEST API

    fun request(client: HttpClient): RequestBuilder {
        return RequestBuilder(client)
    }

    inner class RequestBuilder(
            val client: HttpClient
    ) {
        private object DefaultValidator : DocumentValidator {
            override fun validate(requestBuilder: RequestBuilder, root: Element) = true
        }

        private var request: HttpUriRequest? = null
        private var validator: DocumentValidator = DefaultValidator

        fun request(request: HttpUriRequest): RequestBuilder {
            this.request = request
            return this
        }

        fun get(uri: URI): RequestBuilder {
            return request(HttpGet(uri))
        }

        fun get(uri: String): RequestBuilder {
            return request(HttpGet(uri))
        }

        fun cookies(key: String, value: String): RequestBuilder {
            return cookies(listOf(key), listOf(value))
        }

        fun cookies(key1: String, value1: String, key2: String, value2: String): RequestBuilder {
            return cookies(Arrays.asList(key1, key2), Arrays.asList(value1, value2))
        }

        fun cookies(keys: List<String>, values: List<String>): RequestBuilder {
            assert(keys.size == values.size)
            val cookieString = StringBuilder()
            for (i in keys.indices) {
                val key = keys[i]
                val value = values[i]
                if (i != 0) {
                    cookieString.append(';')
                }
                cookieString.append(URLEncoder.encode(key, "UTF-8")).append('=').append(URLEncoder.encode(value, "UTF-8"))
            }
            return header("Cookie", cookieString.toString())
        }

        fun header(key: String, value: String): RequestBuilder {
            request!!.addHeader(key, value)
            return this
        }

        fun validator(validator: DocumentValidator): RequestBuilder {
            this.validator = validator
            return this
        }

        private fun requestRootOnce(): Document {
            val response = client.execute(request)
            val contentEncoding = response.entity.contentEncoding
            var charset = Charsets.UTF_8
            if (contentEncoding != null) {
                charset = Charset.forName(contentEncoding.value)
            }
            return response.entity.content.use { Jsoup.parse(it, charset.name(), request!!.uri.toString()) }
        }

        fun send(): T {
            var root: Document
            do {
                root = requestRootOnce()
            } while (!validator.validate(this, root))
            return parse(root)
        }
    }

    interface DocumentValidator {
        /**
         * Validate the given document root.

         * @return `true` if the document is valid, `false` if it should be refetched.
         *
         * @throws Exception If the request should be aborted.
         */
        fun validate(requestBuilder: PageParser.RequestBuilder, root: Element): Boolean
    }
}
