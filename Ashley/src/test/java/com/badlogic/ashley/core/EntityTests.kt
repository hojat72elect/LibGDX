package com.badlogic.ashley.core

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class EntityTests {
    private val am: ComponentMapper<ComponentA> = ComponentMapper.getFor<ComponentA>(ComponentA::class.java)
    private val bm: ComponentMapper<ComponentB> = ComponentMapper.getFor<ComponentB>(ComponentB::class.java)

    @Test
    fun addAndReturnComponent() {
        val entity = Entity()
        val componentA = ComponentA()
        val componentB = ComponentB()

        assertEquals(componentA, entity.addAndReturn(componentA))
        assertEquals(componentB, entity.addAndReturn(componentB))

        assertEquals(2, entity.components.size())
    }

    @Test
    fun noComponents() {
        val entity = Entity()

        assertEquals(0, entity.components.size())
        assertTrue(entity.getComponentBits().isEmpty())
        assertNull(am.get(entity))
        assertNull(bm.get(entity))
        assertFalse(am.has(entity))
        assertFalse(bm.has(entity))
    }

    @Test
    fun addAndRemoveComponent() {
        val entity = Entity()

        entity.add(ComponentA())

        assertEquals(1, entity.components.size())

        val componentBits = entity.getComponentBits()
        val componentAIndex = ComponentType.getIndexFor(ComponentA::class.java)

        for (i in 0..<componentBits.length()) {
            assertEquals(i == componentAIndex, componentBits.get(i))
        }

        assertNotNull(am.get(entity))
        assertNull(bm.get(entity))
        assertTrue(am.has(entity))
        assertFalse(bm.has(entity))

        entity.remove(ComponentA::class.java)

        assertEquals(0, entity.components.size())

        for (i in 0..<componentBits.length()) {
            assertFalse(componentBits.get(i))
        }

        assertNull(am.get(entity))
        assertNull(bm.get(entity))
        assertFalse(am.has(entity))
        assertFalse(bm.has(entity))
    }

    @Test
    @Throws(Exception::class)
    fun removeUnexistingComponent() {
        // ensure remove unexisting component work with
        // new component type at default bag limits (64)
        val entity = Entity()

        val cl = ComponentClassFactory()

        for (i in 0..64) {
            val type = cl.createComponentType("Component$i")
            entity.remove(type)
            entity.add(type.newInstance())
        }
    }

    @Test
    fun addAndRemoveAllComponents() {
        val entity = Entity()

        entity.add(ComponentA())
        entity.add(ComponentB())

        assertEquals(2, entity.components.size())

        val componentBits = entity.getComponentBits()
        val componentAIndex = ComponentType.getIndexFor(ComponentA::class.java)
        val componentBIndex = ComponentType.getIndexFor(ComponentB::class.java)

        for (i in 0..<componentBits.length()) {
            assertEquals(i == componentAIndex || i == componentBIndex, componentBits.get(i))
        }

        assertNotNull(am.get(entity))
        assertNotNull(bm.get(entity))
        assertTrue(am.has(entity))
        assertTrue(bm.has(entity))

        entity.removeAll()

        assertEquals(0, entity.components.size())

        for (i in 0..<componentBits.length()) {
            assertFalse(componentBits.get(i))
        }

        assertNull(am.get(entity))
        assertNull(bm.get(entity))
        assertFalse(am.has(entity))
        assertFalse(bm.has(entity))
    }

    @Test
    fun addSameComponent() {
        val entity = Entity()

        val a1 = ComponentA()
        val a2 = ComponentA()

        entity.add(a1)
        entity.add(a2)

        assertEquals(1, entity.components.size())
        assertTrue(am.has(entity))
        assertNotEquals(a1, am.get(entity))
        assertEquals(a2, am.get(entity))
    }

    @Test
    fun componentListener() {
        val addedListener = EntityListenerMock()
        val removedListener = EntityListenerMock()

        val entity = Entity()
        entity.componentAdded.add(addedListener)
        entity.componentRemoved.add(removedListener)

        assertEquals(0, addedListener.counter)
        assertEquals(0, removedListener.counter)

        entity.add(ComponentA())

        assertEquals(1, addedListener.counter)
        assertEquals(0, removedListener.counter)

        entity.remove(ComponentA::class.java)

        assertEquals(1, addedListener.counter)
        assertEquals(1, removedListener.counter)

        entity.add(ComponentB())
        assertEquals(2, addedListener.counter)
        entity.remove(ComponentB::class.java)
        assertEquals(2, removedListener.counter)
    }

    @Test
    fun getComponentByClass() {
        val compA = ComponentA()
        val compB = ComponentB()

        val entity = Entity()
        entity.add(compA).add(compB)

        val retA = entity.getComponent(ComponentA::class.java)
        val retB = entity.getComponent(ComponentB::class.java)

        assertNotNull(retA)
        assertNotNull(retB)

        assertSame(retA, compA)
        assertSame(retB, compB)
    }

    private class ComponentA : Component
    private class ComponentB : Component

    private class EntityListenerMock : Listener<Entity> {
        var counter = 0

        override fun receive(signal: Signal<Entity>, receivedObject: Entity) {
            counter++

            assertNotNull(signal)
            assertNotNull(receivedObject)
        }
    }
}
