package com.badlogic.ashley.core

import com.badlogic.ashley.core.SystemManager.SystemListener
import com.badlogic.gdx.utils.Array
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertEquals
import org.junit.Test

class SystemManagerTests {

    @Test
    fun addAndRemoveSystem() {

        val systemA = EntitySystemMockA()
        val systemB = EntitySystemMockB()

        val systemSpy = SystemListenerSpy()
        val manager = SystemManager(systemSpy)

        assertNull(manager.getSystem(EntitySystemMockA::class.java))
        assertNull(manager.getSystem(EntitySystemMockB::class.java))

        manager.addSystem(systemA)
        manager.addSystem(systemB)

        assertNotNull(manager.getSystem(EntitySystemMockA::class.java))
        assertNotNull(manager.getSystem(EntitySystemMockB::class.java))
        assertEquals(1, systemA.addedCalls)
        assertEquals(1, systemB.addedCalls)

        manager.removeSystem(systemA)
        manager.removeSystem(systemB)

        assertNull(manager.getSystem(EntitySystemMockA::class.java))
        assertNull(manager.getSystem(EntitySystemMockB::class.java))
        assertEquals(1, systemA.removedCalls)
        assertEquals(1, systemB.removedCalls)

        manager.addSystem(systemA)
        manager.addSystem(systemB)
        manager.removeAllSystems()

        assertNull(manager.getSystem(EntitySystemMockA::class.java))
        assertNull(manager.getSystem(EntitySystemMockB::class.java))
        assertEquals(2, systemA.removedCalls)
        assertEquals(2, systemB.removedCalls)
    }

    @Test
    fun getSystems() {
        val systemSpy = SystemListenerSpy()
        val manager = SystemManager(systemSpy)
        val systemA = EntitySystemMockA()
        val systemB = EntitySystemMockB()

        assertEquals(0, manager.getSystems().size())

        manager.addSystem(systemA)
        manager.addSystem(systemB)

        assertEquals(2, manager.getSystems().size())
        assertEquals(2, systemSpy.addedCount)

        manager.removeSystem(systemA)
        manager.removeSystem(systemB)

        assertEquals(0, manager.getSystems().size())
        assertEquals(2, systemSpy.addedCount)
        assertEquals(2, systemSpy.removedCount)
    }

    @Test
    fun addTwoSystemsOfSameClass() {
        val systemSpy = SystemListenerSpy()
        val manager = SystemManager(systemSpy)
        val system1 = EntitySystemMockA()
        val system2 = EntitySystemMockA()

        assertEquals(0, manager.getSystems().size())

        manager.addSystem(system1)

        assertEquals(1, manager.getSystems().size())
        assertEquals(system1, manager.getSystem(EntitySystemMockA::class.java))
        assertEquals(1, systemSpy.addedCount)

        manager.addSystem(system2)

        assertEquals(1, manager.getSystems().size())
        assertEquals(system2, manager.getSystem(EntitySystemMockA::class.java))
        assertEquals(2, systemSpy.addedCount)
        assertEquals(1, systemSpy.removedCount)
    }

    @Test
    fun systemUpdateOrder() {
        val updates = Array<Int>()

        val systemSpy = SystemListenerSpy()
        val manager = SystemManager(systemSpy)
        val system1: EntitySystemMock = EntitySystemMockA(updates)
        val system2: EntitySystemMock = EntitySystemMockB(updates)

        system1.priority = 2
        system2.priority = 1

        manager.addSystem(system1)
        manager.addSystem(system2)

        val systems = manager.getSystems()
        assertEquals(system2, systems.get(0))
        assertEquals(system1, systems.get(1))
    }

    private class SystemListenerSpy : SystemListener {
        var addedCount: Int = 0
        var removedCount: Int = 0

        override fun systemAdded(system: EntitySystem) {
            system.addedToEngine(null)
            ++addedCount
        }

        override fun systemRemoved(system: EntitySystem) {
            system.removedFromEngine(null)
            removedCount++
        }
    }

    private open class EntitySystemMock : EntitySystem {
        var addedCalls = 0
        var removedCalls = 0

        private lateinit var updates: Array<Int>

        constructor() : super()

        constructor(updates: Array<Int>) : super() {
            this.updates = updates
        }

        override fun update(deltaTime: Float) {
            updates.add(priority)
        }

        override fun addedToEngine(engine: Engine?) {
            addedCalls++
        }

        override fun removedFromEngine(engine: Engine?) {
           removedCalls++
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
}
