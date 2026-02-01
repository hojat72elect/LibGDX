package com.badlogic.ashley.systems

import com.badlogic.ashley.benchmark.ashley.components.MovementComponent
import com.badlogic.ashley.benchmark.ashley.components.PositionComponent
import com.badlogic.ashley.benchmark.ashley.systems.MovementSystem
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import org.junit.Assert.assertEquals
import org.junit.Test

class MovementSystemTest {

    @Test
    fun shouldUpdatePositionAndVelocity() {
        val engine = Engine()
        val movementSystem = MovementSystem()
        engine.addSystem(movementSystem)

        val entity = Entity()
        val positionComponent = PositionComponent()
        val movementComponent = MovementComponent()

        // Set initial state
        positionComponent.pos.set(0f, 0f, 0f)
        movementComponent.velocity.set(10f, 0f)
        movementComponent.accel.set(0f, 0f)

        entity.add(positionComponent)
        entity.add(movementComponent)
        engine.addEntity(entity)

        val deltaTime = 1.0f
        engine.update(deltaTime)

        // Velocity should remain 10, 0 as accel is 0
        assertEquals(10f, movementComponent.velocity.x, 0.001f)
        assertEquals(0f, movementComponent.velocity.y, 0.001f)

        // Position should be 10, 0, 0
        assertEquals(10f, positionComponent.pos.x, 0.001f)
        assertEquals(0f, positionComponent.pos.y, 0.001f)
        assertEquals(0f, positionComponent.pos.z, 0.001f)
    }

    @Test
    fun shouldApplyAcceleration() {
        val engine = Engine()
        val movementSystem = MovementSystem()
        engine.addSystem(movementSystem)

        val entity = Entity()
        val positionComponent = PositionComponent()
        val movementComponent = MovementComponent()

        // Set initial state
        positionComponent.pos.set(0f, 0f, 0f)
        movementComponent.velocity.set(0f, 0f)
        movementComponent.accel.set(1f, 2f)

        entity.add(positionComponent)
        entity.add(movementComponent)
        engine.addEntity(entity)

        val deltaTime = 1.0f
        engine.update(deltaTime)

        // Velocity should be updated by accel * deltaTime
        // vel = 0 + 1*1 = 1
        assertEquals(1f, movementComponent.velocity.x, 0.001f)
        assertEquals(2f, movementComponent.velocity.y, 0.001f)

        // Position should be updated by velocity * deltaTime
        // Note: MovementSystem implementation updates velocity first, then uses new velocity for position
        // vel = (1, 2)
        // pos = (0,0) + (1,2)*1 = (1,2)
        
        assertEquals(1f, positionComponent.pos.x, 0.001f)
        assertEquals(2f, positionComponent.pos.y, 0.001f)
    }
}
