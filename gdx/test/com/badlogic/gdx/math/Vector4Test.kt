package com.badlogic.gdx.math

import org.junit.Assert
import org.junit.Test

class Vector4Test {
    @Test
    fun testToString() {
        Assert.assertEquals("(-5.0,42.00055,44444.32,-1.975)", Vector4(-5F, 42.00055F, 44444.32F, -1.975F).toString())
    }

    @Test
    fun testFromString() {
        Assert.assertEquals(Vector4(-5F, 42.00055F, 44444.32F, -1.975F), Vector4().fromString("(-5,42.00055,44444.32,-1.9750)"))
    }
}