package com.badlogic.ashley.core

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ComponentTypeTests {
    @Test
    fun validComponentType() {
        assertNotNull(ComponentType.getFor(ComponentA::class.java))
        assertNotNull(ComponentType.getFor(ComponentB::class.java))
    }

    @Test
    fun sameComponentType() {
        val componentType1 = ComponentType.getFor(ComponentA::class.java)
        val componentType2 = ComponentType.getFor(ComponentA::class.java)

        assertEquals(componentType1, componentType2)
        assertEquals(componentType2, componentType1)
        assertEquals(componentType1.index, componentType2.index)
        assertEquals(componentType1.index, ComponentType.getIndexFor(ComponentA::class.java))
        assertEquals(componentType2.index, ComponentType.getIndexFor(ComponentA::class.java))
    }

    @Test
    fun differentComponentType() {
        val componentType1 = ComponentType.getFor(ComponentA::class.java)
        val componentType2 = ComponentType.getFor(ComponentB::class.java)

        assertNotEquals(componentType1, componentType2)
        assertNotEquals(componentType2, componentType1)
        assertNotEquals(componentType1.index, componentType2.index)
        assertNotEquals(componentType1.index, ComponentType.getIndexFor(ComponentB::class.java))
        assertNotEquals(componentType2.index, ComponentType.getIndexFor(ComponentA::class.java))
    }

    private class ComponentA : Component
    private class ComponentB : Component
}
