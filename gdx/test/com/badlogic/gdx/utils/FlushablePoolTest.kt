package com.badlogic.gdx.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FlushablePoolTest {
    @Test
    fun initializeFlushablePoolTest1() {
        val flushablePool = FlushablePoolClass()
        assertEquals(0, flushablePool.getFree().toLong())
        assertEquals(Int.MAX_VALUE.toLong(), flushablePool.max.toLong())
    }

    @Test
    fun initializeFlushablePoolTest2() {
        val flushablePool = FlushablePoolClass(10)
        assertEquals(0, flushablePool.getFree().toLong())
        assertEquals(Int.MAX_VALUE.toLong(), flushablePool.max.toLong())
    }

    @Test
    fun initializeFlushablePoolTest3() {
        val flushablePool = FlushablePoolClass(10, 10)
        assertEquals(0, flushablePool.getFree().toLong())
        assertEquals(10, flushablePool.max.toLong())
    }

    @Test
    fun obtainTest() {
        val flushablePool = FlushablePoolClass(10, 10)
        flushablePool.newObject()
        assertEquals(0, flushablePool.obtained.size.toLong())
        flushablePool.obtain()
        assertEquals(1, flushablePool.obtained.size.toLong())
        flushablePool.flush()
        assertEquals(0, flushablePool.obtained.size.toLong())
    }

    @Test
    fun flushTest() {
        val flushablePool = FlushablePoolClass(10, 10)
        flushablePool.newObject()
        flushablePool.obtain()
        assertEquals(1, flushablePool.obtained.size.toLong())
        flushablePool.flush()
        assertEquals(0, flushablePool.obtained.size.toLong())
    }

    @Test
    fun freeTest() {
        // Create the flushable pool.
        val flushablePool = FlushablePoolClass(10, 10)
        flushablePool.newObject()
        flushablePool.newObject()

        // Obtain the elements.
        val element1 = flushablePool.obtain()
        val element2 = flushablePool.obtain()

        // Test preconditions.
        assertTrue(flushablePool.obtained.contains(element1, true))
        assertTrue(flushablePool.obtained.contains(element2, true))

        // Free element and check containment.
        flushablePool.free(element2)
        assertTrue(flushablePool.obtained.contains(element1, true))
        assertFalse(flushablePool.obtained.contains(element2, true))
    }

    @Test
    fun freeAllTest() {
        // Create the flushable pool.
        val flushablePool = FlushablePoolClass(5, 5)
        flushablePool.newObject()
        flushablePool.newObject()

        // Obtain the elements.
        val element1 = flushablePool.obtain()
        val element2 = flushablePool.obtain()

        // Create array with elements.
        val elementArray = Array<String>()
        elementArray.add(element1)
        elementArray.add(element2)

        // Test preconditions.
        assertTrue(flushablePool.obtained.contains(element1, true))
        assertTrue(flushablePool.obtained.contains(element2, true))

        // Free elements and check containment.
        flushablePool.freeAll(elementArray)
        assertFalse(flushablePool.obtained.contains(element1, true))
        assertFalse(flushablePool.obtained.contains(element2, true))
    }

    /** Test implementation class of FlushablePool.  */
    private class FlushablePoolClass : FlushablePool<String> {
        constructor() : super()

        constructor(initialCapacity: Int) : super(initialCapacity)

        constructor(initialCapacity: Int, max: Int) : super(initialCapacity, max)

        public override fun newObject() = getFree().toString()

    }
}
