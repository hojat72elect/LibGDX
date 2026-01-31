package com.badlogic.gdx.math

import org.junit.Assert.assertEquals
import org.junit.Test

class EllipseTest {
    @Test
    fun `should destructure Ellipse`() {
        val ellipse = Ellipse(1F, 2F, 3F, 4F)

        val (x, y, w, h) = ellipse

        assertEquals(1F, x)
        assertEquals(2F, y)
        assertEquals(3F, w)
        assertEquals(4F, h)
    }
}