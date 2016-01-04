package at.yawk.fiction.impl

import at.yawk.fiction.ProviderManager
import at.yawk.fiction.impl.fanfiction.FfnFictionProvider
import at.yawk.fiction.impl.fimfiction.FimFictionProvider
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.impl.client.HttpClientBuilder
import org.jsoup.Jsoup
import java.net.URI
import java.nio.charset.StandardCharsets

/**
 * @author yawkat
 */
object ParseTest {
    fun <T> parse(parser: PageParser<T>, uri: String): T {
        return parse(parser, URI(uri))
    }

    fun <T> parse(parser: PageParser<T>, uri: URI): T {
        return uri.toURL().openStream().use { parser.parse(Jsoup.parse(it, StandardCharsets.UTF_8.name(), uri.toString())) }
    }

    fun toJson(o: Any): String {
        return makeObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(o)
    }

    fun makeObjectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)
        mapper.findAndRegisterModules()
        val httpClient = HttpClientBuilder.create().build()
        mapper.registerModule(ProviderManager(listOf(FimFictionProvider(httpClient, mapper), FfnFictionProvider(httpClient))).JacksonModule())
        return mapper
    }

    fun print(o: Any) {
        println(toJson(o))
    }
}
