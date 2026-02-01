package com.badlogic.ashley.core

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import org.junit.Assert.*
import org.junit.Test

class ComponentOperationHandlerTest {

    private class TestComponent : Component

    @Test
    fun add() {
        val delayed = TestBooleanInformer(true)
        val handler = ComponentOperationHandler(delayed)
        val entity = Entity()
        entity.componentOperationHandler = handler

        val addListener = TestListener()
        entity.componentAdded.add(addListener)
        val removeListener = TestListener()
        entity.componentRemoved.add(removeListener)

        entity.add(TestComponent())

        assertEquals(0, addListener.count)
        assertEquals(0, removeListener.count)
        assertTrue(handler.hasOperationsToProcess())

        handler.processOperations()

        assertEquals(1, addListener.count)
        assertEquals(0, removeListener.count)
        assertFalse(handler.hasOperationsToProcess())
    }

    @Test
    fun remove() {
        val delayed = TestBooleanInformer(true)
        val handler = ComponentOperationHandler(delayed)
        val entity = Entity()
        entity.componentOperationHandler = handler
        entity.add(TestComponent())
        // Clear any pending operations from setup
        handler.processOperations()

        val addListener = TestListener()
        entity.componentAdded.add(addListener)
        val removeListener = TestListener()
        entity.componentRemoved.add(removeListener)

        entity.remove(TestComponent::class.java)

        assertEquals(0, addListener.count)
        assertEquals(0, removeListener.count)
        assertTrue(handler.hasOperationsToProcess())

        handler.processOperations()

        assertEquals(0, addListener.count)
        assertEquals(1, removeListener.count)
        assertFalse(handler.hasOperationsToProcess())
    }

    @Test
    fun addNotDelayed() {
        val delayed = TestBooleanInformer(false)
        val handler = ComponentOperationHandler(delayed)
        val entity = Entity()
        entity.componentOperationHandler = handler

        val listener = TestListener()
        entity.componentAdded.add(listener)

        entity.add(TestComponent())

        assertEquals(1, listener.count)
        assertFalse(handler.hasOperationsToProcess())
    }

    @Test
    fun removeNotDelayed() {
        val delayed = TestBooleanInformer(false)
        val handler = ComponentOperationHandler(delayed)
        val entity = Entity()
        entity.componentOperationHandler = handler
        entity.add(TestComponent())

        val listener = TestListener()
        entity.componentRemoved.add(listener)

        entity.remove(TestComponent::class.java)

        assertEquals(1, listener.count)
        assertFalse(handler.hasOperationsToProcess())
    }

    @Test
    fun multipleOperations() {
        val delayed = TestBooleanInformer(true)
        val handler = ComponentOperationHandler(delayed)
        val entity = Entity()
        entity.componentOperationHandler = handler

        val addListener = TestListener()
        entity.componentAdded.add(addListener)
        val removeListener = TestListener()
        entity.componentRemoved.add(removeListener)

        entity.add(TestComponent())
        entity.remove(TestComponent::class.java)

        assertEquals(0, addListener.count)
        assertEquals(0, removeListener.count)
        assertTrue(handler.hasOperationsToProcess())

        handler.processOperations()

        assertEquals(1, addListener.count)
        assertEquals(1, removeListener.count)
        assertFalse(handler.hasOperationsToProcess())
    }

    private class TestBooleanInformer(private val value: Boolean) : ComponentOperationHandler.BooleanInformer {
        override fun value(): Boolean {
            return value
        }
    }

    private class TestListener : Listener<Entity> {
        var count = 0

        override fun receive(signal: Signal<Entity>, entity: Entity) {
            count++
        }
    }
}
