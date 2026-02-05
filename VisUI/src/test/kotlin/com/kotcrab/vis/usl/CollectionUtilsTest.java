package com.kotcrab.vis.usl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link CollectionUtils}.
 */
public class CollectionUtilsTest {

    @Test
    public void testIsEqualCollectionBothEmpty() {
        Collection<String> a = new ArrayList<>();
        Collection<String> b = new ArrayList<>();
        assertTrue("Empty collections should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionOneEmptyOneNotEmpty() {
        Collection<String> a = new ArrayList<>();
        Collection<String> b = new ArrayList<>();
        b.add("item");
        assertFalse("Empty collection should not equal non-empty collection", CollectionUtils.isEqualCollection(a, b));
        assertFalse("Non-empty collection should not equal empty collection", CollectionUtils.isEqualCollection(b, a));
    }

    @Test
    public void testIsEqualCollectionSameElementsSameOrder() {
        Collection<String> a = new ArrayList<>();
        Collection<String> b = new ArrayList<>();
        a.add("a");
        a.add("b");
        a.add("c");
        b.add("a");
        b.add("b");
        b.add("c");
        assertTrue("Collections with same elements in same order should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionSameElementsDifferentOrder() {
        Collection<String> a = new ArrayList<>();
        Collection<String> b = new ArrayList<>();
        a.add("a");
        a.add("b");
        a.add("c");
        b.add("c");
        b.add("a");
        b.add("b");
        assertTrue("Collections with same elements in different order should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionDifferentElements() {
        Collection<String> a = new ArrayList<>();
        Collection<String> b = new ArrayList<>();
        a.add("a");
        a.add("b");
        b.add("x");
        b.add("y");
        assertFalse("Collections with different elements should not be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionWithDuplicates() {
        Collection<String> a = new ArrayList<>();
        Collection<String> b = new ArrayList<>();
        a.add("a");
        a.add("a");
        a.add("b");
        b.add("a");
        b.add("b");
        b.add("a");
        assertTrue("Collections with same duplicates should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionDifferentCardinality() {
        Collection<String> a = new ArrayList<>();
        Collection<String> b = new ArrayList<>();
        a.add("a");
        a.add("a");
        a.add("b");
        b.add("a");
        b.add("b");
        b.add("b");
        assertFalse("Collections with different cardinality should not be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionWithNullElements() {
        Collection<String> a = new ArrayList<>();
        Collection<String> b = new ArrayList<>();
        a.add(null);
        a.add("a");
        b.add(null);
        b.add("a");
        assertTrue("Collections with null elements should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionWithNullElementsDifferentCardinality() {
        Collection<String> a = new ArrayList<>();
        Collection<String> b = new ArrayList<>();
        a.add(null);
        a.add(null);
        a.add("a");
        b.add(null);
        b.add("a");
        assertFalse("Collections with different null cardinality should not be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionDifferentTypes() {
        Collection<String> a = new ArrayList<>();
        Collection<Integer> b = new ArrayList<>();
        a.add("1");
        a.add("2");
        b.add(1);
        b.add(2);
        assertFalse("Collections with different element types should not be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionDifferentCollectionTypes() {
        List<String> a = new ArrayList<>();
        List<String> b = new LinkedList<>();
        a.add("a");
        a.add("b");
        b.add("a");
        b.add("b");
        assertTrue("Different collection types with same elements should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionLargeCollections() {
        Collection<Integer> a = new ArrayList<>();
        Collection<Integer> b = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            a.add(i);
            b.add(i);
        }
        assertTrue("Large collections with same elements should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionWithMixedObjectTypes() {
        Collection<Object> a = new ArrayList<>();
        Collection<Object> b = new ArrayList<>();
        a.add("string");
        a.add(42);
        a.add(3.14);
        b.add("string");
        b.add(42);
        b.add(3.14);
        assertTrue("Collections with mixed object types should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testGetCardinalityMapEmpty() {
        Collection<String> col = new ArrayList<>();
        Map<String, Integer> result = CollectionUtils.getCardinalityMap(col);
        assertTrue("Cardinality map of empty collection should be empty", result.isEmpty());
    }

    @Test
    public void testGetCardinalityMapSingleElement() {
        Collection<String> col = new ArrayList<>();
        col.add("a");
        Map<String, Integer> result = CollectionUtils.getCardinalityMap(col);
        assertEquals("Cardinality map should have one entry", 1, result.size());
        assertEquals("Element should have cardinality 1", Integer.valueOf(1), result.get("a"));
    }

    @Test
    public void testGetCardinalityMapUniqueElements() {
        Collection<String> col = new ArrayList<>();
        col.add("a");
        col.add("b");
        col.add("c");
        Map<String, Integer> result = CollectionUtils.getCardinalityMap(col);
        assertEquals("Cardinality map should have three entries", 3, result.size());
        assertEquals("Element 'a' should have cardinality 1", Integer.valueOf(1), result.get("a"));
        assertEquals("Element 'b' should have cardinality 1", Integer.valueOf(1), result.get("b"));
        assertEquals("Element 'c' should have cardinality 1", Integer.valueOf(1), result.get("c"));
    }

    @Test
    public void testGetCardinalityMapWithDuplicates() {
        Collection<String> col = new ArrayList<>();
        col.add("a");
        col.add("a");
        col.add("b");
        col.add("a");
        col.add("c");
        col.add("b");
        col.add("b");
        Map<String, Integer> result = CollectionUtils.getCardinalityMap(col);
        assertEquals("Cardinality map should have three entries", 3, result.size());
        assertEquals("Element 'a' should have cardinality 3", Integer.valueOf(3), result.get("a"));
        assertEquals("Element 'b' should have cardinality 3", Integer.valueOf(3), result.get("b"));
        assertEquals("Element 'c' should have cardinality 1", Integer.valueOf(1), result.get("c"));
    }

    @Test
    public void testGetCardinalityMapWithNullElements() {
        Collection<String> col = new ArrayList<>();
        col.add(null);
        col.add("a");
        col.add(null);
        col.add("b");
        Map<Object, Integer> result = CollectionUtils.getCardinalityMap(col);
        assertEquals("Cardinality map should have three entries", 3, result.size());
        assertEquals("Null element should have cardinality 2", Integer.valueOf(2), result.get(null));
        assertEquals("Element 'a' should have cardinality 1", Integer.valueOf(1), result.get("a"));
        assertEquals("Element 'b' should have cardinality 1", Integer.valueOf(1), result.get("b"));
    }

    @Test
    public void testGetCardinalityMapWithMixedTypes() {
        Collection<Object> col = new ArrayList<>();
        col.add("string");
        col.add(42);
        col.add("string");
        col.add(3.14);
        col.add(42);
        col.add(42);
        Map<Object, Integer> result = CollectionUtils.getCardinalityMap(col);
        assertEquals("Cardinality map should have three entries", 3, result.size());
        assertEquals("String element should have cardinality 2", Integer.valueOf(2), result.get("string"));
        assertEquals("Integer element should have cardinality 3", Integer.valueOf(3), result.get(42));
        assertEquals("Double element should have cardinality 1", Integer.valueOf(1), result.get(3.14));
    }

    @Test
    public void testGetCardinalityMapReturnsHashMap() {
        Collection<String> col = new ArrayList<>();
        col.add("a");
        Map<String, Integer> result = CollectionUtils.getCardinalityMap(col);
        assertTrue("Result should be a HashMap", result instanceof HashMap);
    }

    @Test
    public void testGetCardinalityMapLargeCollection() {
        Collection<Integer> col = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            // Add each number i times to create interesting cardinalities
            for (int j = 0; j < i; j++) {
                col.add(i);
            }
        }
        Map<Integer, Integer> result = CollectionUtils.getCardinalityMap(col);
        assertEquals("Cardinality map should have 99 entries (1-99, 0 is not added)", 99, result.size());
        for (int i = 1; i < 100; i++) {
            assertEquals("Element " + i + " should have cardinality " + i,
                    Integer.valueOf(i), result.get(i));
        }
        assertNull("Element 0 should not be in map", result.get(0));
    }

    @Test
    public void testIsEqualCollectionUsesCardinalityMap() {
        // Test that isEqualCollection works correctly by verifying it uses cardinality
        Collection<String> a = new ArrayList<>();
        Collection<String> b = new ArrayList<>();

        // Create collections with same elements but different initial order
        a.add("x");
        a.add("y");
        a.add("x");
        a.add("z");

        b.add("z");
        b.add("x");
        b.add("y");
        b.add("x");

        Map<String, Integer> mapA = CollectionUtils.getCardinalityMap(a);
        Map<String, Integer> mapB = CollectionUtils.getCardinalityMap(b);

        assertEquals("Cardinality maps should be equal", mapA, mapB);
        assertTrue("Collections should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionWithSets() {
        // Test with sets (no duplicates)
        Collection<String> a = new HashSet<>();
        Collection<String> b = new HashSet<>();

        a.add("a");
        a.add("b");
        a.add("c");

        b.add("c");
        b.add("a");
        b.add("b");

        assertTrue("Sets with same elements should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testIsEqualCollectionWithVectors() {
        // Test with Vector (legacy collection type)
        Vector<String> a = new Vector<>();
        Vector<String> b = new Vector<>();

        a.add("a");
        a.add("b");

        b.add("b");
        b.add("a");

        assertTrue("Vectors with same elements should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    // --- Edge Case Tests ---

    @Test
    public void testGetCardinalityMapWithSameObjectReferences() {
        Collection<Object> col = new ArrayList<>();
        Object obj = new Object();
        col.add(obj);
        col.add(obj);
        col.add(new Object()); // Different object with same toString()

        Map<Object, Integer> result = CollectionUtils.getCardinalityMap(col);
        assertEquals("Should have 2 entries", 2, result.size());
        assertEquals("Same reference should have cardinality 2", Integer.valueOf(2), result.get(obj));
    }

    @Test
    public void testIsEqualCollectionWithCustomObjects() {
        class TestObject {
            private final int value;

            TestObject(int value) {
                this.value = value;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                TestObject that = (TestObject) obj;
                return value == that.value;
            }

            @Override
            public int hashCode() {
                return value;
            }
        }

        Collection<TestObject> a = new ArrayList<>();
        Collection<TestObject> b = new ArrayList<>();

        a.add(new TestObject(1));
        a.add(new TestObject(2));
        a.add(new TestObject(1));

        b.add(new TestObject(2));
        b.add(new TestObject(1));
        b.add(new TestObject(1));

        assertTrue("Collections with equal custom objects should be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testGetFreqBehaviorThroughIsEqualCollection() {
        // Test the private getFreq method behavior indirectly
        Collection<String> a = new ArrayList<>();
        Collection<String> b = new ArrayList<>();

        a.add("a");
        a.add("b");

        b.add("a");
        // b doesn't contain "b"

        assertFalse("Collections with different elements should not be equal", CollectionUtils.isEqualCollection(a, b));
    }

    @Test
    public void testNullHandlingInGetFreq() {
        // Test that getFreq handles null keys properly (indirectly through getCardinalityMap)
        Collection<String> col = new ArrayList<>();
        col.add(null);
        col.add(null);
        col.add("test");

        Map<Object, Integer> result = CollectionUtils.getCardinalityMap(col);
        assertEquals("Null should be handled properly", Integer.valueOf(2), result.get(null));
        assertEquals("Non-null should work normally", Integer.valueOf(1), result.get("test"));
    }
}
