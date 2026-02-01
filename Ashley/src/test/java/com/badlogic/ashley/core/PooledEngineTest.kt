package com.badlogic.ashley.core

import com.badlogic.gdx.utils.Pool.Poolable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Test

class PooledEngineTest {

    private class PooledComponent : Component, Poolable {
        var resetCalled = false

        override fun reset() {
            resetCalled = true
        }
    }

    @Test
    fun entityRemoval() {
        val engine = PooledEngine(1, 100, 1, 100)
        val entity = engine.createEntity()
        engine.addEntity(entity)

        assertEquals(1, engine.entities.size())

        engine.removeEntity(entity)

        assertEquals(0, engine.entities.size())
    }

    @Test
    fun resetEntity() {
        val engine = PooledEngine(1, 100, 1, 100)
        val entity = engine.createEntity()
        engine.addEntity(entity)

        assertFalse(entity.isScheduledForRemoval)
        assertFalse(entity.isRemoving)
        assertEquals(0, entity.flags)

        entity.flags = 10
        engine.removeEntity(entity)

        val newEntity = engine.createEntity()
        assertEquals(0, newEntity.flags)
        assertFalse(newEntity.isScheduledForRemoval)
        assertFalse(newEntity.isRemoving)
    }

    @Test
    fun clearPools() {
        val engine = PooledEngine(1, 100, 1, 100)
        val entity = engine.createEntity()
        engine.addEntity(entity)

        engine.removeEntity(entity)
        engine.clearPools()

        val newEntity = engine.createEntity()
        assertNotSame(entity, newEntity)
    }
}
