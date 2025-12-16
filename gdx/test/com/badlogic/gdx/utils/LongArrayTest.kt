package com.badlogic.gdx.utils

import org.junit.Assert
import org.junit.Test

class LongArrayTest {
    /**
     * Test of the different adding methods.
     */
    @Test
    fun addTest() {
        /*
         * Test of the classic add(long value) method, it should be adding a number at the first available place.
         */
        val longArray1 = LongArray(3)
        longArray1.add(3)
        Assert.assertArrayEquals(longArrayOf(3), longArray1.toArray())

        /*
         * Test of the add(long value1,long value2) method, it should be adding these numbers in the array, when it is possible.
         */
        val longArray2 = LongArray()
        longArray2.add(1, 2)
        Assert.assertArrayEquals(longArrayOf(1, 2), longArray2.toArray())

        /*
         * Some test with the addAll(LongArray array) and addAll (long... array) methods. The second call should resize longArray3
         * to a size of 17.
         */
        val longArray3 = LongArray()
        longArray3.addAll(longArray2)
        Assert.assertArrayEquals(longArray2.toArray(), longArray3.toArray())
        longArray3.addAll(longArray1)
        Assert.assertArrayEquals(longArrayOf(1, 2, 3), longArray3.toArray())
        longArray3.addAll(4, 5, 6, 2, 8, 10, 1, 6, 2, 3, 30, 31, 25, 20)
        Assert.assertEquals(17, longArray3.size.toLong())
        Assert.assertArrayEquals(longArrayOf(1, 2, 3, 4, 5, 6, 2, 8, 10, 1, 6, 2, 3, 30, 31, 25, 20), longArray3.toArray())

        /*
         * Test of the method addAll(long[] array, int offset, int length) that adds the numbers contained between index offset to
         * offset+length in array to longArray4.
         */
        val longArray4 = LongArray()
        longArray4.addAll(longArrayOf(4, 5, 6, 2, 21, 45, 78), 3, 3)
        Assert.assertArrayEquals(longArrayOf(2, 21, 45), longArray4.toArray())
    }

    /**
     * Test of the get() method.
     */
    @Test
    fun getTest() {
        val longArray = LongArray()
        longArray.add(3, 4, 5, 1)
        Assert.assertEquals(3, longArray.get(0))
        try {
            longArray.get(9)
            Assert.fail()
        } catch (_: IndexOutOfBoundsException) {
            // We should get here as we want to get an index that's out of bounds.
        }
    }

    /**
     * Test of the set() method
     */
    @Test
    fun setTest() {
        val longArray = LongArray(longArrayOf(3, 4, 5, 7))
        longArray.set(1, 51)
        Assert.assertEquals(51, longArray.get(1))
        try {
            longArray.set(5, 8)
            Assert.fail()
        } catch (_: IndexOutOfBoundsException) {
            // We should get here as we are trying to set the value of an index that's out of bounds.
        }
    }

    /**
     * Test of the incr() method
     */
    @Test
    fun incrTest() {
        val longArray = LongArray(longArrayOf(3, 4, 5, 1, 56, 32))
        longArray.incr(3, 45)
        Assert.assertEquals(46, longArray.get(3))
        longArray.incr(3)
        Assert.assertArrayEquals(longArrayOf(6, 7, 8, 49, 59, 35), longArray.toArray())
        try {
            longArray.incr(28, 4)
            Assert.fail()
        } catch (_: IndexOutOfBoundsException) {
            // This should throw an exception as we are trying to increase at an index out of bounds
        }
    }

    /**
     * Test of the mul() method
     */
    @Test
    fun mulTest() {
        val longArray = LongArray(longArrayOf(3, 4, 5, 1, 56, 32))
        longArray.mul(1, 3)
        Assert.assertEquals(12, longArray.get(1))
        longArray.mul(2)
        Assert.assertArrayEquals(longArrayOf(6, 24, 10, 2, 112, 64), longArray.toArray())
        try {
            longArray.mul(17, 8)
            Assert.fail()
        } catch (_: IndexOutOfBoundsException) {
            // This should throw an exception as we are trying to multiply at an index out of bounds
        }
    }

    /**
     * Test of the insert() method
     */
    @Test
    fun insertTest() {
        // With an ordered array
        val longArray1 = LongArray()
        longArray1.addAll(1, 3, 4, 5, 6)
        longArray1.insert(1, 2)
        Assert.assertArrayEquals(longArrayOf(1, 2, 3, 4, 5, 6), longArray1.toArray())
        longArray1.insertRange(2, 3)
        Assert.assertArrayEquals(longArrayOf(1, 2, 3, 4, 5, 3, 4, 5, 6), longArray1.toArray())
        try {
            longArray1.insertRange(400, 4)
            Assert.fail()
        } catch (_: IndexOutOfBoundsException) {
            // This should throw an exception as we are trying to insert at an index out of bounds
        }

        // With an unordered array
        val longArray2 = LongArray(false, 16)
        longArray2.addAll(1, 3, 4, 5, 6)
        longArray2.insert(1, 2)
        Assert.assertArrayEquals(longArrayOf(1, 2, 4, 5, 6, 3), longArray2.toArray())
        try {
            longArray2.insert(2783, 3)
            Assert.fail()
        } catch (_: IndexOutOfBoundsException) {
            // This should throw an exception as we are trying to insert at an index out of bounds
        }
    }

    /**
     * Test of the swap() method
     */
    @Test
    fun swapTest() {
        val longArray1 = LongArray(longArrayOf(1, 3, 4, 5, 6))
        longArray1.swap(1, 4)
        Assert.assertArrayEquals(longArrayOf(1, 6, 4, 5, 3), longArray1.toArray())
        try {
            longArray1.swap(100, 3)
            Assert.fail()
        } catch (_: IndexOutOfBoundsException) {
            // This should throw an exception as we are trying to swap at an index out of bounds
        }
        try {
            longArray1.swap(3, 100)
            Assert.fail()
        } catch (_: IndexOutOfBoundsException) {
            // This should throw an exception as we are trying to swap at an index out of bounds
        }
    }

    @Test
    fun containsTest() {
        val longArray1 = LongArray(longArrayOf(1, 3, 4, 5, 6))
        Assert.assertTrue(longArray1.contains(3))
        Assert.assertFalse(longArray1.contains(100))
    }

    /**
     * Test of the indexOf() method.
     */
    @Test
    fun indexOfTest() {
        val longArray1 = LongArray(longArrayOf(1, 3, 4, 5, 6, 6, 3, 9, 68000, 68000))
        Assert.assertEquals(-1, longArray1.indexOf(100).toLong())
        Assert.assertEquals(1, longArray1.indexOf(3).toLong())
        Assert.assertEquals(9, longArray1.lastIndexOf(68000).toLong())
        Assert.assertEquals(-1, longArray1.lastIndexOf(100).toLong())
    }

    /**
     * Test of all remove methods (removeValue, removeIndex, removeRange, removeAll)
     */
    @Test
    fun removeTest() {
        // removeValue test
        val longArray1: LongArray = LongArray.with(1, 3, 4, 5, 6, 6, 3, 9)
        Assert.assertTrue(longArray1.removeValue(3))
        Assert.assertArrayEquals(longArrayOf(1, 4, 5, 6, 6, 3, 9), longArray1.toArray())
        Assert.assertEquals(7, longArray1.size.toLong())
        Assert.assertFalse(longArray1.removeValue(99))
        // removeIndex test
        Assert.assertEquals(4, longArray1.removeIndex(1))
        Assert.assertArrayEquals(longArrayOf(1, 5, 6, 6, 3, 9), longArray1.toArray())
        Assert.assertEquals(6, longArray1.size.toLong())
        try {
            longArray1.removeIndex(56)
            Assert.fail()
        } catch (_: IndexOutOfBoundsException) {
            // This should throw an exception as we are trying to remove at an index that is out of bounds
        }

        // removeRange test
        val longArray2 = LongArray()
        longArray2.addAll(1, 10, 25, 2, 23, 345)
        longArray2.removeRange(2, 5)
        Assert.assertArrayEquals(longArrayOf(1, 10), longArray2.toArray())
        try {
            longArray2.removeRange(3, 4)
            Assert.fail()
        } catch (_: IndexOutOfBoundsException) {
            // This should throw an exception as we are trying to remove at a range that is out of bounds
        }
        try {
            longArray2.removeRange(1, 0)
            Assert.fail()
        } catch (_: IndexOutOfBoundsException) {
            // This should throw an exception as the starting index is > than ending index
        }

        // removeAll test
        val longArray3 = LongArray()
        longArray3.addAll(1, 10, 25, 35, 50, 40)
        var toBeRemoved = LongArray(longArrayOf(1, 25, 35))
        Assert.assertTrue(longArray3.removeAll(toBeRemoved))
        Assert.assertArrayEquals(longArrayOf(10, 50, 40), longArray3.toArray())
        Assert.assertFalse(longArray3.removeAll(toBeRemoved))
        toBeRemoved = LongArray(longArrayOf(10, 30, 22))
        Assert.assertTrue(longArray3.removeAll(toBeRemoved))
        Assert.assertArrayEquals(longArrayOf(50, 40), longArray3.toArray())
    }

    /**
     * Test of the pop(), peek() and first() methods
     */
    @Test
    fun popPeekFirstTest() {
        val longArray: LongArray = LongArray.with(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val emptyLongArray = LongArray()
        Assert.assertEquals(1, longArray.first())
        Assert.assertEquals(10, longArray.peek())
        Assert.assertEquals(10, longArray.pop())
        Assert.assertArrayEquals(longArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9), longArray.toArray())
        try {
            emptyLongArray.first()
            Assert.fail()
        } catch (_: IllegalStateException) {
            // This should throw an exception as we can't take the first item of an empty array
        }
        try {
            emptyLongArray.pop()
            Assert.fail()
        } catch (_: IllegalStateException) {
            // This should throw an exception as we can't take the last item of an empty array
        }
        try {
            emptyLongArray.peek()
            Assert.fail()
        } catch (_: IllegalStateException) {
            // This should throw an exception as we can't take the last item of an empty array
        }
    }

    /**
     * test of the isEmpty() and notEmpty() methods
     */
    @Test
    fun emptyTest() {
        Assert.assertTrue((LongArray()).isEmpty)
        Assert.assertFalse((LongArray(longArrayOf(1))).isEmpty)
        Assert.assertFalse((LongArray()).notEmpty())
        Assert.assertTrue((LongArray(longArrayOf(1))).notEmpty())
    }

    /**
     * Test of the clear() method
     */
    @Test
    fun clearTest() {
        val longArray = LongArray(longArrayOf(1))
        longArray.clear()
        Assert.assertTrue(longArray.isEmpty)
    }

    /**
     * Test of the shrink() method
     */
    @Test
    fun shrinkTest() {
        val longArray = LongArray() // This LongArray will have an "items" attribute of length 16 by default
        longArray.add(1, 2, 3)
        Assert.assertArrayEquals(longArrayOf(1, 2, 3), longArray.shrink())
        Assert.assertEquals(3, longArray.items.size.toLong())
    }

    /**
     * Test of the ensureCapacity method
     */
    @Test
    fun ensureCapacityTest() {
        val longArray1 = LongArray(
            longArrayOf(1, 2, 4, 6, 32, 53, 564, 53, 2, 1, 89, 0, 10, 389, 8, 392, 4, 27346, 2, 234, 12)
        )
        val longArray2 = LongArray(longArrayOf(1, 2, 3))
        Assert.assertArrayEquals(longArrayOf(1, 2, 3, 0, 0, 0, 0, 0), longArray2.ensureCapacity(2))
        Assert.assertArrayEquals(
            longArrayOf(
                1, 2, 4, 6, 32, 53, 564, 53, 2, 1, 89, 0, 10, 389, 8, 392, 4, 27346, 2, 234, 12, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
            ), longArray1.ensureCapacity(18)
        )
        try {
            longArray1.ensureCapacity(-6)
            Assert.fail()
        } catch (_: IllegalArgumentException) {
            // This should throw an exception as it is not allowed to have a negative integer as ensureCapacity argument
        }
    }

    /**
     * Test of the setSize() method
     */
    @Test
    fun setSizeTest() {
        val longArray1 = LongArray(
            longArrayOf(1, 2, 4, 6, 32, 53, 564, 53, 2, 1, 89, 90, 10, 389, 8, 392, 4, 27346, 2, 234, 12)
        )
        longArray1.setSize(23)
        Assert.assertEquals(23, longArray1.size.toLong())
        longArray1.setSize(10)
        Assert.assertArrayEquals(longArrayOf(1, 2, 4, 6, 32, 53, 564, 53, 2, 1), longArray1.toArray())
        val longArray2 = LongArray(longArrayOf(1, 2, 3))
        Assert.assertArrayEquals(longArrayOf(1, 2, 3, 0, 0, 0, 0, 0), longArray2.setSize(5))
        try {
            longArray1.setSize(-3)
            Assert.fail()
        } catch (_: IllegalArgumentException) {
            // This should throw an exception as it is not allowed to have a negative integer as setSize argument
        }
    }

    /**
     * Test of resize() method
     */
    @Test
    fun resizeTest() {
        val longArray1 = LongArray(
            longArrayOf(1, 2, 4, 6, 32, 53, 564, 53, 2, 1, 89, 90, 10, 389, 8, 392, 4, 27346, 2, 234, 12)
        )
        Assert.assertArrayEquals(
            longArrayOf(1, 2, 4, 6, 32, 53, 564, 53, 2, 1, 89, 90, 10, 389, 8, 392, 4, 27346, 2, 234, 12, 0, 0),
            longArray1.resize(23)
        )
    }

    /**
     * Test of sort() and reverse() methods
     */
    @Test
    fun sortAndReverseTest() {
        val longArray1: LongArray = LongArray.with(1, 2, 4, 6, 32, 53, 564, 53, 2, 1, 89, 90, 10, 389, 8, 392, 4, 27346, 2, 234, 12)
        longArray1.sort()
        Assert.assertArrayEquals(
            longArrayOf(1, 1, 2, 2, 2, 4, 4, 6, 8, 10, 12, 32, 53, 53, 89, 90, 234, 389, 392, 564, 27346),
            longArray1.toArray()
        )
        longArray1.reverse()
        Assert.assertArrayEquals(
            longArrayOf(27346, 564, 392, 389, 234, 90, 89, 53, 53, 32, 12, 10, 8, 6, 4, 4, 2, 2, 2, 1, 1),
            longArray1.toArray()
        )
    }

    /**
     * Test of equals() method
     */
    @Test
    fun equalsTest() {
        val longArray1 = LongArray()
        val longArray2 = LongArray()
        longArray1.add(1, 2)
        longArray2.add(1, 2)

        Assert.assertEquals(longArray1, longArray2)

        // Verifying that an object of instance =/= LongArray cannot be equal
        val o = ArrayMap<Int, Int>() // Random object of a different class
        Assert.assertNotEquals(longArray1, o)

        // An unordered array, even if the content is the same, cannot be equal to an ordered array
        val longArray3 = LongArray(false, 16)
        longArray3.add(1, 2)
        Assert.assertNotEquals(longArray1, longArray3)

        // The capacity isn't something that makes two arrays not equal
        val longArray4 = LongArray(true, 12)
        longArray4.add(1, 2)
        Assert.assertEquals(longArray1, longArray4)

        // Standard verification that two arrays with different content are not equal
        longArray1.add(3)
        Assert.assertNotEquals(longArray1, longArray2)
    }
}
