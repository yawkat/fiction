/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package at.yawk.fiction

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * @author yawkat
 */
class DefaultConstructorJsonReaderTest {
    lateinit var objectMapper: ObjectMapper

    @BeforeMethod
    fun setUp() {
        objectMapper = ObjectMapper()
        objectMapper.registerModule(KotlinModule())
    }

    @JsonDeserialize(using = Test1Deserializer::class)
    public data class TestClass1(val a: Int, val b: Int = 2, val c: Int)

    private class Test1Deserializer : DefaultParameterDeserializer<TestClass1>(::TestClass1)

    @Test
    fun testConstructor() {
        Assert.assertEquals(
                objectMapper.readValue("{\"a\": 2, \"b\": 3, \"c\": 4}", TestClass1::class.java),
                TestClass1(2, 3, 4)
        )
        Assert.assertEquals(
                objectMapper.readValue("{\"a\": 2, \"c\": 4}", TestClass1::class.java),
                TestClass1(2, 2, 4)
        )
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes(JsonSubTypes.Type(value = TestClass2::class, name = "tc2"))
    public interface TestInterface

    @JsonDeserialize(using = Test2Deserializer::class)
    public data class TestClass2(val a: Int, val b: Int = 2, val c: Int) : TestInterface

    private class Test2Deserializer : DefaultParameterDeserializer<TestClass2>(::TestClass2)

    @Test
    fun testSubclass() {
        Assert.assertEquals(
                objectMapper.readValue("{\"type\": \"tc2\", \"a\": 2, \"c\": 4}", TestInterface::class.java),
                TestClass2(2, 2, 4)
        )
    }

    @JsonDeserialize(using = Test3Deserializer::class)
    public data class TestClass3(val a: List<Int>)

    private class Test3Deserializer : DefaultParameterDeserializer<TestClass3>(::TestClass3)

    @Test
    fun testGeneric() {
        Assert.assertEquals(
                objectMapper.readValue("{\"a\": [1, 2, 3]}", TestClass3::class.java),
                TestClass3(listOf(1, 2, 3))
        )
    }

    @JsonDeserialize(using = Test4Deserializer::class)
    public data class TestClass4(val a: Map<Int, Int>, val b: Map<Int, Int>)

    private class Test4Deserializer : DefaultParameterDeserializer<TestClass4>(::TestClass4)

    @Test
    fun testObject() {
        Assert.assertEquals(
                objectMapper.readValue("{\"a\": {\"1\": 2}, \"b\": {\"3\": 4}}", TestClass4::class.java),
                TestClass4(mapOf(Pair(1, 2)), mapOf(Pair(3, 4)))
        )
    }

    data class X(val tc4: TestClass4, val suff: String)

    @Test
    fun testNest() {
        Assert.assertEquals(
                objectMapper.readValue("{\"tc4\": {\"a\": {\"1\": 2}, \"b\": {\"3\": 4}}, \"suff\": \"a\"}", X::class.java),
                X(TestClass4(mapOf(Pair(1, 2)), mapOf(Pair(3, 4))), "a")
        )
    }
}