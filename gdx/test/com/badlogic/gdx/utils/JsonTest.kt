package com.badlogic.gdx.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class JsonTest {
    @Test
    fun testFromJsonObject() {
        val json = Json()
        val value = json.fromJson<JsonValue>(null, JsonValue::class.java, "{\"key\":\"value\"}")
        assertEquals("value", value.getString("key"))
    }

    @Test
    fun testFromJsonArray() {
        val json = Json()
        val value = json.fromJson<Array<String>>(null, "[\"value1\",\"value2\"]")
        assertEquals("value1", value.get(0))
        assertEquals("value2", value.get(1))
    }

    @Test
    fun testCharFromNumber() {
        val json = Json()
        val value = json.fromJson(Char::class.javaPrimitiveType, "90")
        assertEquals('Z'.code.toLong(), value.code.toLong())
    }

    @Test
    fun testReuseReader() {
        val json = Json()
        var value = json.fromJson<JsonValue>(null, JsonValue::class.java, "{\"key\":\"value\"}")
        assertEquals("value", value.getString("key"))
        value = json.fromJson(null, JsonValue::class.java, "{\"key2\":\"value2\"}")
        assertEquals("value2", value.getString("key2"))
    }
}
