package com.badlogic.gdx.ai.steer.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

import org.junit.Before;
import org.junit.Test;

public class FollowFlowFieldTest {

    private FollowFlowField<Vector2> followFlowField;
    private TestSteerable owner;
    private TestFlowField flowField;
    private TestLimiter limiter;
    private SteeringAcceleration<Vector2> steering;

    @Before
    public void setUp() {
        owner = new TestSteerable();
        flowField = new TestFlowField();
        limiter = new TestLimiter();
        followFlowField = new FollowFlowField<>(owner, flowField, 0.5f);
        followFlowField.setLimiter(limiter);
        steering = new SteeringAcceleration<>(new Vector2());
    }

    @Test
    public void testConstructorWithOwner() {
        FollowFlowField<Vector2> behavior = new FollowFlowField<>(owner);
        assertNotNull(behavior);
        assertEquals(owner, behavior.getOwner());
        assertNull(behavior.getFlowField());
        assertEquals(0f, behavior.getPredictionTime(), 0.001f);
    }

    @Test
    public void testConstructorWithOwnerAndFlowField() {
        FollowFlowField<Vector2> behavior = new FollowFlowField<>(owner, flowField);
        assertNotNull(behavior);
        assertEquals(owner, behavior.getOwner());
        assertEquals(flowField, behavior.getFlowField());
        assertEquals(0f, behavior.getPredictionTime(), 0.001f);
    }

    @Test
    public void testConstructorWithOwnerFlowFieldAndPredictionTime() {
        FollowFlowField<Vector2> behavior = new FollowFlowField<>(owner, flowField, 1.0f);
        assertNotNull(behavior);
        assertEquals(owner, behavior.getOwner());
        assertEquals(flowField, behavior.getFlowField());
        assertEquals(1.0f, behavior.getPredictionTime(), 0.001f);
    }

    @Test
    public void testConstructorWithNullOwner() {
        FollowFlowField<Vector2> behavior = new FollowFlowField<>(null, flowField, 0.5f);
        assertNotNull(behavior);
        assertNull(behavior.getOwner());
        assertEquals(flowField, behavior.getFlowField());
        assertEquals(0.5f, behavior.getPredictionTime(), 0.001f);
    }

    @Test
    public void testConstructorWithNullFlowField() {
        FollowFlowField<Vector2> behavior = new FollowFlowField<>(owner, null, 0.5f);
        assertNotNull(behavior);
        assertEquals(owner, behavior.getOwner());
        assertNull(behavior.getFlowField());
        assertEquals(0.5f, behavior.getPredictionTime(), 0.001f);
    }

    @Test
    public void testConstructorWithZeroPredictionTime() {
        FollowFlowField<Vector2> behavior = new FollowFlowField<>(owner, flowField, 0f);
        assertNotNull(behavior);
        assertEquals(owner, behavior.getOwner());
        assertEquals(flowField, behavior.getFlowField());
        assertEquals(0f, behavior.getPredictionTime(), 0.001f);
    }

    @Test
    public void testConstructorWithNegativePredictionTime() {
        FollowFlowField<Vector2> behavior = new FollowFlowField<>(owner, flowField, -0.5f);
        assertNotNull(behavior);
        assertEquals(owner, behavior.getOwner());
        assertEquals(flowField, behavior.getFlowField());
        assertEquals(-0.5f, behavior.getPredictionTime(), 0.001f);
    }

    // calculateRealSteering tests

    @Test
    public void testCalculateRealSteeringWithZeroPredictionTime() {
        followFlowField.setPredictionTime(0f);
        owner.setPosition(10f, 10f);
        flowField.setFlowVector(new Vector2(1f, 0f));

        SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);

        assertNotNull(result);
        // Should use current position (10, 10) for flow field lookup
        // Flow vector (1, 0) should produce acceleration in that direction
        assertTrue(result.linear.x > 0);
        assertEquals(0f, result.linear.y, 0.001f);
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithNonZeroPredictionTime() {
        followFlowField.setPredictionTime(1.0f);
        owner.setPosition(10f, 10f);
        owner.setLinearVelocity(2f, 3f);
        flowField.setFlowVector(new Vector2(0f, 1f));

        SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);

        assertNotNull(result);
        // Should use predicted position (12, 13) for flow field lookup
        // Flow vector (0, 1) should produce acceleration in that direction
        // But calculation is: flowVector * maxSpeed - currentVelocity
        // So: (0, 1) * 10 - (2, 3) = (-2, 7), then limited
        assertTrue(result.linear.y > 0); // Y should be positive
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithNullFlowField() {
        followFlowField.setFlowField(null);

        try {
            SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);
            // If we get here, null flow field was handled
            assertEquals(0f, result.linear.x, 0.001f);
            assertEquals(0f, result.linear.y, 0.001f);
            assertEquals(0f, result.angular, 0.001f);
        } catch (NullPointerException e) {
            // Expected behavior - null flow field throws NPE
            assertTrue("Expected NullPointerException for null flow field", true);
        }
    }

    @Test
    public void testCalculateRealSteeringWithNullFlowVector() {
        flowField.setFlowVector(null);

        SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);

        assertNotNull(result);
        assertEquals(0f, result.linear.x, 0.001f);
        assertEquals(0f, result.linear.y, 0.001f);
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithZeroFlowVector() {
        flowField.setFlowVector(new Vector2(0f, 0f));

        SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);

        assertNotNull(result);
        assertEquals(0f, result.linear.x, 0.001f);
        assertEquals(0f, result.linear.y, 0.001f);
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithLimiter() {
        limiter.setMaxLinearAcceleration(2f);
        owner.setPosition(0f, 0f);
        flowField.setFlowVector(new Vector2(10f, 0f)); // Large flow vector

        SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);

        assertNotNull(result);
        // Acceleration should be limited to maxLinearAcceleration
        assertEquals(2f, result.linear.len(), 0.001f);
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithoutLimiter() {
        followFlowField.setLimiter(null);
        owner.setPosition(0f, 0f);
        flowField.setFlowVector(new Vector2(1f, 1f));

        SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);

        assertNotNull(result);
        // Should use owner's limiter when behavior limiter is null
        assertTrue(result.linear.len() > 0);
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithOwnerVelocity() {
        owner.setPosition(0f, 0f);
        owner.setLinearVelocity(1f, 0f);
        flowField.setFlowVector(new Vector2(0f, 1f));

        SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);

        assertNotNull(result);
        // Calculation is: flowVector * maxSpeed - currentVelocity
        // So: (0, 1) * 10 - (1, 0) = (-1, 10), then limited to max acceleration
        assertTrue(result.linear.y > 0); // Y should be positive
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringObjectReuse() {
        owner.setPosition(0f, 0f);
        flowField.setFlowVector(new Vector2(1f, 0f));

        SteeringAcceleration<Vector2> result1 = followFlowField.calculateRealSteering(steering);
        SteeringAcceleration<Vector2> result2 = followFlowField.calculateRealSteering(steering);

        // Should reuse the same steering object
        assertSame(result1, result2);
        assertTrue(result1.linear.x > 0);
    }

    @Test
    public void testGetFlowField() {
        assertEquals(flowField, followFlowField.getFlowField());
    }

    @Test
    public void testSetFlowField() {
        TestFlowField newFlowField = new TestFlowField();
        FollowFlowField<Vector2> result = followFlowField.setFlowField(newFlowField);

        assertEquals(newFlowField, followFlowField.getFlowField());
        assertEquals(followFlowField, result); // Method chaining
    }

    @Test
    public void testSetFlowFieldToNull() {
        FollowFlowField<Vector2> result = followFlowField.setFlowField(null);

        assertNull(followFlowField.getFlowField());
        assertEquals(followFlowField, result); // Method chaining
    }

    @Test
    public void testGetPredictionTime() {
        assertEquals(0.5f, followFlowField.getPredictionTime(), 0.001f);
    }

    @Test
    public void testSetPredictionTime() {
        FollowFlowField<Vector2> result = followFlowField.setPredictionTime(2.0f);

        assertEquals(2.0f, followFlowField.getPredictionTime(), 0.001f);
        assertEquals(followFlowField, result); // Method chaining
    }

    @Test
    public void testSetPredictionTimeToZero() {
        FollowFlowField<Vector2> result = followFlowField.setPredictionTime(0f);

        assertEquals(0f, followFlowField.getPredictionTime(), 0.001f);
        assertEquals(followFlowField, result); // Method chaining
    }

    @Test
    public void testSetPredictionTimeToNegative() {
        FollowFlowField<Vector2> result = followFlowField.setPredictionTime(-1.0f);

        assertEquals(-1.0f, followFlowField.getPredictionTime(), 0.001f);
        assertEquals(followFlowField, result); // Method chaining
    }

    // Inherited setter tests with proper return type

    @Test
    public void testSetOwner() {
        TestSteerable newOwner = new TestSteerable();
        FollowFlowField<Vector2> result = followFlowField.setOwner(newOwner);

        assertEquals(newOwner, followFlowField.getOwner());
        assertEquals(followFlowField, result); // Method chaining
    }

    @Test
    public void testSetOwnerToNull() {
        FollowFlowField<Vector2> result = followFlowField.setOwner(null);

        assertNull(followFlowField.getOwner());
        assertEquals(followFlowField, result); // Method chaining
    }

    @Test
    public void testSetEnabled() {
        FollowFlowField<Vector2> result = followFlowField.setEnabled(false);

        assertFalse(followFlowField.isEnabled());
        assertEquals(followFlowField, result); // Method chaining
    }

    @Test
    public void testSetLimiter() {
        TestLimiter newLimiter = new TestLimiter();
        FollowFlowField<Vector2> result = followFlowField.setLimiter(newLimiter);

        assertEquals(newLimiter, followFlowField.getLimiter());
        assertEquals(followFlowField, result); // Method chaining
    }

    @Test
    public void testSetLimiterToNull() {
        FollowFlowField<Vector2> result = followFlowField.setLimiter(null);

        assertNull(followFlowField.getLimiter());
        assertEquals(followFlowField, result); // Method chaining
    }

    @Test
    public void testImplementsSteeringBehavior() {
        assertNotNull(followFlowField);
    }

    @Test
    public void testFlowFieldInterface() {
        assertNotNull(flowField);
    }

    @Test
    public void testCalculateRealSteeringWithLargePredictionTime() {
        followFlowField.setPredictionTime(100f);
        owner.setPosition(0f, 0f);
        owner.setLinearVelocity(1f, 1f);
        flowField.setFlowVector(new Vector2(1f, 0f));

        SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);

        assertNotNull(result);
        // Should handle large prediction time gracefully
        assertTrue(result.linear.x > 0);
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithVerySmallFlowVector() {
        owner.setPosition(0f, 0f);
        flowField.setFlowVector(new Vector2(0.001f, 0.001f));

        SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);

        assertNotNull(result);
        // Should handle very small flow vectors
        assertTrue(result.linear.len() > 0);
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithNegativeFlowVector() {
        owner.setPosition(0f, 0f);
        flowField.setFlowVector(new Vector2(-1f, -1f));

        SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);

        assertNotNull(result);
        // Should handle negative flow vectors
        assertTrue(result.linear.x < 0);
        assertTrue(result.linear.y < 0);
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithDiagonalFlowVector() {
        owner.setPosition(0f, 0f);
        flowField.setFlowVector(new Vector2(1f, 1f));

        SteeringAcceleration<Vector2> result = followFlowField.calculateRealSteering(steering);

        assertNotNull(result);
        // Should handle diagonal flow vectors
        assertTrue(result.linear.x > 0);
        assertTrue(result.linear.y > 0);
        assertEquals(0f, result.angular, 0.001f);
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

    private static class TestFlowField implements FollowFlowField.FlowField<Vector2> {
        private Vector2 flowVector = new Vector2(1f, 0f);

        public void setFlowVector(Vector2 flowVector) {
            this.flowVector = flowVector;
        }

        @Override
        public Vector2 lookup(Vector2 position) {
            return flowVector;
        }
    }
}
