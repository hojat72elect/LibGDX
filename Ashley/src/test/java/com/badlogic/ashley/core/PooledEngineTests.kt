package com.badlogic.ashley.core

import com.badlogic.ashley.signals.Listener
import com.badlogic.ashley.signals.Signal
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool.Poolable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotEquals
import org.junit.Test

class PooledEngineTests {
    private val poolableMapper: ComponentMapper<PoolableComponent> = ComponentMapper.getFor<PoolableComponent>(PoolableComponent::class.java)

    @Test
    fun entityRemovalListenerOrder() {
        val engine = PooledEngine()

        val combinedSystem = CombinedSystem()

        engine.addSystem(combinedSystem)
        engine.addEntityListener(Family.all(PositionComponent::class.java).get(), MyPositionListener())

        for (i in 0..9) {
            val entity = engine.createEntity()
            entity.add(engine.createComponent(PositionComponent::class.java))
            engine.addEntity(entity)
        }

        assertEquals(10, combinedSystem.allEntities.size())

        for (i in 0..9) {
            val deltaTime = 0.16f
            engine.update(deltaTime)
        }

        engine.removeAllEntities()
    }

    @Test
    fun resetEntityCorrectly() {
        val engine = PooledEngine()

        val addedListener = ComponentCounterListener()
        val removedListener = ComponentCounterListener()

        // force the engine to create a Family so family bits get set
        val familyEntities = engine.getEntitiesFor(Family.all(PositionComponent::class.java).get())

        val entities = arrayOfNulls<Entity>(10)
        val totalEntities = 10

        for (i in 0..<totalEntities) {
            entities[i] = engine.createEntity()

            entities[i]!!.flags = 5

            entities[i]!!.componentAdded.add(addedListener)
            entities[i]!!.componentRemoved.add(removedListener)

            entities[i]!!.add(engine.createComponent(PositionComponent::class.java))
            engine.addEntity(entities[i])

            assertNotNull(entities[i]!!.componentOperationHandler)
            assertEquals(1, entities[i]!!.components.size())
            assertFalse(entities[i]!!.getFamilyBits().isEmpty())
            assertTrue(familyEntities.contains(entities[i], true))
        }

        assertEquals(totalEntities, addedListener.totalCalls)
        assertEquals(0, removedListener.totalCalls)

        engine.removeAllEntities()

        assertEquals(totalEntities, addedListener.totalCalls)
        assertEquals(totalEntities, removedListener.totalCalls)

        for (i in 0..<totalEntities) {
            assertFalse(entities[i]!!.removing)
            assertEquals(0, entities[i]!!.flags)
            assertNull(entities[i]!!.componentOperationHandler)
            assertEquals(0, entities[i]!!.components.size())
            assertTrue(entities[i]!!.getFamilyBits().isEmpty())
            assertFalse(familyEntities.contains(entities[i], true))

            entities[i]!!.componentAdded.dispatch(entities[i])
            entities[i]!!.componentRemoved.dispatch(entities[i])
        }

        assertEquals(totalEntities, addedListener.totalCalls)
        assertEquals(totalEntities, removedListener.totalCalls)
    }

    @Test
    fun recycleEntity() {
        val numEntities = 5
        val engine = PooledEngine(numEntities, 100, 0, 100)
        val entities = Array<Entity>()

        for (i in 0..<numEntities) {
            val entity = engine.createEntity()
            assertFalse(entity.removing)
            assertEquals(0, entity.flags)
            engine.addEntity(entity)
            entities.add(entity)
            entity.flags = 1
        }

        for (entity in entities) {
            engine.removeEntity(entity)
            assertEquals(0, entity.flags)
            assertFalse(entity.removing)
        }

        for (i in 0..<numEntities) {
            val entity = engine.createEntity()
            assertEquals(0, entity.flags)
            assertFalse(entity.removing)
            assertTrue(entities.contains(entity, true))
        }
    }

    @Test
    fun removeEntityTwice() {
        val engine = PooledEngine()

        for (i in 0..99) {
            val entities = Array<Entity>()

            for (j in 0..99) {
                val entity = engine.createEntity()
                engine.addEntity(entity)
                assertEquals(0, entity.flags)
                entity.flags = 1
                entities.add(entity)
            }

            for (entity in entities) {
                engine.removeEntity(entity)
                engine.removeEntity(entity)
            }
        }
    }

    @Test
    fun recycleComponent() {
        val maxEntities = 10
        val maxComponents = 10
        val engine = PooledEngine(maxEntities, maxEntities, maxComponents, maxComponents)

        for (i in 0..<maxComponents) {
            val e = engine.createEntity()
            val c: PooledComponentSpy = engine.createComponent(PooledComponentSpy::class.java)

            assertFalse(c.recycled)

            e.add(c)

            engine.addEntity(e)
        }

        engine.removeAllEntities()

        for (i in 0..<maxComponents) {
            val e = engine.createEntity()
            val c: PooledComponentSpy = engine.createComponent(PooledComponentSpy::class.java)

            assertTrue(c.recycled)

            e.add(c)
        }

        engine.removeAllEntities()
    }

    @Test
    fun createNewComponent() {
        val engine = PooledEngine()
        val componentA = engine.createComponent(ComponentA::class.java)

        assertNotNull(componentA)
    }

    @Test
    fun addSameComponentShouldResetAndReturnOldComponentToPool() {
        val engine = PooledEngine()

        val component1 = engine.createComponent(PoolableComponent::class.java)
        component1.reset = false
        val component2 = engine.createComponent(PoolableComponent::class.java)
        component2.reset = false

        val entity = engine.createEntity()
        entity.add(component1)
        entity.add(component2)

        assertEquals(1, entity.components.size())
        assertTrue(poolableMapper.has(entity))
        assertNotEquals(component1, poolableMapper.get(entity))
        assertEquals(component2, poolableMapper.get(entity))

        assertTrue(component1.reset)
    }

    @Test
    fun removeComponentReturnsItToThePoolExactlyOnce() {
        val engine = PooledEngine()

        val removedComponent = engine.createComponent(PoolableComponent::class.java)

        val entity = engine.createEntity()
        entity.add(removedComponent)

        entity.remove(PoolableComponent::class.java)

        val newComponent1 = engine.createComponent(PoolableComponent::class.java)
        val newComponent2 = engine.createComponent(PoolableComponent::class.java)

        assertNotEquals(newComponent1, newComponent2)
    }

    class ComponentA : Component

    class PoolableComponent : Component, Poolable {
        var reset = true

        override fun reset() {
            reset = true
        }
    }

    class PositionComponent : Component

    class MyPositionListener : EntityListener {

        override fun entityAdded(entity: Entity) {
        }

        override fun entityRemoved(entity: Entity) {
            val position = positionMapper.get(entity)
            assertNotNull(position)
        }

        companion object {
            var positionMapper: ComponentMapper<PositionComponent> = ComponentMapper.getFor<PositionComponent>(PositionComponent::class.java)
        }
    }

    class CombinedSystem : EntitySystem() {
        lateinit var allEntities: ImmutableArray<Entity>
        private var counter = 0

        override fun addedToEngine(engine: Engine) {
            allEntities = engine.getEntitiesFor(Family.all(PositionComponent::class.java).get())
        }

        override fun update(deltaTime: Float) {
            if (counter in 6..8) {
                engine.removeEntity(allEntities.get(2))
            }
            counter++
        }
    }

    class ComponentCounterListener : Listener<Entity> {
        var totalCalls: Int = 0

        override fun receive(signal: Signal<Entity>, receivedObject: Entity) {
            totalCalls++
        }
    }

    class PooledComponentSpy : Component, Poolable {
        var recycled: Boolean = false


        override fun reset() {
            recycled = true
        }
    }
}
