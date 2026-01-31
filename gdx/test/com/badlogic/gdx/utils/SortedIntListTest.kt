package com.badlogic.gdx.utils

import org.junit.Assert
import org.junit.Test

class SortedIntListTest {

    @Test
    fun testIteratorWithAllocation() {
        Collections.allocateIterators = true
        try {
            val list = SortedIntList<String?>()
            list.insert(0, "hello")
            Assert.assertEquals(1, list.size.toLong())
            Assert.assertEquals("hello", list.get(0))
            Assert.assertEquals("hello", list.iterator().next().value)
        } finally {
            Collections.allocateIterators = false
        }
    }
}
