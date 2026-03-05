package com.badlogic.gdx.ai.steer.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.math.Vector2;

import org.junit.Before;
import org.junit.Test;

public class BlendedSteeringTest {

    private BlendedSteering<Vector2> blendedSteering;
    private TestSteerable owner;
    private TestLimiter limiter;

    @Before
    public void setUp() {
        owner = new TestSteerable();
        limiter = new TestLimiter();
        blendedSteering = new BlendedSteering<>(owner);
    }

    @Test
    public void testConstructor() {
        assertNotNull(blendedSteering);
        assertEquals(owner, blendedSteering.getOwner());
    }

    @Test
    public void testConstructorWithNullOwner() {
        try {
            new BlendedSteering<>(null);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testCalculateRealSteeringWithEmptyList() {
        // Test with no behaviors added
        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        blendedSteering.calculateSteering(steering);

        // Should be zero steering when no behaviors
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithSingleBehavior() {
        // Add one behavior with weight
        TestSteeringBehavior behavior1 = new TestSteeringBehavior();
        behavior1.setDesiredSteering(2f, 1f, 0.5f);

        blendedSteering.add(behavior1, 1f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        blendedSteering.calculateSteering(steering);

        // Should equal single behavior's steering
        assertEquals(2f, steering.linear.x, 0.001f);
        assertEquals(1f, steering.linear.y, 0.001f);
        assertEquals(0.5f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithMultipleBehaviors() {
        // Add multiple behaviors with different weights
        TestSteeringBehavior behavior1 = new TestSteeringBehavior();
        behavior1.setDesiredSteering(2f, 0f, 0f);

        TestSteeringBehavior behavior2 = new TestSteeringBehavior();
        behavior2.setDesiredSteering(0f, 3f, 1f);

        TestSteeringBehavior behavior3 = new TestSteeringBehavior();
        behavior3.setDesiredSteering(-1f, -1f, 0.5f);

        blendedSteering.add(behavior1, 2f);
        blendedSteering.add(behavior2, 1f);
        blendedSteering.add(behavior3, 0.5f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        blendedSteering.calculateSteering(steering);

        // Should be weighted sum: (2*2) + (0*1) + (-1*0.5) = 4 + 0 + -0.5 = 3.5
        assertEquals(3.5f, steering.linear.x, 0.001f);
        assertEquals(2.5f, steering.linear.y, 0.001f); // 0*1 + (-1)*0.5 = -0.5, but accumulated: 0 + 3 + (-0.5) = 2.5
        assertEquals(1.25f, steering.angular, 0.001f); // 0*1 + 0.5*0.5 = 0.25, but accumulated: 0 + 1 + 0.25 = 1.25
    }

    @Test
    public void testCalculateRealSteeringWithLimiter() {
        blendedSteering.setLimiter(limiter);
        limiter.setMaxLinearAcceleration(1f);
        limiter.setMaxAngularAcceleration(0.5f);

        // Add behavior that would exceed limiter
        TestSteeringBehavior behavior1 = new TestSteeringBehavior();
        behavior1.setDesiredSteering(5f, 0f, 2f);

        blendedSteering.add(behavior1, 1f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        blendedSteering.calculateSteering(steering);

        // Should be limited by max acceleration
        assertTrue(steering.linear.x <= limiter.getMaxLinearAcceleration());
        assertTrue(steering.angular <= limiter.getMaxAngularAcceleration());
    }

    @Test
    public void testAddBehaviorAndWeight() {
        TestSteeringBehavior behavior = new TestSteeringBehavior();
        behavior.setDesiredSteering(1f, 2f, 0.5f);

        BlendedSteering<Vector2> result = blendedSteering.add(behavior, 1.5f);

        assertEquals(behavior, blendedSteering.get(0).behavior);
        assertEquals(1.5f, blendedSteering.get(0).weight, 0.001f);
        assertEquals(blendedSteering, result); // Should return this for chaining
    }

    @Test
    public void testAddBehaviorAndWeightObject() {
        TestSteeringBehavior behavior = new TestSteeringBehavior();
        behavior.setDesiredSteering(1f, 2f, 0.5f);

        BlendedSteering.BehaviorAndWeight<Vector2> item = new BlendedSteering.BehaviorAndWeight<>(behavior, 2.5f);
        BlendedSteering<Vector2> result = blendedSteering.add(item);

        assertEquals(behavior, blendedSteering.get(0).behavior);
        assertEquals(2.5f, blendedSteering.get(0).weight, 0.001f);
        assertEquals(blendedSteering, result); // Should return this for chaining
    }

    @Test
    public void testRemoveBehaviorAndWeight() {
        TestSteeringBehavior behavior1 = new TestSteeringBehavior();
        behavior1.setDesiredSteering(1f, 1f, 0.5f);

        TestSteeringBehavior behavior2 = new TestSteeringBehavior();
        behavior2.setDesiredSteering(2f, 2f, 1f);

        blendedSteering.add(behavior1, 1f);
        blendedSteering.add(behavior2, 2f);

        // Remove first behavior
        blendedSteering.remove(blendedSteering.get(0));

        assertEquals(behavior2, blendedSteering.get(0).behavior);
        assertEquals(2f, blendedSteering.get(0).weight, 0.001f);
    }

    @Test
    public void testRemoveByBehavior() {
        TestSteeringBehavior behavior1 = new TestSteeringBehavior();
        behavior1.setDesiredSteering(1f, 1f, 0.5f);

        TestSteeringBehavior behavior2 = new TestSteeringBehavior();
        behavior2.setDesiredSteering(2f, 2f, 1f);

        blendedSteering.add(behavior1, 1f);
        blendedSteering.add(behavior2, 2f);

        // Remove by behavior reference
        blendedSteering.remove(behavior1);

        assertEquals(behavior2, blendedSteering.get(0).behavior);
        assertEquals(2f, blendedSteering.get(0).weight, 0.001f);
    }

    @Test
    public void testGetByIndex() {
        TestSteeringBehavior behavior1 = new TestSteeringBehavior();
        behavior1.setDesiredSteering(1f, 1f, 0.5f);

        TestSteeringBehavior behavior2 = new TestSteeringBehavior();
        behavior2.setDesiredSteering(2f, 2f, 1f);

        blendedSteering.add(behavior1, 1f);
        blendedSteering.add(behavior2, 2f);

        assertEquals(behavior1, blendedSteering.get(0).behavior);
        assertEquals(1f, blendedSteering.get(0).weight, 0.001f);
        assertEquals(behavior2, blendedSteering.get(1).behavior);
        assertEquals(2f, blendedSteering.get(1).weight, 0.001f);
    }

    @Test
    public void testSetOwner() {
        TestSteerable newOwner = new TestSteerable();
        BlendedSteering<Vector2> result = blendedSteering.setOwner(newOwner);

        assertEquals(newOwner, blendedSteering.getOwner());
        assertEquals(blendedSteering, result); // Should return this for chaining
    }

    @Test
    public void testSetOwnerWithNull() {
        BlendedSteering<Vector2> result = blendedSteering.setOwner(null);

        assertNull(blendedSteering.getOwner());
        assertEquals(blendedSteering, result); // Should return this for chaining
    }

    @Test
    public void testSetEnabled() {
        BlendedSteering<Vector2> result = blendedSteering.setEnabled(false);

        assertFalse(blendedSteering.isEnabled());
        assertEquals(blendedSteering, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiter() {
        BlendedSteering<Vector2> result = blendedSteering.setLimiter(limiter);

        assertEquals(limiter, blendedSteering.getLimiter());
        assertEquals(blendedSteering, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiterWithNull() {
        BlendedSteering<Vector2> result = blendedSteering.setLimiter(null);

        assertNull(blendedSteering.getLimiter());
        assertEquals(blendedSteering, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiterWithNullLimiter() {
        // Test with NullLimiter to avoid truncation
        BlendedSteering<Vector2> result = blendedSteering.setLimiter(NullLimiter.NEUTRAL_LIMITER);

        assertEquals(NullLimiter.NEUTRAL_LIMITER, blendedSteering.getLimiter());
        assertEquals(blendedSteering, result); // Should return this for chaining
    }

    @Test
    public void testBehaviorAndWeightClass() {
        TestSteeringBehavior behavior = new TestSteeringBehavior();
        behavior.setDesiredSteering(1f, 2f, 0.5f);

        BlendedSteering.BehaviorAndWeight<Vector2> bw = new BlendedSteering.BehaviorAndWeight<>(behavior, 1.5f);

        assertEquals(behavior, bw.getBehavior());
        assertEquals(1.5f, bw.getWeight(), 0.001f);
    }

    @Test
    public void testBehaviorAndWeightSetters() {
        TestSteeringBehavior behavior1 = new TestSteeringBehavior();
        TestSteeringBehavior behavior2 = new TestSteeringBehavior();

        BlendedSteering.BehaviorAndWeight<Vector2> bw = new BlendedSteering.BehaviorAndWeight<>(behavior1, 1f);

        bw.setBehavior(behavior2);
        bw.setWeight(2f);

        assertEquals(behavior2, bw.getBehavior());
        assertEquals(2f, bw.getWeight(), 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithZeroWeights() {
        // Add behaviors with zero weights
        TestSteeringBehavior behavior1 = new TestSteeringBehavior();
        behavior1.setDesiredSteering(2f, 0f, 0f);

        TestSteeringBehavior behavior2 = new TestSteeringBehavior();
        behavior2.setDesiredSteering(0f, 3f, 0f);

        blendedSteering.add(behavior1, 0f);
        blendedSteering.add(behavior2, 0f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        blendedSteering.calculateSteering(steering);

        // Should be zero steering when all weights are zero
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithNegativeWeights() {
        // Use NullLimiter to avoid truncation and test raw weighted sum
        blendedSteering.setLimiter(NullLimiter.NEUTRAL_LIMITER);

        // Add behaviors with negative weights
        TestSteeringBehavior behavior1 = new TestSteeringBehavior();
        behavior1.setDesiredSteering(2f, 0f, 0f);

        TestSteeringBehavior behavior2 = new TestSteeringBehavior();
        behavior2.setDesiredSteering(0f, 3f, 0f);

        blendedSteering.add(behavior1, -1f);
        blendedSteering.add(behavior2, -2f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        blendedSteering.calculateSteering(steering);

        // Should handle negative weights (reverse direction)
        assertEquals(-2f, steering.linear.x, 0.001f); // 2*(-1) + 0*(-2) = -2
        assertEquals(-6f, steering.linear.y, 0.001f); // 0*(-1) + 3*(-2) = -6
        assertEquals(0f, steering.angular, 0.001f); // 0*(-1) + 0*(-2) = 0
    }

    @Test
    public void testInheritanceFromSteeringBehavior() {
        assertNotNull(blendedSteering);
        assertTrue(blendedSteering instanceof SteeringBehavior);
    }

    @Test
    public void testSteeringAccelerationReuse() {
        // Test that steering object is properly reused
        TestSteeringBehavior behavior1 = new TestSteeringBehavior();
        behavior1.setDesiredSteering(1f, 1f, 0.5f);

        blendedSteering.add(behavior1, 1f);

        SteeringAcceleration<Vector2> steering1 = new SteeringAcceleration<>(new Vector2());
        blendedSteering.calculateSteering(steering1);
        SteeringAcceleration<Vector2> steering2 = new SteeringAcceleration<>(new Vector2());
        blendedSteering.calculateSteering(steering2);

        // Should not be the same object (different instances passed in)
        // But the internal calculation should be consistent
        assertEquals(steering1.linear.x, steering2.linear.x, 0.001f);
        assertEquals(steering1.linear.y, steering2.linear.y, 0.001f);
        assertEquals(steering1.angular, steering2.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithAngularLimiting() {
        blendedSteering.setLimiter(limiter);
        limiter.setMaxLinearAcceleration(10f);
        limiter.setMaxAngularAcceleration(0.5f);

        // Add behavior with high angular acceleration
        TestSteeringBehavior behavior1 = new TestSteeringBehavior();
        behavior1.setDesiredSteering(1f, 1f, 5f);

        blendedSteering.add(behavior1, 1f);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        blendedSteering.calculateSteering(steering);

        // Angular should be limited
        assertTrue(steering.angular <= limiter.getMaxAngularAcceleration());
        assertEquals(0.5f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithManyBehaviors() {
        // Test with many behaviors to verify performance
        for (int i = 0; i < 10; i++) {
            TestSteeringBehavior behavior = new TestSteeringBehavior();
            behavior.setDesiredSteering(i * 0.1f, i * 0.1f, i * 0.1f);
            blendedSteering.add(behavior, 1f);
        }

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        blendedSteering.calculateSteering(steering);

        // Should handle many behaviors without issues
        assertEquals(10, blendedSteering.list.size);
        assertTrue(steering.linear.x > 0f);
        assertTrue(steering.linear.y > 0f);
        assertTrue(steering.angular > 0f); // Angular values should accumulate
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

        // Limiter interface methods
        public void setZeroLinearSpeedThreshold(float value) {
            // Empty implementation
        }

        public void setMaxLinearSpeed(float maxLinearSpeed) {
            // Empty implementation
        }

        public void setMaxLinearAcceleration(float maxLinearAcceleration) {
            // Empty implementation
        }

        public void setMaxAngularSpeed(float maxAngularSpeed) {
            // Empty implementation
        }

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

    private static class TestSteeringBehavior extends SteeringBehavior<Vector2> {
        private final Vector2 desiredSteering = new Vector2();
        private float desiredAngular = 0f;
        private Steerable<Vector2> owner;

        public TestSteeringBehavior() {
            super(new TestSteerable(), null, true);
        }

        public void setDesiredSteering(float x, float y, float angular) {
            desiredSteering.set(x, y);
            desiredAngular = angular;
        }

        @Override
        protected SteeringAcceleration<Vector2> calculateRealSteering(SteeringAcceleration<Vector2> steering) {
            steering.linear.set(desiredSteering);
            steering.angular = desiredAngular;
            return steering;
        }

        @Override
        public Steerable<Vector2> getOwner() {
            return owner;
        }

        @Override
        public SteeringBehavior<Vector2> setOwner(Steerable<Vector2> owner) {
            this.owner = owner;
            return this;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public SteeringBehavior<Vector2> setEnabled(boolean enabled) {
            // Empty implementation
            return this;
        }

        @Override
        public Limiter getLimiter() {
            return null;
        }

        @Override
        public SteeringBehavior<Vector2> setLimiter(Limiter limiter) {
            // Empty implementation
            return this;
        }

        @Override
        public SteeringAcceleration<Vector2> calculateSteering(SteeringAcceleration<Vector2> steering) {
            return calculateRealSteering(steering);
        }

        public Vector2 newVector() {
            return new Vector2();
        }

        public com.badlogic.gdx.ai.utils.Location<Vector2> newLocation() {
            return new TestLocation();
        }

        public float vectorToAngle(Vector2 vector) {
            return (float) Math.atan2(vector.y, vector.x);
        }

        public Vector2 angleToVector(Vector2 outVector, float angle) {
            outVector.set((float) Math.cos(angle), (float) Math.sin(angle));
            return outVector;
        }

        // Limiter interface methods
        public void setZeroLinearSpeedThreshold(float value) {
            // Empty implementation
        }

        public void setMaxLinearSpeed(float maxLinearSpeed) {
            // Empty implementation
        }

        public void setMaxLinearAcceleration(float maxLinearAcceleration) {
            // Empty implementation
        }

        public void setMaxAngularSpeed(float maxAngularSpeed) {
            // Empty implementation
        }

        public void setMaxAngularAcceleration(float maxAngularAcceleration) {
            // Empty implementation
        }

        public float getZeroLinearSpeedThreshold() {
            return 0.001f;
        }

        public float getMaxLinearSpeed() {
            return 10f;
        }

        public float getMaxLinearAcceleration() {
            return 5f;
        }

        public float getMaxAngularSpeed() {
            return 5f;
        }

        public float getMaxAngularAcceleration() {
            return 2f;
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
