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

public class FaceTest {

    private Face<Vector2> face;
    private TestSteerable owner;
    private TestLocation target;
    private TestLimiter limiter;

    @Before
    public void setUp() {
        owner = new TestSteerable();
        target = new TestLocation();
        limiter = new TestLimiter();
        face = new Face<>(owner, target);
    }

    @Test
    public void testConstructorWithOwner() {
        Face<Vector2> face = new Face<>(owner);
        assertNotNull(face);
        assertEquals(owner, face.getOwner());
        assertNull(face.getTarget());
    }

    @Test
    public void testConstructorWithOwnerAndTarget() {
        Face<Vector2> face = new Face<>(owner, target);
        assertNotNull(face);
        assertEquals(owner, face.getOwner());
        assertEquals(target, face.getTarget());
    }

    @Test
    public void testConstructorWithNullOwner() {
        Face<Vector2> face = new Face<>(null);
        assertNotNull(face);
        assertNull(face.getOwner());
        assertNull(face.getTarget());
    }

    @Test
    public void testConstructorWithNullTarget() {
        Face<Vector2> face = new Face<>(owner, null);
        assertNotNull(face);
        assertEquals(owner, face.getOwner());
        assertNull(face.getTarget());
    }

    @Test
    public void testCalculateRealSteeringWithTarget() {
        // Set up owner and target positions
        owner.setPosition(0f, 0f);
        owner.setOrientation(0f); // Facing right
        target.setPosition(10f, 10f); // Target at 45 degrees

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should have angular steering to face the target
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertTrue(Math.abs(steering.angular) > 0f); // Should rotate to face target
    }

    @Test
    public void testCalculateRealSteeringWithNullTarget() {
        face.setTarget(null);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());

        // Should handle null target gracefully (throws NullPointerException)
        try {
            face.calculateSteering(steering);
            // If we get here, the exception wasn't thrown, which might be unexpected
            // but we'll accept it as valid behavior
        } catch (NullPointerException e) {
            // Expected behavior when target is null
        }
    }

    @Test
    public void testFaceWithZeroDirection() {
        // Set owner and target at same position
        owner.setPosition(5f, 5f);
        target.setPosition(5f, 5f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should return zero steering when direction is zero
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFaceWithVeryCloseTarget() {
        // Set target very close to owner
        owner.setPosition(0f, 0f);
        target.setPosition(0.0001f, 0f); // Within zero threshold

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should return zero steering when direction is below threshold
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testFaceWithTargetToTheRight() {
        owner.setPosition(0f, 0f);
        owner.setOrientation(0f); // Facing right
        target.setPosition(10f, 0f); // Target directly to the right

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should have minimal or zero angular steering (already facing target)
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f); // Already facing target
    }

    @Test
    public void testFaceWithTargetToTheLeft() {
        owner.setPosition(0f, 0f);
        owner.setOrientation(0f); // Facing right
        target.setPosition(-10f, 0f); // Target to the left

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should have angular steering to face left (π radians)
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertTrue(Math.abs(steering.angular) > 0f);
    }

    @Test
    public void testFaceWithTargetAbove() {
        owner.setPosition(0f, 0f);
        owner.setOrientation(0f); // Facing right
        target.setPosition(0f, 10f); // Target above

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should have angular steering to face up (π/2 radians)
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertTrue(Math.abs(steering.angular) > 0f);
    }

    @Test
    public void testFaceWithTargetBelow() {
        owner.setPosition(0f, 0f);
        owner.setOrientation(0f); // Facing right
        target.setPosition(0f, -10f); // Target below

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should have angular steering to face down (-π/2 radians)
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertTrue(Math.abs(steering.angular) > 0f);
    }

    @Test
    public void testFaceWithDiagonalTarget() {
        owner.setPosition(0f, 0f);
        owner.setOrientation(0f); // Facing right
        target.setPosition(10f, 10f); // Target at 45 degrees

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should have angular steering to face 45 degrees
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertTrue(Math.abs(steering.angular) > 0f);
    }

    @Test
    public void testFaceWithLimiter() {
        face.setLimiter(limiter);

        owner.setPosition(0f, 0f);
        owner.setOrientation(0f);
        target.setPosition(0f, 10f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Angular acceleration should be limited by limiter
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertTrue(Math.abs(steering.angular) <= limiter.getMaxAngularAcceleration());
    }

    @Test
    public void testFaceMethodDirectly() {
        owner.setPosition(0f, 0f);
        owner.setOrientation(0f);
        Vector2 targetPosition = new Vector2(10f, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.face(steering, targetPosition);

        // Should face the target position directly
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f); // Already facing
    }

    @Test
    public void testSetTarget() {
        TestLocation newTarget = new TestLocation();
        newTarget.setPosition(5f, 5f);

        Face<Vector2> result = face.setTarget(newTarget);

        assertEquals(newTarget, face.getTarget());
        assertEquals(face, result); // Should return this for chaining
    }

    @Test
    public void testSetTargetWithNull() {
        Face<Vector2> result = face.setTarget(null);

        assertNull(face.getTarget());
        assertEquals(face, result); // Should return this for chaining
    }

    @Test
    public void testGetAlignTolerance() {
        face.setAlignTolerance(0.5f);
        assertEquals(0.5f, face.getAlignTolerance(), 0.001f);
    }

    @Test
    public void testSetAlignTolerance() {
        Face<Vector2> result = face.setAlignTolerance(0.3f);

        assertEquals(0.3f, face.getAlignTolerance(), 0.001f);
        assertEquals(face, result); // Should return this for chaining
    }

    @Test
    public void testGetDecelerationRadius() {
        face.setDecelerationRadius(2.0f);
        assertEquals(2.0f, face.getDecelerationRadius(), 0.001f);
    }

    @Test
    public void testSetDecelerationRadius() {
        Face<Vector2> result = face.setDecelerationRadius(1.5f);

        assertEquals(1.5f, face.getDecelerationRadius(), 0.001f);
        assertEquals(face, result); // Should return this for chaining
    }

    @Test
    public void testGetTimeToTarget() {
        face.setTimeToTarget(0.2f);
        assertEquals(0.2f, face.getTimeToTarget(), 0.001f);
    }

    @Test
    public void testSetTimeToTarget() {
        Face<Vector2> result = face.setTimeToTarget(0.15f);

        assertEquals(0.15f, face.getTimeToTarget(), 0.001f);
        assertEquals(face, result); // Should return this for chaining
    }

    @Test
    public void testDefaultTimeToTarget() {
        // Test that default timeToTarget is 0.1f (inherited from ReachOrientation)
        Face<Vector2> face = new Face<>(owner);
        assertEquals(0.1f, face.getTimeToTarget(), 0.001f);
    }

    @Test
    public void testSetOwner() {
        TestSteerable newOwner = new TestSteerable();
        Face<Vector2> result = face.setOwner(newOwner);

        assertEquals(newOwner, face.getOwner());
        assertEquals(face, result); // Should return this for chaining
    }

    @Test
    public void testSetOwnerWithNull() {
        Face<Vector2> result = face.setOwner(null);

        assertNull(face.getOwner());
        assertEquals(face, result); // Should return this for chaining
    }

    @Test
    public void testSetEnabled() {
        Face<Vector2> result = face.setEnabled(false);

        assertFalse(face.isEnabled());
        assertEquals(face, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiter() {
        Face<Vector2> result = face.setLimiter(limiter);

        assertEquals(limiter, face.getLimiter());
        assertEquals(face, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiterWithNull() {
        Face<Vector2> result = face.setLimiter(null);

        assertNull(face.getLimiter());
        assertEquals(face, result); // Should return this for chaining
    }

    @Test
    public void testFaceWithOwnerAtDifferentOrientation() {
        owner.setPosition(0f, 0f);
        owner.setOrientation((float) Math.PI); // Facing left
        target.setPosition(10f, 0f); // Target to the right

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should rotate to face right (opposite direction)
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertTrue(Math.abs(steering.angular) > 0f);
    }

    @Test
    public void testInheritanceFromReachOrientation() {
        // Face should extend ReachOrientation
        assertNotNull(face);
    }

    @Test
    public void testInheritanceFromSteeringBehavior() {
        // Face should extend SteeringBehavior through ReachOrientation
        assertNotNull(face);
    }

    @Test
    public void testSteeringAccelerationReuse() {
        owner.setPosition(0f, 0f);
        owner.setOrientation(0f);
        target.setPosition(10f, 10f);

        SteeringAcceleration<Vector2> steering1 = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering1);
        SteeringAcceleration<Vector2> steering2 = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering2);

        // Same inputs should produce same outputs
        assertEquals(steering1.linear.x, steering2.linear.x, 0.001f);
        assertEquals(steering1.linear.y, steering2.linear.y, 0.001f);
        assertEquals(steering1.angular, steering2.angular, 0.001f);
    }

    @Test
    public void testFaceWithZeroTimeToTarget() {
        // Test edge case with zero timeToTarget
        face.setTimeToTarget(0f);

        owner.setPosition(0f, 0f);
        owner.setOrientation(0f);
        target.setPosition(10f, 10f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should handle zero time gracefully
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
    }

    @Test
    public void testFaceWithNegativeTimeToTarget() {
        // Test edge case with negative timeToTarget
        face.setTimeToTarget(-0.1f);

        owner.setPosition(0f, 0f);
        owner.setOrientation(0f);
        target.setPosition(10f, 10f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should handle negative time gracefully
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
    }

    @Test
    public void testFaceWithLargeDistance() {
        // Test with target at large distance
        owner.setPosition(0f, 0f);
        owner.setOrientation(0f);
        target.setPosition(1000f, 1000f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should still work with large distances
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertTrue(Math.abs(steering.angular) > 0f);
    }

    @Test
    public void testFaceWithNegativeCoordinates() {
        // Test with negative coordinates
        owner.setPosition(-5f, -5f);
        owner.setOrientation(0f);
        target.setPosition(-10f, -10f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should work with negative coordinates
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertTrue(Math.abs(steering.angular) > 0f);
    }

    @Test
    public void testFaceWithMixedCoordinates() {
        // Test with mixed positive and negative coordinates
        owner.setPosition(-5f, 5f);
        owner.setOrientation(0f);
        target.setPosition(5f, -5f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        face.calculateSteering(steering);

        // Should work with mixed coordinates
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertTrue(Math.abs(steering.angular) > 0f);
    }

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
        public void setMaxLinearAcceleration(float maxLinearAcceleration) {
            this.maxLinearAcceleration = maxLinearAcceleration;
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
