package com.badlogic.gdx.math

import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.math.collision.Ray
import com.badlogic.gdx.utils.ObjectSet
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OctreeTest {
    @Test
    fun testInsert() {
        val maxDepth = 2
        val maxItemsPerNode = 1

        val min = Vector3(-5f, -5f, -5f)
        val max = Vector3(5f, 5f, 5f)

        val octree = Octree(min, max, maxDepth, maxItemsPerNode, object : Octree.Collider<BoundingBox> {
            val tmp: Vector3 = Vector3()

            override fun intersects(nodeBounds: BoundingBox, geometry: BoundingBox): Boolean {
                return nodeBounds.intersects(geometry)
            }

            override fun intersects(frustum: Frustum, geometry: BoundingBox): Boolean {
                return false
            }

            override fun intersects(ray: Ray, geometry: BoundingBox): Float {
                if (!Intersector.intersectRayBounds(ray, geometry, tmp)) {
                    return tmp.dst2(ray.origin)
                }
                return Float.POSITIVE_INFINITY
            }
        })

        assertTrue(octree.root.isLeaf)

        val box1 = BoundingBox(Vector3(0f, 0f, 0f), Vector3(1f, 1f, 1f))
        octree.add(box1)

        val box2 = BoundingBox(Vector3(2f, 2f, 2f), Vector3(3f, 3f, 3f))
        octree.add(box2)
        assertFalse(octree.root.isLeaf)

        val result = ObjectSet<BoundingBox>()
        octree.getAll(result)
        assertTrue(result.contains(box1))
        assertTrue(result.contains(box2))
        assertEquals(2, result.size.toLong())

        octree.remove(box2)
        result.clear()
        // Refill result geometries
        octree.getAll(result)
        assertEquals(1, result.size.toLong())
        assertTrue(result.contains(box1))
    }
}
