package com.crashinvaders.vfx.utils;

import com.badlogic.gdx.utils.Array;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ValueArrayMapTest {

    @Test
    public void testPutAndGet() {
        ValueArrayMap<String, Integer> map = new ValueArrayMap<>();
        map.put("one", 1);
        map.put("two", 2);

        assertEquals(Integer.valueOf(1), map.get("one"));
        assertEquals(Integer.valueOf(2), map.get("two"));
        assertEquals(2, map.size());
    }

    @Test
    public void testGetValueAt() {
        ValueArrayMap<String, Integer> map = new ValueArrayMap<>();
        map.put("one", 1);
        map.put("two", 2);

        // Note: order is determined by insertion order until sorted
        assertEquals(Integer.valueOf(1), map.getValueAt(0));
        assertEquals(Integer.valueOf(2), map.getValueAt(1));
    }

    @Test
    public void testRemove() {
        ValueArrayMap<String, Integer> map = new ValueArrayMap<>();
        map.put("one", 1);
        map.put("two", 2);

        Integer removed = map.remove("one");
        assertEquals(Integer.valueOf(1), removed);
        assertEquals(1, map.size());
        assertNull(map.get("one"));
        assertEquals(Integer.valueOf(2), map.getValueAt(0));
    }

    @Test
    public void testRemoveByValue() {
        ValueArrayMap<String, Integer> map = new ValueArrayMap<>();
        map.put("one", 1);
        map.put("two", 2);

        Integer removed = map.removeByValue(2);
        assertEquals(Integer.valueOf(2), removed);
        assertEquals(1, map.size());
        assertFalse(map.contains("two"));
    }

    @Test
    public void testFindKey() {
        ValueArrayMap<String, Integer> map = new ValueArrayMap<>();
        map.put("one", 1);
        map.put("two", 2);

        assertEquals("one", map.findKey(1));
        assertEquals("two", map.findKey(2));
        assertNull(map.findKey(3));
    }

    @Test
    public void testClear() {
        ValueArrayMap<String, Integer> map = new ValueArrayMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.clear();

        assertEquals(0, map.size());
        assertNull(map.get("one"));
        assertEquals(0, map.getValues().size);
    }

    @Test
    public void testContains() {
        ValueArrayMap<String, Integer> map = new ValueArrayMap<>();
        map.put("one", 1);

        assertTrue(map.contains("one"));
        assertFalse(map.contains("two"));
    }

    @Test
    public void testSort() {
        ValueArrayMap<String, Integer> map = new ValueArrayMap<>();
        map.put("three", 3);
        map.put("one", 1);
        map.put("two", 2);

        map.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        assertEquals(Integer.valueOf(1), map.getValueAt(0));
        assertEquals(Integer.valueOf(2), map.getValueAt(1));
        assertEquals(Integer.valueOf(3), map.getValueAt(2));
    }

    @Test
    public void testGetValues() {
        ValueArrayMap<String, Integer> map = new ValueArrayMap<>();
        map.put("one", 1);
        Array<Integer> values = map.getValues();
        assertEquals(1, values.size);
        assertEquals(Integer.valueOf(1), values.get(0));
    }

    @Test
    public void testGetKeys() {
        ValueArrayMap<String, Integer> map = new ValueArrayMap<>();
        map.put("one", 1);
        map.put("two", 2);

        Array<String> keys = map.getKeys();
        assertEquals(2, keys.size);
        assertTrue(keys.contains("one", false));
        assertTrue(keys.contains("two", false));
    }

    @Test
    public void testToString() {
        ValueArrayMap<String, Integer> map = new ValueArrayMap<>();
        map.put("one", 1);
        assertEquals("[1]", map.toString());
        assertEquals("1", map.toString(","));
    }
}
