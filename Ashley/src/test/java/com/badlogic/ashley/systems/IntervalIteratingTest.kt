package com.badlogic.ashley.systems

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import org.junit.Assert.assertEquals
import org.junit.Test

class IntervalIteratingTest {
    @Test
    fun intervalSystem() {
        val engine = Engine()
        val intervalSystemSpy = IntervalIteratingSystemSpy()
        val entities = engine.getEntitiesFor(Family.all(IntervalComponentSpy::class.java).get())
        val im: ComponentMapper<IntervalComponentSpy> = ComponentMapper.getFor(IntervalComponentSpy::class.java)

        engine.addSystem(intervalSystemSpy)

        for (i in 0..9) {
            val entity = Entity()
            entity.add(IntervalComponentSpy())
            engine.addEntity(entity)
        }

        for (i in 1..10) {
            engine.update(DELTA_TIME)

            for (j in 0..<entities.size()) {
                assertEquals((i / 2), im.get(entities.get(j)).numUpdates)
            }
        }
    }

    @Test
    fun processingUtilityFunctions() {
        val engine = Engine()

        val system = IntervalIteratingSystemSpy()

        engine.addSystem(system)

        engine.update(DELTA_TIME)
        assertEquals(0, system.numStartProcessing)
        assertEquals(0, system.numEndProcessing)

        engine.update(DELTA_TIME)
        assertEquals(1, system.numStartProcessing)
        assertEquals(1, system.numEndProcessing)
    }

    private class IntervalComponentSpy : Component {
        var numUpdates: Int = 0
    }

    private class IntervalIteratingSystemSpy : IntervalIteratingSystem(Family.all(IntervalComponentSpy::class.java).get(), DELTA_TIME * 2.0F) {
        var numStartProcessing = 0
        var numEndProcessing = 0
        private val im: ComponentMapper<IntervalComponentSpy> = ComponentMapper.getFor(IntervalComponentSpy::class.java)

        override fun startProcessing() {
            numStartProcessing++
        }

        override fun processEntity(entity: Entity) {
            im.get(entity).numUpdates++
        }

        override fun endProcessing() {
            numEndProcessing++
        }
    }

    companion object {
        private const val DELTA_TIME = 0.1F
    }
}
