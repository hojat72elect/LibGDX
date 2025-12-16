package com.badlogic.gdx.math

import org.junit.Assert.assertEquals
import org.junit.Test

class RectangleTest {
    @Test
    fun testToString() {
        assertEquals("[5.0,-4.1,0.03,-0.02]", Rectangle(5F, -4.1F, 0.03F, -0.02F).toString())
    }

    @Test
    fun testFromString() {
        assertEquals(Rectangle(5F, -4.1F, 0.03F, -0.02F), Rectangle().fromString("[5.0,-4.1,0.03,-0.02]"))
    }
}
