package com.badlogic.gdx.math

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CircleTest {

    @Test
    fun testConstructors() {
        val c1 = Circle()
        assertEquals(0f, c1.x, 0f)
        assertEquals(0f, c1.y, 0f)
        assertEquals(0f, c1.radius, 0f)

        val c2 = Circle(1f, 2f, 3f)
        assertEquals(1f, c2.x, 0f)
        assertEquals(2f, c2.y, 0f)
        assertEquals(3f, c2.radius, 0f)

        val c3 = Circle(Vector2(4f, 5f), 6f)
        assertEquals(4f, c3.x, 0f)
        assertEquals(5f, c3.y, 0f)
        assertEquals(6f, c3.radius, 0f)

        val c4 = Circle(c2)
        assertEquals(1f, c4.x, 0f)
        assertEquals(2f, c4.y, 0f)
        assertEquals(3f, c4.radius, 0f)

        val c5 = Circle(Vector2(1f, 1f), Vector2(1f, 4f))
        assertEquals(1f, c5.x, 0f)
        assertEquals(1f, c5.y, 0f)
        assertEquals(3f, c5.radius, 1e-5f)
    }

    @Test
    fun testSet() {
        val c = Circle()
        c.set(1f, 2f, 3f)
        assertEquals(1f, c.x, 0f)
        assertEquals(2f, c.y, 0f)
        assertEquals(3f, c.radius, 0f)

        c.set(Vector2(4f, 5f), 6f)
        assertEquals(4f, c.x, 0f)
        assertEquals(5f, c.y, 0f)
        assertEquals(6f, c.radius, 0f)

        val c2 = Circle(7f, 8f, 9f)
        c.set(c2)
        assertEquals(7f, c.x, 0f)
        assertEquals(8f, c.y, 0f)
        assertEquals(9f, c.radius, 0f)

        c.set(Vector2(1f, 1f), Vector2(1f, 4f))
        assertEquals(1f, c.x, 0f)
        assertEquals(1f, c.y, 0f)
        assertEquals(3f, c.radius, 1e-5f)
    }

    @Test
    fun testPosition() {
        val c = Circle()
        c.setPosition(1f, 2f)
        assertEquals(1f, c.x, 0f)
        assertEquals(2f, c.y, 0f)

        c.setPosition(Vector2(3f, 4f))
        assertEquals(3f, c.x, 0f)
        assertEquals(4f, c.y, 0f)

        c.setX(5f)
        assertEquals(5f, c.x, 0f)

        c.setY(6f)
        assertEquals(6f, c.y, 0f)

        c.setRadius(7f)
        assertEquals(7f, c.radius, 0f)
    }

    @Test
    fun testContains() {
        val c = Circle(0f, 0f, 1f)
        assertTrue(c.contains(0f, 0f))
        assertTrue(c.contains(1f, 0f))
        assertTrue(c.contains(0f, 1f))
        assertFalse(c.contains(1.1f, 0f))

        assertTrue(c.contains(Vector2(0f, 0.5f)))
        assertFalse(c.contains(Vector2(0f, 1.1f)))

        val c2 = Circle(0.1f, 0f, 0.5f)
        assertTrue(c.contains(c2))

        val c3 = Circle(2f, 0f, 1f)
        assertFalse(c.contains(c3))
    }

    @Test
    fun testOverlaps() {
        val c1 = Circle(0f, 0f, 1f)
        val c2 = Circle(1.5f, 0f, 1f)
        assertTrue(c1.overlaps(c2))

        val c3 = Circle(3f, 0f, 1f)
        assertFalse(c1.overlaps(c3))
    }

    @Test
    fun testToString() {
        assertEquals("1.0,2.0,3.0", Circle(1f, 2f, 3f).toString())
    }

    @Test
    fun testCircumference() {
        val c = Circle(0f, 0f, 1f)
        assertEquals(MathUtils.PI2, c.circumference(), 1e-5f)
    }

    @Test
    fun testArea() {
        val c = Circle(0f, 0f, 1f)
        assertEquals(MathUtils.PI, c.area(), 1e-5f)
    }

    @Test
    fun testEquals() {
        val c1 = Circle(1f, 2f, 3f)
        val c2 = Circle(1f, 2f, 3f)
        assertEquals(c1, c2)
        assertEquals(c1.hashCode().toLong(), c2.hashCode().toLong())

        val c3 = Circle(1f, 2f, 4f)
        assertNotEquals(c1, c3)
    }

    @Test
    fun `should destructure Circle`() {
        val circle = Circle(1f, 2f, 3f)

        val (x, y, radius) = circle

        assertEquals(1f, x)
        assertEquals(2f, y)
        assertEquals(3f, radius)
    }
}
