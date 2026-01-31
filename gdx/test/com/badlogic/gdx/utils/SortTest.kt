package com.badlogic.gdx.utils

import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * This test class verifies the correctness of the sort functions in the Sort class.
 * Each test ensures that the corresponding sort function correctly sorts arrays
 * and Array objects according to the specified criteria.
 */
class SortTest {
    private lateinit var sortInstance: Sort

    @Before
    fun setUp() {
        sortInstance = Sort.instance()
    }

    internal class NullsFirstComparator : Comparator<Int> {
        override fun compare(o1: Int?, o2: Int?): Int {
            return if (o1 == null && o2 == null) {
                0
            } else if (o1 == null) {
                -1
            } else if (o2 == null) {
                1
            } else {
                o1.compareTo(o2)
            }
        }
    }

    @Test
    fun testSortArrayComparable() {
        val array = arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)
        sortInstance.sort(array)
        Assert.assertArrayEquals(arrayOf(1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9), array)
    }

    @Test
    fun testSortArrayWithComparator() {
        val array = arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)
        val comparator = Comparator.naturalOrder<Int>()
        sortInstance.sort(array, comparator)
        Assert.assertArrayEquals(arrayOf(1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9), array)
    }

    @Test
    fun testSortArrayWithComparatorAndRange() {
        val array = arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)
        val comparator = Comparator { obj: Int, integer: Int -> obj.compareTo(integer) }
        sortInstance.sort(array, comparator, 2, 7)
        Assert.assertArrayEquals(arrayOf(3, 1, 1, 2, 4, 5, 9, 6, 5, 3, 5), array)
    }

    @Test
    fun testSortArrayRange() {
        val array = arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)
        sortInstance.sort(array, 2, 7)
        Assert.assertArrayEquals(arrayOf(3, 1, 1, 2, 4, 5, 9, 6, 5, 3, 5), array)
    }

    @Test
    fun testSortArray() {
        val array = Array(arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5))
        sortInstance.sort(array)
        Assert.assertArrayEquals(arrayOf(1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9), array.items)
    }

    @Test
    @Throws(Exception::class)
    fun testSortArrayComparableWithPreExistingComparableTimSort() {
        val comparableTimSortField = Sort::class.java.getDeclaredField("comparableTimSort")
        comparableTimSortField.isAccessible = true
        comparableTimSortField.set(sortInstance, ComparableTimSort())

        val array = Array(arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5))
        sortInstance.sort(array)
        Assert.assertArrayEquals(arrayOf(1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9), array.items)
    }

    @Test
    @Throws(Exception::class)
    fun testSortArrayComparableWithNullComparableTimSort() {
        val comparableTimSortField = Sort::class.java.getDeclaredField("comparableTimSort")
        comparableTimSortField.isAccessible = true
        comparableTimSortField.set(sortInstance, null)
        val array = arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)
        sortInstance.sort(array)
        Assert.assertArrayEquals(arrayOf(1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9), array)
    }

    @Test
    @Throws(Exception::class)
    fun testSortArrayWithRangeWithNullComparableTimSort() {
        val comparableTimSortField = Sort::class.java.getDeclaredField("comparableTimSort")
        comparableTimSortField.isAccessible = true
        comparableTimSortField.set(sortInstance, null)
        val array = arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)
        sortInstance.sort(array, 2, 7)
        Assert.assertArrayEquals(arrayOf(3, 1, 1, 2, 4, 5, 9, 6, 5, 3, 5), array)
    }

    @Test
    @Throws(Exception::class)
    fun testSortArrayWithNullTimSort() {
        val timSortField = Sort::class.java.getDeclaredField("timSort")
        timSortField.isAccessible = true
        timSortField.set(sortInstance, null)
        val array = arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)
        val comparator = Comparator { obj: Int, integer: Int -> obj.compareTo(integer) }
        sortInstance.sort(array, comparator)
        Assert.assertArrayEquals(arrayOf(1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9), array)
    }

    @Test
    @Throws(Exception::class)
    fun testSortArrayWithNullTimSortArray() {
        val timSortField = Sort::class.java.getDeclaredField("timSort")
        timSortField.isAccessible = true
        timSortField.set(sortInstance, null)
        val array = Array(arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5))
        val comparator = Comparator { obj: Int, integer: Int -> obj.compareTo(integer) }
        sortInstance.sort(array, comparator)
        Assert.assertArrayEquals(arrayOf(1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9), array.items)
    }

    @Test
    @Throws(Exception::class)
    fun testSortArrayWithComparatorAndRangeWithNullTimSort() {
        val timSortField = Sort::class.java.getDeclaredField("timSort")
        timSortField.isAccessible = true
        timSortField.set(sortInstance, null)
        val array = arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)
        val comparator = Comparator { obj: Int, integer: Int -> obj.compareTo(integer) }
        sortInstance.sort(array, comparator, 2, 7)
        Assert.assertArrayEquals(arrayOf(3, 1, 1, 2, 4, 5, 9, 6, 5, 3, 5), array)
    }

    @Test
    fun testSortArrayWithCustomComparator() {
        val array = Array(arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5))
        val customComparator = Comparator.reverseOrder<Int>()
        sortInstance.sort(array, customComparator)
        Assert.assertArrayEquals(arrayOf(9, 6, 5, 5, 5, 4, 3, 3, 2, 1, 1), array.items)
    }

    @Test
    fun testSortEmptyArray() {
        val emptyArray = arrayOf<Int>()
        sortInstance.sort(emptyArray)
        Assert.assertArrayEquals(arrayOf<Int>(), emptyArray)
    }

    @Test
    fun testSortSingleElementArray() {
        val singleElementArray = arrayOf(1)
        sortInstance.sort(singleElementArray)
        Assert.assertArrayEquals(arrayOf(1), singleElementArray)
    }

    @Test
    fun testSortArrayWithNulls() {
        val arrayWithNulls = arrayOf(3, null, 1, 4, null, 2)
        val comparator: Comparator<Int> = NullsFirstComparator()
        sortInstance.sort<Int>(arrayWithNulls, comparator)
        Assert.assertArrayEquals(arrayOf(null, null, 1, 2, 3, 4), arrayWithNulls)
    }

    @Test(expected = ArrayIndexOutOfBoundsException::class)
    fun testSortArrayRangeWithInvalidIndices() {
        val array = arrayOf(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5)
        sortInstance.sort(array, -1, 15)
    }

    @Test
    fun testSortAlreadySortedArrayComparable() {
        val sortedArray = Array(arrayOf(1, 2, 3, 4, 5))
        sortInstance.sort(sortedArray)
        Assert.assertArrayEquals(arrayOf(1, 2, 3, 4, 5), sortedArray.items)
    }

    @Test
    fun testSortArrayWithEqualElements() {
        val equalElementsArray = Array(arrayOf(2, 2, 2, 2, 2))
        sortInstance.sort(equalElementsArray)
        Assert.assertArrayEquals(arrayOf(2, 2, 2, 2, 2), equalElementsArray.items)
    }

    @Test
    fun testSortSingleElementArrayComparable() {
        val singleElementArray = Array(arrayOf(1))
        sortInstance.sort(singleElementArray)
        Assert.assertArrayEquals(arrayOf(1), singleElementArray.items)
    }

    @Test
    fun testSortEmptyArrayComparable() {
        val emptyArray = Array(arrayOf<Int>())
        sortInstance.sort(emptyArray)
        Assert.assertArrayEquals(arrayOf<Int>(), emptyArray.items)
    }
}
