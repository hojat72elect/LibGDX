package com.badlogic.ashley.core

import org.junit.Test
import org.mockito.Mockito

class EntityListenerTests {

    @Test
    fun addEntityListenerFamilyRemove() {
        val engine = Engine()

        val e = Entity()
        e.add(PositionComponent())
        engine.addEntity(e)

        val family = Family.all(PositionComponent::class.java).get()
        engine.addEntityListener(family, object : EntityListener {
            override fun entityRemoved(entity: Entity) {
                engine.addEntity(Entity())
            }

            override fun entityAdded(entity: Entity) {}
        })

        engine.removeEntity(e)
    }

    @Test
    fun addEntityListenerFamilyAdd() {

        val engine = Engine()
        val e = Entity()
        e.add(PositionComponent())

        val family = Family.all(PositionComponent::class.java).get()
        engine.addEntityListener(family, object : EntityListener {
            override fun entityRemoved(entity: Entity) {
            }

            override fun entityAdded(entity: Entity) {
                engine.addEntity(Entity())
            }
        })

        engine.addEntity(e)
    }

    @Test
    fun addEntityListenerNoFamilyRemove() {
        val engine = Engine()

        val e = Entity()
        e.add(PositionComponent())
        engine.addEntity(e)

        val family = Family.all(PositionComponent::class.java).get()
        engine.addEntityListener(object : EntityListener {
            override fun entityRemoved(entity: Entity) {
                if (family.matches(entity)) engine.addEntity(Entity())
            }

            override fun entityAdded(entity: Entity) {
            }
        })

        engine.removeEntity(e)
    }

    @Test
    fun addEntityListenerNoFamilyAdd() {
        val engine = Engine()

        val e = Entity()
        e.add(PositionComponent())

        val family = Family.all(PositionComponent::class.java).get()
        engine.addEntityListener(object : EntityListener {
            override fun entityRemoved(entity: Entity) {
            }

            override fun entityAdded(entity: Entity) {
                if (family.matches(entity)) engine.addEntity(Entity())
            }
        })

        engine.addEntity(e)
    }

    @Test
    fun entityListenerPriority() {
        val a = Mockito.mock(EntityListener::class.java)
        val b = Mockito.mock(EntityListener::class.java)
        val c = Mockito.mock(EntityListener::class.java)
        val inOrder = Mockito.inOrder(a, b, c)

        val entity = Entity()
        val engine = Engine()
        engine.addEntityListener(-3, b)
        engine.addEntityListener(c)
        engine.addEntityListener(-4, a)
        inOrder.verifyNoMoreInteractions()

        engine.addEntity(entity)
        inOrder.verify(a).entityAdded(entity)
        inOrder.verify(b).entityAdded(entity)
        inOrder.verify(c).entityAdded(entity)
        inOrder.verifyNoMoreInteractions()

        engine.removeEntity(entity)
        inOrder.verify(a).entityRemoved(entity)
        inOrder.verify(b).entityRemoved(entity)
        inOrder.verify(c).entityRemoved(entity)
        inOrder.verifyNoMoreInteractions()

        engine.removeEntityListener(b)
        inOrder.verifyNoMoreInteractions()

        engine.addEntity(entity)
        inOrder.verify(a).entityAdded(entity)
        inOrder.verify(c).entityAdded(entity)
        inOrder.verifyNoMoreInteractions()

        engine.addEntityListener(4, b)
        inOrder.verifyNoMoreInteractions()

        engine.removeEntity(entity)
        inOrder.verify(a).entityRemoved(entity)
        inOrder.verify(c).entityRemoved(entity)
        inOrder.verify(b).entityRemoved(entity)
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun familyListenerPriority() {
        val a = Mockito.mock(EntityListener::class.java)
        val b = Mockito.mock(EntityListener::class.java)
        val inOrder = Mockito.inOrder(a, b)

        val engine = Engine()
        engine.addEntityListener(Family.all(ComponentB::class.java).get(), -2, b)
        engine.addEntityListener(Family.all(ComponentA::class.java).get(), -3, a)
        inOrder.verifyNoMoreInteractions()

        val entity = Entity()
        entity.add(ComponentA())
        entity.add(ComponentB())

        engine.addEntity(entity)
        inOrder.verify(a).entityAdded(entity)
        inOrder.verify(b).entityAdded(entity)
        inOrder.verifyNoMoreInteractions()

        entity.remove(ComponentB::class.java)
        inOrder.verify(b).entityRemoved(entity)
        inOrder.verifyNoMoreInteractions()

        entity.remove(ComponentA::class.java)
        inOrder.verify(a).entityRemoved(entity)
        inOrder.verifyNoMoreInteractions()

        entity.add(ComponentA())
        inOrder.verify(a).entityAdded(entity)
        inOrder.verifyNoMoreInteractions()

        entity.add(ComponentB())
        inOrder.verify(b).entityAdded(entity)
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun componentHandlingInListeners() {
        val engine = Engine()

        val recorder: ComponentRecorder = Mockito.mock(ComponentRecorder::class.java)

        engine.addEntityListener(object : EntityListener {
            override fun entityAdded(entity: Entity) {
                recorder.addingComponentA()
                entity.add(ComponentA())
            }

            override fun entityRemoved(entity: Entity) {
                recorder.removingComponentA()
                entity.remove(ComponentA::class.java)
            }
        })

        engine.addEntityListener(object : EntityListener {
            override fun entityAdded(entity: Entity) {
                recorder.addingComponentB()
                entity.add(ComponentB())
            }

            override fun entityRemoved(entity: Entity) {
                recorder.removingComponentB()
                entity.remove(ComponentB::class.java)
            }
        })

        engine.update(0f)
        val e = Entity()
        engine.addEntity(e)
        engine.update(0f)
        engine.removeEntity(e)
        engine.update(0f)

        Mockito.verify(recorder).addingComponentA()
        Mockito.verify(recorder).removingComponentA()
        Mockito.verify(recorder).addingComponentB()
        Mockito.verify(recorder).removingComponentB()
    }

    private interface ComponentRecorder {
        fun addingComponentA()
        fun removingComponentA()
        fun addingComponentB()
        fun removingComponentB()
    }

    private class ComponentA : Component
    private class ComponentB : Component
    class PositionComponent : Component
}
