package com.badlogic.ashley.systems

import com.badlogic.ashley.core.Engine
import org.junit.Assert.assertEquals
import org.junit.Test

class IntervalSystemTest {

    @Test
    fun intervalSystem() {
        val engine = Engine()
        val intervalSystemSpy = IntervalSystemSpy()
        engine.addSystem(intervalSystemSpy)

        for (i in 1..10) {
            engine.update(DELTA_TIME)
            assertEquals(i / 2, intervalSystemSpy.numUpdates)
        }
    }

    @Test
    fun testGetInterval() {
        val intervalSystemSpy = IntervalSystemSpy()
        assertEquals(DELTA_TIME * 2.0F, intervalSystemSpy.getInterval(), 0F)
    }

    private class IntervalSystemSpy : IntervalSystem(DELTA_TIME * 2.0F) {
        var numUpdates = 0

        override fun updateInterval() {
            numUpdates++
        }
    }

    companion object {
        private const val DELTA_TIME = 0.1F
    }
}
