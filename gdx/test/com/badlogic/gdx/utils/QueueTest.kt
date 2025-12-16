package com.badlogic.gdx.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.fail
import org.junit.Test

class QueueTest {
    @Test
    fun addFirstAndLastTest() {
        val queue = Queue<Int?>()
        queue.addFirst(1)
        queue.addLast(2)
        queue.addFirst(3)
        queue.addLast(4)

        assertEquals(0, queue.indexOf(3, true).toLong())
        assertEquals(1, queue.indexOf(1, true).toLong())
        assertEquals(2, queue.indexOf(2, true).toLong())
        assertEquals(3, queue.indexOf(4, true).toLong())
    }

    @Test
    fun removeLastTest() {
        val queue = Queue<Int?>()
        queue.addLast(1)
        queue.addLast(2)
        queue.addLast(3)
        queue.addLast(4)

        assertEquals(4, queue.size.toLong())
        assertEquals(3, queue.indexOf(4, true).toLong())
        assertEquals(4, queue.removeLast() as Any?)
        assertEquals(3, queue.size.toLong())
        assertEquals(2, queue.indexOf(3, true).toLong())
        assertEquals(3, queue.removeLast() as Any?)
        assertEquals(2, queue.size.toLong())
        assertEquals(1, queue.indexOf(2, true).toLong())
        assertEquals(2, queue.removeLast() as Any?)
        assertEquals(1, queue.size.toLong())
        assertEquals(0, queue.indexOf(1, true).toLong())
        assertEquals(1, queue.removeLast() as Any?)
        assertEquals(0, queue.size.toLong())
    }

    @Test
    fun removeFirstTest() {
        val queue = Queue<Int?>()
        queue.addLast(1)
        queue.addLast(2)
        queue.addLast(3)
        queue.addLast(4)

        assertEquals(4, queue.size.toLong())
        assertEquals(0, queue.indexOf(1, true).toLong())
        assertEquals(1, queue.removeFirst() as Any?)
        assertEquals(3, queue.size.toLong())
        assertEquals(0, queue.indexOf(2, true).toLong())
        assertEquals(2, queue.removeFirst() as Any?)
        assertEquals(2, queue.size.toLong())
        assertEquals(0, queue.indexOf(3, true).toLong())
        assertEquals(3, queue.removeFirst() as Any?)
        assertEquals(1, queue.size.toLong())
        assertEquals(0, queue.indexOf(4, true).toLong())
        assertEquals(4, queue.removeFirst() as Any?)
        assertEquals(0, queue.size.toLong())
    }

    @Test
    fun resizableQueueTest() {
        val q = Queue<Int?>(8)

        assertEquals("New queue is not empty!", 0, q.size.toLong())

        for (i in 0..99) {
            for (j in 0..<i) {
                try {
                    q.addLast(j)
                } catch (_: IllegalStateException) {
                    fail("Failed to add element $j ($i)")
                }
                val peeked = q.last()
                assertEquals("peekLast shows $peeked, should be $j ($i)", (peeked as Int).toLong(), j.toLong())
                val size = q.size
                assertEquals("Size should be " + (j + 1) + " but is " + size + " (" + i + ")", size.toLong(), (j + 1).toLong())
            }

            if (i != 0) {
                val peek = q.first()
                assertEquals("First thing is not zero but $peek ($i)", 0, (peek as Int).toLong())
            }

            for (j in 0..<i) {
                val pop = q.removeFirst()
                assertEquals("Popped should be $j but is $pop ($i)", (pop as Int).toLong(), j.toLong())

                val size = q.size
                assertEquals("Size should be " + (i - 1 - j) + " but is " + size + " (" + i + ")", size.toLong(), (i - 1 - j).toLong())
            }

            assertEquals("Not empty after cycle $i", 0, q.size.toLong())
        }

        for (i in 0..55) {
            q.addLast(42)
        }
        q.clear()
        assertEquals("Clear did not clear properly", 0, q.size.toLong())
    }

    /**
     * Same as resizableQueueTest, but in reverse
     */
    @Test
    fun resizableDequeTest() {
        val q = Queue<Int?>(8)

        assertEquals("New deque is not empty!", 0, q.size.toLong())

        for (i in 0..99) {
            for (j in 0..<i) {
                try {
                    q.addFirst(j)
                } catch (_: IllegalStateException) {
                    fail("Failed to add element $j ($i)")
                }
                val peeked = q.first()
                assertEquals("peek shows $peeked, should be $j ($i)", (peeked as Int).toLong(), j.toLong())
                val size = q.size
                assertEquals("Size should be " + (j + 1) + " but is " + size + " (" + i + ")", size.toLong(), (j + 1).toLong())
            }

            if (i != 0) {
                val peek = q.last()
                assertEquals("Last thing is not zero but $peek ($i)", 0, (peek as Int).toLong())
            }

            for (j in 0..<i) {
                val pop = q.removeLast()
                assertEquals("Popped should be $j but is $pop ($i)", (pop as Int).toLong(), j.toLong())

                val size = q.size
                assertEquals("Size should be " + (i - 1 - j) + " but is " + size + " (" + i + ")", size.toLong(), (i - 1 - j).toLong())
            }

            assertEquals("Not empty after cycle $i", 0, q.size.toLong())
        }

        for (i in 0..55) {
            q.addFirst(42)
        }
        q.clear()
        assertEquals("Clear did not clear properly", 0, q.size.toLong())
    }

    @Test
    fun getTest() {
        val q = Queue<Int?>(7)
        for (i in 0..4) {
            for (j in 0..3) {
                q.addLast(j)
            }
            assertEquals("get(0) is not equal to peek ($i)", q.get(0), q.first())
            assertEquals("get(size-1) is not equal to peekLast ($i)", q.get(q.size - 1), q.last())
            for (j in 0..3) {
                assertEquals((q.get(j) as Int).toLong(), j.toLong())
            }
            for (j in 0..<4 - 1) {
                q.removeFirst()
                assertEquals("get(0) is not equal to peek ($i)", q.get(0), q.first())
            }
            q.removeFirst()
            assert(
                q.size == 0 // Failing this means broken test
            )
            try {
                q.get(0)
                fail("get() on empty queue did not throw")
            } catch (_: IndexOutOfBoundsException) {
                // Expected
            }
        }
    }

    @Test
    fun removeTest() {
        val q = Queue<Int?>()

        // Test head < tail.
        for (j in 0..6) q.addLast(j)
        assertValues(q, 0, 1, 2, 3, 4, 5, 6)
        q.removeIndex(0)
        assertValues(q, 1, 2, 3, 4, 5, 6)
        q.removeIndex(1)
        assertValues(q, 1, 3, 4, 5, 6)
        q.removeIndex(4)
        assertValues(q, 1, 3, 4, 5)
        q.removeIndex(2)
        assertValues(q, 1, 3, 5)

        // Test head >= tail and index >= head.
        q.clear()
        for (j in 2 downTo 0) q.addFirst(j)
        for (j in 3..6) q.addLast(j)
        assertValues(q, 0, 1, 2, 3, 4, 5, 6)
        q.removeIndex(1)
        assertValues(q, 0, 2, 3, 4, 5, 6)
        q.removeIndex(0)
        assertValues(q, 2, 3, 4, 5, 6)

        // Test head >= tail and index < tail.
        q.clear()
        for (j in 2 downTo 0) q.addFirst(j)
        for (j in 3..6) q.addLast(j)
        assertValues(q, 0, 1, 2, 3, 4, 5, 6)
        q.removeIndex(5)
        assertValues(q, 0, 1, 2, 3, 4, 6)
        q.removeIndex(5)
        assertValues(q, 0, 1, 2, 3, 4)
    }

    @Test
    fun indexOfTest() {
        val q = Queue<Int?>()

        // Test head < tail.
        for (j in 0..6) q.addLast(j)
        for (j in 0..6) assertEquals(q.indexOf(j, false).toLong(), j.toLong())

        // Test head >= tail.
        q.clear()
        for (j in 2 downTo 0) q.addFirst(j)
        for (j in 3..6) q.addLast(j)
        for (j in 0..6) assertEquals(q.indexOf(j, false).toLong(), j.toLong())
    }

    @Test
    fun iteratorTest() {
        val q = Queue<Int?>()

        // Test head < tail.
        for (j in 0..6) q.addLast(j)
        var iter = q.iterator()
        for (j in 0..6) assertEquals(iter.next()!!.toLong(), j.toLong())
        iter = q.iterator()
        iter.next()
        iter.remove()
        assertValues(q, 1, 2, 3, 4, 5, 6)
        iter.next()
        iter.remove()
        assertValues(q, 2, 3, 4, 5, 6)
        iter.next()
        iter.next()
        iter.remove()
        assertValues(q, 2, 4, 5, 6)
        iter.next()
        iter.next()
        iter.next()
        iter.remove()
        assertValues(q, 2, 4, 5)

        // Test head >= tail.
        q.clear()
        for (j in 2 downTo 0) q.addFirst(j)
        for (j in 3..6) q.addLast(j)
        iter = q.iterator()
        for (j in 0..6) assertEquals(iter.next()!!.toLong(), j.toLong())
        iter = q.iterator()
        iter.next()
        iter.remove()
        assertValues(q, 1, 2, 3, 4, 5, 6)
        iter.next()
        iter.remove()
        assertValues(q, 2, 3, 4, 5, 6)
        iter.next()
        iter.next()
        iter.remove()
        assertValues(q, 2, 4, 5, 6)
        iter.next()
        iter.next()
        iter.next()
        iter.remove()
        assertValues(q, 2, 4, 5)
    }

    @Test
    fun iteratorRemoveEdgeCaseTest() { // See #4300
        val queue = Queue<Int?>()

        // Simulate normal usage
        for (i in 0..99) {
            queue.addLast(i)
            if (i > 50) queue.removeFirst()
        }

        val it = queue.iterator()
        while (it.hasNext()) {
            it.next()
            it.remove()
        }

        queue.addLast(1337)

        val i = queue.first()
        assertEquals(1337, (i as Int).toLong())
    }

    @Test
    fun toStringTest() {
        val q = Queue<Int?>(1)
        assertEquals("[]", q.toString())
        q.addLast(4)
        assertEquals("[4]", q.toString())
        q.addLast(5)
        q.addLast(6)
        q.addLast(7)
        assertEquals("[4, 5, 6, 7]", q.toString())
    }

    @Test
    fun hashEqualsTest() {
        val q1 = Queue<Int?>()
        val q2 = Queue<Int?>()

        assertEqualsAndHash(q1, q2)
        q1.addFirst(1)
        assertNotEquals(q1, q2)
        q2.addFirst(1)
        assertEqualsAndHash(q1, q2)

        q1.clear()
        q1.addLast(1)
        q1.addLast(2)
        q2.addLast(2)
        assertEqualsAndHash(q1, q2)

        for (i in 0..99) {
            q1.addLast(i)
            q1.addLast(i)
            q1.removeFirst()

            assertNotEquals(q1, q2)

            q2.addLast(i)
            q2.addLast(i)
            q2.removeFirst()

            assertEqualsAndHash(q1, q2)
        }
    }

    private fun assertEqualsAndHash(q1: Queue<*>, q2: Queue<*>) {
        assertEquals(q1, q2)
        assertEquals("Hash codes are not equal", q1.hashCode().toLong(), q2.hashCode().toLong())
    }

    private fun assertValues(q: Queue<Int?>, vararg values: Int?) {
        var i = 0
        val n = values.size
        while (i < n) {
            assertEquals(values[i], q.get(i))
            i++
        }
    }
}
