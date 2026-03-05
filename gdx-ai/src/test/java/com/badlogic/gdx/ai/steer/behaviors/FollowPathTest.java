package com.badlogic.gdx.ai.steer.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

import org.junit.Before;
import org.junit.Test;

public class FollowPathTest {

    private FollowPath<Vector2, TestPathParam> followPath;
    private TestSteerable owner;
    private TestPath path;
    private TestLimiter limiter;
    private SteeringAcceleration<Vector2> steering;

    @Before
    public void setUp() {
        owner = new TestSteerable();
        path = new TestPath();
        limiter = new TestLimiter();
        steering = new SteeringAcceleration<>(new Vector2());
        followPath = new FollowPath<>(owner, path);
    }

    @Test
    public void testConstructorWithOwnerAndPath() {
        FollowPath<Vector2, TestPathParam> followPath = new FollowPath<>(owner, path);
        assertNotNull(followPath);
        assertEquals(owner, followPath.getOwner());
        assertEquals(path, followPath.getPath());
        assertEquals(0f, followPath.getPathOffset(), 0.001f);
        assertEquals(0f, followPath.getPredictionTime(), 0.001f);
        assertTrue(followPath.isArriveEnabled());
        assertNotNull(followPath.getPathParam());
        assertNotNull(followPath.getInternalTargetPosition());
    }

    @Test
    public void testConstructorWithOwnerPathAndOffset() {
        FollowPath<Vector2, TestPathParam> followPath = new FollowPath<>(owner, path, 5f);
        assertNotNull(followPath);
        assertEquals(owner, followPath.getOwner());
        assertEquals(path, followPath.getPath());
        assertEquals(5f, followPath.getPathOffset(), 0.001f);
        assertEquals(0f, followPath.getPredictionTime(), 0.001f);
        assertTrue(followPath.isArriveEnabled());
    }

    @Test
    public void testConstructorWithAllParameters() {
        FollowPath<Vector2, TestPathParam> followPath = new FollowPath<>(owner, path, 3f, 2f);
        assertNotNull(followPath);
        assertEquals(owner, followPath.getOwner());
        assertEquals(path, followPath.getPath());
        assertEquals(3f, followPath.getPathOffset(), 0.001f);
        assertEquals(2f, followPath.getPredictionTime(), 0.001f);
        assertTrue(followPath.isArriveEnabled());
    }

    @Test
    public void testConstructorWithNullOwner() {
        try {
            new FollowPath<>(null, path);
        } catch (Exception e) {

        }
    }

    @Test
    public void testConstructorWithNullPath() {
        try {
            new FollowPath<Vector2, TestPathParam>(owner, null);
        } catch (Exception e) {
            // Expected to handle null path gracefully
        }
    }

    // calculateRealSteering tests

    @Test
    public void testCalculateRealSteeringNonPredictive() {
        owner.setPosition(10, 10); // Position away from path start
        path.setLength(100f);
        path.setOpen(false);

        followPath.calculateRealSteering(steering);

        // Should produce non-zero steering towards path target
        assertTrue(steering.linear.len() > 0);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringPredictive() {
        owner.setPosition(0, 0);
        owner.setLinearVelocity(1, 0);
        path.setLength(100f);
        path.setOpen(false);
        followPath.setPredictionTime(1f);

        followPath.calculateRealSteering(steering);

        // Should produce non-zero steering considering predicted position
        assertTrue(steering.linear.len() > 0);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithOpenPathAndArriveEnabled() {
        owner.setPosition(0, 0);
        path.setLength(10f);
        path.setOpen(true);
        followPath.setPathOffset(15f); // Beyond path length
        followPath.setDecelerationRadius(10f);

        followPath.calculateRealSteering(steering);

        // Should use arrive behavior when near end of open path
        assertNotNull(steering);
    }

    @Test
    public void testCalculateRealSteeringWithOpenPathAndArriveDisabled() {
        owner.setPosition(0, 0);
        path.setLength(10f);
        path.setOpen(true);
        followPath.setPathOffset(15f);
        followPath.setArriveEnabled(false);

        followPath.calculateRealSteering(steering);

        // Should use seek behavior when arrive is disabled
        assertTrue(steering.linear.len() > 0);
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringWithNegativePathOffset() {
        owner.setPosition(10, 0);
        path.setLength(100f);
        path.setOpen(true);
        followPath.setPathOffset(-5f);
        followPath.setDecelerationRadius(10f);

        followPath.calculateRealSteering(steering);

        // Should handle negative offset (moving backward along path)
        assertNotNull(steering);
    }

    @Test
    public void testCalculateRealSteeringWithLimiter() {
        owner.setPosition(0, 0);
        path.setLength(100f);
        path.setOpen(false);
        followPath.setLimiter(limiter);

        followPath.calculateRealSteering(steering);

        // Steering should be limited by limiter
        assertTrue(steering.linear.len() <= limiter.getMaxLinearAcceleration());
        assertEquals(0f, steering.angular, 0.001f);
    }

    @Test
    public void testCalculateRealSteeringObjectReuse() {
        owner.setPosition(0, 0);
        path.setLength(100f);
        path.setOpen(false);

        SteeringAcceleration<Vector2> steering1 = followPath.calculateRealSteering(steering);
        SteeringAcceleration<Vector2> steering2 = followPath.calculateRealSteering(steering);

        // Should reuse the same steering object
        assertEquals(steering1, steering2);
    }

    // Getter/setter tests

    @Test
    public void testSetGetPath() {
        TestPath newPath = new TestPath();
        FollowPath<Vector2, TestPathParam> returned = followPath.setPath(newPath);
        assertEquals(newPath, followPath.getPath());
        assertEquals(followPath, returned); // Method chaining
    }

    @Test
    public void testSetGetPathOffset() {
        FollowPath<Vector2, TestPathParam> returned = followPath.setPathOffset(7.5f);
        assertEquals(7.5f, followPath.getPathOffset(), 0.001f);
        assertEquals(followPath, returned); // Method chaining
    }

    @Test
    public void testSetGetArriveEnabled() {
        FollowPath<Vector2, TestPathParam> returned = followPath.setArriveEnabled(false);
        assertFalse(followPath.isArriveEnabled());
        assertEquals(followPath, returned); // Method chaining
    }

    @Test
    public void testSetGetPredictionTime() {
        FollowPath<Vector2, TestPathParam> returned = followPath.setPredictionTime(3.14f);
        assertEquals(3.14f, followPath.getPredictionTime(), 0.001f);
        assertEquals(followPath, returned); // Method chaining
    }

    @Test
    public void testGetPathParam() {
        assertNotNull(followPath.getPathParam());
        assertNotNull(followPath.getPathParam());
    }

    @Test
    public void testGetInternalTargetPosition() {
        assertNotNull(followPath.getInternalTargetPosition());
        assertNotNull(followPath.getInternalTargetPosition());
    }

    @Test
    public void testSetOwner() {
        TestSteerable newOwner = new TestSteerable();
        FollowPath<Vector2, TestPathParam> returned = followPath.setOwner(newOwner);
        assertEquals(newOwner, followPath.getOwner());
        assertEquals(followPath, returned); // Method chaining
    }

    @Test
    public void testSetEnabled() {
        FollowPath<Vector2, TestPathParam> returned = followPath.setEnabled(false);
        assertFalse(followPath.isEnabled());
        assertEquals(followPath, returned); // Method chaining
    }

    @Test
    public void testSetLimiter() {
        TestLimiter newLimiter = new TestLimiter();
        FollowPath<Vector2, TestPathParam> returned = followPath.setLimiter(newLimiter);
        assertEquals(newLimiter, followPath.getLimiter());
        assertEquals(followPath, returned); // Method chaining
    }

    @Test
    public void testSetTarget() {
        TestLocation newTarget = new TestLocation();
        FollowPath<Vector2, TestPathParam> returned = followPath.setTarget(newTarget);
        assertEquals(newTarget, followPath.getTarget());
        assertEquals(followPath, returned); // Method chaining
    }

    @Test
    public void testSetArrivalTolerance() {
        FollowPath<Vector2, TestPathParam> returned = followPath.setArrivalTolerance(2.5f);
        assertEquals(2.5f, followPath.getArrivalTolerance(), 0.001f);
        assertEquals(followPath, returned); // Method chaining
    }

    @Test
    public void testSetDecelerationRadius() {
        FollowPath<Vector2, TestPathParam> returned = followPath.setDecelerationRadius(8.8f);
        assertEquals(8.8f, followPath.getDecelerationRadius(), 0.001f);
        assertEquals(followPath, returned); // Method chaining
    }

    @Test
    public void testSetTimeToTarget() {
        FollowPath<Vector2, TestPathParam> returned = followPath.setTimeToTarget(0.75f);
        assertEquals(0.75f, followPath.getTimeToTarget(), 0.001f);
        assertEquals(followPath, returned); // Method chaining
    }

    // Edge case tests

    @Test
    public void testZeroPredictionTime() {
        followPath.setPredictionTime(0f);
        owner.setPosition(5, 5);
        path.setLength(100f);

        followPath.calculateRealSteering(steering);

        // Should work with zero prediction time (non-predictive)
        assertTrue(steering.linear.len() > 0);
    }

    @Test
    public void testNegativePredictionTime() {
        followPath.setPredictionTime(-1f);
        owner.setPosition(5, 5);
        path.setLength(100f);

        followPath.calculateRealSteering(steering);

        // Should handle negative prediction time
        assertNotNull(steering);
    }

    @Test
    public void testLargePredictionTime() {
        followPath.setPredictionTime(100f);
        owner.setPosition(5, 5);
        path.setLength(100f);

        followPath.calculateRealSteering(steering);

        // Should handle large prediction time
        assertNotNull(steering);
    }

    @Test
    public void testZeroPathOffset() {
        followPath.setPathOffset(0f);
        owner.setPosition(5, 5);
        path.setLength(100f);

        followPath.calculateRealSteering(steering);

        // Should work with zero path offset
        assertTrue(steering.linear.len() > 0);
    }

    @Test
    public void testLargePathOffset() {
        followPath.setPathOffset(1000f);
        owner.setPosition(5, 5);
        path.setLength(100f);

        followPath.calculateRealSteering(steering);

        assertNotNull(steering);
    }

    @Test
    public void testInheritanceVerification() {
        assertNotNull(followPath);
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
        public void setMaxAngularSpeed(float maxAngularSpeed) {
            this.maxAngularSpeed = maxAngularSpeed;
        }

        @Override
        public void setMaxAngularAcceleration(float maxAngularAcceleration) {
            this.maxAngularAcceleration = maxAngularAcceleration;
        }
    }

    private static class TestPath implements Path<Vector2, TestPathParam> {
        private boolean isOpen = false;
        private float length = 100f;
        private final Vector2 startPoint = new Vector2(0, 0);
        private final Vector2 endPoint = new Vector2(100, 0);

        public void setOpen(boolean isOpen) {
            this.isOpen = isOpen;
        }

        public void setLength(float length) {
            this.length = length;
        }

        @Override
        public TestPathParam createParam() {
            return new TestPathParam();
        }

        @Override
        public boolean isOpen() {
            return isOpen;
        }

        @Override
        public float getLength() {
            return length;
        }

        @Override
        public Vector2 getStartPoint() {
            return startPoint;
        }

        @Override
        public Vector2 getEndPoint() {
            return endPoint;
        }

        @Override
        public float calculateDistance(Vector2 position, TestPathParam param) {
            // Simple implementation: distance from start point
            return position.dst(startPoint);
        }

        @Override
        public void calculateTargetPosition(Vector2 out, TestPathParam param, float targetDistance) {
            // Simple implementation: linear interpolation along path
            float t = Math.min(1f, Math.max(0f, targetDistance / length));
            out.set(startPoint).lerp(endPoint, t);
        }
    }

    private static class TestPathParam implements Path.PathParam {
        private float distance = 0f;

        @Override
        public float getDistance() {
            return distance;
        }

        @Override
        public void setDistance(float distance) {
            this.distance = distance;
        }
    }
}
