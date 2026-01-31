package com.badlogic.gdx.math

import org.junit.Assert.assertEquals
import org.junit.Test

class Vector3Test {
    @Test
    fun testToString() {
        assertEquals("(-5.0,42.00055,44444.32)", Vector3(-5F, 42.00055F, 44444.32F).toString())
    }

    @Test
    fun testFromString() {
        assertEquals(Vector3(-5F, 42.00055F, 44444.32F), Vector3().fromString("(-5,42.00055,44444.32)"))
    }
}