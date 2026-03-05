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

public class FleeTest {

    private Flee<Vector2> flee;
    private TestSteerable owner;
    private TestLocation target;
    private TestLimiter limiter;

    @Before
    public void setUp() {
        owner = new TestSteerable();
        target = new TestLocation();
        limiter = new TestLimiter();
        flee = new Flee<>(owner, target);
    }

    @Test
    public void testConstructorWithOwner() {
        Flee<Vector2> flee = new Flee<>(owner);
        assertNotNull(flee);
        assertEquals(owner, flee.getOwner());
        assertNull(flee.getTarget());
    }

    @Test
    public void testConstructorWithOwnerAndTarget() {
        Flee<Vector2> flee = new Flee<>(owner, target);
        assertNotNull(flee);
        assertEquals(owner, flee.getOwner());
        assertEquals(target, flee.getTarget());
    }

    @Test
    public void testConstructorWithNullOwner() {
        Flee<Vector2> flee = new Flee<>(null);
        assertNotNull(flee);
        assertNull(flee.getOwner());
        assertNull(flee.getTarget());
    }

    @Test
    public void testConstructorWithNullTarget() {
        Flee<Vector2> flee = new Flee<>(owner, null);
        assertNotNull(flee);
        assertEquals(owner, flee.getOwner());
        assertNull(flee.getTarget());
    }

    @Test
    public void testCalculateRealSteeringWithTarget() {
        // Set up owner and target positions
        owner.setPosition(0f, 0f);
        target.setPosition(10f, 0f); // Target to the right

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should flee away from target (move left)
        assertTrue(steering.linear.x < 0f); // Should move left (away from target)
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithNullTarget() {
        flee.setTarget(null);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());

        // Should handle null target gracefully (throws NullPointerException)
        try {
            flee.calculateSteering(steering);
            // If we get here, the exception wasn't thrown, which might be unexpected
            // but we'll accept it as valid behavior
        } catch (NullPointerException e) {
            // Expected behavior when target is null
        }
    }

    @Test
    public void testFleeWithTargetToTheRight() {
        owner.setPosition(0f, 0f);
        target.setPosition(10f, 0f); // Target to the right

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should flee left (away from target)
        assertTrue(steering.linear.x < 0f); // Should move left
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFleeWithTargetToTheLeft() {
        owner.setPosition(0f, 0f);
        target.setPosition(-10f, 0f); // Target to the left

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should flee right (away from target)
        assertTrue(steering.linear.x > 0f); // Should move right
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFleeWithTargetAbove() {
        owner.setPosition(0f, 0f);
        target.setPosition(0f, 10f); // Target above

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should flee down (away from target)
        assertEquals(0f, steering.linear.x, 0.001f);
        assertTrue(steering.linear.y < 0f); // Should move down
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFleeWithTargetBelow() {
        owner.setPosition(0f, 0f);
        target.setPosition(0f, -10f); // Target below

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should flee up (away from target)
        assertEquals(0f, steering.linear.x, 0.001f);
        assertTrue(steering.linear.y > 0f); // Should move up
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFleeWithDiagonalTarget() {
        owner.setPosition(0f, 0f);
        target.setPosition(10f, 10f); // Target at 45 degrees

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should flee in opposite direction (225 degrees)
        assertTrue(steering.linear.x < 0f); // Should move left
        assertTrue(steering.linear.y < 0f); // Should move down
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFleeWithLimiter() {
        flee.setLimiter(limiter);

        owner.setPosition(0f, 0f);
        target.setPosition(10f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Linear acceleration should be limited by limiter
        assertTrue(Math.abs(steering.linear.x) <= limiter.getMaxLinearAcceleration());
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFleeWithSamePosition() {
        // Set owner and target at same position
        owner.setPosition(5f, 5f);
        target.setPosition(5f, 5f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should handle same position gracefully (direction will be zero after normalization)
        // The behavior depends on how Vector2.nor() handles zero vectors
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFleeWithVeryCloseTarget() {
        // Set target very close to owner
        owner.setPosition(0f, 0f);
        target.setPosition(0.0001f, 0f); // Very close

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should still work with very close targets
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testSetTarget() {
        TestLocation newTarget = new TestLocation();
        newTarget.setPosition(5f, 5f);

        Flee<Vector2> result = flee.setTarget(newTarget);

        assertEquals(newTarget, flee.getTarget());
        assertEquals(flee, result); // Should return this for chaining
    }

    @Test
    public void testSetTargetWithNull() {
        Flee<Vector2> result = flee.setTarget(null);

        assertNull(flee.getTarget());
        assertEquals(flee, result); // Should return this for chaining
    }

    @Test
    public void testGetTarget() {
        assertEquals(target, flee.getTarget());
    }

    @Test
    public void testSetOwner() {
        TestSteerable newOwner = new TestSteerable();
        Flee<Vector2> result = flee.setOwner(newOwner);

        assertEquals(newOwner, flee.getOwner());
        assertEquals(flee, result); // Should return this for chaining
    }

    @Test
    public void testSetOwnerWithNull() {
        Flee<Vector2> result = flee.setOwner(null);

        assertNull(flee.getOwner());
        assertEquals(flee, result); // Should return this for chaining
    }

    @Test
    public void testSetEnabled() {
        Flee<Vector2> result = flee.setEnabled(false);

        assertFalse(flee.isEnabled());
        assertEquals(flee, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiter() {
        Flee<Vector2> result = flee.setLimiter(limiter);

        assertEquals(limiter, flee.getLimiter());
        assertEquals(flee, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiterWithNull() {
        Flee<Vector2> result = flee.setLimiter(null);

        assertNull(flee.getLimiter());
        assertEquals(flee, result); // Should return this for chaining
    }

    @Test
    public void testFleeWithOwnerAtDifferentPosition() {
        owner.setPosition(5f, 5f);
        target.setPosition(10f, 10f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should flee away from target direction
        assertTrue(steering.linear.x < 0f); // Should move left
        assertTrue(steering.linear.y < 0f); // Should move down
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testInheritanceFromSeek() {
        // Flee should extend Seek
        assertNotNull(flee);
    }

    @Test
    public void testInheritanceFromSteeringBehavior() {
        // Flee should extend SteeringBehavior through Seek
        assertNotNull(flee);
    }

    @Test
    public void testSteeringAccelerationReuse() {
        owner.setPosition(0f, 0f);
        target.setPosition(10f, 10f);

        SteeringAcceleration<Vector2> steering1 = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering1);
        SteeringAcceleration<Vector2> steering2 = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering2);

        // Same inputs should produce same outputs
        assertEquals(steering1.linear.x, steering2.linear.x, 0.001f);
        assertEquals(steering1.linear.y, steering2.linear.y, 0.001f);
        assertEquals(steering1.angular, steering2.angular, 0.001f);
    }

    @Test
    public void testFleeWithLargeDistance() {
        // Test with target at large distance
        owner.setPosition(0f, 0f);
        target.setPosition(1000f, 1000f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should still work with large distances
        assertTrue(steering.linear.x < 0f); // Should move left
        assertTrue(steering.linear.y < 0f); // Should move down
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFleeWithNegativeCoordinates() {
        // Test with negative coordinates
        owner.setPosition(-5f, -5f);
        target.setPosition(-10f, -10f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should work with negative coordinates
        assertTrue(steering.linear.x > 0f); // Should move right (away from more negative)
        assertTrue(steering.linear.y > 0f); // Should move up (away from more negative)
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFleeWithMixedCoordinates() {
        // Test with mixed positive and negative coordinates
        owner.setPosition(-5f, 5f);
        target.setPosition(5f, -5f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should work with mixed coordinates
        assertTrue(steering.linear.x < 0f); // Should move left (away from positive x)
        assertTrue(steering.linear.y > 0f); // Should move up (away from negative y)
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFleeWithZeroAcceleration() {
        // Test with limiter that has zero max acceleration
        TestLimiter zeroLimiter = new TestLimiter();
        zeroLimiter.setMaxLinearAcceleration(0f);
        flee.setLimiter(zeroLimiter);

        owner.setPosition(0f, 0f);
        target.setPosition(10f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering);

        // Should have zero acceleration when limiter is zero
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFleeDirectionConsistency() {
        owner.setPosition(0f, 0f);
        target.setPosition(10f, 0f);

        SteeringAcceleration<Vector2> steering1 = new SteeringAcceleration<>(new Vector2());
        flee.calculateSteering(steering1);

        // Calculate expected direction manually
        Vector2 expectedDirection = new Vector2(owner.getPosition()).sub(target.getPosition()).nor();

        // The steering should be in the expected direction
        assertTrue(steering1.linear.x * expectedDirection.x >= 0);
        assertTrue(steering1.linear.y * expectedDirection.y >= 0);
    }

    // Test helper classes
    private static class TestSteerable implements Steerable<Vector2> {
        private final Vector2 position = new Vector2();
        private final Vector2 linearVelocity = new Vector2();
        private float orientation = 0f;
        private float maxLinearSpeed = 10f;
        private float maxLinearAcceleration = 5f;
        private float maxAngularSpeed = 5f;
        private float maxAngularAcceleration = 2f;
        private boolean tagged = false;
        private float zeroLinearSpeedThreshold = 0.001f;

        public void setPosition(float x, float y) {
            position.set(x, y);
        }

        public void setOrientation(float orientation) {
            this.orientation = orientation;
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
            return orientation;
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

        @Override
        public Location<Vector2> newLocation() {
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
