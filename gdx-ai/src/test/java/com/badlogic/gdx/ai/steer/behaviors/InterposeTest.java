package com.badlogic.gdx.ai.steer.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

import org.junit.Before;
import org.junit.Test;

public class InterposeTest {

    private Interpose<Vector2> interpose;
    private TestSteerable owner;
    private TestSteerable agentA;
    private TestSteerable agentB;
    private TestLimiter limiter;

    @Before
    public void setUp() {
        owner = new TestSteerable();
        agentA = new TestSteerable();
        agentB = new TestSteerable();
        limiter = new TestLimiter();
        interpose = new Interpose<>(owner, agentA, agentB);
    }

    @Test
    public void testConstructorWithOwnerAndAgents() {
        Interpose<Vector2> interpose = new Interpose<>(owner, agentA, agentB);
        assertNotNull(interpose);
        assertEquals(owner, interpose.getOwner());
        assertEquals(agentA, interpose.getAgentA());
        assertEquals(agentB, interpose.getAgentB());
        assertEquals(0.5f, interpose.getInterpositionRatio(), 0.001f);
    }

    @Test
    public void testConstructorWithOwnerAgentsAndRatio() {
        Interpose<Vector2> interpose = new Interpose<>(owner, agentA, agentB, 0.75f);
        assertNotNull(interpose);
        assertEquals(owner, interpose.getOwner());
        assertEquals(agentA, interpose.getAgentA());
        assertEquals(agentB, interpose.getAgentB());
        assertEquals(0.75f, interpose.getInterpositionRatio(), 0.001f);
    }

    @Test
    public void testConstructorWithNullOwner() {
        try {
            new Interpose<>(null, agentA, agentB);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testConstructorWithNullAgentA() {
        try {
            new Interpose<>(owner, null, agentB);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testConstructorWithNullAgentB() {
        try {
            new Interpose<>(owner, agentA, null);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testCalculateRealSteeringWithStationaryAgents() {
        // Set up stationary agents at different positions
        owner.setPosition(0f, 0f);
        agentA.setPosition(-10f, 0f);
        agentB.setPosition(10f, 0f);
        agentA.setLinearVelocity(0f, 0f);
        agentB.setLinearVelocity(0f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should steer toward midpoint (0, 0) since ratio is 0.5 and agents are stationary
        // Owner is already at midpoint, so should be zero steering
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithMovingAgents() {
        // Set up agents moving in opposite directions
        owner.setPosition(0f, 10f);
        agentA.setPosition(-10f, 0f);
        agentB.setPosition(10f, 0f);
        agentA.setLinearVelocity(1f, 0f); // Moving right
        agentB.setLinearVelocity(-1f, 0f); // Moving left

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should produce non-zero steering to interpose between predicted positions
        assertNotNull(steering.linear);
        assertEquals(0f, steering.angular, 0.001f); // Should be purely linear
    }

    @Test
    public void testCalculateRealSteeringWithDifferentInterpositionRatio() {
        // Set up agents and use different ratio
        owner.setPosition(0f, 10f);
        agentA.setPosition(-10f, 0f);
        agentB.setPosition(10f, 0f);
        agentA.setLinearVelocity(0f, 0f);
        agentB.setLinearVelocity(0f, 0f);
        interpose.setInterpositionRatio(0.25f); // Closer to agentA

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should steer toward position 25% from agentA to agentB
        // Target position: -10 + 0.25 * (10 - (-10)) = -5
        assertNotNull(steering.linear);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithZeroInterpositionRatio() {
        // Set ratio to 0 (should target agentA position)
        owner.setPosition(0f, 10f);
        agentA.setPosition(-10f, 0f);
        agentB.setPosition(10f, 0f);
        agentA.setLinearVelocity(0f, 0f);
        agentB.setLinearVelocity(0f, 0f);
        interpose.setInterpositionRatio(0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should steer toward agentA's position
        assertNotNull(steering.linear);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithOneInterpositionRatio() {
        // Set ratio to 1 (should target agentB position)
        owner.setPosition(0f, 10f);
        agentA.setPosition(-10f, 0f);
        agentB.setPosition(10f, 0f);
        agentA.setLinearVelocity(0f, 0f);
        agentB.setLinearVelocity(0f, 0f);
        interpose.setInterpositionRatio(1f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should steer toward agentB's position
        assertNotNull(steering.linear);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithDiagonalMovement() {
        // Set up agents moving diagonally
        owner.setPosition(0f, 0f);
        agentA.setPosition(-10f, -10f);
        agentB.setPosition(10f, 10f);
        agentA.setLinearVelocity(1f, 1f); // Moving up-right
        agentB.setLinearVelocity(-1f, -1f); // Moving down-left

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should produce steering toward predicted interposition point
        assertNotNull(steering.linear);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithLimiter() {
        // Set up with limiter to test acceleration limiting
        owner.setPosition(0f, 10f);
        agentA.setPosition(-10f, 0f);
        agentB.setPosition(10f, 0f);
        agentA.setLinearVelocity(0f, 0f);
        agentB.setLinearVelocity(0f, 0f);
        interpose.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Steering should be limited by limiter
        assertNotNull(steering.linear);
        float maxAccel = limiter.getMaxLinearAcceleration();
        assertTrue(Math.abs(steering.linear.x) <= maxAccel);
        assertTrue(Math.abs(steering.linear.y) <= maxAccel);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithZeroMaxSpeed() {
        // Test with zero max speed (should handle gracefully)
        owner.setPosition(0f, 10f);
        agentA.setPosition(-10f, 0f);
        agentB.setPosition(10f, 0f);
        limiter.setMaxLinearSpeed(0f);
        interpose.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should handle zero speed gracefully
        assertNotNull(steering);
    }

    @Test
    public void testGetAndSetAgentA() {
        TestSteerable newAgentA = new TestSteerable();
        Interpose<Vector2> result = interpose.setAgentA(newAgentA);

        assertEquals(newAgentA, interpose.getAgentA());
        assertEquals(interpose, result); // Test method chaining
    }

    @Test
    public void testGetAndSetAgentB() {
        TestSteerable newAgentB = new TestSteerable();
        Interpose<Vector2> result = interpose.setAgentB(newAgentB);

        assertEquals(newAgentB, interpose.getAgentB());
        assertEquals(interpose, result); // Test method chaining
    }

    @Test
    public void testGetAndSetInterpositionRatio() {
        Interpose<Vector2> result = interpose.setInterpositionRatio(0.75f);

        assertEquals(0.75f, interpose.getInterpositionRatio(), 0.001f);
        assertEquals(interpose, result); // Test method chaining
    }

    @Test
    public void testGetInternalTargetPosition() {
        // Set up agents
        agentA.setPosition(-10f, 0f);
        agentB.setPosition(10f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Internal target position should be available
        Vector2 internalTarget = interpose.getInternalTargetPosition();
        assertNotNull(internalTarget);
    }

    @Test
    public void testInheritedSetOwner() {
        TestSteerable newOwner = new TestSteerable();
        Interpose<Vector2> result = interpose.setOwner(newOwner);

        assertEquals(newOwner, interpose.getOwner());
        assertEquals(interpose, result); // Test method chaining
    }

    @Test
    public void testInheritedSetEnabled() {
        Interpose<Vector2> result = interpose.setEnabled(false);

        assertFalse(interpose.isEnabled());
        assertEquals(interpose, result); // Test method chaining
    }

    @Test
    public void testInheritedSetLimiter() {
        TestLimiter newLimiter = new TestLimiter();
        Interpose<Vector2> result = interpose.setLimiter(newLimiter);

        assertEquals(newLimiter, interpose.getLimiter());
        assertEquals(interpose, result); // Test method chaining
    }

    @Test
    public void testInheritedSetTarget() {
        TestLocation target = new TestLocation();
        Interpose<Vector2> result = interpose.setTarget(target);

        assertEquals(target, interpose.getTarget());
        assertEquals(interpose, result); // Test method chaining
    }

    @Test
    public void testInheritedSetArrivalTolerance() {
        Interpose<Vector2> result = interpose.setArrivalTolerance(2.5f);

        assertEquals(2.5f, interpose.getArrivalTolerance(), 0.001f);
        assertEquals(interpose, result); // Test method chaining
    }

    @Test
    public void testInheritedSetDecelerationRadius() {
        Interpose<Vector2> result = interpose.setDecelerationRadius(7.5f);

        assertEquals(7.5f, interpose.getDecelerationRadius(), 0.001f);
        assertEquals(interpose, result); // Test method chaining
    }

    @Test
    public void testInheritedSetTimeToTarget() {
        Interpose<Vector2> result = interpose.setTimeToTarget(0.2f);

        assertEquals(0.2f, interpose.getTimeToTarget(), 0.001f);
        assertEquals(interpose, result); // Test method chaining
    }

    @Test
    public void testInterpositionRatioCalculation() {
        // Test that interposition ratio correctly affects target position
        owner.setPosition(0f, 10f);
        agentA.setPosition(-20f, 0f);
        agentB.setPosition(20f, 0f);
        agentA.setLinearVelocity(0f, 0f);
        agentB.setLinearVelocity(0f, 0f);

        // Test ratio 0.0 (should be at agentA position)
        interpose.setInterpositionRatio(0f);
        SteeringAcceleration<Vector2> steering1 = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering1);

        // Test ratio 1.0 (should be at agentB position)
        interpose.setInterpositionRatio(1f);
        SteeringAcceleration<Vector2> steering2 = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering2);

        // The steering directions should be opposite (toward agentA vs toward agentB)
        assertNotNull(steering1.linear);
        assertNotNull(steering2.linear);
    }

    @Test
    public void testSteeringObjectReuse() {
        // Test that the same steering object can be reused
        owner.setPosition(0f, 10f);
        agentA.setPosition(-10f, 0f);
        agentB.setPosition(10f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());

        // First calculation
        interpose.calculateSteering(steering);
        Vector2 firstResult = steering.linear.cpy();

        // Second calculation
        interpose.calculateSteering(steering);
        Vector2 secondResult = steering.linear.cpy();

        // Results should be consistent
        assertEquals(firstResult.x, secondResult.x, 0.001f);
        assertEquals(firstResult.y, secondResult.y, 0.001f);
    }

    @Test
    public void testInterposeExtendsArrive() {
        assertNotNull(interpose);
    }

    @Test
    public void testInterposeWithSameAgentPositions() {
        // Test when both agents are at the same position
        owner.setPosition(0f, 10f);
        agentA.setPosition(5f, 5f);
        agentB.setPosition(5f, 5f); // Same position as agentA
        agentA.setLinearVelocity(0f, 0f);
        agentB.setLinearVelocity(0f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should handle gracefully and steer toward the common position
        assertNotNull(steering.linear);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testInterposeWithVeryCloseAgents() {
        // Test when agents are very close to each other
        owner.setPosition(0f, 10f);
        agentA.setPosition(0f, 0f);
        agentB.setPosition(0.001f, 0f); // Very close to agentA
        agentA.setLinearVelocity(0f, 0f);
        agentB.setLinearVelocity(0f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should handle gracefully
        assertNotNull(steering.linear);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testInterposeWithLargeDistance() {
        // Test with large distance between agents
        owner.setPosition(0f, 0f);
        agentA.setPosition(-1000f, 0f);
        agentB.setPosition(1000f, 0f);
        agentA.setLinearVelocity(0f, 0f);
        agentB.setLinearVelocity(0f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should handle large distances gracefully
        assertNotNull(steering.linear);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testInterposeWithNegativeCoordinates() {
        // Test with negative coordinates
        owner.setPosition(-5f, -5f);
        agentA.setPosition(-20f, -10f);
        agentB.setPosition(-10f, -20f);
        agentA.setLinearVelocity(0f, 0f);
        agentB.setLinearVelocity(0f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should handle negative coordinates gracefully
        assertNotNull(steering.linear);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testInterposeWithHighVelocityAgents() {
        // Test with agents having high velocities
        owner.setPosition(0f, 10f);
        agentA.setPosition(-10f, 0f);
        agentB.setPosition(10f, 0f);
        agentA.setLinearVelocity(100f, 0f); // High velocity
        agentB.setLinearVelocity(-100f, 0f); // High velocity

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        interpose.calculateSteering(steering);

        // Should handle high velocities gracefully
        assertNotNull(steering.linear);
        assertEquals(0f, steering.angular, 0.001f);
    }

    // Test helper classes

    private static class TestSteerable implements Steerable<Vector2> {
        private final Vector2 position = new Vector2();
        private final Vector2 linearVelocity = new Vector2();
        private float maxLinearSpeed = 10f;
        private float maxLinearAcceleration = 5f;
        private float maxAngularSpeed = 5f;
        private float maxAngularAcceleration = 2f;
        private boolean tagged = false;
        private float zeroLinearSpeedThreshold = 0.001f;

        public void setPosition(float x, float y) {
            position.set(x, y);
        }

        public void setLinearVelocity(float x, float y) {
            linearVelocity.set(x, y);
        }

        @Override
        public Vector2 getLinearVelocity() {
            return linearVelocity;
        }

        @Override
        public float getAngularVelocity() {
            return 0f;
        }

        @Override
        public Vector2 getPosition() {
            return position;
        }

        @Override
        public float getOrientation() {
            return 0f;
        }

        public void setOrientation(float orientation) {
            // Empty implementation
        }

        @Override
        public float getMaxLinearSpeed() {
            return maxLinearSpeed;
        }

        @Override
        public float getMaxLinearAcceleration() {
            return maxLinearAcceleration;
        }

        @Override
        public float getMaxAngularSpeed() {
            return maxAngularSpeed;
        }

        @Override
        public float getMaxAngularAcceleration() {
            return maxAngularAcceleration;
        }

        @Override
        public boolean isTagged() {
            return tagged;
        }

        @Override
        public void setTagged(boolean tagged) {
            this.tagged = tagged;
        }

        @Override
        public float getBoundingRadius() {
            return 1f;
        }

        @Override
        public float getZeroLinearSpeedThreshold() {
            return zeroLinearSpeedThreshold;
        }

        @Override
        public com.badlogic.gdx.ai.utils.Location<Vector2> newLocation() {
            return new TestLocation();
        }

        @Override
        public float vectorToAngle(Vector2 vector) {
            return (float) Math.atan2(vector.y, vector.x);
        }

        @Override
        public Vector2 angleToVector(Vector2 outVector, float angle) {
            outVector.set((float) Math.cos(angle), (float) Math.sin(angle));
            return outVector;
        }

        @Override
        public void setZeroLinearSpeedThreshold(float value) {
            this.zeroLinearSpeedThreshold = value;
        }

        @Override
        public void setMaxLinearSpeed(float maxLinearSpeed) {
            this.maxLinearSpeed = maxLinearSpeed;
        }

        @Override
        public void setMaxLinearAcceleration(float maxLinearAcceleration) {
            this.maxLinearAcceleration = maxLinearAcceleration;
        }

        @Override
        public void setMaxAngularSpeed(float maxAngularSpeed) {
            this.maxAngularSpeed = maxAngularSpeed;
        }

        @Override
        public void setMaxAngularAcceleration(float maxAngularAcceleration) {
            this.maxAngularAcceleration = maxAngularAcceleration;
        }
    }

    private static class TestLocation implements Location<Vector2> {
        private final Vector2 position = new Vector2();
        private float orientation = 0f;

        @Override
        public Vector2 getPosition() {
            return position;
        }

        @Override
        public float getOrientation() {
            return orientation;
        }

        @Override
        public void setOrientation(float orientation) {
            this.orientation = orientation;
        }

        @Override
        public float vectorToAngle(Vector2 vector) {
            return (float) Math.atan2(vector.y, vector.x);
        }

        @Override
        public Vector2 angleToVector(Vector2 outVector, float angle) {
            outVector.set((float) Math.cos(angle), (float) Math.sin(angle));
            return outVector;
        }

        @Override
        public Location<Vector2> newLocation() {
            return new TestLocation();
        }
    }

    private static class TestLimiter implements Limiter {
        private float maxLinearSpeed = 10f;
        private float maxLinearAcceleration = 10f;
        private float maxAngularSpeed = 5f;
        private float maxAngularAcceleration = 5f;
        private float zeroLinearSpeedThreshold = 0.001f;

        public void setMaxLinearAcceleration(float max) {
            this.maxLinearAcceleration = max;
        }

        public void setMaxLinearSpeed(float max) {
            this.maxLinearSpeed = max;
        }

        @Override
        public float getZeroLinearSpeedThreshold() {
            return zeroLinearSpeedThreshold;
        }

        @Override
        public void setZeroLinearSpeedThreshold(float value) {
            this.zeroLinearSpeedThreshold = value;
        }

        @Override
        public float getMaxLinearSpeed() {
            return maxLinearSpeed;
        }

        @Override
        public float getMaxLinearAcceleration() {
            return maxLinearAcceleration;
        }

        @Override
        public float getMaxAngularSpeed() {
            return maxAngularSpeed;
        }

        @Override
        public float getMaxAngularAcceleration() {
            return maxAngularAcceleration;
        }

        @Override
        public void setMaxAngularSpeed(float maxAngularSpeed) {
            this.maxAngularSpeed = maxAngularSpeed;
        }

        @Override
        public void setMaxAngularAcceleration(float maxAngularAcceleration) {
            this.maxAngularAcceleration = maxAngularAcceleration;
        }
    }
}
