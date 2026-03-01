package com.badlogic.gdx.ai.msg;

import com.badlogic.gdx.ai.GdxAI;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PriorityQueueTest {

    private PriorityQueue<Integer> queue;
    private PriorityQueue<String> stringQueue;

    @Before
    public void setUp() {
        // Set up GdxAI logger to avoid null pointer exceptions
        GdxAI.setLogger(new com.badlogic.gdx.ai.Logger() {
            @Override
            public void debug(String tag, String message) {
                // Suppress debug output during tests
            }

            @Override
            public void debug(String tag, String message, Throwable exception) {
                // Suppress debug output during tests
            }

            @Override
            public void info(String tag, String message) {
                // Suppress info output during tests
            }

            @Override
            public void info(String tag, String message, Throwable exception) {
                // Suppress info output during tests
            }

            @Override
            public void error(String tag, String message) {
                // Suppress error output during tests
            }

            @Override
            public void error(String tag, String message, Throwable exception) {
                // Suppress error output during tests
            }
        });

        queue = new PriorityQueue<>();
        stringQueue = new PriorityQueue<>();
    }

    @After
    public void tearDown() {
        queue = null;
        stringQueue = null;
        GdxAI.setLogger(null);
    }

    @Test
    public void testConstructorDefault() {
        PriorityQueue<Integer> q = new PriorityQueue<>();
        Assert.assertNotNull(q);
        Assert.assertEquals(0, q.size());
        Assert.assertNull(q.peek());
        Assert.assertFalse(q.getUniqueness());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        PriorityQueue<Integer> q = new PriorityQueue<>(20);
        Assert.assertNotNull(q);
        Assert.assertEquals(0, q.size());
        Assert.assertNull(q.peek());
        Assert.assertFalse(q.getUniqueness());
    }

    @Test
    public void testGetAndSetUniqueness() {
        Assert.assertFalse(queue.getUniqueness());

        queue.setUniqueness(true);
        Assert.assertTrue(queue.getUniqueness());

        queue.setUniqueness(false);
        Assert.assertFalse(queue.getUniqueness());
    }

    @Test
    public void testAddSingleElement() {
        Assert.assertTrue(queue.add(5));
        Assert.assertEquals(1, queue.size());
        Assert.assertEquals(Integer.valueOf(5), queue.peek());
    }

    @Test
    public void testAddMultipleElements() {
        Assert.assertTrue(queue.add(5));
        Assert.assertTrue(queue.add(3));
        Assert.assertTrue(queue.add(7));
        Assert.assertTrue(queue.add(1));

        Assert.assertEquals(4, queue.size());
        // Should be min-heap, so smallest element (1) should be at head
        Assert.assertEquals(Integer.valueOf(1), queue.peek());
    }

    @Test
    public void testAddWithNullElement() {
        try {
            queue.add(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Element cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testAddWithUniquenessEnabled() {
        queue.setUniqueness(true);

        Assert.assertTrue(queue.add(5));
        Assert.assertEquals(1, queue.size());

        // Adding duplicate should return false and not change size
        Assert.assertFalse(queue.add(5));
        Assert.assertEquals(1, queue.size());

        // Adding different element should work
        Assert.assertTrue(queue.add(3));
        Assert.assertEquals(2, queue.size());
    }

    @Test
    public void testAddWithUniquenessDisabled() {
        queue.setUniqueness(false);

        Assert.assertTrue(queue.add(5));
        Assert.assertEquals(1, queue.size());

        // Adding duplicate should work when uniqueness is disabled
        Assert.assertTrue(queue.add(5));
        Assert.assertEquals(2, queue.size());
    }

    @Test
    public void testPeekEmptyQueue() {
        Assert.assertNull(queue.peek());
    }

    @Test
    public void testPeekNonEmptyQueue() {
        queue.add(5);
        queue.add(3);
        queue.add(7);

        // Should return smallest element
        Assert.assertEquals(Integer.valueOf(3), queue.peek());

        // Peek should not remove element
        Assert.assertEquals(3, queue.size());
        Assert.assertEquals(Integer.valueOf(3), queue.peek());
    }

    @Test
    public void testGetValidIndex() {
        queue.add(5);
        queue.add(3);
        queue.add(7);

        // Note: get() doesn't guarantee order, but should return non-null for valid indices
        Assert.assertNotNull(queue.get(0));
        Assert.assertNotNull(queue.get(1));
        Assert.assertNotNull(queue.get(2));
    }

    @Test
    public void testGetInvalidIndex() {
        queue.add(5);
        queue.add(3);

        Assert.assertNull(queue.get(2));
        Assert.assertNull(queue.get(10));
    }

    @Test
    public void testSizeEmptyQueue() {
        Assert.assertEquals(0, queue.size());
    }

    @Test
    public void testSizeNonEmptyQueue() {
        Assert.assertEquals(0, queue.size());

        queue.add(5);
        Assert.assertEquals(1, queue.size());

        queue.add(3);
        Assert.assertEquals(2, queue.size());

        queue.add(7);
        Assert.assertEquals(3, queue.size());
    }

    @Test
    public void testClearEmptyQueue() {
        queue.clear();
        Assert.assertEquals(0, queue.size());
        Assert.assertNull(queue.peek());
    }

    @Test
    public void testClearNonEmptyQueue() {
        queue.add(5);
        queue.add(3);
        queue.add(7);

        Assert.assertEquals(3, queue.size());

        queue.clear();
        Assert.assertEquals(0, queue.size());
        Assert.assertNull(queue.peek());

        // Should be able to add elements after clear
        Assert.assertTrue(queue.add(10));
        Assert.assertEquals(1, queue.size());
        Assert.assertEquals(Integer.valueOf(10), queue.peek());
    }

    @Test
    public void testClearWithUniquenessEnabled() {
        queue.setUniqueness(true);
        queue.add(5);
        queue.add(3);

        Assert.assertEquals(2, queue.size());

        queue.clear();
        Assert.assertEquals(0, queue.size());

        // Should be able to add the same elements after clear
        Assert.assertTrue(queue.add(5));
        Assert.assertTrue(queue.add(3));
        Assert.assertEquals(2, queue.size());
    }

    @Test
    public void testPollEmptyQueue() {
        Assert.assertNull(queue.poll());
        Assert.assertEquals(0, queue.size());
    }

    @Test
    public void testPollSingleElement() {
        queue.add(5);

        Assert.assertEquals(Integer.valueOf(5), queue.poll());
        Assert.assertEquals(0, queue.size());
        Assert.assertNull(queue.peek());
    }

    @Test
    public void testPollMultipleElements() {
        queue.add(5);
        queue.add(3);
        queue.add(7);
        queue.add(1);
        queue.add(9);

        // Should remove elements in ascending order (min-heap)
        Assert.assertEquals(Integer.valueOf(1), queue.poll());
        Assert.assertEquals(4, queue.size());
        Assert.assertEquals(Integer.valueOf(3), queue.poll());
        Assert.assertEquals(3, queue.size());
        Assert.assertEquals(Integer.valueOf(5), queue.poll());
        Assert.assertEquals(2, queue.size());
        Assert.assertEquals(Integer.valueOf(7), queue.poll());
        Assert.assertEquals(1, queue.size());
        Assert.assertEquals(Integer.valueOf(9), queue.poll());
        Assert.assertEquals(0, queue.size());
    }

    @Test
    public void testPollWithUniquenessEnabled() {
        queue.setUniqueness(true);
        queue.add(5);
        queue.add(3);
        queue.add(7);

        Assert.assertEquals(3, queue.size());

        Assert.assertEquals(Integer.valueOf(3), queue.poll());
        Assert.assertEquals(2, queue.size());

        // Should be able to add the same element back after polling
        Assert.assertTrue(queue.add(3));
        Assert.assertEquals(3, queue.size());
    }

    @Test
    public void testStringElements() {
        stringQueue.add("zebra");
        stringQueue.add("apple");
        stringQueue.add("banana");

        // String natural ordering - alphabetical
        Assert.assertEquals("apple", stringQueue.peek());
        Assert.assertEquals("apple", stringQueue.poll());
        Assert.assertEquals("banana", stringQueue.poll());
        Assert.assertEquals("zebra", stringQueue.poll());
    }

    @Test
    public void testMixedPositiveAndNegativeNumbers() {
        queue.add(5);
        queue.add(-3);
        queue.add(0);
        queue.add(-10);
        queue.add(7);

        Assert.assertEquals(Integer.valueOf(-10), queue.poll());
        Assert.assertEquals(Integer.valueOf(-3), queue.poll());
        Assert.assertEquals(Integer.valueOf(0), queue.poll());
        Assert.assertEquals(Integer.valueOf(5), queue.poll());
        Assert.assertEquals(Integer.valueOf(7), queue.poll());
    }

    @Test
    public void testLargeNumberOfElements() {

        // Add 1000 elements in random order
        for (int i = 0; i < 1000; i++) {
            int value = (int) (Math.random() * 10000);
            queue.add(value);
        }

        Assert.assertEquals(1000, queue.size());

        // Verify heap property by polling all elements
        Integer prev = null;
        int count = 0;
        Integer current;
        while ((current = queue.poll()) != null) {
            if (prev != null) {
                Assert.assertTrue("Heap property violated: " + prev + " should be <= " + current, prev <= current);
            }
            prev = current;
            count++;
        }

        Assert.assertEquals(1000, count);
        Assert.assertEquals(0, queue.size());
    }

    @Test
    public void testCapacityGrowth() {
        // Start with small capacity
        PriorityQueue<Integer> smallQueue = new PriorityQueue<>(2);

        // Add more elements than initial capacity
        for (int i = 0; i < 100; i++) {
            Assert.assertTrue(smallQueue.add(i));
        }

        Assert.assertEquals(100, smallQueue.size());

        // Verify all elements are still accessible and in correct order
        for (int i = 0; i < 100; i++) {
            Assert.assertEquals(Integer.valueOf(i), smallQueue.poll());
        }
    }

    @Test
    public void testComparableElements() {
        // Test with custom comparable objects
        PriorityQueue<TestComparable> comparableQueue = new PriorityQueue<>();

        TestComparable a = new TestComparable(5);
        TestComparable b = new TestComparable(3);
        TestComparable c = new TestComparable(7);

        comparableQueue.add(a);
        comparableQueue.add(b);
        comparableQueue.add(c);

        Assert.assertEquals(b, comparableQueue.peek());
        Assert.assertEquals(b, comparableQueue.poll());
        Assert.assertEquals(a, comparableQueue.poll());
        Assert.assertEquals(c, comparableQueue.poll());
    }

    @Test
    public void testAddAllAndPollAll() {
        int[] values = {5, 3, 7, 1, 9, 2, 8, 4, 6};

        for (int value : values) {
            queue.add(value);
        }

        Assert.assertEquals(values.length, queue.size());

        // Poll all elements and verify they come out in sorted order
        int expectedValue = 1;
        while (queue.size() > 0) {
            Assert.assertEquals(Integer.valueOf(expectedValue), queue.poll());
            expectedValue++;
        }
    }

    @Test
    public void testPeekAfterPoll() {
        queue.add(5);
        queue.add(3);
        queue.add(7);

        Assert.assertEquals(Integer.valueOf(3), queue.peek());

        queue.poll();
        Assert.assertEquals(Integer.valueOf(5), queue.peek());

        queue.poll();
        Assert.assertEquals(Integer.valueOf(7), queue.peek());

        queue.poll();
        Assert.assertNull(queue.peek());
    }

    @Test
    public void testGetAfterOperations() {
        queue.add(5);
        queue.add(3);
        queue.add(7);

        // get() should return non-null for valid indices
        for (int i = 0; i < queue.size(); i++) {
            Assert.assertNotNull(queue.get(i));
        }

        queue.poll();

        // After poll, indices should still be valid for remaining elements
        for (int i = 0; i < queue.size(); i++) {
            Assert.assertNotNull(queue.get(i));
        }
    }

    @Test
    public void testUniquenessWithClear() {
        queue.setUniqueness(true);

        queue.add(5);
        queue.add(3);
        queue.add(7);

        Assert.assertEquals(3, queue.size());

        queue.clear();
        Assert.assertEquals(0, queue.size());

        // Should be able to add the same elements after clear
        Assert.assertTrue(queue.add(5));
        Assert.assertTrue(queue.add(3));
        Assert.assertTrue(queue.add(7));
        Assert.assertEquals(3, queue.size());
    }

    @Test
    public void testEdgeCaseMaxIntegerValue() {
        queue.add(Integer.MAX_VALUE);
        queue.add(Integer.MIN_VALUE);
        queue.add(0);

        Assert.assertEquals(Integer.valueOf(Integer.MIN_VALUE), queue.peek());
        Assert.assertEquals(Integer.valueOf(Integer.MIN_VALUE), queue.poll());
        Assert.assertEquals(Integer.valueOf(0), queue.poll());
        Assert.assertEquals(Integer.valueOf(Integer.MAX_VALUE), queue.poll());
    }

    @Test
    public void testConsistencyAfterMixedOperations() {
        // Perform mixed add/poll operations and verify consistency
        queue.add(10);
        queue.add(5);
        queue.add(15);

        Assert.assertEquals(Integer.valueOf(5), queue.poll());

        queue.add(3);
        queue.add(20);

        Assert.assertEquals(Integer.valueOf(3), queue.poll());
        Assert.assertEquals(Integer.valueOf(10), queue.poll());

        queue.add(1);

        Assert.assertEquals(Integer.valueOf(1), queue.poll());
        Assert.assertEquals(Integer.valueOf(15), queue.poll());
        Assert.assertEquals(Integer.valueOf(20), queue.poll());
    }

    // Helper class for testing comparable elements
    private static class TestComparable implements Comparable<TestComparable> {
        private final int value;

        public TestComparable(int value) {
            this.value = value;
        }

        @Override
        public int compareTo(TestComparable other) {
            return Integer.compare(this.value, other.value);
        }

        @Override
        public String toString() {
            return "TestComparable(" + value + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TestComparable that = (TestComparable) obj;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return value;
        }
    }
}
