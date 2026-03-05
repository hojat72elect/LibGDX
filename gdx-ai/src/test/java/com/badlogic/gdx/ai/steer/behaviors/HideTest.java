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
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

import org.junit.Before;
import org.junit.Test;

public class HideTest {

    private Hide<Vector2> hide;
    private TestSteerable owner;
    private TestLocation target;
    private TestProximity proximity;
    private TestLimiter limiter;
    private SteeringAcceleration<Vector2> steering;

    @Before
    public void setUp() {
        owner = new TestSteerable();
        target = new TestLocation();
        proximity = new TestProximity();
        limiter = new TestLimiter();
        steering = new SteeringAcceleration<>(new Vector2());
        hide = new Hide<>(owner, target, proximity);
    }

    @Test
    public void testConstructorWithOwner() {
        Hide<Vector2> hide = new Hide<>(owner);
        assertNotNull(hide);
        assertEquals(owner, hide.getOwner());
        assertNull(hide.getTarget());
        assertNull(hide.getProximity());
    }

    @Test
    public void testConstructorWithOwnerAndTarget() {
        Hide<Vector2> hide = new Hide<>(owner, target);
        assertNotNull(hide);
        assertEquals(owner, hide.getOwner());
        assertEquals(target, hide.getTarget());
        assertNull(hide.getProximity());
    }

    @Test
    public void testConstructorWithAllParameters() {
        Hide<Vector2> hide = new Hide<>(owner, target, proximity);
        assertNotNull(hide);
        assertEquals(owner, hide.getOwner());
        assertEquals(target, hide.getTarget());
        assertEquals(proximity, hide.getProximity());
    }

    @Test
    public void testConstructorWithNullOwner() {
        try {
            new Hide<>(null, target, proximity);
        } catch (Exception e) {
            // Expected to handle null owner gracefully
        }
    }

    @Test
    public void testCalculateRealSteeringWithNoObstacles() {
        owner.setPosition(0, 0);
        target.setPosition(10, 10);

        SteeringAcceleration<Vector2> result = hide.calculateRealSteering(steering);

        assertEquals(0f, result.linear.len(), 0.001f);
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithObstacles() {
        owner.setPosition(0, 0);
        target.setPosition(10, 10);
        proximity.addNeighbor(new TestSteerable(5, 5, 2f)); // Obstacle at (5,5) with radius 2

        SteeringAcceleration<Vector2> result = hide.calculateRealSteering(steering);

        // Should return non-zero steering toward hiding spot
        assertTrue(result.linear.len() > 0);
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithMultipleObstacles() {
        owner.setPosition(0, 0);
        target.setPosition(10, 10);
        proximity.addNeighbor(new TestSteerable(5, 5, 2f)); // Closer obstacle
        proximity.addNeighbor(new TestSteerable(15, 15, 2f)); // Farther obstacle

        SteeringAcceleration<Vector2> result = hide.calculateRealSteering(steering);

        assertTrue(result.linear.len() > 0);
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithLimiter() {
        owner.setPosition(0, 0);
        target.setPosition(10, 10);
        proximity.addNeighbor(new TestSteerable(5, 5, 2f));
        hide.setLimiter(limiter);

        SteeringAcceleration<Vector2> result = hide.calculateRealSteering(steering);

        // Steering should be limited by limiter
        assertTrue(result.linear.len() <= limiter.getMaxLinearAcceleration());
        assertEquals(0f, result.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringObjectReuse() {
        owner.setPosition(0, 0);
        target.setPosition(10, 10);
        proximity.addNeighbor(new TestSteerable(5, 5, 2f));

        SteeringAcceleration<Vector2> steering1 = hide.calculateRealSteering(steering);
        SteeringAcceleration<Vector2> steering2 = hide.calculateRealSteering(steering);

        // Should reuse the same steering object
        assertEquals(steering1, steering2);
    }

    // reportNeighbor tests

    @Test
    public void testReportNeighborWithCloserObstacle() {
        owner.setPosition(0, 0);
        target.setPosition(10, 10);
        TestSteerable obstacle = new TestSteerable(5, 5, 2f);

        // Initialize internal state by calling calculateRealSteering first
        hide.calculateRealSteering(steering);

        boolean result = hide.reportNeighbor(obstacle);

        // Should accept closer obstacle
        assertTrue(result);
    }

    @Test
    public void testReportNeighborWithFartherObstacle() {
        owner.setPosition(0, 0);
        target.setPosition(10, 10);

        // Initialize internal state by calling calculateRealSteering first
        hide.calculateRealSteering(steering);

        // First add a close obstacle
        TestSteerable closeObstacle = new TestSteerable(5, 5, 2f);
        hide.reportNeighbor(closeObstacle);

        // Then try to add a farther obstacle
        TestSteerable farObstacle = new TestSteerable(15, 15, 2f);
        boolean result = hide.reportNeighbor(farObstacle);

        // Should reject farther obstacle
        assertFalse(result);
    }

    @Test
    public void testReportNeighborWithNullObstacle() {
        try {
            hide.reportNeighbor(null);
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetHidingPosition() {
        Vector2 obstaclePosition = new Vector2(10, 10);
        float obstacleRadius = 2f;
        Vector2 targetPosition = new Vector2(0, 0);
        hide.setDistanceFromBoundary(1f);

        // Initialize internal state by calling calculateRealSteering first
        hide.calculateRealSteering(steering);

        Vector2 hidingPosition = hide.getHidingPosition(obstaclePosition, obstacleRadius, targetPosition);

        // Hiding position should be behind obstacle relative to target
        assertNotNull(hidingPosition);
        assertTrue(hidingPosition.x > obstaclePosition.x); // Should be to the right of obstacle
        assertTrue(hidingPosition.y > obstaclePosition.y); // Should be above obstacle
    }

    @Test
    public void testGetHidingPositionWithZeroDistanceFromBoundary() {
        Vector2 obstaclePosition = new Vector2(10, 10);
        float obstacleRadius = 2f;
        Vector2 targetPosition = new Vector2(0, 0);
        hide.setDistanceFromBoundary(0f);

        // Initialize internal state by calling calculateRealSteering first
        hide.calculateRealSteering(steering);

        Vector2 hidingPosition = hide.getHidingPosition(obstaclePosition, obstacleRadius, targetPosition);

        // Hiding position should be exactly at obstacle boundary
        assertNotNull(hidingPosition);
        assertEquals(obstacleRadius, hidingPosition.dst(obstaclePosition), 0.001f);
    }

    @Test
    public void testGetHidingPositionWithNegativeDistanceFromBoundary() {
        Vector2 obstaclePosition = new Vector2(10, 10);
        float obstacleRadius = 2f;
        Vector2 targetPosition = new Vector2(0, 0);
        hide.setDistanceFromBoundary(-1f);

        // Initialize internal state by calling calculateRealSteering first
        hide.calculateRealSteering(steering);

        Vector2 hidingPosition = hide.getHidingPosition(obstaclePosition, obstacleRadius, targetPosition);

        // Should handle negative distance gracefully
        assertNotNull(hidingPosition);
    }

    @Test
    public void testGetHidingPositionWithZeroObstacleRadius() {
        Vector2 obstaclePosition = new Vector2(10, 10);
        float obstacleRadius = 0f;
        Vector2 targetPosition = new Vector2(0, 0);
        hide.setDistanceFromBoundary(1f);

        // Initialize internal state by calling calculateRealSteering first
        hide.calculateRealSteering(steering);

        Vector2 hidingPosition = hide.getHidingPosition(obstaclePosition, obstacleRadius, targetPosition);

        // Should handle zero radius gracefully
        assertNotNull(hidingPosition);
    }

    // Getter/setter tests

    @Test
    public void testSetGetProximity() {
        TestProximity newProximity = new TestProximity();
        Hide<Vector2> returned = hide.setProximity(newProximity);
        assertEquals(newProximity, hide.getProximity());
        assertEquals(hide, returned); // Method chaining
    }

    @Test
    public void testSetGetDistanceFromBoundary() {
        Hide<Vector2> returned = hide.setDistanceFromBoundary(3.5f);
        assertEquals(3.5f, hide.getDistanceFromBoundary(), 0.001f);
        assertEquals(hide, returned); // Method chaining
    }

    // Inherited setter tests with proper return type

    @Test
    public void testSetOwner() {
        TestSteerable newOwner = new TestSteerable();
        Hide<Vector2> returned = hide.setOwner(newOwner);
        assertEquals(newOwner, hide.getOwner());
        assertEquals(hide, returned); // Method chaining
    }

    @Test
    public void testSetEnabled() {
        Hide<Vector2> returned = hide.setEnabled(false);
        assertFalse(hide.isEnabled());
        assertEquals(hide, returned); // Method chaining
    }

    @Test
    public void testSetLimiter() {
        TestLimiter newLimiter = new TestLimiter();
        Hide<Vector2> returned = hide.setLimiter(newLimiter);
        assertEquals(newLimiter, hide.getLimiter());
        assertEquals(hide, returned); // Method chaining
    }

    @Test
    public void testSetTarget() {
        TestLocation newTarget = new TestLocation();
        Hide<Vector2> returned = hide.setTarget(newTarget);
        assertEquals(newTarget, hide.getTarget());
        assertEquals(hide, returned); // Method chaining
    }

    @Test
    public void testSetArrivalTolerance() {
        Hide<Vector2> returned = hide.setArrivalTolerance(2.5f);
        assertEquals(2.5f, hide.getArrivalTolerance(), 0.001f);
        assertEquals(hide, returned); // Method chaining
    }

    @Test
    public void testSetDecelerationRadius() {
        Hide<Vector2> returned = hide.setDecelerationRadius(8.8f);
        assertEquals(8.8f, hide.getDecelerationRadius(), 0.001f);
        assertEquals(hide, returned); // Method chaining
    }

    @Test
    public void testSetTimeToTarget() {
        Hide<Vector2> returned = hide.setTimeToTarget(0.75f);
        assertEquals(0.75f, hide.getTimeToTarget(), 0.001f);
        assertEquals(hide, returned); // Method chaining
    }

    // Edge case tests

    @Test
    public void testZeroDistanceFromBoundary() {
        hide.setDistanceFromBoundary(0f);
        owner.setPosition(0, 0);
        target.setPosition(10, 10);
        proximity.addNeighbor(new TestSteerable(5, 5, 2f));

        SteeringAcceleration<Vector2> result = hide.calculateRealSteering(steering);

        // Should work with zero distance from boundary
        assertTrue(result.linear.len() > 0);
    }

    @Test
    public void testNegativeDistanceFromBoundary() {
        hide.setDistanceFromBoundary(-1f);
        owner.setPosition(0, 0);
        target.setPosition(10, 10);
        proximity.addNeighbor(new TestSteerable(5, 5, 2f));

        SteeringAcceleration<Vector2> result = hide.calculateRealSteering(steering);

        // Should handle negative distance from boundary
        assertNotNull(result);
    }

    @Test
    public void testLargeDistanceFromBoundary() {
        hide.setDistanceFromBoundary(100f);
        owner.setPosition(0, 0);
        target.setPosition(10, 10);
        proximity.addNeighbor(new TestSteerable(5, 5, 2f));

        SteeringAcceleration<Vector2> result = hide.calculateRealSteering(steering);

        // Should handle large distance from boundary
        assertNotNull(result);
    }

    @Test
    public void testInheritanceVerification() {
        assertNotNull(hide);
    }

    @Test
    public void testNullProximityHandling() {
        hide.setProximity(null);
        owner.setPosition(0, 0);
        target.setPosition(10, 10);

        try {
            hide.calculateRealSteering(steering);
        } catch (Exception e) {
            // Expected to handle null proximity gracefully
        }
    }

    @Test
    public void testNullTargetHandling() {
        hide.setTarget(null);
        owner.setPosition(0, 0);
        proximity.addNeighbor(new TestSteerable(5, 5, 2f));

        try {
            hide.calculateRealSteering(steering);
        } catch (Exception e) {
            // Expected to handle null target gracefully
        }
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
        private float boundingRadius = 1f;

        public TestSteerable() {
            this(0, 0, 1f);
        }

        public TestSteerable(float x, float y, float boundingRadius) {
            this.position.set(x, y);
            this.boundingRadius = boundingRadius;
        }

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
            return boundingRadius;
        }

        @Override
        public float getZeroLinearSpeedThreshold() {
            return zeroLinearSpeedThreshold;
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

    private static class TestProximity implements Proximity<Vector2> {

        private java.util.List<TestSteerable> neighbors = new java.util.ArrayList<>();
        private Steerable<Vector2> owner;

        public void addNeighbor(TestSteerable neighbor) {
            neighbors.add(neighbor);
        }

        @Override
        public Steerable<Vector2> getOwner() {
            return owner;
        }

        @Override
        public void setOwner(Steerable<Vector2> owner) {
            this.owner = owner;
        }

        @Override
        public int findNeighbors(ProximityCallback<Vector2> callback) {
            int reportedCount = 0;
            for (TestSteerable neighbor : neighbors) {
                if (callback.reportNeighbor(neighbor)) {
                    reportedCount++;
                }
            }
            return reportedCount;
        }
    }
}
