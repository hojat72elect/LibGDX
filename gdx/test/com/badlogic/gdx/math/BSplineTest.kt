package com.badlogic.gdx.math

import org.junit.Assert
import org.junit.Test

class BSplineTest {
    @Test
    fun testCubicSplineNonContinuous() {
        val controlPoints = arrayOf(Vector3(0f, 0f, 0f), Vector3(1f, 1f, 0f), Vector3(2f, 0f, 0f), Vector3(3f, -1f, 0f))
        val spline = BSpline(controlPoints, 3, false)

        val result = Vector3()
        spline.valueAt(result, 0.5f)

        val expected = Vector3(1.5f, 0.5f, 0f)
        Assert.assertEquals(expected.x, result.x, 0.1f) // Error tolerance is large because the curves are... curvy.
        Assert.assertEquals(expected.y, result.y, 0.1f)
        Assert.assertEquals(expected.z, result.z, 0.1f)
    }

    @Test
    fun testCubicSplineContinuous() {
        // Define a rough circle based on the 4 cardinal directions.
        val controlPoints = arrayOf(Vector3(1f, 0f, 0f), Vector3(0f, 1f, 0f), Vector3(-1f, 0f, 0f), Vector3(0f, -1f, 0f))
        val spline = BSpline(controlPoints, 3, true)

        val result = Vector3()
        // 0.875f turns around the circle takes us to the southeast quadrant.
        spline.valueAt(result, 0.875f)

        // The BSpline does not travel through the control points.
        val expected = Vector3(0.45f, -0.45f, 0f)
        Assert.assertEquals(expected.x, result.x, 0.1f)
        Assert.assertEquals(expected.y, result.y, 0.1f)
        Assert.assertEquals(expected.z, result.z, 0.1f)
    }

    @Test
    fun testCubicDerivative() {
        val controlPoints = arrayOf(Vector3(0f, 0f, 0f), Vector3(1f, 1f, 0f), Vector3(2f, 0f, 0f), Vector3(3f, -1f, 0f))
        val spline = BSpline(controlPoints, 3, true)

        val derivative = Vector3()
        spline.derivativeAt(derivative, 0.5f)

        val expectedDerivative = Vector3(1f, -1f, 0f)
        Assert.assertEquals(expectedDerivative.x, derivative.x, 0.001f)
        Assert.assertEquals(expectedDerivative.y, derivative.y, 0.001f)
        Assert.assertEquals(expectedDerivative.z, derivative.z, 0.001f)
    }

    @Test
    fun testContinuousApproximation() {
        val controlPoints = arrayOf(Vector3(1f, 0f, 0f), Vector3(0f, 1f, 0f), Vector3(-1f, 0f, 0f), Vector3(0f, -1f, 0f))
        val spline = BSpline(controlPoints, 3, true)

        val point = Vector3(0.45f, -0.45f, 0.0f)
        val t = spline.approximate(point)

        // 0.875 turns corresponds to the southeast quadrant, where point is.
        Assert.assertEquals(0.875f, t, 0.1f)
    }

    @Test
    fun testNonContinuousApproximation() {
        val controlPoints = arrayOf(Vector3(1f, 0f, 0f), Vector3(0f, 1f, 0f), Vector3(-1f, 0f, 0f), Vector3(0f, -1f, 0f))
        val spline = BSpline(controlPoints, 3, false)
        var t: Float
        var point = Vector3(0.0f, 0.666f, 0.0f)
        t = spline.approximate(point)
        Assert.assertEquals(0.0f, t, 0.1f)
        point = Vector3(-0.666f, 0.0f, 0.0f)
        t = spline.approximate(point)
        Assert.assertEquals(1.0f, t, 0.1f)
        point = Vector3(-0.45f, 0.45f, 0.0f)
        t = spline.approximate(point)
        Assert.assertEquals(0.5f, t, 0.1f)
    }

    @Test
    fun testSplineContinuity() {
        val controlPoints = arrayOf(Vector3(0f, 0f, 0f), Vector3(1f, 1f, 0f), Vector3(2f, 0f, 0f), Vector3(3f, -1f, 0f))
        val spline = BSpline(controlPoints, 3, true)

        val start = Vector3()
        val end = Vector3()
        spline.valueAt(start, 0.0f)
        spline.valueAt(end, 1.0f)

        // For a continuous spline, the start and end points should be equal
        Assert.assertEquals(start.x, end.x, 0.001f)
        Assert.assertEquals(start.y, end.y, 0.001f)
        Assert.assertEquals(start.z, end.z, 0.001f)
    }

    /**
     * Test to validate calculation with edge cases (t = 0 and t = 1).
     */
    @Test
    fun testEdgeCases() {
        // The first and last control points aren't on the path.
        val controlPoints = arrayOf(Vector3(0f, 0f, 0f), Vector3(1f, 1f, 0f), Vector3(2f, 0f, 0f), Vector3(3f, -1f, 0f))
        val spline = BSpline(controlPoints, 3, false)

        val start = Vector3()
        val expectedStart = Vector3(1f, 0.666f, 0f)
        val end = Vector3()
        val expectedEnd = Vector3(2f, 0f, 0f)
        spline.valueAt(start, 0.0f)

        Assert.assertEquals(expectedStart.x, start.x, 0.001f)
        Assert.assertEquals(expectedStart.y, start.y, 0.001f)
        Assert.assertEquals(expectedStart.z, start.z, 0.001f)

        spline.valueAt(end, 1.0f)

        Assert.assertEquals(expectedEnd.x, end.x, 0.001f)
        Assert.assertEquals(expectedEnd.y, end.y, 0.001f)
        Assert.assertEquals(expectedEnd.z, end.z, 0.001f)
    }
}
