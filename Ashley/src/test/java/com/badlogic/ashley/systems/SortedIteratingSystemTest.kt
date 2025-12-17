package com.badlogic.ashley.systems

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.LinkedList

class SortedIteratingSystemTest {
    @Test
    fun shouldIterateEntitiesWithCorrectFamily() {
        val engine = Engine()

        val family = Family.all(OrderComponent::class.java, ComponentB::class.java).get()
        val system = SortedIteratingSystemMock(family)
        val e = Entity()

        engine.addSystem(system)
        engine.addEntity(e)

        // When entity has OrderComponent
        e.add(OrderComponent("A", 0))
        engine.update(DELTA_TIME)

        // When entity has OrderComponent and ComponentB
        e.add(ComponentB())
        system.expectedNames.addLast("A")
        engine.update(DELTA_TIME)

        // When entity has OrderComponent, ComponentB and ComponentC
        e.add(ComponentC())
        system.expectedNames.addLast("A")
        engine.update(DELTA_TIME)

        // When entity has ComponentB and ComponentC
        e.remove(OrderComponent::class.java)
        e.add(ComponentC())
        engine.update(DELTA_TIME)
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
            e.add(OrderComponent("" + i, i))

            val `in` = IndexComponent()
            `in`.index = i + 1

            e.add(`in`)

            engine.addEntity(e)
        }

        engine.update(DELTA_TIME)

        assertEquals((numEntities / 2).toLong(), entities.size().toLong())

        for (i in 0..<entities.size()) {
            val e = entities.get(i)

            assertEquals(1, sm.get(e).updates.toLong())
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
            e.add(OrderComponent("" + i, i))

            val `in` = IndexComponent()
            `in`.index = i + 1

            e.add(`in`)

            engine.addEntity(e)
        }

        engine.update(DELTA_TIME)

        assertEquals((numEntities / 2).toLong(), entities.size().toLong())

        for (i in 0..<entities.size()) {
            val e = entities.get(i)

            assertEquals(1, sm.get(e).updates.toLong())
        }
    }

    @Test
    fun entityOrder() {
        val engine = Engine()

        val family = Family.all(OrderComponent::class.java).get()
        val system = SortedIteratingSystemMock(family)
        engine.addSystem(system)

        val a: Entity = createOrderEntity("A", 0)
        val b: Entity = createOrderEntity("B", 1)
        val c: Entity = createOrderEntity("C", 3)
        val d: Entity = createOrderEntity("D", 2)

        engine.addEntity(a)
        engine.addEntity(b)
        engine.addEntity(c)
        system.expectedNames.addLast("A")
        system.expectedNames.addLast("B")
        system.expectedNames.addLast("C")
        engine.update(0f)

        engine.addEntity(d)
        system.expectedNames.addLast("A")
        system.expectedNames.addLast("B")
        system.expectedNames.addLast("D")
        system.expectedNames.addLast("C")
        engine.update(0f)

        orderMapper.get(a).zLayer = 3
        orderMapper.get(b).zLayer = 2
        orderMapper.get(c).zLayer = 1
        orderMapper.get(d).zLayer = 0
        system.forceSort()
        system.expectedNames.addLast("D")
        system.expectedNames.addLast("C")
        system.expectedNames.addLast("B")
        system.expectedNames.addLast("A")
        engine.update(0f)
    }

    @Test
    fun processingUtilityFunctions() {
        val engine = Engine()

        val system = SortedIteratingSystemMock(Family.all().get())

        engine.addSystem(system)

        engine.update(DELTA_TIME)
        assertEquals(1, system.numStartProcessing.toLong())
        assertEquals(1, system.numEndProcessing.toLong())

        engine.update(DELTA_TIME)
        assertEquals(2, system.numStartProcessing.toLong())
        assertEquals(2, system.numEndProcessing.toLong())
    }

    private class ComponentB : Component

    private class ComponentC : Component

    private class SortedIteratingSystemMock(family: Family) : SortedIteratingSystem(family, comparator) {
        var expectedNames: LinkedList<String> = LinkedList<String>()
        var numStartProcessing: Int = 0
        var numEndProcessing: Int = 0

        override fun update(deltaTime: Float) {
            super.update(deltaTime)
            assertTrue(expectedNames.isEmpty())
        }

        override fun startProcessing() {
            ++numStartProcessing
        }

        override fun processEntity(entity: Entity, deltaTime: Float) {
            val component: OrderComponent = orderMapper.get(entity)
            assertNotNull(component)
            assertFalse(expectedNames.isEmpty())
            assertEquals(expectedNames.poll(), component.name)
        }

        override fun endProcessing() {
            ++numEndProcessing
        }
    }

    class OrderComponent(var name: String, var zLayer: Int) : Component

    private class SpyComponent : Component {
        var updates: Int = 0
    }

    private class IndexComponent : Component {
        var index: Int = 0
    }

    private class IteratingComponentRemovalSystem : SortedIteratingSystem(Family.all(SpyComponent::class.java, IndexComponent::class.java).get(), comparator) {
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

    private class IteratingRemovalSystem : SortedIteratingSystem(Family.all(SpyComponent::class.java, IndexComponent::class.java).get(), comparator) {
        private lateinit var engine: Engine
        private val sm: ComponentMapper<SpyComponent> = ComponentMapper.getFor(SpyComponent::class.java)
        private val im: ComponentMapper<IndexComponent> = ComponentMapper.getFor(IndexComponent::class.java)

        override fun addedToEngine(engine: Engine) {
            super.addedToEngine(engine)
            this.engine = engine
        }

        override fun processEntity(entity: Entity, deltaTime: Float) {
            val index = im.get(entity).index
            if (index % 2 == 0) {
                engine.removeEntity(entity)
            } else {
                sm.get(entity).updates++
            }
        }
    }

    private class OrderComparator : Comparator<Entity> {
        override fun compare(a: Entity, b: Entity): Int {
            val ac: OrderComponent = orderMapper.get(a)
            val bc: OrderComponent = orderMapper.get(b)
            return ac.zLayer.compareTo(bc.zLayer)
        }
    }

    companion object {
        private val orderMapper: ComponentMapper<OrderComponent> = ComponentMapper.getFor<OrderComponent>(OrderComponent::class.java)
        private val comparator = OrderComparator()
        private const val DELTA_TIME = 0.16F

        private fun createOrderEntity(name: String, zLayer: Int): Entity {
            val entity = Entity()
            entity.add(OrderComponent(name, zLayer))
            return entity
        }
    }
}
