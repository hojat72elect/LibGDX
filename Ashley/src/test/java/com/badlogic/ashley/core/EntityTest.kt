package com.badlogic.ashley.core

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import com.badlogic.gdx.utils.Array
import org.junit.Assert.*
import org.junit.Test

class EntityTest {
    private class ComponentA : Component
    private class ComponentB : Component

    @Test
    fun addAndReturnComponent() {
        val entity = Entity()
        val componentA = ComponentA()
        val componentB = ComponentB()

        assertEquals(componentA, entity.addAndReturn(componentA))
        assertEquals(componentB, entity.addAndReturn(componentB))

        assertEquals(2, entity.components.size())
        assertTrue(entity.components.contains(componentA, true))
        assertTrue(entity.components.contains(componentB, true))
    }

    @Test
    fun addComponent() {
        val entity = Entity()
        val componentA = ComponentA()
        val componentB = ComponentB()

        entity.add(componentA)
        entity.add(componentB)

        assertEquals(2, entity.components.size())
        assertTrue(entity.components.contains(componentA, true))
        assertTrue(entity.components.contains(componentB, true))
    }

    @Test
    fun getComponent() {
        val entity = Entity()
        val componentA = ComponentA()
        val componentB = ComponentB()

        entity.add(componentA)
        entity.add(componentB)

        assertEquals(componentA, entity.getComponent(ComponentA::class.java))
        assertEquals(componentB, entity.getComponent(ComponentB::class.java))
    }

    @Test
    fun hasComponent() {
        val entity = Entity()
        val componentA = ComponentA()
        val componentB = ComponentB()

        entity.add(componentA)
        entity.add(componentB)

        assertTrue(entity.hasComponent(ComponentType.getFor(ComponentA::class.java)))
        assertTrue(entity.hasComponent(ComponentType.getFor(ComponentB::class.java)))
    }

    @Test
    fun removeComponent() {
        val entity = Entity()
        val componentA = ComponentA()
        val componentB = ComponentB()

        entity.add(componentA)
        entity.add(componentB)

        assertEquals(componentA, entity.remove(ComponentA::class.java))
        assertEquals(1, entity.components.size())
        assertFalse(entity.components.contains(componentA, true))
        assertTrue(entity.components.contains(componentB, true))

        assertEquals(componentB, entity.remove(ComponentB::class.java))
        assertEquals(0, entity.components.size())
        assertFalse(entity.components.contains(componentA, true))
        assertFalse(entity.components.contains(componentB, true))
    }

    @Test
    fun removeAllComponents() {
        val entity = Entity()
        val componentA = ComponentA()
        val componentB = ComponentB()

        entity.add(componentA)
        entity.add(componentB)

        entity.removeAll()

        assertEquals(0, entity.components.size())
        assertFalse(entity.components.contains(componentA, true))
        assertFalse(entity.components.contains(componentB, true))
    }

    @Test
    fun addSameComponent() {
        val entity = Entity()
        val componentA1 = ComponentA()
        val componentA2 = ComponentA()

        entity.add(componentA1)
        entity.add(componentA2)

        assertEquals(1, entity.components.size())
        assertTrue(entity.components.contains(componentA2, true))
        assertFalse(entity.components.contains(componentA1, true))
    }

    @Test
    fun componentListener() {
        val entity = Entity()
        val componentA = ComponentA()

        var added = false
        var removed = false

        val listener = object : Listener<Entity> {
            override fun receive(signal: Signal<Entity>, `object`: Entity) {
                if (signal === entity.componentAdded) {
                    added = true
                } else if (signal === entity.componentRemoved) {
                    removed = true
                }
            }
        }

        entity.componentAdded.add(listener)
        entity.componentRemoved.add(listener)

        entity.add(componentA)
        assertTrue(added)
        assertFalse(removed)

        added = false
        entity.remove(ComponentA::class.java)
        assertFalse(added)
        assertTrue(removed)
    }

    @Test
    fun getComponentBits() {
        val entity = Entity()
        val componentA = ComponentA()
        val componentB = ComponentB()

        entity.add(componentA)
        entity.add(componentB)

        val bits = entity.componentBits
        val typeA = ComponentType.getFor(ComponentA::class.java)
        val typeB = ComponentType.getFor(ComponentB::class.java)

        assertTrue(bits.get(typeA.index))
        assertTrue(bits.get(typeB.index))
    }
}
