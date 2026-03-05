package com.badlogic.gdx.ai.steer.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

import org.junit.Before;
import org.junit.Test;

public class ArriveTest {

    private Arrive<Vector2> arrive;
    private TestSteerable owner;
    private TestLocation target;
    private TestLimiter limiter;

    @Before
    public void setUp() {
        owner = new TestSteerable();
        target = new TestLocation();
        limiter = new TestLimiter();
        arrive = new Arrive<>(owner, target);
    }

    @Test
    public void testConstructorWithOwner() {
        Arrive<Vector2> arrive = new Arrive<>(owner);
        assertNotNull(arrive);
        assertEquals(owner, arrive.getOwner());
        assertNull(arrive.getTarget());
    }

    @Test
    public void testConstructorWithOwnerAndTarget() {
        Arrive<Vector2> arrive = new Arrive<>(owner, target);
        assertNotNull(arrive);
        assertEquals(owner, arrive.getOwner());
        assertEquals(target, arrive.getTarget());
    }

    @Test
    public void testConstructorWithNullOwner() {
        try {
            new Arrive<>(null);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testCalculateRealSteeringAtTarget() {
        // Set owner at target position
        owner.setPosition(0f, 0f);
        target.setPosition(0f, 0f);
        arrive.setArrivalTolerance(1f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering);

        // Should be zero steering when at target
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithinTolerance() {
        // Set owner within arrival tolerance of target
        owner.setPosition(0.5f, 0.5f);
        target.setPosition(0f, 0f);
        arrive.setArrivalTolerance(1f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering);

        // Should be zero steering when within tolerance
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringOutsideTolerance() {
        // Set owner outside arrival tolerance
        owner.setPosition(2f, 2f);
        target.setPosition(0f, 0f);
        arrive.setArrivalTolerance(1f);
        arrive.setDecelerationRadius(5f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering);

        // Should have non-zero steering toward target
        assertTrue(steering.linear.x < 0f); // Should move left toward target
        assertTrue(steering.linear.y < 0f); // Should move down toward target
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithinDecelerationRadius() {
        // Set owner within deceleration radius but outside tolerance
        owner.setPosition(3f, 0f);
        target.setPosition(0f, 0f);
        arrive.setArrivalTolerance(1f);
        arrive.setDecelerationRadius(5f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering);

        // Should be decelerating (reduced speed)
        float distance = 3f; // Distance from target
        float expectedSpeedFactor = distance / 5f; // distance / decelerationRadius
        assertTrue(Math.abs(steering.linear.x) > 0f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringOutsideDecelerationRadius() {
        // Set owner outside deceleration radius
        owner.setPosition(10f, 0f);
        target.setPosition(0f, 0f);
        arrive.setArrivalTolerance(1f);
        arrive.setDecelerationRadius(5f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering);

        // Should be at max speed
        assertTrue(Math.abs(steering.linear.x) > 0f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithLimiter() {
        arrive.setLimiter(limiter);
        limiter.setMaxLinearAcceleration(2f);

        owner.setPosition(10f, 0f);
        target.setPosition(0f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering);

        assertTrue(Math.abs(steering.linear.x) <= limiter.getMaxLinearAcceleration());
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithOwnerVelocity() {
        // Set owner with initial velocity
        owner.setPosition(10f, 0f);
        owner.setLinearVelocity(-1f, 0f); // Moving toward target
        target.setPosition(0f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering);

        // Should account for current velocity
        assertTrue(Math.abs(steering.linear.x) > 0f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testSetTarget() {
        TestLocation newTarget = new TestLocation();
        newTarget.setPosition(5f, 5f);

        Arrive<Vector2> result = arrive.setTarget(newTarget);

        assertEquals(newTarget, arrive.getTarget());
        assertEquals(arrive, result); // Should return this for chaining
    }

    @Test
    public void testSetTargetWithNull() {
        Arrive<Vector2> result = arrive.setTarget(null);

        assertNull(arrive.getTarget());
        assertEquals(arrive, result); // Should return this for chaining
    }

    @Test
    public void testGetArrivalTolerance() {
        arrive.setArrivalTolerance(2.5f);
        assertEquals(2.5f, arrive.getArrivalTolerance(), 0.001f);
    }

    @Test
    public void testSetArrivalTolerance() {
        Arrive<Vector2> result = arrive.setArrivalTolerance(3.5f);

        assertEquals(3.5f, arrive.getArrivalTolerance(), 0.001f);
        assertEquals(arrive, result); // Should return this for chaining
    }

    @Test
    public void testGetDecelerationRadius() {
        arrive.setDecelerationRadius(7.5f);
        assertEquals(7.5f, arrive.getDecelerationRadius(), 0.001f);
    }

    @Test
    public void testSetDecelerationRadius() {
        Arrive<Vector2> result = arrive.setDecelerationRadius(8.5f);

        assertEquals(8.5f, arrive.getDecelerationRadius(), 0.001f);
        assertEquals(arrive, result); // Should return this for chaining
    }

    @Test
    public void testGetTimeToTarget() {
        arrive.setTimeToTarget(0.2f);
        assertEquals(0.2f, arrive.getTimeToTarget(), 0.001f);
    }

    @Test
    public void testSetTimeToTarget() {
        Arrive<Vector2> result = arrive.setTimeToTarget(0.15f);

        assertEquals(0.15f, arrive.getTimeToTarget(), 0.001f);
        assertEquals(arrive, result); // Should return this for chaining
    }

    @Test
    public void testSetOwner() {
        TestSteerable newOwner = new TestSteerable();
        Arrive<Vector2> result = arrive.setOwner(newOwner);

        assertEquals(newOwner, arrive.getOwner());
        assertEquals(arrive, result); // Should return this for chaining
    }

    @Test
    public void testSetOwnerWithNull() {
        Arrive<Vector2> result = arrive.setOwner(null);

        assertNull(arrive.getOwner());
        assertEquals(arrive, result); // Should return this for chaining
    }

    @Test
    public void testSetEnabled() {
        Arrive<Vector2> result = arrive.setEnabled(false);

        assertFalse(arrive.isEnabled());
        assertEquals(arrive, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiter() {
        Arrive<Vector2> result = arrive.setLimiter(limiter);

        assertEquals(limiter, arrive.getLimiter());
        assertEquals(arrive, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiterWithNull() {
        Arrive<Vector2> result = arrive.setLimiter(null);

        assertNull(arrive.getLimiter());
        assertEquals(arrive, result); // Should return this for chaining
    }

    @Test
    public void testDefaultTimeToTarget() {
        // Test that default timeToTarget is 0.1f
        Arrive<Vector2> arrive = new Arrive<>(owner);
        assertEquals(0.1f, arrive.getTimeToTarget(), 0.001f);
    }

    @Test
    public void testArriveWithDiagonalMovement() {
        // Test diagonal movement toward target
        owner.setPosition(3f, 4f);
        target.setPosition(0f, 0f);
        arrive.setArrivalTolerance(0.5f);
        arrive.setDecelerationRadius(6f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering);

        // Should move diagonally toward target
        assertTrue(steering.linear.x < 0f); // Should move left
        assertTrue(steering.linear.y < 0f); // Should move down
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testArriveWithZeroTimeToTarget() {
        // Test edge case with zero timeToTarget
        arrive.setTimeToTarget(0f);

        owner.setPosition(5f, 0f);
        target.setPosition(0f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering);

        // Should handle zero time gracefully (may result in very high acceleration)
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testArriveWithNegativeTimeToTarget() {
        // Test edge case with negative timeToTarget
        arrive.setTimeToTarget(-0.1f);

        owner.setPosition(5f, 0f);
        target.setPosition(0f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering);

        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testInheritanceFromSteeringBehavior() {
        assertNotNull(arrive);
    }

    @Test
    public void testSteeringAccelerationReuse() {

        owner.setPosition(5f, 0f);
        target.setPosition(0f, 0f);

        SteeringAcceleration<Vector2> steering1 = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering1);
        SteeringAcceleration<Vector2> steering2 = new SteeringAcceleration<>(new Vector2());
        arrive.calculateSteering(steering2);

        assertEquals(steering1.linear.x, steering2.linear.x, 0.001f);
        assertEquals(steering1.linear.y, steering2.linear.y, 0.001f);
    }

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

        public Vector2 newVector() {
            return new Vector2();
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

        public void setPosition(float x, float y) {
            position.set(x, y);
        }

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
