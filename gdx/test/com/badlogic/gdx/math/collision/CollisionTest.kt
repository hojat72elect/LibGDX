package com.badlogic.gdx.math.collision

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import org.junit.Assert
import org.junit.Test

class CollisionTest {
    @Test
    fun testBoundingBox() {
        val b1 = BoundingBox(Vector3.Zero, Vector3(1f, 1f, 1f))
        val b2 = BoundingBox(Vector3(1f, 1f, 1f), Vector3(2f, 2f, 2f))
        Assert.assertTrue(b1.contains(Vector3.Zero))
        Assert.assertTrue(b1.contains(b1))
        Assert.assertFalse(b1.contains(b2))
        // Note, in stage the bottom and left sides are inclusive while the right and top sides are exclusive.
    }

    @Test
    fun testOrientedBoundingBox() {
        val b1 = OrientedBoundingBox(BoundingBox(Vector3.Zero, Vector3(1f, 1f, 1f)))
        val b2 = OrientedBoundingBox(BoundingBox(Vector3(1f, 1f, 1f), Vector3(2f, 2f, 2f)))
        Assert.assertTrue(b1.contains(Vector3.Zero))
        Assert.assertTrue(b1.contains(b1))
        Assert.assertFalse(b1.contains(b2))
        // Note, in stage the bottom and left sides are inclusive while the right and top sides are exclusive.
    }

    @Test
    fun testOrientedBoundingBoxCollision() {
        var b1 = OrientedBoundingBox(BoundingBox(Vector3.Zero, Vector3(1f, 1f, 1f)))
        var b2 = OrientedBoundingBox(
            BoundingBox(Vector3(1 + MathUtils.FLOAT_ROUNDING_ERROR, 1f, 1f), Vector3(2f, 2f, 2f))
        )

        Assert.assertFalse(b1.intersects(b2))

        b1 = OrientedBoundingBox(BoundingBox(Vector3.Zero, Vector3(1f, 1f, 1f)))
        b2 = OrientedBoundingBox(BoundingBox(Vector3(0.5f, 0.5f, 0.5f), Vector3(2f, 2f, 2f)))

        Assert.assertTrue(b1.intersects(b2))
    }
}
