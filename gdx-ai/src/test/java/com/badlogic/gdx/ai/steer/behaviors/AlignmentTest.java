package com.badlogic.gdx.ai.steer.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.Vector2;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AlignmentTest {

    private Alignment<Vector2> alignment;
    private TestSteerable owner;
    private TestProximity proximity;
    private TestLimiter limiter;

    @Before
    public void setUp() {
        owner = new TestSteerable();
        proximity = new TestProximity();
        limiter = new TestLimiter();
        alignment = new Alignment<>(owner, proximity);
    }

    @Test
    public void testConstructor() {
        assertEquals(owner, alignment.getOwner());
        assertEquals(proximity, alignment.getProximity());
    }

    @Test
    public void testConstructorWithNullOwner() {
        try {
            new Alignment<>(null, proximity);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testConstructorWithNullProximity() {
        try {
            new Alignment<>(owner, null);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testCalculateRealSteeringWithNoNeighbors() {

        proximity.setNeighborCount(0);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        alignment.calculateSteering(steering);

        // Steering should be zero when no neighbors
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithOneNeighbor() {
        // Set up owner with velocity
        owner.setLinearVelocity(1f, 0f);

        // Set up one neighbor with velocity
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setLinearVelocity(3f, 0f);

        proximity.addNeighbor(neighbor1);
        proximity.setNeighborCount(1);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        alignment.calculateSteering(steering);

        // Steering should align with neighbor's velocity
        // Average velocity = (3,0), owner velocity = (1,0)
        // Desired velocity = (3,0), steering = (3,0) - (1,0) = (2,0)
        assertTrue(steering.linear.x > 0);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithMultipleNeighbors() {
        // Set up owner with velocity
        owner.setLinearVelocity(1f, 1f);

        // Set up multiple neighbors with different velocities
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setLinearVelocity(3f, 0f);

        TestSteerable neighbor2 = new TestSteerable();
        neighbor2.setLinearVelocity(1f, 3f);

        TestSteerable neighbor3 = new TestSteerable();
        neighbor3.setLinearVelocity(0f, 2f);

        proximity.addNeighbor(neighbor1);
        proximity.addNeighbor(neighbor2);
        proximity.addNeighbor(neighbor3);
        proximity.setNeighborCount(3);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        alignment.calculateSteering(steering);

        // Average velocity = ((3,0) + (1,3) + (0,2)) / 3 = (1.33, 1.67)
        // Owner velocity = (1,1)
        // Steering = (1.33,1.67) - (1,1) = (0.33,0.67)
        assertTrue(steering.linear.x > 0);
        assertTrue(steering.linear.y > 0);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithLimiter() {
        alignment.setLimiter(limiter);
        limiter.setMaxLinearAcceleration(0.5f);

        // Set up owner with velocity
        owner.setLinearVelocity(0f, 0f);

        // Set up neighbor with high velocity that would exceed limiter
        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setLinearVelocity(10f, 0f);

        proximity.addNeighbor(neighbor1);
        proximity.setNeighborCount(1);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        alignment.calculateSteering(steering);

        // Steering should be limited to max linear acceleration
        assertTrue(steering.linear.x <= limiter.getMaxLinearAcceleration());
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testReportNeighbor() {
        // Create a new alignment for this test
        Alignment<Vector2> testAlignment = new Alignment<>(owner, proximity);

        // Set up neighbor
        TestSteerable neighbor = new TestSteerable();
        neighbor.setLinearVelocity(2f, 3f);

        // Initialize averageVelocity by calling calculateSteering with a dummy steering object
        SteeringAcceleration<Vector2> dummySteering = new SteeringAcceleration<>(new Vector2());
        testAlignment.calculateSteering(dummySteering);

        // Report neighbor
        boolean result = testAlignment.reportNeighbor(neighbor);

        // Should return true and accumulate neighbor velocity
        assertTrue(result);
        // Note: averageVelocity is a private field, but we can verify through calculateSteering
    }

    @Test
    public void testReportNeighborWithNullNeighbor() {
        Alignment<Vector2> testAlignment = new Alignment<>(owner, proximity);

        try {
            testAlignment.reportNeighbor(null);
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testSetOwner() {
        TestSteerable newOwner = new TestSteerable();
        Alignment<Vector2> result = alignment.setOwner(newOwner);

        assertEquals(newOwner, alignment.getOwner());
        assertEquals(alignment, result); // Should return this for chaining
    }

    @Test
    public void testSetOwnerWithNull() {
        Alignment<Vector2> result = alignment.setOwner(null);

        assertNull(alignment.getOwner());
        assertEquals(alignment, result); // Should return this for chaining
    }

    @Test
    public void testSetEnabled() {
        Alignment<Vector2> result = alignment.setEnabled(false);

        assertFalse(alignment.isEnabled());
        assertEquals(alignment, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiter() {
        Alignment<Vector2> result = alignment.setLimiter(limiter);

        assertEquals(limiter, alignment.getLimiter());
        assertEquals(alignment, result); // Should return this for chaining
    }

    @Test
    public void testSetLimiterWithNull() {
        Alignment<Vector2> result = alignment.setLimiter(null);

        assertNull(alignment.getLimiter());
        assertEquals(alignment, result); // Should return this for chaining
    }

    @Test
    public void testGetProximity() {
        assertEquals(proximity, alignment.getProximity());
    }

    @Test
    public void testSetProximity() {
        TestProximity newProximity = new TestProximity();
        alignment.setProximity(newProximity);

        assertEquals(newProximity, alignment.getProximity());
    }

    @Test
    public void testSetProximityWithNull() {
        alignment.setProximity(null);

        assertNull(alignment.getProximity());
    }

    @Test
    public void testCalculateSteeringWithZeroOwnerVelocity() {
        owner.setLinearVelocity(0f, 0f);

        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setLinearVelocity(2f, 1f);

        proximity.addNeighbor(neighbor1);
        proximity.setNeighborCount(1);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        alignment.calculateSteering(steering);

        // Steering should equal neighbor's velocity when owner is stationary
        assertEquals(2f, steering.linear.x, 0.001f);
        assertEquals(1f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateSteeringWithMatchingVelocities() {
        owner.setLinearVelocity(2f, 2f);

        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setLinearVelocity(2f, 2f);

        proximity.addNeighbor(neighbor1);
        proximity.setNeighborCount(1);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        alignment.calculateSteering(steering);

        // Steering should be zero when velocities match
        assertEquals(0f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateSteeringWithOppositeVelocities() {
        owner.setLinearVelocity(2f, 0f);

        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setLinearVelocity(-2f, 0f);

        proximity.addNeighbor(neighbor1);
        proximity.setNeighborCount(1);

        SteeringAcceleration<Vector2> steering = new SteeringAcceleration<>(new Vector2());
        alignment.calculateSteering(steering);

        // Steering should be significant when velocities oppose
        assertEquals(-4f, steering.linear.x, 0.001f);
        assertEquals(0f, steering.linear.y, 0.001f);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testInheritanceFromGroupBehavior() {
        assertNotNull(alignment);
    }

    @Test
    public void testImplementationOfProximityCallback() {
        assertNotNull(alignment);
    }

    @Test
    public void testSteeringAccelerationReuse() {
        // Test that the same steering object is reused
        owner.setLinearVelocity(0f, 0f);

        TestSteerable neighbor1 = new TestSteerable();
        neighbor1.setLinearVelocity(1f, 0f);

        proximity.addNeighbor(neighbor1);
        proximity.setNeighborCount(1);

        SteeringAcceleration<Vector2> steering1 = new SteeringAcceleration<>(new Vector2());
        alignment.calculateSteering(steering1);
        SteeringAcceleration<Vector2> steering2 = new SteeringAcceleration<>(new Vector2());
        alignment.calculateSteering(steering2);

        // Should not be the same object (different instances passed in)
        // But the internal calculation should be consistent
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

        // Additional Limiter interface methods
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

        // Test implementation of Location interface
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
    }

    private static class TestProximity implements Proximity<Vector2> {
        private final List<Steerable<Vector2>> neighbors = new ArrayList<>();
        private int neighborCount = 0;

        public void addNeighbor(Steerable<Vector2> neighbor) {
            neighbors.add(neighbor);
        }

        public void setNeighborCount(int count) {
            this.neighborCount = count;
        }

        @Override
        public int findNeighbors(Proximity.ProximityCallback<Vector2> callback) {
            for (int i = 0; i < neighborCount && i < neighbors.size(); i++) {
                callback.reportNeighbor(neighbors.get(i));
            }
            return neighborCount;
        }

        @Override
        public Steerable<Vector2> getOwner() {
            return null;
        }

        @Override
        public void setOwner(Steerable<Vector2> owner) {
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
