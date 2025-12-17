package com.badlogic.ashley.core

import com.badlogic.ashley.core.ComponentOperationHandler.BooleanInformer
import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class ComponentOperationHandlerTests {

    @Test
    fun add() {
        val spy = ComponentSpy()
        val informer = BooleanInformerMock()
        val handler = ComponentOperationHandler(informer)

        val entity = Entity()
        entity.componentOperationHandler = handler
        entity.componentAdded.add(spy)

        handler.add(entity)

        assertTrue(spy.called)
    }

    @Test
    fun addDelayed() {
        val spy = ComponentSpy()
        val informer = BooleanInformerMock()
        val handler = ComponentOperationHandler(informer)

        informer.delayed = true

        val entity = Entity()
        entity.componentOperationHandler = handler
        entity.componentAdded.add(spy)

        handler.add(entity)

        assertFalse(spy.called)
        handler.processOperations()
        assertTrue(spy.called)
    }

    @Test
    fun remove() {
        val spy = ComponentSpy()
        val informer = BooleanInformerMock()
        val handler = ComponentOperationHandler(informer)

        val entity = Entity()
        entity.componentOperationHandler = handler
        entity.componentRemoved.add(spy)

        handler.remove(entity)

        assertTrue(spy.called)
    }

    @Test
    fun removeDelayed() {
        val spy = ComponentSpy()
        val informer = BooleanInformerMock()
        val handler = ComponentOperationHandler(informer)

        informer.delayed = true

        val entity = Entity()
        entity.componentOperationHandler = handler
        entity.componentRemoved.add(spy)

        handler.remove(entity)

        assertFalse(spy.called)
        handler.processOperations()
        assertTrue(spy.called)
    }

    private class BooleanInformerMock : BooleanInformer {
        var delayed = false
        override fun value() = delayed
    }

    private class ComponentSpy : Listener<Entity> {
        var called = false

        override fun receive(signal: Signal<Entity>, receivedEntity: Entity) {
            called = true
        }
    }
}
