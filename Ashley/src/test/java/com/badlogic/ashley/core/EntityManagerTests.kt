package com.badlogic.ashley.core

import com.badlogic.gdx.utils.Array
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotEquals
import org.junit.Test

class EntityManagerTests {

    @Test
    fun addAndRemoveEntity() {
        val listener = EntityListenerMock()
        val manager = EntityManager(listener)

        val entity1 = Entity()
        manager.addEntity(entity1)

        assertEquals(1, listener.addedCount)
        val entity2 = Entity()
        manager.addEntity(entity2)

        assertEquals(2, listener.addedCount)

        manager.removeAllEntities()

        assertEquals(2, listener.removedCount)
    }

    @Test
    fun getEntities() {
        val numEntities = 10

        val listener = EntityListenerMock()
        val manager = EntityManager(listener)

        val entities = Array<Entity>()

        for (i in 0..<numEntities) {
            val entity = Entity()
            entities.add(entity)
            manager.addEntity(entity)
        }

        val engineEntities = manager.getEntities()

        assertEquals(entities.size, engineEntities.size())

        for (i in 0..<numEntities) {
            assertEquals(entities.get(i), engineEntities.get(i))
        }

        manager.removeAllEntities()

        assertEquals(0, engineEntities.size())
    }

    @Test(expected = IllegalArgumentException::class)
    fun addEntityTwice1() {
        val listener = EntityListenerMock()
        val manager = EntityManager(listener)
        val entity = Entity()
        manager.addEntity(entity)
        manager.addEntity(entity)
    }

    @Test(expected = IllegalArgumentException::class)
    fun addEntityTwice2() {
        val listener = EntityListenerMock()
        val manager = EntityManager(listener)
        val entity = Entity()
        manager.addEntity(entity, false)
        manager.addEntity(entity, false)
    }

    @Test(expected = IllegalArgumentException::class)
    fun addEntityTwiceDelayed() {
        val listener = EntityListenerMock()
        val manager = EntityManager(listener)

        val entity = Entity()
        manager.addEntity(entity, true)
        manager.addEntity(entity, true)
        manager.processPendingOperations()
    }

    @Test
    fun delayedOperationsOrder() {
        val listener = EntityListenerMock()
        val manager = EntityManager(listener)

        val entityA = Entity()
        val entityB = Entity()

        val delayed = true
        manager.addEntity(entityA)
        manager.addEntity(entityB)

        assertEquals(2, manager.getEntities().size())

        val entityC = Entity()
        val entityD = Entity()
        manager.removeAllEntities(delayed)
        manager.addEntity(entityC, delayed)
        manager.addEntity(entityD, delayed)
        manager.processPendingOperations()

        assertEquals(2, manager.getEntities().size())
        assertNotEquals(-1, manager.getEntities().indexOf(entityC, true))
        assertNotEquals(-1, manager.getEntities().indexOf(entityD, true))
    }

    @Test
    fun removeAndAddEntityDelayed() {
        val listener = EntityListenerMock()
        val manager = EntityManager(listener)

        val entity = Entity()
        manager.addEntity(entity, false) // immediate
        assertEquals(1, manager.getEntities().size())

        manager.removeEntity(entity, true) // delayed
        assertEquals(1, manager.getEntities().size())

        manager.addEntity(entity, true) // delayed
        assertEquals(1, manager.getEntities().size())

        manager.processPendingOperations()
        assertEquals(1, manager.getEntities().size())
    }

    @Test
    fun removeAllAndAddEntityDelayed() {
        val listener = EntityListenerMock()
        val manager = EntityManager(listener)

        val entity = Entity()
        manager.addEntity(entity, false) // immediate
        assertEquals(1, manager.getEntities().size())

        manager.removeAllEntities(true) // delayed
        assertEquals(1, manager.getEntities().size())

        manager.addEntity(entity, true) // delayed
        assertEquals(1, manager.getEntities().size())

        manager.processPendingOperations()
        assertEquals(1, manager.getEntities().size())
    }

    private class EntityListenerMock : EntityListener {
        var addedCount = 0
        var removedCount = 0

        override fun entityAdded(entity: Entity) {
            addedCount++
            assertNotNull(entity)
        }

        override fun entityRemoved(entity: Entity) {
            ++removedCount
            assertNotNull(entity)
        }
    }
}
