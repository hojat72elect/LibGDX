package com.badlogic.gdx.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class JsonValueTest {
    @Test
    fun testAddingRemovedValue() {
        // Prepare two JSON objects
        val firstObject = JsonValue(JsonValue.ValueType.objectValue)
        val secondObject = JsonValue(JsonValue.ValueType.objectValue)

        firstObject.addChild("a", JsonValue("A"))
        secondObject.addChild("b", JsonValue("B"))
        secondObject.addChild("c", JsonValue("C"))

        // Remove an item from one object and add it to the other
        val b = secondObject.remove("b")
        firstObject.addChild(b)

        // Check if both objects have the expected children
        assertNotNull(firstObject.get("a"))
        assertNotNull(firstObject.get("b"))
        assertNull(firstObject.get("c"))

        assertNull(secondObject.get("a"))
        assertNull(secondObject.get("b"))
        assertNotNull(secondObject.get("c"))
    }

    @Test
    fun testReplaceValue() {
        val jsonObject = JsonValue(JsonValue.ValueType.objectValue)

        jsonObject.addChild("a", JsonValue("A"))
        jsonObject.addChild("b", JsonValue("B"))
        jsonObject.addChild("c", JsonValue("C"))
        jsonObject.addChild("d", JsonValue("D"))

        jsonObject.setChild("b", JsonValue("X"))
        jsonObject.setChild("c", JsonValue("Y"))

        assertEquals("A", jsonObject.get("a").asString())
        assertEquals("X", jsonObject.get("b").asString())
        assertEquals("Y", jsonObject.get("c").asString())
        assertEquals("D", jsonObject.get("d").asString())

        jsonObject.setChild("a", JsonValue("W"))
        val z = JsonValue("Z")
        z.name = "d"
        jsonObject.setChild(z)

        assertEquals(jsonObject.get("a").parent(), jsonObject)
        assertEquals("W", jsonObject.get("a").asString())
        assertEquals("X", jsonObject.get("b").asString())
        assertEquals("Y", jsonObject.get("c").asString())
        assertEquals("Z", jsonObject.get("d").asString())
    }

    @Test
    fun testCopyConstructor() {
        val b = JsonValue(JsonValue.ValueType.objectValue)
        b.addChild("c", JsonValue("C"))
        b.addChild("d", JsonValue("D"))

        val `object` = JsonValue(JsonValue.ValueType.objectValue)
        `object`.addChild("a", JsonValue("A"))
        `object`.addChild("b", b)
        `object`.addChild("e", JsonValue(123))

        val copy = JsonValue(`object`)

        assertEquals("A", `object`.get("a").asString())
        assertEquals(`object`.get("b"), b)
        assertEquals("C", `object`.get("b").get("c").asString())
        assertEquals("D", `object`.get("b").get("d").asString())
        assertEquals(`object`.get("b").parent(), `object`)
        assertEquals(123, `object`.get("e").asInt().toLong())
        assertNull(`object`.get("E"))
        assertEquals(123, `object`.getIgnoreCase("E").asInt().toLong())

        assertEquals("A", copy.get("a").asString())
        assertNotEquals(copy.get("b"), b)
        assertEquals("C", copy.get("b").get("c").asString())
        assertEquals("D", copy.get("b").get("d").asString())
        assertEquals(copy.get("b").parent(), copy)
        assertEquals(123, copy.get("e").asInt().toLong())
        assertNull(copy.get("E"))
        assertEquals(123, copy.getIgnoreCase("E").asInt().toLong())

        val bCopy = JsonValue(copy.get("b"))
        assertNotEquals(b, bCopy)
        assertNotEquals(copy.get("b"), bCopy)
        assertEquals("C", bCopy.get("c").asString())
        assertEquals("D", bCopy.get("d").asString())
        assertEquals(bCopy.get("d").parent(), bCopy)
    }
}
