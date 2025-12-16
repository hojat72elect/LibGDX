package com.badlogic.gdx.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class PooledLinkedListTest {
    private lateinit var list: PooledLinkedList<Int>

    @Before
    fun setUp() {
        list = PooledLinkedList<Int>(10)
        list.add(1)
        list.add(2)
        list.add(3)
    }

    @Test
    fun size() {
        assertEquals(3, list.size().toLong())
        list.iter()
        list.next()
        list.remove()
        assertEquals(2, list.size().toLong())
    }

    @Test
    fun iteration() {
        list.iter()
        assertEquals(1, list.next())
        assertEquals(2, list.next())
        assertEquals(3, list.next())
        assertNull(list.next())
    }

    @Test
    fun reverseIteration() {
        list.iterReverse()
        assertEquals(3, list.previous())
        assertEquals(2, list.previous())
        assertEquals(1, list.previous())
        assertNull(list.previous())
    }

    @Test
    fun remove() {
        list.iter()
        list.next() // 1
        list.remove()
        list.next() // 2
        list.next() // 3
        list.remove()
        list.iter()
        assertEquals(2, list.next())
        assertNull(list.next())
    }

    @Test
    fun removeLast() {
        list.iter()
        assertEquals(1, list.next())
        list.removeLast()
        assertEquals(2, list.next())
        assertNull(list.next())
    }

    @Test
    fun clear() {
        list.clear()
        assertEquals(0, list.size().toLong())
        list.iter()
        assertNull(list.next())
    }
}
