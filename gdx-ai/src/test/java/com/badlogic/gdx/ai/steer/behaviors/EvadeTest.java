package com.badlogic.gdx.ai.steer.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector2;

import org.junit.Before;
import org.junit.Test;

public class EvadeTest {

    private Evade<Vector2> evade;
    private TestSteerable owner;
    private TestSteerable target;
    private TestLimiter limiter;

    @Before
    public void setUp() {
        owner = new TestSteerable();
        target = new TestSteerable();
        limiter = new TestLimiter();
        evade = new Evade<>(owner, target);
    }

    @Test
    public void testConstructor() {
        assertNotNull(evade);
        assertEquals(owner, evade.getOwner());
        assertEquals(target, evade.getTarget());
        assertEquals(1.0f, evade.getMaxPredictionTime(), 0.001f);
    }

    @Test
    public void testConstructorWithPredictionTime() {
        Evade<Vector2> evadeWithPrediction = new Evade<>(owner, target, 2.5f);
        assertNotNull(evadeWithPrediction);
        assertEquals(owner, evadeWithPrediction.getOwner());
        assertEquals(target, evadeWithPrediction.getTarget());
        assertEquals(2.5f, evadeWithPrediction.getMaxPredictionTime(), 0.001f);
    }

    @Test
    public void testConstructorWithNullOwner() {
        try {
            new Evade<>(null, target);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testConstructorWithNullTarget() {
        try {
            new Evade<>(owner, null);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testGetActualMaxLinearAcceleration() {
        evade.setLimiter(limiter);
        assertEquals(-limiter.getMaxLinearAcceleration(), evade.getActualMaxLinearAcceleration(), 0.001f);
    }

    @Test
    public void testGetActualMaxLinearAccelerationWithNullLimiter() {
        evade.setLimiter(null);
        assertEquals(-owner.getMaxLinearAcceleration(), evade.getActualMaxLinearAcceleration(), 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithStationaryTarget() {
        owner.setPosition(0, 0);
        target.setPosition(10, 0);
        target.setLinearVelocity(0, 0);
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Should flee directly away from target (negative of pursue direction)
        assertEquals(-10f, steering.linear.x, 0.001f); // Negative of pursue (would be +10f)
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithMovingTarget() {
        owner.setPosition(0, 0);
        target.setPosition(10, 0);
        target.setLinearVelocity(1, 0); // Moving away from owner
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Should predict target position and flee from it
        assertTrue(steering.linear.x < 0); // Should flee left (negative)
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithTargetMovingTowardsOwner() {
        owner.setPosition(0, 0);
        target.setPosition(10, 0);
        target.setLinearVelocity(-2, 0); // Moving towards owner
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Should predict target position and flee from it
        assertTrue(steering.linear.x < 0); // Should flee left (negative)
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithDiagonalTargetMovement() {
        owner.setPosition(0, 0);
        target.setPosition(10, 10);
        target.setLinearVelocity(1, 1);
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Should flee diagonally away from predicted position
        assertTrue(steering.linear.x < 0); // Should flee left
        assertTrue(steering.linear.y < 0); // Should flee down
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithZeroOwnerSpeed() {
        owner.setPosition(0, 0);
        owner.setLinearVelocity(0, 0);
        target.setPosition(10, 0);
        target.setLinearVelocity(1, 0);
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Should use max prediction time when owner speed is zero
        assertTrue(steering.linear.x < 0); // Should flee left
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithHighOwnerSpeed() {
        owner.setPosition(0, 0);
        owner.setLinearVelocity(100, 0); // Very fast owner
        target.setPosition(10, 0);
        target.setLinearVelocity(1, 0);
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Should use reduced prediction time due to high owner speed
        assertTrue(steering.linear.x < 0); // Should flee left
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithPredictionTimeLimit() {
        owner.setPosition(0, 0);
        owner.setLinearVelocity(1, 0);
        target.setPosition(1000, 0); // Very far away
        target.setLinearVelocity(1, 0);
        evade.setMaxPredictionTime(0.5f); // Small prediction time
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Should use max prediction time limit
        assertTrue(steering.linear.x < 0); // Should flee left
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testSetOwner() {
        TestSteerable newOwner = new TestSteerable();
        Evade<Vector2> result = evade.setOwner(newOwner);
        assertEquals(newOwner, evade.getOwner());
        assertEquals(evade, result); // Method chaining
    }

    @Test
    public void testSetEnabled() {
        Evade<Vector2> result = evade.setEnabled(false);
        assertFalse(evade.isEnabled());
        assertEquals(evade, result); // Method chaining

        result = evade.setEnabled(true);
        assertTrue(evade.isEnabled());
        assertEquals(evade, result); // Method chaining
    }

    @Test
    public void testSetLimiter() {
        Evade<Vector2> result = evade.setLimiter(limiter);
        assertEquals(limiter, evade.getLimiter());
        assertEquals(evade, result); // Method chaining
    }

    @Test
    public void testSetTarget() {
        TestSteerable newTarget = new TestSteerable();
        Evade<Vector2> result = evade.setTarget(newTarget);
        assertEquals(newTarget, evade.getTarget());
        assertEquals(evade, result); // Method chaining
    }

    @Test
    public void testSetMaxPredictionTime() {
        Pursue<Vector2> result = evade.setMaxPredictionTime(3.0f);
        assertEquals(3.0f, evade.getMaxPredictionTime(), 0.001f);
        assertEquals(evade, result); // Method chaining returns same object
    }

    @Test
    public void testInheritanceFromPursue() {
        assertNotNull(evade);
    }

    @Test
    public void testEvadeVsPursueDirection() {
        owner.setPosition(0, 0);
        target.setPosition(10, 0);
        target.setLinearVelocity(0, 0);
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> evadeSteering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Create a pursue behavior for comparison
        Pursue<Vector2> pursue = new Pursue<>(owner, target);
        pursue.setLimiter(limiter);
        SteeringAcceleration<Vector2> pursueSteering = pursue.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Evade should be opposite direction of pursue
        assertEquals(evadeSteering.linear.x, -pursueSteering.linear.x, 0.001f);
        assertEquals(evadeSteering.linear.y, -pursueSteering.linear.y, 0.001f);
        assertEquals(evadeSteering.angular, pursueSteering.angular, 0.001f);
    }

    @Test
    public void testSteeringAccelerationObjectReuse() {
        owner.setPosition(0, 0);
        target.setPosition(10, 0);
        target.setLinearVelocity(0, 0);
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        SteeringAcceleration<Vector2> result1 = evade.calculateRealSteering(steering);
        SteeringAcceleration<Vector2> result2 = evade.calculateRealSteering(steering);

        // Should return the same object (reuse)
        assertEquals(steering, result1);
        assertEquals(steering, result2);
    }

    @Test
    public void testCalculateRealSteeringWithVeryCloseTarget() {
        owner.setPosition(0, 0);
        target.setPosition(0.1f, 0); // Very close
        target.setLinearVelocity(1, 0);
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Should still flee from predicted position
        assertTrue(Math.abs(steering.linear.x) > 0.001f); // Should have non-zero steering
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithZeroMaxPredictionTime() {
        owner.setPosition(0, 0);
        target.setPosition(10, 0);
        target.setLinearVelocity(1, 0);
        evade.setMaxPredictionTime(0);
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Should flee from current position (no prediction)
        assertTrue(steering.linear.x < 0); // Should flee left
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithNegativeMaxPredictionTime() {
        owner.setPosition(0, 0);
        target.setPosition(10, 0);
        target.setLinearVelocity(1, 0);
        evade.setMaxPredictionTime(-1);
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Should handle negative prediction time gracefully
        assertTrue(Math.abs(steering.linear.x) > 0.001f); // Should have non-zero steering
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithTargetAtSamePosition() {
        owner.setPosition(0, 0);
        target.setPosition(0, 0); // Same position
        target.setLinearVelocity(1, 0);
        evade.setLimiter(limiter);

        SteeringAcceleration<Vector2> steering = evade.calculateRealSteering(new SteeringAcceleration<>(new Vector2()));

        // Should flee from predicted position
        assertTrue(steering.linear.x < 0); // Should flee left
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    private static class TestSteerable implements Steerable<Vector2> {
        private final Vector2 position = new Vector2();
        private final Vector2 linearVelocity = new Vector2();
        private boolean tagged = false;

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
            return 10f;
        }

        @Override
        public float getMaxLinearAcceleration() {
            return 5f;
        }

        @Override
        public float getMaxAngularSpeed() {
            return 5f;
        }

        @Override
        public float getMaxAngularAcceleration() {
            return 2f;
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
            return 0.001f;
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

        // Limiter interface methods
        @Override
        public void setZeroLinearSpeedThreshold(float value) {
            // Empty implementation
        }

        @Override
        public void setMaxLinearSpeed(float maxLinearSpeed) {
            // Empty implementation
        }

        @Override
        public void setMaxLinearAcceleration(float maxLinearAcceleration) {
            // Empty implementation
        }

        @Override
        public void setMaxAngularSpeed(float maxAngularSpeed) {
            // Empty implementation
        }

        @Override
        public void setMaxAngularAcceleration(float maxAngularAcceleration) {
            // Empty implementation
        }
    }

    private static class TestLocation implements com.badlogic.gdx.ai.utils.Location<Vector2> {
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
        public com.badlogic.gdx.ai.utils.Location<Vector2> newLocation() {
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
        public void setMaxLinearSpeed(float maxLinearSpeed) {
            this.maxLinearSpeed = maxLinearSpeed;
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
        public void setMaxAngularSpeed(float maxAngularSpeed) {
            this.maxAngularSpeed = maxAngularSpeed;
        }

        @Override
        public float getMaxAngularAcceleration() {
            return maxAngularAcceleration;
        }

        @Override
        public void setMaxAngularAcceleration(float maxAngularAcceleration) {
            this.maxAngularAcceleration = maxAngularAcceleration;
        }
    }
}
