package com.badlogic.ashley.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class EngineTest {

    private class ComponentA : Component
    private class ComponentB : Component

    private class SystemMock : EntitySystem() {
        var updateCalls = 0
        var addedCalls = 0
        var removedCalls = 0

        override fun update(deltaTime: Float) {
            updateCalls++
        }

        override fun addedToEngine(engine: Engine) {
            addedCalls++
        }

        override fun removedFromEngine(engine: Engine) {
            removedCalls++
        }
    }

    private class EntityListenerMock : EntityListener {
        var addedCount = 0
        var removedCount = 0

        override fun entityAdded(entity: Entity) {
            addedCount++
        }

        override fun entityRemoved(entity: Entity) {
            removedCount++
        }
    }

    @Test
    fun addAndRemoveEntity() {
        val engine = Engine()
        val entity = Entity()

        engine.addEntity(entity)
        assertEquals(1, engine.entities.size())

        engine.removeEntity(entity)
        assertEquals(0, engine.entities.size())
    }

    @Test
    fun addAndRemoveSystem() {
        val engine = Engine()
        val system = SystemMock()

        engine.addSystem(system)
        assertEquals(1, engine.systems.size())
        assertEquals(system, engine.getSystem(SystemMock::class.java))
        assertEquals(1, system.addedCalls)

        engine.removeSystem(system)
        assertEquals(0, engine.systems.size())
        assertNull(engine.getSystem(SystemMock::class.java))
        assertEquals(1, system.removedCalls)
    }

    @Test
    fun getEntitiesForFamily() {
        val engine = Engine()
        val family = Family.all(ComponentA::class.java).get()
        val entity = Entity()
        entity.add(ComponentA())

        engine.addEntity(entity)

        val entities = engine.getEntitiesFor(family)
        assertEquals(1, entities.size())
        assertEquals(entity, entities[0])
    }

    @Test
    fun entityListener() {
        val engine = Engine()
        val listener = EntityListenerMock()
        engine.addEntityListener(listener)

        val entity = Entity()
        engine.addEntity(entity)
        assertEquals(1, listener.addedCount)

        engine.removeEntity(entity)
        assertEquals(1, listener.removedCount)
    }

    @Test
    fun updateSystems() {
        val engine = Engine()
        val system = SystemMock()
        engine.addSystem(system)

        engine.update(0.1f)
        assertEquals(1, system.updateCalls)
    }

    @Test
    fun createEntity() {
        val engine = Engine()
        val entity = engine.createEntity()
        assertNotNull(entity)
    }

    @Test
    fun removeAllEntities() {
        val engine = Engine()
        val entity1 = Entity()
        val entity2 = Entity()
        engine.addEntity(entity1)
        engine.addEntity(entity2)

        assertEquals(2, engine.entities.size())
        engine.removeAllEntities()
        assertEquals(0, engine.entities.size())
    }

    @Test
    fun removeAllSystems() {
        val engine = Engine()
        val system1 = SystemMock()
        val system2 = SystemMock()
        engine.addSystem(system1)
        engine.addSystem(system2)

        assertEquals(1, engine.systems.size()) // Same class, so it replaces
    }

    @Test
    fun removeAllSystemsDifferentClasses() {
        val engine = Engine()
        val system1 = SystemMock()
        val system2 = object : EntitySystem() {}
        engine.addSystem(system1)
        engine.addSystem(system2)

        assertEquals(2, engine.systems.size())
        engine.removeAllSystems()
        assertEquals(0, engine.systems.size())
    }
}
