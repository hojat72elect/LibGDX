package com.badlogic.ashley.core

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

class EntityManagerTest {

    @Test
    fun addEntity() {
        val listener = mock(EntityListener::class.java)
        val manager = EntityManager(listener)
        val entity = Entity()

        manager.addEntity(entity)

        assertEquals(1, manager.entities.size())
        assertEquals(entity, manager.entities.get(0))
        verify(listener).entityAdded(entity)
    }

    @Test
    fun addEntityDelayed() {
        val listener = mock(EntityListener::class.java)
        val manager = EntityManager(listener)
        val entity = Entity()

        manager.addEntity(entity, true)

        assertEquals(0, manager.entities.size())
        assertTrue(manager.hasPendingOperations())
        verifyNoInteractions(listener)

        manager.processPendingOperations()

        assertEquals(1, manager.entities.size())
        assertEquals(entity, manager.entities.get(0))
        assertFalse(manager.hasPendingOperations())
        verify(listener).entityAdded(entity)
    }

    @Test
    fun removeEntity() {
        val listener = mock(EntityListener::class.java)
        val manager = EntityManager(listener)
        val entity = Entity()

        manager.addEntity(entity)
        manager.removeEntity(entity)

        assertEquals(0, manager.entities.size())
        verify(listener).entityRemoved(entity)
    }

    @Test
    fun removeEntityDelayed() {
        val listener = mock(EntityListener::class.java)
        val manager = EntityManager(listener)
        val entity = Entity()

        manager.addEntity(entity)
        manager.removeEntity(entity, true)

        assertEquals(1, manager.entities.size())
        assertTrue(manager.hasPendingOperations())
        verify(listener, never()).entityRemoved(entity)

        manager.processPendingOperations()

        assertEquals(0, manager.entities.size())
        assertFalse(manager.hasPendingOperations())
        verify(listener).entityRemoved(entity)
    }

    @Test
    fun removeAllEntities() {
        val listener = mock(EntityListener::class.java)
        val manager = EntityManager(listener)
        val entity1 = Entity()
        val entity2 = Entity()

        manager.addEntity(entity1)
        manager.addEntity(entity2)

        manager.removeAllEntities()

        assertEquals(0, manager.entities.size())
        verify(listener).entityRemoved(entity1)
        verify(listener).entityRemoved(entity2)
    }

    @Test
    fun removeAllEntitiesDelayed() {
        val listener = mock(EntityListener::class.java)
        val manager = EntityManager(listener)
        val entity1 = Entity()
        val entity2 = Entity()

        manager.addEntity(entity1)
        manager.addEntity(entity2)

        manager.removeAllEntities(true)

        assertEquals(2, manager.entities.size())
        assertTrue(manager.hasPendingOperations())
        verify(listener, never()).entityRemoved(entity1)
        verify(listener, never()).entityRemoved(entity2)

        manager.processPendingOperations()

        assertEquals(0, manager.entities.size())
        assertFalse(manager.hasPendingOperations())
        verify(listener).entityRemoved(entity1)
        verify(listener).entityRemoved(entity2)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun addDuplicateEntity() {
        val listener = mock(EntityListener::class.java)
        val manager = EntityManager(listener)
        val entity = Entity()

        manager.addEntity(entity)
        manager.addEntity(entity)
    }

    @Test
    fun getEntities() {
        val listener = mock(EntityListener::class.java)
        val manager = EntityManager(listener)
        val entity1 = Entity()
        val entity2 = Entity()

        manager.addEntity(entity1)
        manager.addEntity(entity2)

        val entities = manager.entities
        assertEquals(2, entities.size())
        assertTrue(entities.contains(entity1, true))
        assertTrue(entities.contains(entity2, true))
    }
}
