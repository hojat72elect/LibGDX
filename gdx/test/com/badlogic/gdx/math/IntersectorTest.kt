package com.badlogic.gdx.math

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.Intersector.SplitTriangle
import org.junit.Assert
import org.junit.Test

class IntersectorTest {
    @Test
    fun testSplitTriangle() {
        val plane = Plane(Vector3(1f, 0f, 0f), 0f)
        val split = SplitTriangle(3)

        run {
            // All back
            val fTriangle = floatArrayOf(-10f, 0f, 10f, -1f, 0f, 0f, -12f, 0f, 10f) // Whole triangle on the back side
            Intersector.splitTriangle(fTriangle, plane, split)
            Assert.assertEquals(1, split.numBack.toLong())
            Assert.assertEquals(0, split.numFront.toLong())
            Assert.assertEquals(1, split.total.toLong())
            Assert.assertTrue(triangleEquals(split.back, 0, fTriangle))

            fTriangle[4] = 5f
            Assert.assertFalse("Test is broken", triangleEquals(split.back, 0, fTriangle))
        }

        run {
            // All front
            val fTriangle = floatArrayOf(10f, 0f, 10f, 1f, 0f, 0f, 12f, 0f, 10f) // Whole triangle on the front side
            Intersector.splitTriangle(fTriangle, plane, split)
            Assert.assertEquals(0, split.numBack.toLong())
            Assert.assertEquals(1, split.numFront.toLong())
            Assert.assertEquals(1, split.total.toLong())
            Assert.assertTrue(triangleEquals(split.front, 0, fTriangle))
        }

        run {
            // Two back, one front
            val triangle = floatArrayOf(-10f, 0f, 10f, 10f, 0f, 0f, -10f, 0f, -10f) // ABC One vertex in front, two in back
            Intersector.splitTriangle(triangle, plane, split) // Split points are D (0,0,5) and E (0,0,-5)
            Assert.assertEquals(2, split.numBack.toLong())
            Assert.assertEquals(1, split.numFront.toLong())
            Assert.assertEquals(3, split.total.toLong())
            // There is only one way to triangulate front
            Assert.assertTrue(triangleEquals(split.front, 0, floatArrayOf(0f, 0f, 5f, 10f, 0f, 0f, 0f, 0f, -5f)))

            // There are two ways to triangulate back
            val firstWay = arrayOf(floatArrayOf(-10f, 0f, 10f, 0f, 0f, 5f, 0f, 0f, -5f), floatArrayOf(-10f, 0f, 10f, 0f, 0f, -5f, -10f, 0f, -10f)) // ADE AEC
            val secondWay = arrayOf(floatArrayOf(-10f, 0f, 10f, 0f, 0f, 5f, -10f, 0f, -10f), floatArrayOf(0f, 0f, 5f, 0f, 0f, -5f, -10f, 0f, -10f)) // ADC DEC
            val base = split.back
            val first = (triangleEquals(base, 0, firstWay[0]) && triangleEquals(base, 9, firstWay[1]))
                    || (triangleEquals(base, 0, firstWay[1]) && triangleEquals(base, 9, firstWay[0]))
            val second = (triangleEquals(base, 0, secondWay[0]) && triangleEquals(base, 9, secondWay[1]))
                    || (triangleEquals(base, 0, secondWay[1]) && triangleEquals(base, 9, secondWay[0]))
            Assert.assertTrue("Either first or second way must be right (first: $first, second: $second)", first xor second)
        }

        run {
            // Two front, one back
            val triangle = floatArrayOf(10f, 0f, 10f, -10f, 0f, 0f, 10f, 0f, -10f) // ABC One vertex in back, two in front
            Intersector.splitTriangle(triangle, plane, split) // Split points are D (0,0,5) and E (0,0,-5)
            Assert.assertEquals(1, split.numBack.toLong())
            Assert.assertEquals(2, split.numFront.toLong())
            Assert.assertEquals(3, split.total.toLong())
            // There is only one way to triangulate back
            Assert.assertTrue(triangleEquals(split.back, 0, floatArrayOf(0f, 0f, 5f, -10f, 0f, 0f, 0f, 0f, -5f)))

            // There are two ways to triangulate front
            val firstWay = arrayOf(floatArrayOf(10f, 0f, 10f, 0f, 0f, 5f, 0f, 0f, -5f), floatArrayOf(10f, 0f, 10f, 0f, 0f, -5f, 10f, 0f, -10f)) // ADE AEC
            val secondWay = arrayOf(floatArrayOf(10f, 0f, 10f, 0f, 0f, 5f, 10f, 0f, -10f), floatArrayOf(0f, 0f, 5f, 0f, 0f, -5f, 10f, 0f, -10f)) // ADC DEC
            val base = split.front
            val first = (triangleEquals(base, 0, firstWay[0]) && triangleEquals(base, 9, firstWay[1]))
                    || (triangleEquals(base, 0, firstWay[1]) && triangleEquals(base, 9, firstWay[0]))
            val second = (triangleEquals(base, 0, secondWay[0]) && triangleEquals(base, 9, secondWay[1]))
                    || (triangleEquals(base, 0, secondWay[1]) && triangleEquals(base, 9, secondWay[0]))
            Assert.assertTrue("Either first or second way must be right (first: $first, second: $second)", first xor second)
        }
    }

    @Test
    fun intersectSegmentCircle() {
        val circle = Circle(5f, 5f, 4f)
        // Segment intersects, both segment points outside circle
        var intersects = Intersector.intersectSegmentCircle(Vector2(0f, 1f), Vector2(12f, 3f), circle, null)
        Assert.assertTrue(intersects)
        // Segment intersects, only one of the points inside circle (and is aligned with center)
        intersects = Intersector.intersectSegmentCircle(Vector2(0f, 5f), Vector2(2f, 5f), circle, null)
        Assert.assertTrue(intersects)
        // Segment intersects, no points outside circle
        intersects = Intersector.intersectSegmentCircle(Vector2(5.5f, 6f), Vector2(7f, 5.5f), circle, null)
        Assert.assertTrue(intersects)
        // Segment doesn't intersect
        intersects = Intersector.intersectSegmentCircle(Vector2(0f, 6f), Vector2(0.5f, 2f), circle, null)
        Assert.assertFalse(intersects)
        // Segment is parallel to Y axis left of circle's center
        val mtv = MinimumTranslationVector()
        intersects = Intersector.intersectSegmentCircle(Vector2(1.5f, 6f), Vector2(1.5f, 3f), circle, mtv)
        Assert.assertTrue(intersects)
        Assert.assertEquals(Vector2(-1f, 0f), mtv.normal)
        Assert.assertEquals(0.5, mtv.depth.toDouble(), 0.0)
        // Segment contains circle center point
        intersects = Intersector.intersectSegmentCircle(Vector2(4f, 5f), Vector2(6f, 5f), circle, mtv)
        Assert.assertTrue(intersects)
        Assert.assertTrue(mtv.normal == Vector2(0f, 1f) || mtv.normal == Vector2(0f, -1f))
        Assert.assertEquals(4.0, mtv.depth.toDouble(), 0.0)
        // Segment contains circle center point which is the same as the end point
        intersects = Intersector.intersectSegmentCircle(Vector2(4f, 5f), Vector2(5f, 5f), circle, mtv)
        Assert.assertTrue(intersects)
        Assert.assertTrue(mtv.normal == Vector2(0f, 1f) || mtv.normal == Vector2(0f, -1f))
        Assert.assertEquals(4.0, mtv.depth.toDouble(), 0.0)
    }

    @Test
    fun testIntersectPlanes() {


        /*
         * camera = new PerspectiveCamera(60, 1280, 720); camera.direction.set(0, 0, 1); camera.near = 0.1f; camera.far = 100f;
         * camera.update(); Plane[] planes = camera.frustum.planes;
         */
        val planes = arrayOfNulls<Plane>(6)
        planes[NEAR] = Plane(Vector3(0.0f, 0.0f, 1.0f), -0.1f)
        planes[FAR] = Plane(Vector3(0.0f, -0.0f, -1.0f), 99.99771f)
        planes[LEFT] = Plane(Vector3(-0.69783056f, 0.0f, 0.71626294f), -9.3877316E-7f)
        planes[RIGHT] = Plane(Vector3(0.6978352f, 0.0f, 0.71625835f), -0.0f)
        planes[TOP] = Plane(Vector3(0.0f, -0.86602545f, 0.5f), -0.0f)
        planes[BOTTOM] = Plane(Vector3(-0.0f, 0.86602545f, 0.5f), -0.0f)

        val intersection = Vector3()
        Intersector.intersectPlanes(planes[TOP], planes[FAR], planes[LEFT], intersection)
        Assert.assertEquals(102.63903f, intersection.x, 0.1f)
        Assert.assertEquals(57.7337f, intersection.y, 0.1f)
        Assert.assertEquals(100f, intersection.z, 0.1f)

        Intersector.intersectPlanes(planes[TOP], planes[FAR], planes[RIGHT], intersection)
        Assert.assertEquals(-102.63903f, intersection.x, 0.1f)
        Assert.assertEquals(57.7337f, intersection.y, 0.1f)
        Assert.assertEquals(100f, intersection.z, 0.1f)

        Intersector.intersectPlanes(planes[BOTTOM], planes[FAR], planes[LEFT], intersection)
        Assert.assertEquals(102.63903f, intersection.x, 0.1f)
        Assert.assertEquals(-57.7337f, intersection.y, 0.1f)
        Assert.assertEquals(100f, intersection.z, 0.1f)

        Intersector.intersectPlanes(planes[BOTTOM], planes[FAR], planes[RIGHT], intersection)
        Assert.assertEquals(-102.63903f, intersection.x, 0.1f)
        Assert.assertEquals(-57.7337f, intersection.y, 0.1f)
        Assert.assertEquals(100f, intersection.z, 0.1f)
    }

    @Test
    fun testIsPointInTriangle2D() {
        Assert.assertFalse(Intersector.isPointInTriangle(Vector2(0.1f, 0f), Vector2(0f, 0f), Vector2(1f, 1f), Vector2(-1f, -1f)))

        Assert.assertTrue(Intersector.isPointInTriangle(Vector2(0f, 0.1f), Vector2(-1f, 1f), Vector2(1f, 1f), Vector2(-1f, -2f)))
    }

    @Test
    fun testIsPointInTriangle3D() {
        // 2D ---
        Assert.assertFalse(
            Intersector.isPointInTriangle(
                Vector3(0.1f, 0f, 0f), Vector3(0f, 0f, 0f), Vector3(1f, 1f, 0f),
                Vector3(-1f, -1f, 0f)
            )
        )

        Assert.assertTrue(
            Intersector.isPointInTriangle(
                Vector3(0f, 0.1f, 0f), Vector3(-1f, 1f, 0f), Vector3(1f, 1f, 0f),
                Vector3(-1f, -2f, 0f)
            )
        )

        // 3D ---
        Assert.assertTrue(
            Intersector.isPointInTriangle(
                Vector3(0.2f, 0f, 1.25f), Vector3(-1f, 1f, 0f), Vector3(1.4f, 0.99f, 2.5f),
                Vector3(-1f, -2f, 0f)
            )
        )
        // 1.2f away.
        Assert.assertFalse(
            Intersector.isPointInTriangle(
                Vector3(2.6f, 0f, 3.75f), Vector3(-1f, 1f, 0f),
                Vector3(1.4f, 0.99f, 2.5f), Vector3(-1f, -2f, 0f)
            )
        )
        // In an edge.
        Assert.assertTrue(
            Intersector.isPointInTriangle(
                Vector3(0f, -0.5f, 0.5f), Vector3(-1f, 1f, 0f), Vector3(1f, 1f, 1f),
                Vector3(-1f, -2f, 0f)
            )
        )
        // Really close to the edge.
        val epsilon = 0.0000001f // One more 0 will fail.
        val almost1 = 1 - epsilon
        Assert.assertFalse(
            Intersector.isPointInTriangle(
                Vector3(0f, -0.5f, 0.5f), Vector3(-1f, 1f, 0f), Vector3(almost1, 1f, 1f),
                Vector3(-1f, -2f, 0f)
            )
        )

        // A really long distance away.
        Assert.assertFalse(
            Intersector.isPointInTriangle(
                Vector3(199f, 1f, 500f), Vector3(-1f, 1f, 0f), Vector3(1f, 1f, 5f),
                Vector3(-1f, -2f, 0f)
            )
        )

        Assert.assertFalse(
            Intersector.isPointInTriangle(
                Vector3(-5120.8345f, 8946.126f, -3270.5813f),
                Vector3(50.008057f, 22.20586f, 124.62208f), Vector3(62.282288f, 22.205864f, 109.665924f),
                Vector3(70.92052f, 7.205861f, 115.437805f)
            )
        )
    }

    @Test
    fun testIntersectPolygons() {
        // Corner case with extremely small overlap polygon
        val intersectionPolygon = Polygon()
        Assert.assertFalse(
            Intersector.intersectPolygons(
                Polygon(floatArrayOf(3200.1453f, 88.00839f, 3233.9087f, 190.34174f, 3266.2905f, 0.0f)),
                Polygon(floatArrayOf(3213.0f, 131.0f, 3214.0f, 131.0f, 3214.0f, 130.0f, 3213.0f, 130.0f)), intersectionPolygon
            )
        )
        Assert.assertEquals(0, intersectionPolygon.vertexCount.toLong())
    }

    @Test
    fun testIntersectPolygonsWithVertexLyingOnEdge() {
        val p1 = Polygon(floatArrayOf(1f, -1f, 2f, -1f, 2f, -2f, 1f, -2f))
        val p2 = Polygon(floatArrayOf(0.5f, -1.5f, 1.5f, -1.5f, 1.5f, -2.5f))

        val intersectionPolygon = Polygon()
        val checkResult = Intersector.intersectPolygons(p1, p2, intersectionPolygon)

        Assert.assertTrue(checkResult)
        Assert.assertEquals(4, intersectionPolygon.vertexCount.toLong())
        Assert.assertEquals(Vector2(1.0f, -2.0f), intersectionPolygon.getVertex(0, Vector2()))
        Assert.assertEquals(Vector2(1.0f, -1.5f), intersectionPolygon.getVertex(1, Vector2()))
        Assert.assertEquals(Vector2(1.5f, -1.5f), intersectionPolygon.getVertex(2, Vector2()))
        Assert.assertEquals(Vector2(1.5f, -2.0f), intersectionPolygon.getVertex(3, Vector2()))
    }

    @Test
    fun testIntersectPolygonsWithTransformationsOnProvidedResultPolygon() {
        val p1 = Polygon(floatArrayOf(1f, -1f, 2f, -1f, 2f, -2f, 1f, -2f))
        val p2 = Polygon(floatArrayOf(0.5f, -1.5f, 1.5f, -1.5f, 1.5f, -2.5f))
        val intersectionPolygon = Polygon(FloatArray(8))
        intersectionPolygon.setScale(5f, 5f)
        intersectionPolygon.setOrigin(10f, 20f)
        intersectionPolygon.setPosition(-33f, -33f)
        intersectionPolygon.setRotation(48f)

        val checkResult = Intersector.intersectPolygons(p1, p2, intersectionPolygon)

        Assert.assertTrue(checkResult)
        Assert.assertArrayEquals(floatArrayOf(1f, -2f, 1f, -1.5f, 1.5f, -1.5f, 1.5f, -2f), intersectionPolygon.vertices, 0f)
        Assert.assertArrayEquals(floatArrayOf(1f, -2f, 1f, -1.5f, 1.5f, -1.5f, 1.5f, -2f), intersectionPolygon.getTransformedVertices(), 0f)
        // verify that the origin has also been reset
        intersectionPolygon.setScale(2f, 2f)
        Assert.assertArrayEquals(
            floatArrayOf(2f, (2 * -2).toFloat(), 2f, 2 * -1.5f, 2 * 1.5f, 2 * -1.5f, 2 * 1.5f, (2 * -2).toFloat()),
            intersectionPolygon.getTransformedVertices(), 0f
        )
    }

    companion object {
        /**
         * Compares two triangles for equality. Triangles must have the same winding, but may begin with different vertex. Values are
         * epsilon compared, with default tolerance. Triangles are assumed to be valid triangles - no duplicate vertices.
         */
        private fun triangleEquals(base: FloatArray, baseOffset: Int, comp: FloatArray): Boolean {
            Assert.assertTrue(base.size - baseOffset >= 9)
            Assert.assertEquals(9, comp.size.toLong())

            var offset = -1
            // Find first comp vertex in base triangle
            for (i in 0..2) {
                val b = baseOffset + i * 3
                if (MathUtils.isEqual(base[b], comp[0]) && MathUtils.isEqual(base[b + 1], comp[1])
                    && MathUtils.isEqual(base[b + 2], comp[2])
                ) {
                    offset = i
                    break
                }
            }
            Assert.assertTrue("Triangles do not have common first vertex.", offset != -1)
            // Compare vertices
            for (i in 0..2) {
                val b = baseOffset + ((offset + i) * 3) % (3 * 3)
                val c = i * 3
                if (!MathUtils.isEqual(base[b], comp[c]) || !MathUtils.isEqual(base[b + 1], comp[c + 1]) || !MathUtils.isEqual(base[b + 2], comp[c + 2])) {
                    return false
                }
            }
            return true
        }

        private const val NEAR = 0
        private const val FAR = 1
        private const val LEFT = 2
        private const val RIGHT = 3
        private const val TOP = 4
        private const val BOTTOM = 5
    }
}
