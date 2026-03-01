package com.badlogic.gdx.ai.fma.patterns;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Unit tests for DefensiveCircleFormationPattern
 */
public class DefensiveCircleFormationPatternTest {

    private DefensiveCircleFormationPattern<Vector2> pattern2D;
    private DefensiveCircleFormationPattern<Vector3> pattern3D;
    private TestLocation2D testLocation2D;
    private TestLocation3D testLocation3D;

    @Before
    public void setUp() {
        pattern2D = new DefensiveCircleFormationPattern<>(1.0f);
        pattern3D = new DefensiveCircleFormationPattern<>(2.0f);
        testLocation2D = new TestLocation2D();
        testLocation3D = new TestLocation3D();
    }

    @Test
    public void testConstructor() {

        DefensiveCircleFormationPattern<Vector2> pattern = new DefensiveCircleFormationPattern<>(1.5f);
        Assert.assertEquals(1.5f, pattern.memberRadius, 0.001f);

        DefensiveCircleFormationPattern<Vector2> zeroPattern = new DefensiveCircleFormationPattern<>(0.0f);
        Assert.assertEquals(0.0f, zeroPattern.memberRadius, 0.001f);

        DefensiveCircleFormationPattern<Vector2> negativePattern = new DefensiveCircleFormationPattern<>(-1.0f);
        Assert.assertEquals(-1.0f, negativePattern.memberRadius, 0.001f);
    }

    @Test
    public void testSetNumberOfSlots() {

        pattern2D.setNumberOfSlots(5);
        Assert.assertEquals(5, getNumberOfSlots(pattern2D));

        pattern2D.setNumberOfSlots(0);
        Assert.assertEquals(0, getNumberOfSlots(pattern2D));

        pattern2D.setNumberOfSlots(-3);
        Assert.assertEquals(-3, getNumberOfSlots(pattern2D));

        pattern2D.setNumberOfSlots(1000);
        Assert.assertEquals(1000, getNumberOfSlots(pattern2D));
    }

    @Test
    public void testSupportsSlots() {
        Assert.assertTrue("Should support 0 slots", pattern2D.supportsSlots(0));
        Assert.assertTrue("Should support 1 slot", pattern2D.supportsSlots(1));
        Assert.assertTrue("Should support 2 slots", pattern2D.supportsSlots(2));
        Assert.assertTrue("Should support 5 slots", pattern2D.supportsSlots(5));
        Assert.assertTrue("Should support 10 slots", pattern2D.supportsSlots(10));
        Assert.assertTrue("Should support 100 slots", pattern2D.supportsSlots(100));
        Assert.assertTrue("Should support 1000 slots", pattern2D.supportsSlots(1000));
        Assert.assertTrue("Should support negative slots", pattern2D.supportsSlots(-1));
    }

    @Test
    public void testCalculateSlotLocationWithOneSlot() {
        pattern2D.setNumberOfSlots(1);

        Location<Vector2> result = pattern2D.calculateSlotLocation(testLocation2D, 0);
        Assert.assertSame(testLocation2D, result);

        Assert.assertEquals(0.0f, testLocation2D.getPosition().x, 0.001f);
        Assert.assertEquals(0.0f, testLocation2D.getPosition().y, 0.001f);

        Assert.assertEquals(0.0f, testLocation2D.getOrientation(), 0.001f);

        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 2);
        Assert.assertEquals(MathUtils.PI2 * 2, testLocation2D.getOrientation(), 0.001f);
    }

    @Test
    public void testCalculateSlotLocationWithMultipleSlots() {
        pattern2D.setNumberOfSlots(4);

        testLocation2D.reset();
        Location<Vector2> result = pattern2D.calculateSlotLocation(testLocation2D, 0);
        Assert.assertSame(testLocation2D, result);

        Assert.assertEquals(0.0f, testLocation2D.getOrientation(), 0.001f);

        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 1);
        Assert.assertEquals(MathUtils.PI2 / 4, testLocation2D.getOrientation(), 0.001f);

        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 2);
        Assert.assertEquals(MathUtils.PI2 / 2, testLocation2D.getOrientation(), 0.001f);
    }

    @Test
    public void testCalculateSlotLocationRadiusCalculation() {
        pattern2D.setNumberOfSlots(2);
        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 0);
        float expectedRadius2 = 1.0f / (float) Math.sin(Math.PI / 2);
        Assert.assertEquals(expectedRadius2, testLocation2D.getPosition().len(), 0.001f);

        pattern2D.setNumberOfSlots(3);
        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 0);
        float expectedRadius3 = 1.0f / (float) Math.sin(Math.PI / 3);
        Assert.assertEquals(expectedRadius3, testLocation2D.getPosition().len(), 0.001f);

        pattern2D.setNumberOfSlots(4);
        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 0);
        float expectedRadius4 = 1.0f / (float) Math.sin(Math.PI / 4);
        Assert.assertEquals(expectedRadius4, testLocation2D.getPosition().len(), 0.001f);
    }

    @Test
    public void testCalculateSlotLocationWithDifferentMemberRadius() {
        pattern3D.setNumberOfSlots(3);

        pattern3D.calculateSlotLocation(testLocation3D, 0);
        float expectedRadius = 2.0f / (float) Math.sin(Math.PI / 3);
        Assert.assertEquals(expectedRadius, testLocation3D.getPosition().len(), 0.001f);
    }

    @Test
    public void testCalculateSlotLocation3D() {
        pattern3D.setNumberOfSlots(4);

        testLocation3D.reset();
        Location<Vector3> result = pattern3D.calculateSlotLocation(testLocation3D, 1);
        Assert.assertSame(testLocation3D, result);

        Assert.assertEquals(MathUtils.PI2 / 4, testLocation3D.getOrientation(), 0.001f);
    }

    @Test
    public void testCalculateSlotLocationEdgeCases() {

        pattern2D.setNumberOfSlots(0);
        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 0);
        Assert.assertEquals(0.0f, testLocation2D.getPosition().x, 0.001f);
        Assert.assertEquals(0.0f, testLocation2D.getPosition().y, 0.001f);

        pattern2D.setNumberOfSlots(-1);
        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 0);
        Assert.assertEquals(0.0f, testLocation2D.getPosition().x, 0.001f);
        Assert.assertEquals(0.0f, testLocation2D.getPosition().y, 0.001f);
    }

    @Test
    public void testCalculateSlotLocationLargeNumberOfSlots() {
        pattern2D.setNumberOfSlots(100);

        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 50);

        float expectedAngle = (MathUtils.PI2 * 50) / 100;
        Assert.assertEquals(expectedAngle, testLocation2D.getOrientation(), 0.001f);
    }

    @Test
    public void testDefensiveCircleOrientation() {

        pattern2D.setNumberOfSlots(4);
        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 0);
        Assert.assertEquals(0.0f, testLocation2D.getOrientation(), 0.001f);

        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 1);
        Assert.assertEquals(MathUtils.PI2 / 4, testLocation2D.getOrientation(), 0.001f);

        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 2);
        Assert.assertEquals(MathUtils.PI2 / 2, testLocation2D.getOrientation(), 0.001f);
    }

    @Test
    public void testCalculateSlotLocationConsistency() {

        pattern2D.setNumberOfSlots(6);

        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 2);
        float expectedAngle = (MathUtils.PI2 * 2) / 6;
        float firstOrientation = testLocation2D.getOrientation();
        float firstRadius = testLocation2D.getPosition().len();

        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, 2);

        Assert.assertEquals(expectedAngle, testLocation2D.getOrientation(), 0.001f);
        Assert.assertEquals(firstOrientation, testLocation2D.getOrientation(), 0.001f);
        Assert.assertEquals(firstRadius, testLocation2D.getPosition().len(), 0.001f);
    }

    @Test
    public void testMemberRadiusEffectOnSpacing() {
        DefensiveCircleFormationPattern<Vector2> smallPattern = new DefensiveCircleFormationPattern<>(0.5f);
        DefensiveCircleFormationPattern<Vector2> largePattern = new DefensiveCircleFormationPattern<>(2.0f);
        
        smallPattern.setNumberOfSlots(4);
        largePattern.setNumberOfSlots(4);

        testLocation2D.reset();
        smallPattern.calculateSlotLocation(testLocation2D, 0);
        float smallRadius = testLocation2D.getPosition().len();

        testLocation2D.reset();
        largePattern.calculateSlotLocation(testLocation2D, 0);
        float largeRadius = testLocation2D.getPosition().len();

        Assert.assertEquals(largeRadius, smallRadius * 4, 0.001f);
    }

    @Test
    public void testCalculateSlotLocationWithZeroMemberRadius() {
        DefensiveCircleFormationPattern<Vector2> zeroRadiusPattern = new DefensiveCircleFormationPattern<>(0.0f);
        zeroRadiusPattern.setNumberOfSlots(4);
        
        testLocation2D.reset();
        zeroRadiusPattern.calculateSlotLocation(testLocation2D, 0);

        Assert.assertEquals(0.0f, testLocation2D.getPosition().len(), 0.001f);
    }

    @Test
    public void testCalculateSlotLocationWithNegativeSlotNumber() {
        pattern2D.setNumberOfSlots(4);

        testLocation2D.reset();
        pattern2D.calculateSlotLocation(testLocation2D, -1);

        float expectedAngle = (MathUtils.PI2 * -1) / 4;
        Assert.assertEquals(expectedAngle, testLocation2D.getOrientation(), 0.001f);
    }

    /**
     * Helper method to access the private numberOfSlots field using reflection
     */
    private int getNumberOfSlots(DefensiveCircleFormationPattern<?> pattern) {
        try {
            java.lang.reflect.Field field = DefensiveCircleFormationPattern.class.getDeclaredField("numberOfSlots");
            field.setAccessible(true);
            return field.getInt(pattern);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access numberOfSlots field", e);
        }
    }

    /**
     * Test implementation of Location interface for Vector2
     */
    private static class TestLocation2D implements Location<Vector2> {
        private final Vector2 position = new Vector2();
        private float orientation = 0.0f;

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
            outVector.x = (float) Math.cos(angle);
            outVector.y = (float) Math.sin(angle);
            return outVector;
        }

        @Override
        public Location<Vector2> newLocation() {
            return new TestLocation2D();
        }

        public void reset() {
            position.set(0, 0);
            orientation = 0.0f;
        }
    }

    /**
     * Test implementation of Location interface for Vector3
     */
    private static class TestLocation3D implements Location<Vector3> {
        private final Vector3 position = new Vector3();
        private float orientation = 0.0f;

        @Override
        public Vector3 getPosition() {
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
        public float vectorToAngle(Vector3 vector) {
            return (float) Math.atan2(vector.y, vector.x);
        }

        @Override
        public Vector3 angleToVector(Vector3 outVector, float angle) {
            outVector.x = (float) Math.cos(angle);
            outVector.y = (float) Math.sin(angle);
            outVector.z = 0;
            return outVector;
        }

        @Override
        public Location<Vector3> newLocation() {
            return new TestLocation3D();
        }

        public void reset() {
            position.set(0, 0, 0);
            orientation = 0.0f;
        }
    }
}
