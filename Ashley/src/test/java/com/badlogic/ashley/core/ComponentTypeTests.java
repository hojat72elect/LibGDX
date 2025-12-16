package com.badlogic.ashley.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComponentTypeTests {

    @Test
    public void validComponentType() {
        assertNotNull(ComponentType.getFor(ComponentA.class));
        assertNotNull(ComponentType.getFor(ComponentB.class));
    }

    @Test
    public void sameComponentType() {
        ComponentType componentType1 = ComponentType.getFor(ComponentA.class);
        ComponentType componentType2 = ComponentType.getFor(ComponentA.class);

        assertTrue(componentType1.equals(componentType2));
        assertTrue(componentType2.equals(componentType1));
        assertEquals(componentType1.getIndex(), componentType2.getIndex());
        assertEquals(componentType1.getIndex(), ComponentType.getIndexFor(ComponentA.class));
        assertEquals(componentType2.getIndex(), ComponentType.getIndexFor(ComponentA.class));
    }

    @Test
    public void differentComponentType() {
        ComponentType componentType1 = ComponentType.getFor(ComponentA.class);
        ComponentType componentType2 = ComponentType.getFor(ComponentB.class);

        assertFalse(componentType1.equals(componentType2));
        assertFalse(componentType2.equals(componentType1));
        assertNotEquals(componentType1.getIndex(), componentType2.getIndex());
        assertNotEquals(componentType1.getIndex(), ComponentType.getIndexFor(ComponentB.class));
        assertNotEquals(componentType2.getIndex(), ComponentType.getIndexFor(ComponentA.class));
    }

    private static class ComponentA implements Component {

    }

    private static class ComponentB implements Component {

    }
}
