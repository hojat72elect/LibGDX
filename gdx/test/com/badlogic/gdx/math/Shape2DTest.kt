package com.badlogic.gdx.math

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Shape2DTest {

    @Test
    fun testCircle() {
        val c1 = Circle(0F, 0F, 1F)
        val c2 = Circle(0F, 0F, 1F)
        val c3 = Circle(2F, 0F, 1F)
        val c4 = Circle(0F, 0F, 2F)

        assertTrue(c1.overlaps(c1))
        assertTrue(c1.overlaps(c2))
        assertFalse(c1.overlaps(c3))
        assertTrue(c1.overlaps(c4))
        assertTrue(c4.overlaps(c1))
        assertTrue(c1.contains(0F, 1F))
        assertFalse(c1.contains(0F, 2F))
        assertTrue(c1.contains(c1))
        assertFalse(c1.contains(c4))
        assertTrue(c4.contains(c1))
    }

    @Test
    fun testRectangle() {
        val r1 = Rectangle(0F, 0F, 1F, 1F)
        val r2 = Rectangle(1F, 0F, 2F, 1F)
        assertTrue(r1.overlaps(r1))
        assertFalse(r1.overlaps(r2))
        assertTrue(r1.contains(0F, 0F))
    }

}