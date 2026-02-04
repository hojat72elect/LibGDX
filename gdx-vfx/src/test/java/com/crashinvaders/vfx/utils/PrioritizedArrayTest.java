package com.crashinvaders.vfx.utils;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PrioritizedArrayTest {

    @Test
    public void testAddAndGet() {
        PrioritizedArray<String> array = new PrioritizedArray<>();
        array.add("second", 10);
        array.add("first", 5);
        array.add("third", 15);

        assertEquals(3, array.size());
        assertEquals("first", array.get(0));
        assertEquals("second", array.get(1));
        assertEquals("third", array.get(2));
    }

    @Test
    public void testDefaultPriority() {
        PrioritizedArray<String> array = new PrioritizedArray<>();
        array.add("p10", 10);
        array.add("p0"); // default is 0

        assertEquals("p0", array.get(0));
        assertEquals("p10", array.get(1));
    }

    @Test
    public void testRemoveByIndex() {
        PrioritizedArray<String> array = new PrioritizedArray<>();
        array.add("a", 1);
        array.add("b", 2);

        array.remove(0);
        assertEquals(1, array.size());
        assertEquals("b", array.get(0));
    }

    @Test
    public void testRemoveByItem() {
        PrioritizedArray<String> array = new PrioritizedArray<>();
        array.add("a", 1);
        array.add("b", 2);

        array.remove("a");
        assertEquals(1, array.size());
        assertEquals("b", array.get(0));
    }

    @Test
    public void testContains() {
        PrioritizedArray<String> array = new PrioritizedArray<>();
        array.add("a");

        assertTrue(array.contains("a"));
        assertFalse(array.contains("b"));
    }

    @Test
    public void testClear() {
        PrioritizedArray<String> array = new PrioritizedArray<>();
        array.add("a");
        array.clear();

        assertEquals(0, array.size());
        assertFalse(array.contains("a"));
    }

    @Test
    public void testSetPriority() {
        PrioritizedArray<String> array = new PrioritizedArray<>();
        array.add("a", 10);
        array.add("b", 5);

        assertEquals("b", array.get(0));

        array.setPriority("b", 15);
        assertEquals("a", array.get(0));
        assertEquals("b", array.get(1));
    }

    @Test
    public void testIterator() {
        PrioritizedArray<String> array = new PrioritizedArray<>();
        array.add("a", 1);
        array.add("b", 2);

        int count = 0;
        for (String s : array) {
            if (count == 0)
                assertEquals("a", s);
            if (count == 1)
                assertEquals("b", s);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testIteratorRemove() {
        PrioritizedArray<String> array = new PrioritizedArray<>();
        array.add("a", 1);
        array.add("b", 2);

        Iterator<String> iterator = array.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.equals("a")) {
                iterator.remove();
            }
        }

        assertEquals(1, array.size());
        assertEquals("b", array.get(0));
    }

    @Test
    public void testToString() {
        PrioritizedArray<String> array = new PrioritizedArray<>();
        array.add("a", 1);
        // Wrapper.toString() returns item + "[" + priority + "]"
        assertEquals("[a[1]]", array.toString());
    }
}
