package com.badlogic.gdx.ai.pfa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.badlogic.gdx.utils.Array;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

/**
 * Comprehensive unit tests for DefaultGraphPath class.
 * <p>
 * Test Coverage:
 * - All 3 constructor variants (no-args, with capacity, with Array)
 * - All GraphPath interface methods (clear, getCount, add, get, reverse, iterator)
 * - Edge cases and error conditions
 * - Array delegation behavior
 * - Iterator functionality
 * - State management and consistency
 */
public class DefaultGraphPathTest {

    private DefaultGraphPath<String> graphPath;
    private DefaultGraphPath<Integer> intGraphPath;

    @Before
    public void setUp() {
        graphPath = new DefaultGraphPath<>();
        intGraphPath = new DefaultGraphPath<>();
    }

    // Constructor Tests

    @Test
    public void testConstructorNoArgs() {
        DefaultGraphPath<String> path = new DefaultGraphPath<>();
        assertNotNull("nodes array should be initialized", path.nodes);
        assertEquals("nodes array should be empty", 0, path.nodes.size);
        assertEquals("path count should be 0", 0, path.getCount());
    }

    @Test
    public void testConstructorWithCapacity() {
        DefaultGraphPath<String> path = new DefaultGraphPath<>(10);
        assertNotNull("nodes array should be initialized", path.nodes);
        assertEquals("nodes array should be empty", 0, path.nodes.size);
        assertEquals("path count should be 0", 0, path.getCount());
    }

    @Test
    public void testConstructorWithArray() {
        Array<String> array = new Array<>();
        array.add("A");
        array.add("B");
        array.add("C");

        DefaultGraphPath<String> path = new DefaultGraphPath<>(array);
        assertNotNull("nodes array should be initialized", path.nodes);
        assertSame("nodes array should be the provided array", array, path.nodes);
        assertEquals("path count should match array size", 3, path.getCount());
    }

    @Test
    public void testConstructorWithNullArray() {
        DefaultGraphPath<String> path = new DefaultGraphPath<>(null);
        // The constructor assigns the null array directly, so nodes will be null
        assertNull("nodes array should be null", path.nodes);
        // getCount() will throw NullPointerException when nodes is null
        try {
            path.getCount();
            fail("Should throw NullPointerException when getting count with null nodes");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // GraphPath Interface Method Tests

    @Test
    public void testClear() {
        graphPath.add("A");
        graphPath.add("B");
        graphPath.add("C");
        assertEquals("path should have 3 nodes", 3, graphPath.getCount());

        graphPath.clear();
        assertEquals("path should be empty after clear", 0, graphPath.getCount());
        assertTrue("nodes array should be empty", graphPath.nodes.isEmpty());
    }

    @Test
    public void testClearEmptyPath() {
        assertEquals("path should start empty", 0, graphPath.getCount());

        graphPath.clear();
        assertEquals("path should remain empty", 0, graphPath.getCount());
        assertTrue("nodes array should remain empty", graphPath.nodes.isEmpty());
    }

    @Test
    public void testClearWithNullNodes() {
        DefaultGraphPath<String> path = new DefaultGraphPath<>(null);

        try {
            path.clear();
            fail("Should throw NullPointerException when clearing null nodes array");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testGetCount() {
        assertEquals("initial count should be 0", 0, graphPath.getCount());

        graphPath.add("A");
        assertEquals("count should be 1 after adding one node", 1, graphPath.getCount());

        graphPath.add("B");
        assertEquals("count should be 2 after adding second node", 2, graphPath.getCount());

        graphPath.clear();
        assertEquals("count should be 0 after clear", 0, graphPath.getCount());
    }

    @Test
    public void testGetCountWithNullNodes() {
        DefaultGraphPath<String> path = new DefaultGraphPath<>(null);
        try {
            path.getCount();
            fail("Should throw NullPointerException when getting count with null nodes");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testAdd() {
        graphPath.add("A");
        assertEquals("should have 1 node", 1, graphPath.getCount());
        assertEquals("first node should be A", "A", graphPath.get(0));

        graphPath.add("B");
        assertEquals("should have 2 nodes", 2, graphPath.getCount());
        assertEquals("second node should be B", "B", graphPath.get(1));

        graphPath.add("C");
        assertEquals("should have 3 nodes", 3, graphPath.getCount());
        assertEquals("third node should be C", "C", graphPath.get(2));
    }

    @Test
    public void testAddNull() {
        graphPath.add(null);
        assertEquals("should have 1 node", 1, graphPath.getCount());
        assertNull("node should be null", graphPath.get(0));

        graphPath.add("A");
        assertEquals("should have 2 nodes", 2, graphPath.getCount());
        assertEquals("second node should be A", "A", graphPath.get(1));
    }

    @Test
    public void testAddWithNullNodes() {
        DefaultGraphPath<String> path = new DefaultGraphPath<>(null);

        try {
            path.add("A");
            fail("Should throw NullPointerException when adding to null nodes array");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testGet() {
        graphPath.add("A");
        graphPath.add("B");
        graphPath.add("C");

        assertEquals("first node should be A", "A", graphPath.get(0));
        assertEquals("second node should be B", "B", graphPath.get(1));
        assertEquals("third node should be C", "C", graphPath.get(2));
    }

    @Test
    public void testGetWithInvalidIndex() {
        graphPath.add("A");
        graphPath.add("B");

        try {
            graphPath.get(-1);
            fail("Should throw exception for negative index");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
        }

        try {
            graphPath.get(2);
            fail("Should throw exception for index equal to size");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
        }

        try {
            graphPath.get(10);
            fail("Should throw exception for index greater than size");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    @Test
    public void testGetWithEmptyPath() {
        try {
            graphPath.get(0);
            fail("Should throw exception for empty path");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    @Test
    public void testGetWithNullNodes() {
        DefaultGraphPath<String> path = new DefaultGraphPath<>(null);

        try {
            path.get(0);
            fail("Should throw NullPointerException with null nodes array");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testReverse() {
        graphPath.add("A");
        graphPath.add("B");
        graphPath.add("C");

        graphPath.reverse();

        assertEquals("should still have 3 nodes", 3, graphPath.getCount());
        assertEquals("first node should be C", "C", graphPath.get(0));
        assertEquals("second node should be B", "B", graphPath.get(1));
        assertEquals("third node should be A", "A", graphPath.get(2));
    }

    @Test
    public void testReverseEmptyPath() {
        assertEquals("path should start empty", 0, graphPath.getCount());

        graphPath.reverse();
        assertEquals("path should remain empty", 0, graphPath.getCount());
    }

    @Test
    public void testReverseSingleElement() {
        graphPath.add("A");
        assertEquals("path should have 1 element", 1, graphPath.getCount());

        graphPath.reverse();
        assertEquals("path should still have 1 element", 1, graphPath.getCount());
        assertEquals("element should be A", "A", graphPath.get(0));
    }

    @Test
    public void testReverseTwoElements() {
        graphPath.add("A");
        graphPath.add("B");

        graphPath.reverse();

        assertEquals("should still have 2 elements", 2, graphPath.getCount());
        assertEquals("first element should be B", "B", graphPath.get(0));
        assertEquals("second element should be A", "A", graphPath.get(1));
    }

    @Test
    public void testReverseWithNullNodes() {
        DefaultGraphPath<String> path = new DefaultGraphPath<>(null);

        try {
            path.reverse();
            fail("Should throw NullPointerException when reversing null nodes array");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testIterator() {
        graphPath.add("A");
        graphPath.add("B");
        graphPath.add("C");

        Iterator<String> iterator = graphPath.iterator();
        assertNotNull("iterator should not be null", iterator);

        assertTrue("iterator should have next", iterator.hasNext());
        assertEquals("first element should be A", "A", iterator.next());

        assertTrue("iterator should have next", iterator.hasNext());
        assertEquals("second element should be B", "B", iterator.next());

        assertTrue("iterator should have next", iterator.hasNext());
        assertEquals("third element should be C", "C", iterator.next());

        assertFalse("iterator should not have more elements", iterator.hasNext());
    }

    @Test
    public void testIteratorEmptyPath() {
        Iterator<String> iterator = graphPath.iterator();
        assertNotNull("iterator should not be null", iterator);
        assertFalse("iterator should not have elements", iterator.hasNext());
    }

    @Test
    public void testIteratorWithNullNodes() {
        DefaultGraphPath<String> path = new DefaultGraphPath<>(null);

        try {
            path.iterator();
            fail("Should throw NullPointerException with null nodes array");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // Integration Tests

    @Test
    public void testMultipleOperations() {
        // Add elements
        graphPath.add("A");
        graphPath.add("B");
        graphPath.add("C");
        assertEquals("should have 3 elements", 3, graphPath.getCount());

        // Reverse
        graphPath.reverse();
        assertEquals("first should be C", "C", graphPath.get(0));
        assertEquals("second should be B", "B", graphPath.get(1));
        assertEquals("third should be A", "A", graphPath.get(2));

        // Add more elements
        graphPath.add("D");
        graphPath.add("E");
        assertEquals("should have 5 elements", 5, graphPath.getCount());
        assertEquals("fourth should be D", "D", graphPath.get(3));
        assertEquals("fifth should be E", "E", graphPath.get(4));

        // Clear
        graphPath.clear();
        assertEquals("should be empty", 0, graphPath.getCount());

        // Add elements after clear
        graphPath.add("X");
        graphPath.add("Y");
        assertEquals("should have 2 elements after clear", 2, graphPath.getCount());
        assertEquals("first should be X", "X", graphPath.get(0));
        assertEquals("second should be Y", "Y", graphPath.get(1));
    }

    @Test
    public void testWithDifferentTypes() {
        // Test with Integer type
        intGraphPath.add(1);
        intGraphPath.add(2);
        intGraphPath.add(3);

        assertEquals("should have 3 integers", 3, intGraphPath.getCount());
        assertEquals("first should be 1", Integer.valueOf(1), intGraphPath.get(0));
        assertEquals("second should be 2", Integer.valueOf(2), intGraphPath.get(1));
        assertEquals("third should be 3", Integer.valueOf(3), intGraphPath.get(2));

        intGraphPath.reverse();
        assertEquals("first should be 3 after reverse", Integer.valueOf(3), intGraphPath.get(0));
        assertEquals("second should be 2 after reverse", Integer.valueOf(2), intGraphPath.get(1));
        assertEquals("third should be 1 after reverse", Integer.valueOf(1), intGraphPath.get(2));
    }

    @Test
    public void testArrayDelegation() {
        Array<String> array = new Array<>();
        DefaultGraphPath<String> path = new DefaultGraphPath<>(array);

        // Verify that operations affect the underlying array
        path.add("A");
        path.add("B");

        assertEquals("array should have 2 elements", 2, array.size);
        assertEquals("array first element should be A", "A", array.get(0));
        assertEquals("array second element should be B", "B", array.get(1));

        path.reverse();
        assertEquals("array first should be B after reverse", "B", array.get(0));
        assertEquals("array second should be A after reverse", "A", array.get(1));

        path.clear();
        assertEquals("array should be empty", 0, array.size);
    }

    @Test
    public void testLargePath() {
        // Test with a large number of elements
        int size = 1000;
        for (int i = 0; i < size; i++) {
            graphPath.add("Node" + i);
        }

        assertEquals("should have " + size + " elements", size, graphPath.getCount());
        assertEquals("first element should be Node0", "Node0", graphPath.get(0));
        assertEquals("last element should be Node" + (size - 1), "Node" + (size - 1), graphPath.get(size - 1));

        // Test reverse on large path
        graphPath.reverse();
        assertEquals("first should be last after reverse", "Node" + (size - 1), graphPath.get(0));
        assertEquals("last should be first after reverse", "Node0", graphPath.get(size - 1));

        // Test iterator on large path
        int count = 0;
        for (String node : graphPath) {
            assertNotNull("node should not be null", node);
            count++;
        }
        assertEquals("iterator should visit all elements", size, count);
    }

    @Test
    public void testIteratorRemove() {
        graphPath.add("A");
        graphPath.add("B");
        graphPath.add("C");

        Iterator<String> iterator = graphPath.iterator();
        if (iterator.hasNext()) {
            iterator.next();
            try {
                iterator.remove();
                // If remove is supported, verify the path changed
                assertEquals("path should have 2 elements after remove", 2, graphPath.getCount());
            } catch (UnsupportedOperationException e) {
                // Expected behavior - Array iterator may not support remove
                // Verify path is unchanged
                assertEquals("path should still have 3 elements", 3, graphPath.getCount());
            }
        }
    }

    @Test
    public void testGraphPathInterface() {
        // Test that DefaultGraphPath properly implements GraphPath interface
        GraphPath<String> path = graphPath;

        // Test interface methods
        assertEquals("interface getCount should work", 0, path.getCount());

        path.add("A");
        assertEquals("interface getCount should reflect addition", 1, path.getCount());
        assertEquals("interface get should work", "A", path.get(0));

        path.add("B");
        assertEquals("interface getCount should reflect second addition", 2, path.getCount());
        assertEquals("interface get should work for second element", "B", path.get(1));

        path.reverse();
        assertEquals("interface get should work after reverse", "B", path.get(0));
        assertEquals("interface get should work after reverse", "A", path.get(1));

        path.clear();
        assertEquals("interface getCount should reflect clear", 0, path.getCount());

        // Test iterable interface
        path.add("X");
        path.add("Y");

        int count = 0;
        for (String node : path) {
            assertNotNull("iterable node should not be null", node);
            count++;
        }
        assertEquals("iterable should visit all elements", 2, count);
    }

    @Test
    public void testCapacityConstructorBehavior() {
        // Test that capacity constructor creates array with sufficient capacity
        DefaultGraphPath<String> path = new DefaultGraphPath<>(5);

        // Add elements up to capacity
        for (int i = 0; i < 5; i++) {
            path.add("Node" + i);
        }

        assertEquals("should hold 5 elements", 5, path.getCount());

        // Add one more to test array growth
        path.add("Node5");
        assertEquals("should hold 6 elements (array should grow)", 6, path.getCount());
        assertEquals("last element should be Node5", "Node5", path.get(5));
    }
}
