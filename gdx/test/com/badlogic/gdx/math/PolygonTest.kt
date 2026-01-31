package com.badlogic.gdx.math

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class PolygonTest {
    @Test
    fun testZeroRotation() {
        val vertices = floatArrayOf(0F, 0F, 3F, 0F, 3F, 4F)
        val polygon = Polygon(vertices)
        polygon.rotate(0F)
        assertArrayEquals("The polygon's vertices don't correspond.", polygon.getTransformedVertices(), polygon.getVertices(), 1f)
    }

    @Test
    fun test360Rotation() {
        val vertices = floatArrayOf(0F, 0F, 3F, 0F, 3F, 4F)
        val polygon = Polygon(vertices)
        polygon.rotate(360F)
        assertArrayEquals("The polygon's vertices don't correspond.", polygon.getTransformedVertices(), polygon.getVertices(), 1f)
    }

    @Test
    fun testConcavePolygonArea() {
        val concaveVertices = floatArrayOf(0F, 0F, 2F, 4F, 4F, 0F, 2F, 2F)
        val concavePolygon = Polygon(concaveVertices)
        val expectedArea = 4.0F
        assertEquals("The area doesn't correspond.", expectedArea, concavePolygon.area(), 1f)
    }

    @Test
    fun testTriangleArea() {
        val triangleVertices = floatArrayOf(0F, 0F, 2F, 3F, 4F, 0F)
        val triangle = Polygon(triangleVertices)
        val expectedArea = 6.0F
        assertEquals("The area doesn't correspond.", expectedArea, triangle.area(), 1f)
    }
}
