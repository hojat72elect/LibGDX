package com.badlogic.ashley.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class BagTest {
    @Test
    fun testSet() {
        val bag = Bag<String>()
        bag.add("a")
        bag.add("b")
        bag.add("c")
        assertEquals(3, bag.size())

        bag.set(1, "d")
        assertEquals(3, bag.size())
        assertEquals("d", bag.get(1))
    }
}
