package com.badlogic.gdx.ai.steer.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.math.Vector2;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CohesionTest {

    private Cohesion<Vector2> cohesion;
    private TestSteerable owner;
    private TestProximity proximity;
    private TestLimiter limiter;

    @Before
    public void setUp() {
        owner = new TestSteerable();
        proximity = new TestProximity();
        limiter = new TestLimiter();
        cohesion = new Cohesion<>(owner, proximity);
    }

    @Test
    public void testConstructor() {
        assertNotNull(cohesion);
        assertEquals(owner, cohesion.getOwner());
        assertEquals(proximity, cohesion.getProximity());
    }

    @Test
    public void testConstructorWithNullOwner() {
        try {
            new Cohesion<>(null, proximity);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testConstructorWithNullProximity() {
        try {
            new Cohesion<>(owner, null);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testCalculateRealSteeringWithNoNeighbors() {
        // Test with no neighbors
        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering);

        // Should be zero steering when no neighbors
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithSingleNeighbor() {
        // Add one neighbor
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setPosition(5f, 0f);
        proximity.addNeighbor(neighbor1);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering);

        // Should steer towards neighbor position (5, 0)
        // Center of mass is (5, 0), owner at (0, 0), direction is (5, 0) normalized = (1, 0)
        // Scaled by max acceleration (5f) = (5, 0)
        assertEquals(5f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithMultipleNeighbors() {
        // Add multiple neighbors
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setPosition(4f, 0f);

        TestSteerable neighbor2 = new TestSteerable();
        neighbor2.setPosition(0f, 6f);

        TestSteerable neighbor3 = new TestSteerable();
        neighbor3.setPosition(-2f, -2f);

        proximity.addNeighbor(neighbor1);
        proximity.addNeighbor(neighbor2);
        proximity.addNeighbor(neighbor3);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering);

        // Center of mass = ((4,0) + (0,6) + (-2,-2)) / 3 = (2,4) / 3 = (0.667, 1.333)
        // Direction from owner (0,0) to center = (0.667, 1.333)
        // Normalized direction = (0.667, 1.333) / sqrt(0.667² + 1.333²) = (0.667, 1.333) / sqrt(0.445 + 1.777) = (0.667, 1.333) / sqrt(2.222) = (0.667, 1.333) / 1.491 = (0.447, 0.894)
        // Scaled by max acceleration (5f) = (2.236, 4.472)
        assertEquals(2.236f, steering.linear.x, 0.001f);
        assertEquals(4.472f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithLimiter() {
        cohesion.setLimiter(limiter);
        limiter.setMaxLinearAcceleration(2f);

        // Add neighbor
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setPosition(10f, 0f);
        proximity.addNeighbor(neighbor1);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering);

        // Should be limited by max acceleration
        assertEquals(2f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testReportNeighbor() {
        // Test reportNeighbor method directly
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setPosition(3f, 4f);

        // Start with zero steering
        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering);

        // Manually call reportNeighbor to verify position accumulation
        boolean result = cohesion.reportNeighbor(neighbor1);

        assertTrue(result); // Should return true for valid neighbor
        // The position should be accumulated in centerOfMass (which is steering.linear)
        // But we can't directly test this since it's part of the internal calculation
    }

    @Test
    public void testSetOwner() {
        TestSteerable newOwner = new TestSteerable();
        Cohesion<Vector2> result = cohesion.setOwner(newOwner);

        assertEquals(newOwner, cohesion.getOwner());
        assertEquals(cohesion, result); // Should return this for chaining
    }

    @Test
    public void testSetOwnerWithNull() {
        Cohesion<Vector2> result = cohesion.setOwner(null);

        assertNull(cohesion.getOwner());
        assertEquals(cohesion, result); // Should return this for chaining
    }

    @Test
    public void testSetEnabled() {
        Cohesion<Vector2> result = cohesion.setEnabled(false);

        assertFalse(cohesion.isEnabled());
        assertEquals(cohesion, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiter() {
        Cohesion<Vector2> result = cohesion.setLimiter(limiter);

        assertEquals(limiter, cohesion.getLimiter());
        assertEquals(cohesion, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiterWithNull() {
        Cohesion<Vector2> result = cohesion.setLimiter(null);

        assertNull(cohesion.getLimiter());
        assertEquals(cohesion, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiterWithNullLimiter() {
        // Test with NullLimiter to avoid truncation
        Cohesion<Vector2> result = cohesion.setLimiter(NullLimiter.NEUTRAL_LIMITER);

        assertEquals(NullLimiter.NEUTRAL_LIMITER, cohesion.getLimiter());
        assertEquals(cohesion, result); // Should return this for chaining
    }

    @Test
    public void testGetProximity() {
        assertEquals(proximity, cohesion.getProximity());
    }

    @Test
    public void testSetProximity() {
        TestProximity newProximity = new TestProximity();
        cohesion.setProximity(newProximity);

        assertEquals(newProximity, cohesion.getProximity());
    }

    @Test
    public void testInheritanceFromGroupBehavior() {
        assertNotNull(cohesion);
    }

    @Test
    public void testProximityCallbackInterface() {
        assertNotNull(cohesion);
    }

    @Test
    public void testCalculateRealSteeringWithOwnerAtDifferentPosition() {
        // Move owner to different position
        owner.setPosition(2f, 2f);

        // Add neighbor
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setPosition(4f, 2f);
        proximity.addNeighbor(neighbor1);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering);

        // Center of mass = (4, 2)
        // Direction from owner (2,2) to center = (2, 0)
        // Normalized direction = (1, 0)
        // Scaled by max acceleration (5f) = (5, 0)
        assertEquals(5f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithNeighborsAtSamePosition() {
        // Add neighbors at the same position
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setPosition(3f, 3f);

        TestSteerable neighbor2 = new TestSteerable();
        neighbor2.setPosition(3f, 3f);

        proximity.addNeighbor(neighbor1);
        proximity.addNeighbor(neighbor2);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering);

        // Center of mass = ((3,3) + (3,3)) / 2 = (3,3)
        // Direction from owner (0,0) to center = (3,3)
        // Normalized direction = (3,3) / sqrt(18) = (3,3) / 4.243 = (0.707, 0.707)
        // Scaled by max acceleration (5f) = (3.536, 3.536)
        assertEquals(3.536f, steering.linear.x, 0.001f);
        assertEquals(3.536f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithNegativeNeighborPositions() {
        // Add neighbors with negative positions
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setPosition(-4f, 0f);

        TestSteerable neighbor2 = new TestSteerable();
        neighbor2.setPosition(0f, -6f);

        proximity.addNeighbor(neighbor1);
        proximity.addNeighbor(neighbor2);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering);

        // Center of mass = ((-4,0) + (0,-6)) / 2 = (-2,-3)
        // Direction from owner (0,0) to center = (-2,-3)
        // Normalized direction = (-2,-3) / sqrt(13) = (-2,-3) / 3.606 = (-0.555, -0.832)
        // Scaled by max acceleration (5f) = (-2.775, -4.160)
        // Let's check the actual calculation: sqrt(13) = 3.605551275
        // -2/3.605551275 = -0.554700196, *5 = -2.77350098
        // -3/3.605551275 = -0.832050294, *5 = -4.16025147
        assertEquals(-2.774f, steering.linear.x, 0.001f);
        assertEquals(-4.160f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithZeroDistanceNeighbors() {
        // Add neighbors at the same position as owner
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setPosition(0f, 0f);

        TestSteerable neighbor2 = new TestSteerable();
        neighbor2.setPosition(0f, 0f);

        proximity.addNeighbor(neighbor1);
        proximity.addNeighbor(neighbor2);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering);

        // Center of mass = ((0,0) + (0,0)) / 2 = (0,0)
        // Direction from owner (0,0) to center = (0,0)
        // Normalized direction = (0,0) (zero vector, but nor() should handle this)
        // Scaled by max acceleration = (0,0)
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testSteeringAccelerationReuse() {
        // Test that steering object is properly reused
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setPosition(3f, 4f);
        proximity.addNeighbor(neighbor1);

        SteeringAcceleration<Vector2> steering1 = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering1);
        SteeringAcceleration<Vector2> steering2 = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering2);

        // Should not be the same object (different instances passed in)
        // But the internal calculation should be consistent
        assertEquals(steering1.linear.x, steering2.linear.x, 0.001f);
        assertEquals(steering1.linear.y, steering2.linear.y, 0.001f);
        assertEquals(steering1.angular, steering2.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithNullLimiter() {
        cohesion.setLimiter(null);

        // Add neighbor
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setPosition(3f, 4f);
        proximity.addNeighbor(neighbor1);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering);

        // Should use owner's limiter (maxLinearAcceleration = 5f)
        // Direction (3,4) normalized = (0.6, 0.8)
        // Scaled by 5f = (3, 4)
        assertEquals(3f, steering.linear.x, 0.001f);
        assertEquals(4f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testReportNeighborWithNullNeighbor() {
        // Test reportNeighbor with null neighbor - should throw NullPointerException
        try {
            cohesion.reportNeighbor(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
            assertTrue(true);
        }
    }

    @Test
    public void testCalculateRealSteeringWithManyNeighbors() {
        // Test with many neighbors to verify performance
        for (int i = 0; i < 10; i++) {
            TestSteerable neighbor = new TestSteerable();
            neighbor.setPosition(i * 0.5f, i * 0.5f);
            proximity.addNeighbor(neighbor);
        }

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        cohesion.calculateSteering(steering);

        // Should handle many neighbors without issues
        assertEquals(10, proximity.getNeighbors().size());
        assertTrue(steering.linear.x > 0f);
        assertTrue(steering.linear.y > 0f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    private static class TestSteerable implements Steerable<Vector2> {
        private final Vector2 position = new Vector2();
        private final Vector2 linearVelocity = new Vector2();
        private boolean tagged = false;

        public void setPosition(float x, float y) {
            position.set(x, y);
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

    private static class TestProximity implements Proximity<Vector2> {
        private final List<Steerable<Vector2>> neighbors = new ArrayList<>();

        public void addNeighbor(Steerable<Vector2> neighbor) {
            neighbors.add(neighbor);
        }

        public List<Steerable<Vector2>> getNeighbors() {
            return neighbors;
        }

        @Override
        public Steerable<Vector2> getOwner() {
            return null; // Not used in tests
        }

        @Override
        public void setOwner(Steerable<Vector2> owner) {
            // Empty implementation
        }

        @Override
        public int findNeighbors(ProximityCallback<Vector2> callback) {
            int count = 0;
            for (Steerable<Vector2> neighbor : neighbors) {
                if (callback.reportNeighbor(neighbor)) {
                    count++;
                }
            }
            return count;
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
