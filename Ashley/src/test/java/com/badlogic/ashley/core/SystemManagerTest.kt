package com.badlogic.ashley.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class SystemManagerTest {

    private class SystemMock(priority: Int = 0) : EntitySystem(priority)
    private class SystemMockA : EntitySystem()
    private class SystemMockB : EntitySystem()

    private class SystemListenerMock : SystemManager.SystemListener {
        var addedCount = 0
        var removedCount = 0

        override fun systemAdded(system: EntitySystem) {
            addedCount++
        }

        override fun systemRemoved(system: EntitySystem) {
            removedCount++
        }
    }

    @Test
    fun addAndRemoveSystem() {
        val listener = SystemListenerMock()
        val manager = SystemManager(listener)
        val system = SystemMock()

        manager.addSystem(system)
        assertEquals(1, manager.systems.size())
        assertEquals(1, listener.addedCount)
        assertEquals(system, manager.getSystem(SystemMock::class.java))

        manager.removeSystem(system)
        assertEquals(0, manager.systems.size())
        assertEquals(1, listener.removedCount)
        assertNull(manager.getSystem(SystemMock::class.java))
    }

    @Test
    fun addDuplicateSystem() {
        val listener = SystemListenerMock()
        val manager = SystemManager(listener)
        val system1 = SystemMock()
        val system2 = SystemMock() // Same class

        manager.addSystem(system1)
        assertEquals(1, manager.systems.size())
        assertEquals(1, listener.addedCount)

        manager.addSystem(system2)
        assertEquals(1, manager.systems.size())
        assertEquals(2, listener.addedCount) // Added twice (one for system1, one for system2)
        assertEquals(1, listener.removedCount) // Removed system1

        assertEquals(system2, manager.getSystem(SystemMock::class.java))
    }

    @Test
    fun getSystem() {
        val listener = SystemListenerMock()
        val manager = SystemManager(listener)
        val systemA = SystemMockA()
        val systemB = SystemMockB()

        manager.addSystem(systemA)
        manager.addSystem(systemB)

        assertEquals(systemA, manager.getSystem(SystemMockA::class.java))
        assertEquals(systemB, manager.getSystem(SystemMockB::class.java))
    }

    @Test
    fun systemsOrder() {
        val listener = SystemListenerMock()
        val manager = SystemManager(listener)
        
        // Anonymous classes to ensure they are treated as different system types
        val systemA = object : EntitySystem(10) {}
        val systemB = object : EntitySystem(0) {}

        manager.addSystem(systemA)
        manager.addSystem(systemB)

        val systems = manager.systems
        assertEquals(2, systems.size())
        assertEquals(systemB, systems[0])
        assertEquals(systemA, systems[1])
    }

    @Test
    fun removeAllSystems() {
        val listener = SystemListenerMock()
        val manager = SystemManager(listener)
        val systemA = SystemMockA()
        val systemB = SystemMockB()

        manager.addSystem(systemA)
        manager.addSystem(systemB)

        assertEquals(2, manager.systems.size())

        manager.removeAllSystems()
        assertEquals(0, manager.systems.size())
        assertEquals(2, listener.removedCount)
    }
}
