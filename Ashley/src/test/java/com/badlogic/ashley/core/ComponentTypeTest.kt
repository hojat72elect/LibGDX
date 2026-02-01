package com.badlogic.ashley.core

import org.junit.Assert.*
import org.junit.Test

class ComponentTypeTest {
    private class ComponentA : Component
    private class ComponentB : Component

    @Test
    fun validComponentType() {
        assertNotNull(ComponentType.getFor(ComponentA::class.java))
        assertNotNull(ComponentType.getFor(ComponentB::class.java))
    }

    @Test
    fun sameComponentType() {
        val type1 = ComponentType.getFor(ComponentA::class.java)
        val type2 = ComponentType.getFor(ComponentA::class.java)

        assertEquals(type1, type2)
        assertEquals(type1.index, type2.index)
    }

    @Test
    fun differentComponentType() {
        val type1 = ComponentType.getFor(ComponentA::class.java)
        val type2 = ComponentType.getFor(ComponentB::class.java)

        assertNotEquals(type1, type2)
        assertNotEquals(type1.index, type2.index)
    }

    @Test
    fun componentTypeIndex() {
        val type1 = ComponentType.getFor(ComponentA::class.java)
        val type2 = ComponentType.getFor(ComponentB::class.java)

        assertEquals(type1.index, ComponentType.getIndexFor(ComponentA::class.java))
        assertEquals(type2.index, ComponentType.getIndexFor(ComponentB::class.java))
    }

    @Test
    fun componentTypeBits() {
        val type1 = ComponentType.getFor(ComponentA::class.java)
        val type2 = ComponentType.getFor(ComponentB::class.java)

        val bits = ComponentType.getBitsFor(ComponentA::class.java, ComponentB::class.java)

        assertTrue(bits.get(type1.index))
        assertTrue(bits.get(type2.index))
    }

    @Test
    fun componentTypeEquals() {
        val type1 = ComponentType.getFor(ComponentA::class.java)
        val type2 = ComponentType.getFor(ComponentB::class.java)

        assertEquals(type1, type1)
        assertEquals(type2, type2)
        assertNotEquals(type1, type2)
    }
}
