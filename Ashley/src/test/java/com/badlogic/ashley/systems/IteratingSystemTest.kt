package com.badlogic.ashley.systems

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import org.junit.Assert.assertEquals
import org.junit.Test

class IteratingSystemTest {
    @Test
    fun shouldIterateEntitiesWithCorrectFamily() {
        val engine = Engine()

        val family = Family.all(ComponentA::class.java, ComponentB::class.java).get()
        val system = IteratingSystemMock(family)
        val e = Entity()

        engine.addSystem(system)
        engine.addEntity(e)

        // When entity has ComponentA
        e.add(ComponentA())
        engine.update(DELTA_TIME)

        assertEquals(0, system.numUpdates)

        // When entity has ComponentA and ComponentB
        system.numUpdates = 0
        e.add(ComponentB())
        engine.update(DELTA_TIME)

        assertEquals(1, system.numUpdates)

        // When entity has ComponentA, ComponentB and ComponentC
        system.numUpdates = 0
        e.add(ComponentC())
        engine.update(DELTA_TIME)

        assertEquals(1, system.numUpdates)

        // When entity has ComponentB and ComponentC
        system.numUpdates = 0
        e.remove(ComponentA::class.java)
        e.add(ComponentC())
        engine.update(DELTA_TIME)

        assertEquals(0, system.numUpdates)
    }

    @Test
    fun entityRemovalWhileIterating() {
        val engine = Engine()
        val entities = engine.getEntitiesFor(Family.all(SpyComponent::class.java, IndexComponent::class.java).get())
        val sm = ComponentMapper.getFor(SpyComponent::class.java)

        engine.addSystem(IteratingRemovalSystem())

        val numEntities = 10

        for (i in 0..<numEntities) {
            val e = Entity()
            e.add(SpyComponent())

            val `in` = IndexComponent()
            `in`.index = i + 1

            e.add(`in`)

            engine.addEntity(e)
        }

        engine.update(DELTA_TIME)

        assertEquals(numEntities / 2, entities.size())

        for (i in 0..<entities.size()) {
            val e = entities.get(i)
            assertEquals(1, sm.get(e).updates)
        }
    }

    @Test
    fun componentRemovalWhileIterating() {
        val engine = Engine()
        val entities = engine.getEntitiesFor(Family.all(SpyComponent::class.java, IndexComponent::class.java).get())
        val sm = ComponentMapper.getFor(SpyComponent::class.java)

        engine.addSystem(IteratingComponentRemovalSystem())

        val numEntities = 10

        for (i in 0..<numEntities) {
            val e = Entity()
            e.add(SpyComponent())

            val indexComponent = IndexComponent()
            indexComponent.index = i + 1

            e.add(indexComponent)

            engine.addEntity(e)
        }

        engine.update(DELTA_TIME)

        assertEquals((numEntities / 2), entities.size())

        for (i in 0..<entities.size()) {
            val e = entities.get(i)

            assertEquals(1, sm.get(e).updates)
        }
    }

    @Test
    fun processingUtilityFunctions() {
        val engine = Engine()

        val system = IteratingSystemMock(Family.all().get())

        engine.addSystem(system)

        engine.update(DELTA_TIME)
        assertEquals(1, system.numStartProcessing)
        assertEquals(1, system.numEndProcessing)

        engine.update(DELTA_TIME)
        assertEquals(2, system.numStartProcessing)
        assertEquals(2, system.numEndProcessing)
    }

    private class ComponentA : Component
    private class ComponentB : Component
    private class ComponentC : Component

    private class IteratingSystemMock(family: Family) : IteratingSystem(family) {
        var numUpdates = 0
        var numStartProcessing = 0
        var numEndProcessing = 0

        override fun startProcessing() {
            numStartProcessing++
        }

        override fun processEntity(entity: Entity, deltaTime: Float) {
            numUpdates++
        }

        override fun endProcessing() {
            numEndProcessing++
        }
    }

    private class SpyComponent : Component {
        var updates: Int = 0
    }

    private class IndexComponent : Component {
        var index: Int = 0
    }

    private class IteratingComponentRemovalSystem : IteratingSystem(Family.all(SpyComponent::class.java, IndexComponent::class.java).get()) {
        private val sm: ComponentMapper<SpyComponent> = ComponentMapper.getFor(SpyComponent::class.java)
        private val im: ComponentMapper<IndexComponent> = ComponentMapper.getFor(IndexComponent::class.java)

        override fun processEntity(entity: Entity, deltaTime: Float) {
            val index = im.get(entity).index
            if (index % 2 == 0) {
                entity.remove(SpyComponent::class.java)
                entity.remove(IndexComponent::class.java)
            } else {
                sm.get(entity).updates++
            }
        }
    }

    private class IteratingRemovalSystem : IteratingSystem(Family.all(SpyComponent::class.java, IndexComponent::class.java).get()) {
        private val sm: ComponentMapper<SpyComponent> = ComponentMapper.getFor(SpyComponent::class.java)
        private val im: ComponentMapper<IndexComponent> = ComponentMapper.getFor(IndexComponent::class.java)

        override fun addedToEngine(engine: Engine) {
            super.addedToEngine(engine)
        }

        override fun processEntity(entity: Entity, deltaTime: Float) {
            val index = im.get(entity).index
            if (index % 2 == 0)
                engine.removeEntity(entity)
            else
                sm.get(entity).updates++
        }
    }

    companion object {
        private const val DELTA_TIME = 0.16F
    }
}
