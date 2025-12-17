package com.badlogic.ashley.core

import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.GdxRuntimeException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EngineTests {
    private val deltaTime = 0.16F

    @Test
    fun addAndRemoveEntity() {
        val engine = Engine()

        val listenerA = EntityListenerMock()
        val listenerB = EntityListenerMock()

        engine.addEntityListener(listenerA)
        engine.addEntityListener(listenerB)

        val entity1 = Entity()
        engine.addEntity(entity1)

        assertEquals(1, listenerA.addedCount)
        assertEquals(1, listenerB.addedCount)

        engine.removeEntityListener(listenerB)

        val entity2 = Entity()
        engine.addEntity(entity2)

        assertEquals(2, listenerA.addedCount)
        assertEquals(1, listenerB.addedCount)

        engine.addEntityListener(listenerB)

        engine.removeAllEntities()

        assertEquals(2, listenerA.removedCount)
        assertEquals(2, listenerB.removedCount)
    }

    @Test
    fun addComponentInsideListener() {
        val engine = Engine()

        val listenerA: EntityListenerMock = AddComponentBEntityListenerMock()
        val listenerB = EntityListenerMock()

        engine.addEntityListener(Family.all(ComponentA::class.java).get(), listenerA)
        engine.addEntityListener(Family.all(ComponentB::class.java).get(), listenerB)

        val entity1 = Entity()
        entity1.add(ComponentA())
        engine.addEntity(entity1)

        assertEquals(1, listenerA.addedCount)
        assertNotNull(entity1.getComponent(ComponentB::class.java))
        assertEquals(1, listenerB.addedCount)
    }

    @Test
    fun addAndRemoveSystem() {
        val engine = Engine()
        val systemA = EntitySystemMockA()
        val systemB = EntitySystemMockB()

        assertNull(engine.getSystem(EntitySystemMockA::class.java))
        assertNull(engine.getSystem(EntitySystemMockB::class.java))

        engine.addSystem(systemA)
        engine.addSystem(systemB)

        assertNotNull(engine.getSystem(EntitySystemMockA::class.java))
        assertNotNull(engine.getSystem(EntitySystemMockB::class.java))
        assertEquals(1, systemA.addedCalls)
        assertEquals(1, systemB.addedCalls)

        engine.removeSystem(systemA)
        engine.removeSystem(systemB)

        assertNull(engine.getSystem(EntitySystemMockA::class.java))
        assertNull(engine.getSystem(EntitySystemMockB::class.java))
        assertEquals(1, systemA.removedCalls)
        assertEquals(1, systemB.removedCalls)

        engine.addSystem(systemA)
        engine.addSystem(systemB)
        engine.removeAllSystems()

        assertNull(engine.getSystem(EntitySystemMockA::class.java))
        assertNull(engine.getSystem(EntitySystemMockB::class.java))
        assertEquals(2, systemA.removedCalls)
        assertEquals(2, systemB.removedCalls)
    }

    @Test
    fun getSystems() {
        val engine = Engine()
        val systemA = EntitySystemMockA()
        val systemB = EntitySystemMockB()

        assertEquals(0, engine.systems.size())

        engine.addSystem(systemA)
        engine.addSystem(systemB)

        assertEquals(2, engine.systems.size())
    }

    @Test
    fun addTwoSystemsOfSameClass() {
        val engine = Engine()
        val system1 = EntitySystemMockA()
        val system2 = EntitySystemMockA()

        assertEquals(0, engine.systems.size())

        engine.addSystem(system1)

        assertEquals(1, engine.systems.size())
        assertEquals(system1, engine.getSystem(EntitySystemMockA::class.java))

        engine.addSystem(system2)

        assertEquals(1, engine.systems.size())
        assertEquals(system2, engine.getSystem(EntitySystemMockA::class.java))
    }

    @Test
    fun systemUpdate() {
        val engine = Engine()
        val systemA: EntitySystemMock = EntitySystemMockA()
        val systemB: EntitySystemMock = EntitySystemMockB()

        engine.addSystem(systemA)
        engine.addSystem(systemB)

        val numUpdates = 10

        for (i in 0..<numUpdates) {
            assertEquals(i, systemA.updateCalls)
            assertEquals(i, systemB.updateCalls)

            engine.update(deltaTime)

            assertEquals((i + 1), systemA.updateCalls)
            assertEquals((i + 1), systemB.updateCalls)
        }

        engine.removeSystem(systemB)

        for (i in 0..<numUpdates) {
            assertEquals((i + numUpdates), systemA.updateCalls)
            assertEquals(numUpdates, systemB.updateCalls)

            engine.update(deltaTime)

            assertEquals((i + 1 + numUpdates), systemA.updateCalls)
            assertEquals(numUpdates, systemB.updateCalls)
        }
    }

    @Test
    fun systemUpdateOrder() {
        val updates = Array<Int>()

        val engine = Engine()
        val system1: EntitySystemMock = EntitySystemMockA(updates)
        val system2: EntitySystemMock = EntitySystemMockB(updates)

        system1.priority = 2
        system2.priority = 1

        engine.addSystem(system1)
        engine.addSystem(system2)

        engine.update(deltaTime)

        var previous = Int.MIN_VALUE

        for (value in updates) {
            assertTrue(value >= previous)
            previous = value
        }
    }

    @Test
    fun entitySystemEngineReference() {
        val engine = Engine()
        val system: EntitySystem = EntitySystemMock()

        assertNull(system.engine)
        engine.addSystem(system)
        assertEquals(engine, system.engine)
        engine.removeSystem(system)
        assertNull(system.engine)
    }

    @Test
    fun ignoreSystem() {
        val engine = Engine()
        val system = EntitySystemMock()

        engine.addSystem(system)

        val numUpdates = 10

        for (i in 0..<numUpdates) {
            system.setProcessing(i % 2 == 0)
            engine.update(deltaTime)
            assertEquals((i / 2 + 1), system.updateCalls)
        }
    }

    @Test
    fun entitiesForFamily() {
        val engine = Engine()

        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val familyEntities = engine.getEntitiesFor(family)

        assertEquals(0, familyEntities.size())

        val entity1 = Entity()
        val entity2 = Entity()
        val entity3 = Entity()
        val entity4 = Entity()

        entity1.add(ComponentA())
        entity1.add(ComponentB())

        entity2.add(ComponentA())
        entity2.add(ComponentC())

        entity3.add(ComponentA())
        entity3.add(ComponentB())
        entity3.add(ComponentC())

        entity4.add(ComponentA())
        entity4.add(ComponentB())
        entity4.add(ComponentC())

        engine.addEntity(entity1)
        engine.addEntity(entity2)
        engine.addEntity(entity3)
        engine.addEntity(entity4)

        assertEquals(3, familyEntities.size())
        assertTrue(familyEntities.contains(entity1, true))
        assertTrue(familyEntities.contains(entity3, true))
        assertTrue(familyEntities.contains(entity4, true))
        assertFalse(familyEntities.contains(entity2, true))
    }

    @Test
    fun entityForFamilyWithRemoval() {
        // Test for issue #13
        val engine = Engine()

        val entity = Entity()
        entity.add(ComponentA())

        engine.addEntity(entity)

        val entities = engine.getEntitiesFor(Family.all(ComponentA::class.java).get())

        assertEquals(1, entities.size())
        assertTrue(entities.contains(entity, true))

        engine.removeEntity(entity)

        assertEquals(0, entities.size())
        assertFalse(entities.contains(entity, true))
    }

    @Test
    fun entitiesForFamilyAfter() {
        val engine = Engine()

        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val familyEntities = engine.getEntitiesFor(family)

        assertEquals(0, familyEntities.size())

        val entity1 = Entity()
        val entity2 = Entity()
        val entity3 = Entity()
        val entity4 = Entity()

        engine.addEntity(entity1)
        engine.addEntity(entity2)
        engine.addEntity(entity3)
        engine.addEntity(entity4)

        entity1.add(ComponentA())
        entity1.add(ComponentB())

        entity2.add(ComponentA())
        entity2.add(ComponentC())

        entity3.add(ComponentA())
        entity3.add(ComponentB())
        entity3.add(ComponentC())

        entity4.add(ComponentA())
        entity4.add(ComponentB())
        entity4.add(ComponentC())

        assertEquals(3, familyEntities.size())
        assertTrue(familyEntities.contains(entity1, true))
        assertTrue(familyEntities.contains(entity3, true))
        assertTrue(familyEntities.contains(entity4, true))
        assertFalse(familyEntities.contains(entity2, true))
    }

    @Test
    fun entitiesForFamilyWithRemoval() {
        val engine = Engine()

        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val familyEntities = engine.getEntitiesFor(family)

        val entity1 = Entity()
        val entity2 = Entity()
        val entity3 = Entity()
        val entity4 = Entity()

        engine.addEntity(entity1)
        engine.addEntity(entity2)
        engine.addEntity(entity3)
        engine.addEntity(entity4)

        entity1.add(ComponentA())
        entity1.add(ComponentB())

        entity2.add(ComponentA())
        entity2.add(ComponentC())

        entity3.add(ComponentA())
        entity3.add(ComponentB())
        entity3.add(ComponentC())

        entity4.add(ComponentA())
        entity4.add(ComponentB())
        entity4.add(ComponentC())

        assertEquals(3, familyEntities.size())
        assertTrue(familyEntities.contains(entity1, true))
        assertTrue(familyEntities.contains(entity3, true))
        assertTrue(familyEntities.contains(entity4, true))
        assertFalse(familyEntities.contains(entity2, true))

        entity1.remove(ComponentA::class.java)
        engine.removeEntity(entity3)

        assertEquals(1, familyEntities.size())
        assertTrue(familyEntities.contains(entity4, true))
        assertFalse(familyEntities.contains(entity1, true))
        assertFalse(familyEntities.contains(entity3, true))
        assertFalse(familyEntities.contains(entity2, true))
    }

    @Test
    fun entitiesForFamilyWithRemovalAndFiltering() {
        val engine = Engine()

        val entitiesWithComponentAOnly = engine.getEntitiesFor(
            Family.all(ComponentA::class.java)
                .exclude(ComponentB::class.java).get()
        )

        val entitiesWithComponentB = engine.getEntitiesFor(Family.all(ComponentB::class.java).get())

        val entity1 = Entity()
        val entity2 = Entity()

        engine.addEntity(entity1)
        engine.addEntity(entity2)

        entity1.add(ComponentA())

        entity2.add(ComponentA())
        entity2.add(ComponentB())

        assertEquals(1, entitiesWithComponentAOnly.size())
        assertEquals(1, entitiesWithComponentB.size())

        entity2.remove(ComponentB::class.java)

        assertEquals(2, entitiesWithComponentAOnly.size())
        assertEquals(0, entitiesWithComponentB.size())
    }

    @Test
    fun entitySystemRemovalWhileIterating() {
        val engine = Engine()

        engine.addSystem(CounterSystem())

        for (i in 0..19) {
            val entity = Entity()
            entity.add(CounterComponent())
            engine.addEntity(entity)
        }

        val entities = engine.getEntitiesFor(Family.all(CounterComponent::class.java).get())

        for (i in 0..<entities.size()) {
            assertEquals(0, entities.get(i)!!.getComponent(CounterComponent::class.java).counter)
        }

        engine.update(deltaTime)

        for (i in 0..<entities.size()) {
            assertEquals(1, entities.get(i)!!.getComponent(CounterComponent::class.java).counter)
        }
    }

    @Test
    fun entityAddRemoveComponentWhileIterating() {
        val numEntities = 20
        val engine = Engine()
        val addedListener = ComponentAddedListener(numEntities)
        val addSystem = ComponentAddSystem(addedListener)

        val removedListener = ComponentRemovedListener(numEntities)
        val removeSystem = ComponentRemoveSystem(removedListener)

        for (i in 0..<numEntities) {
            val entity = Entity()
            engine.addEntity(entity)
        }

        engine.addEntityListener(Family.all(ComponentA::class.java).get(), addedListener)
        engine.addEntityListener(Family.all(ComponentA::class.java).get(), removedListener)

        engine.addSystem(addSystem)
        engine.update(deltaTime)
        addedListener.checkEntityListenerNonUpdate()
        engine.removeSystem(addSystem)

        engine.addSystem(removeSystem)
        engine.update(deltaTime)
        removedListener.checkEntityListenerNonUpdate()
        engine.removeSystem(removeSystem)
    }

    @Test
    fun cascadeOperationsInListenersWhileUpdating() {
        // This test case mix both add/remove component and add/remove entities
        // in listeners.
        // Listeners trigger each other recursively to test cascade operations :

        // CREATION PHASE :
        // first listener will add a component which trigger the second,
        // second listener will create an entity which trigger the first one,
        // and so on.

        // DESTRUCTION PHASE :
        // first listener will remove component which trigger the second,
        // second listener will remove the entity which trigger the first one,
        // and so on.

        val numEntities = 20
        val engine = Engine()
        val addedListener = ComponentAddedListener(numEntities)
        val removedListener = ComponentRemovedListener(numEntities)

        val entities = Array<Entity>()

        engine.addEntityListener(Family.all(ComponentA::class.java).get(), object : EntityListener {
            override fun entityRemoved(entity: Entity) {
                engine.removeEntity(entity)
            }

            override fun entityAdded(entity: Entity) {
                if (entities.size < numEntities) {
                    val e = Entity()
                    engine.addEntity(e)
                }
            }
        })
        engine.addEntityListener(object : EntityListener {
            override fun entityRemoved(entity: Entity) {
                entities.removeValue(entity, true)
                if (entities.size > 0) {
                    entities.peek()!!.remove(ComponentA::class.java)
                }
            }

            override fun entityAdded(entity: Entity) {
                entities.add(entity)
                entity.add(ComponentA())
            }
        })

        engine.addEntityListener(Family.all(ComponentA::class.java).get(), addedListener)
        engine.addEntityListener(Family.all(ComponentA::class.java).get(), removedListener)

        // this system will just create an entity which will trigger
        // listeners cascade creations (up to 20)
        val addSystem: EntitySystem = object : EntitySystem() {
            override fun update(deltaTime: Float) {
                getEngine().addEntity(Entity())
            }
        }

        engine.addSystem(addSystem)
        engine.update(deltaTime)
        engine.removeSystem(addSystem)
        addedListener.checkEntityListenerNonUpdate()
        removedListener.checkEntityListenerUpdate()

        // this system will just remove an entity which will trigger
        // listeners cascade deletion (up to 0)
        val removeSystem: EntitySystem = object : EntitySystem() {
            override fun update(deltaTime: Float) {
                getEngine().removeEntity(entities.peek())
            }
        }

        engine.addSystem(removeSystem)
        engine.update(deltaTime)
        engine.removeSystem(removeSystem)
        addedListener.checkEntityListenerUpdate()
        removedListener.checkEntityListenerNonUpdate()
    }

    @Test
    fun familyListener() {
        val engine = Engine()

        val listenerA = EntityListenerMock()
        val listenerB = EntityListenerMock()

        val familyA = Family.all(ComponentA::class.java).get()
        val familyB = Family.all(ComponentB::class.java).get()

        engine.addEntityListener(familyA, listenerA)
        engine.addEntityListener(familyB, listenerB)

        val entity1 = Entity()
        engine.addEntity(entity1)

        assertEquals(0, listenerA.addedCount)
        assertEquals(0, listenerB.addedCount)

        val entity2 = Entity()
        engine.addEntity(entity2)

        assertEquals(0, listenerA.addedCount)
        assertEquals(0, listenerB.addedCount)

        entity1.add(ComponentA())

        assertEquals(1, listenerA.addedCount)
        assertEquals(0, listenerB.addedCount)

        entity2.add(ComponentB())

        assertEquals(1, listenerA.addedCount)
        assertEquals(1, listenerB.addedCount)

        entity1.remove(ComponentA::class.java)

        assertEquals(1, listenerA.removedCount)
        assertEquals(0, listenerB.removedCount)

        engine.removeEntity(entity2)

        assertEquals(1, listenerA.removedCount)
        assertEquals(1, listenerB.removedCount)

        engine.removeEntityListener(listenerB)

        engine.addEntity(entity2)

        assertEquals(1, listenerA.addedCount)
        assertEquals(1, listenerB.addedCount)

        entity1.add(ComponentB())
        entity1.add(ComponentA())

        assertEquals(2, listenerA.addedCount)
        assertEquals(1, listenerB.addedCount)

        engine.removeAllEntities()

        assertEquals(2, listenerA.removedCount)
        assertEquals(1, listenerB.removedCount)

        engine.addEntityListener(listenerB)

        engine.addEntity(entity1)
        engine.addEntity(entity2)

        assertEquals(3, listenerA.addedCount)
        assertEquals(3, listenerB.addedCount)

        engine.removeAllEntities(familyA)

        assertEquals(3, listenerA.removedCount)
        assertEquals(2, listenerB.removedCount)

        engine.removeAllEntities(familyB)

        assertEquals(3, listenerA.removedCount)
        assertEquals(3, listenerB.removedCount)
    }

    @Test
    fun createManyEntitiesNoStackOverflow() {
        val engine = Engine()
        engine.addSystem(CounterSystem())

        var i = 0
        while (15000 > i) {
            val e = Entity()
            e.add(CounterComponent())
            engine.addEntity(e)
            i++
        }

        engine.update(0f)
    }

    @Test
    fun getEntities() {
        val numEntities = 10

        val engine = Engine()

        val entities = Array<Entity>()

        for (i in 0..<numEntities) {
            val entity = Entity()
            entities.add(entity)
            engine.addEntity(entity)
        }

        val engineEntities = engine.entities

        assertEquals(entities.size, engineEntities.size())

        for (i in 0..<numEntities) {
            assertEquals(entities.get(i), engineEntities.get(i))
        }

        engine.removeAllEntities()

        assertEquals(0, engineEntities.size())
    }

    @Test(expected = IllegalArgumentException::class)
    fun addEntityTwice() {
        val engine = Engine()
        val entity = Entity()
        engine.addEntity(entity)
        engine.addEntity(entity)
    }

    @Test(expected = IllegalStateException::class)
    fun nestedUpdateException() {
        val engine = Engine()

        engine.addSystem(object : EntitySystem() {
            var duringCallback: Boolean = false

            override fun update(deltaTime: Float) {
                if (!duringCallback) {
                    duringCallback = true
                    getEngine().update(deltaTime)
                    duringCallback = false
                }
            }
        })

        engine.update(deltaTime)
    }

    @Test
    fun systemUpdateThrows() {
        val engine = Engine()

        val system: EntitySystem = object : EntitySystem() {
            override fun update(deltaTime: Float) {
                throw GdxRuntimeException("throwing")
            }
        }

        engine.addSystem(system)

        var thrown = false

        try {
            engine.update(0.0f)
        } catch (_: Exception) {
            thrown = true
        }

        assertTrue(thrown)

        engine.removeSystem(system)

        engine.update(0.0f)
    }

    @Test
    fun createNewEntity() {
        val engine = Engine()
        val entity = engine.createEntity()

        assertNotEquals(null, entity)
    }

    @Test
    fun createNewComponent() {
        val engine = Engine()
        val componentD = engine.createComponent(ComponentD::class.java)

        assertNotNull(componentD)
    }

    @Test
    fun createPrivateComponent() {
        val engine = Engine()
        val componentC = engine.createComponent(ComponentC::class.java)
        assertNull(componentC)
    }

    @Test
    fun removeEntityBeforeAddingAndWhileEngineIsUpdating() {
        // Test for issue #306.
        val engine = Engine()
        engine.addSystem(RemoveAddAndRemoveEntitySystem())
        engine.update(deltaTime)
        assertEquals(0, engine.entities.size())
    }

    private class ComponentA : Component
    private class ComponentB : Component
    private class ComponentC : Component
    class ComponentD : Component

    private open class EntityListenerMock : EntityListener {
        var addedCount: Int = 0
        var removedCount: Int = 0

        override fun entityAdded(entity: Entity) {
            ++addedCount
            assertNotNull(entity)
        }

        override fun entityRemoved(entity: Entity) {
            ++removedCount
            assertNotNull(entity)
        }
    }

    private class AddComponentBEntityListenerMock : EntityListenerMock() {
        override fun entityAdded(entity: Entity) {
            super.entityAdded(entity)
            entity.add(ComponentB())
        }
    }

    private open class EntitySystemMock : EntitySystem {
        var updateCalls = 0
        var addedCalls = 0
        var removedCalls = 0

        private var updates: Array<Int>? = null

        constructor() : super()

        constructor(updates: Array<Int>) : super() {
            this.updates = updates
        }

        override fun update(deltaTime: Float) {
            updateCalls++

            if (updates != null) {
                updates!!.add(priority)
            }
        }

        override fun addedToEngine(engine: Engine) {
            ++addedCalls

            assertNotNull(engine)
        }

        override fun removedFromEngine(engine: Engine) {
            ++removedCalls

            assertNotNull(engine)
        }
    }

    private class EntitySystemMockA : EntitySystemMock {
        constructor() : super()

        constructor(updates: Array<Int>) : super(updates)
    }

    private class EntitySystemMockB : EntitySystemMock {
        constructor() : super()

        constructor(updates: Array<Int>) : super(updates)
    }

    private class CounterComponent : Component {
        var counter: Int = 0
    }

    private class CounterSystem : EntitySystem() {
        private lateinit var entities: ImmutableArray<Entity>

        override fun addedToEngine(engine: Engine) {
            entities = engine.getEntitiesFor(Family.all(CounterComponent::class.java).get())
        }

        override fun update(deltaTime: Float) {
            for (i in 0..<entities.size()) {
                if (i % 2 == 0) {
                    entities.get(i)!!.getComponent(CounterComponent::class.java).counter++
                } else {
                    engine.removeEntity(entities.get(i))
                }
            }
        }
    }

    class ComponentAddedListener(var numEntities: Int) : EntityListener {
        var addedCalls: Int = 0

        override fun entityAdded(entity: Entity) {
            addedCalls++
        }

        override fun entityRemoved(entity: Entity) {
        }

        fun checkEntityListenerNonUpdate() {
            assertEquals(numEntities, addedCalls)
            addedCalls = 0
        }

        fun checkEntityListenerUpdate() {
            assertEquals(0, addedCalls)
        }
    }

    class ComponentRemovedListener(var numEntities: Int) : EntityListener {
        var removedCalls: Int = 0

        override fun entityAdded(entity: Entity) {
        }

        override fun entityRemoved(entity: Entity) {
            removedCalls++
        }

        fun checkEntityListenerNonUpdate() {
            assertEquals(numEntities, removedCalls)
            removedCalls = 0
        }

        fun checkEntityListenerUpdate() {
            assertEquals(0, removedCalls)
        }
    }

    class ComponentAddSystem(private val listener: ComponentAddedListener) : IteratingSystem(Family.all().get()) {
        override fun processEntity(entity: Entity, deltaTime: Float) {
            assertNull(entity.getComponent(ComponentA::class.java))
            entity.add(ComponentA())
            assertNotNull(entity.getComponent(ComponentA::class.java))
            listener.checkEntityListenerUpdate()
        }
    }

    class ComponentRemoveSystem(private val listener: ComponentRemovedListener) : IteratingSystem(Family.all().get()) {
        override fun processEntity(entity: Entity, deltaTime: Float) {
            assertNotNull(entity.getComponent(ComponentA::class.java))
            entity.remove(ComponentA::class.java)
            assertNull(entity.getComponent(ComponentA::class.java))
            listener.checkEntityListenerUpdate()
        }
    }

    class RemoveAddAndRemoveEntitySystem : EntitySystem() {
        override fun update(deltaTime: Float) {
            val entity = Entity()
            engine.removeEntity(entity)
            engine.addEntity(entity)
            engine.removeEntity(entity)
        }
    }
}
