package com.badlogic.ashley.utils

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.GdxRuntimeException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ImmutableArrayTests {
    @Test
    fun sameValues() {
        val array = Array<Int>()
        val immutable = ImmutableArray(array)
        assertEquals(array.size, immutable.size())
        for (i in 0..9)
            array.add(i)
        assertEquals(array.size, immutable.size())

        for (i in 0..<array.size)
            assertEquals(array.get(i), immutable.get(i))

    }

    @Test
    fun iteration() {
        val array = Array<Int>()
        val immutable = ImmutableArray(array)

        for (i in 0..9)
            array.add(i)

        var expected = 0
        for (value in immutable) {
            assertEquals(expected++, value)
        }
    }

    @Test
    fun forbiddenRemoval() {
        val array = Array<Int>()
        val immutable = ImmutableArray(array)

        for (i in 0..9)
            array.add(i)

        var thrown = false

        try {
            immutable.iterator().remove()
        } catch (_: GdxRuntimeException) {
            thrown = true
        }

        assertTrue(thrown)
    }
}
