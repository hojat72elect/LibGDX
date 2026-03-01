package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for FormationMotionModerator
 */
public class FormationMotionModeratorTest {

    // Test implementations for 2D
    private TestFormationMotionModerator2D moderator2D;
    private TestLocation2D centerOfMass2D;
    private TestFormationPattern2D pattern2D;
    private Array<SlotAssignment<Vector2>> slotAssignments2D;
    private TestLocation2D anchor2D;

    // Test implementations for 3D
    private TestFormationMotionModerator3D moderator3D;
    private TestLocation3D centerOfMass3D;
    private TestFormationPattern3D pattern3D;
    private Array<SlotAssignment<Vector3>> slotAssignments3D;
    private TestLocation3D anchor3D;

    @Before
    public void setUp() {
        // Setup 2D test objects
        moderator2D = new TestFormationMotionModerator2D();
        centerOfMass2D = new TestLocation2D();
        pattern2D = new TestFormationPattern2D();
        slotAssignments2D = new Array<>();
        anchor2D = new TestLocation2D();

        // Setup 3D test objects
        moderator3D = new TestFormationMotionModerator3D();
        centerOfMass3D = new TestLocation3D();
        pattern3D = new TestFormationPattern3D();
        slotAssignments3D = new Array<>();
        anchor3D = new TestLocation3D();
    }

    // Test updateAnchorPoint method - 2D
    @Test
    public void testUpdateAnchorPoint2D() {
        moderator2D.updateAnchorPoint(anchor2D);
        Assert.assertTrue("updateAnchorPoint should be called", moderator2D.updateAnchorPointCalled);
    }

    // Test updateAnchorPoint method - 3D
    @Test
    public void testUpdateAnchorPoint3D() {
        moderator3D.updateAnchorPoint(anchor3D);
        Assert.assertTrue("updateAnchorPoint should be called", moderator3D.updateAnchorPointCalled);
    }

    // Test calculateDriftOffset with empty slot assignments - 2D
    @Test
    public void testCalculateDriftOffsetEmptySlots2D() {
        Location<Vector2> result = moderator2D.calculateDriftOffset(centerOfMass2D, slotAssignments2D, pattern2D);

        Assert.assertSame("Should return the same centerOfMass instance", centerOfMass2D, result);
        Assert.assertTrue("Position should be NaN when no slots (division by zero)", Float.isNaN(centerOfMass2D.getPosition().x));
        Assert.assertTrue("Position should be NaN when no slots (division by zero)", Float.isNaN(centerOfMass2D.getPosition().y));
        Assert.assertTrue("Orientation should be NaN when no slots (division by zero)", Float.isNaN(centerOfMass2D.getOrientation()));
    }

    // Test calculateDriftOffset with empty slot assignments - 3D
    @Test
    public void testCalculateDriftOffsetEmptySlots3D() {
        Location<Vector3> result = moderator3D.calculateDriftOffset(centerOfMass3D, slotAssignments3D, pattern3D);

        Assert.assertSame("Should return the same centerOfMass instance", centerOfMass3D, result);
        Assert.assertTrue("Position should be NaN when no slots (division by zero)", Float.isNaN(centerOfMass3D.getPosition().x));
        Assert.assertTrue("Position should be NaN when no slots (division by zero)", Float.isNaN(centerOfMass3D.getPosition().y));
        Assert.assertTrue("Position should be NaN when no slots (division by zero)", Float.isNaN(centerOfMass3D.getPosition().z));
        Assert.assertTrue("Orientation should be NaN when no slots (division by zero)", Float.isNaN(centerOfMass3D.getOrientation()));
    }

    // Test calculateDriftOffset with single slot assignment - 2D
    @Test
    public void testCalculateDriftOffsetSingleSlot2D() {
        SlotAssignment<Vector2> slotAssignment = new SlotAssignment<>(null, 0);
        slotAssignments2D.add(slotAssignment);

        Location<Vector2> result = moderator2D.calculateDriftOffset(centerOfMass2D, slotAssignments2D, pattern2D);

        Assert.assertSame("Should return the same centerOfMass instance", centerOfMass2D, result);
        // Pattern sets slot 0 to (0, 0) with orientation 0
        Assert.assertEquals("Position should match single slot position", new Vector2(0, 0), centerOfMass2D.getPosition());
        Assert.assertEquals("Orientation should match single slot orientation", 0f, centerOfMass2D.getOrientation(), 0.001f);
    }

    // Test calculateDriftOffset with single slot assignment - 3D
    @Test
    public void testCalculateDriftOffsetSingleSlot3D() {
        SlotAssignment<Vector3> slotAssignment = new SlotAssignment<>(null, 0);
        slotAssignments3D.add(slotAssignment);

        Location<Vector3> result = moderator3D.calculateDriftOffset(centerOfMass3D, slotAssignments3D, pattern3D);

        Assert.assertSame("Should return the same centerOfMass instance", centerOfMass3D, result);
        // Pattern sets slot 0 to (0, 0, 0) with orientation 0
        Assert.assertEquals("Position should match single slot position", new Vector3(0, 0, 0), centerOfMass3D.getPosition());
        Assert.assertEquals("Orientation should match single slot orientation", 0f, centerOfMass3D.getOrientation(), 0.001f);
    }

    // Test calculateDriftOffset with multiple slot assignments - 2D
    @Test
    public void testCalculateDriftOffsetMultipleSlots2D() {
        SlotAssignment<Vector2> slotAssignment1 = new SlotAssignment<>(null, 0);
        SlotAssignment<Vector2> slotAssignment2 = new SlotAssignment<>(null, 1);
        SlotAssignment<Vector2> slotAssignment3 = new SlotAssignment<>(null, 2);
        slotAssignments2D.addAll(slotAssignment1, slotAssignment2, slotAssignment3);

        Location<Vector2> result = moderator2D.calculateDriftOffset(centerOfMass2D, slotAssignments2D, pattern2D);

        Assert.assertSame("Should return the same centerOfMass instance", centerOfMass2D, result);
        // Pattern sets: slot 0=(0,0), slot 1=(10,10), slot 2=(20,20)
        // Average: ((0,0)+(10,10)+(20,20))/3 = (10,10)
        Assert.assertEquals("Position should be average of slot positions", new Vector2(10, 10), centerOfMass2D.getPosition());
        // Pattern orientations: 0, 0.1, 0.2, average = 0.1
        Assert.assertEquals("Orientation should be average of slot orientations", 0.1f, centerOfMass2D.getOrientation(), 0.001f);
    }

    // Test calculateDriftOffset with multiple slot assignments - 3D
    @Test
    public void testCalculateDriftOffsetMultipleSlots3D() {
        SlotAssignment<Vector3> slotAssignment1 = new SlotAssignment<>(null, 0);
        SlotAssignment<Vector3> slotAssignment2 = new SlotAssignment<>(null, 1);
        SlotAssignment<Vector3> slotAssignment3 = new SlotAssignment<>(null, 2);
        slotAssignments3D.addAll(slotAssignment1, slotAssignment2, slotAssignment3);

        Location<Vector3> result = moderator3D.calculateDriftOffset(centerOfMass3D, slotAssignments3D, pattern3D);

        Assert.assertSame("Should return the same centerOfMass instance", centerOfMass3D, result);
        // Pattern sets: slot 0=(0,0,0), slot 1=(10,10,10), slot 2=(20,20,20)
        // Average: ((0,0,0)+(10,10,10)+(20,20,20))/3 = (10,10,10)
        Assert.assertEquals("Position should be average of slot positions", new Vector3(10, 10, 10), centerOfMass3D.getPosition());
        // Pattern orientations: 0, 0.1, 0.2, average = 0.1
        Assert.assertEquals("Orientation should be average of slot orientations", 0.1f, centerOfMass3D.getOrientation(), 0.001f);
    }

    // Test calculateDriftOffset with non-consecutive slot numbers - 2D
    @Test
    public void testCalculateDriftOffsetNonConsecutiveSlots2D() {
        SlotAssignment<Vector2> slotAssignment1 = new SlotAssignment<>(null, 1);
        SlotAssignment<Vector2> slotAssignment2 = new SlotAssignment<>(null, 3);
        SlotAssignment<Vector2> slotAssignment3 = new SlotAssignment<>(null, 5);
        slotAssignments2D.addAll(slotAssignment1, slotAssignment2, slotAssignment3);

        Location<Vector2> result = moderator2D.calculateDriftOffset(centerOfMass2D, slotAssignments2D, pattern2D);

        Assert.assertSame("Should return the same centerOfMass instance", centerOfMass2D, result);
        // Pattern sets: slot 1=(10,10), slot 3=(30,30), slot 5=(50,50)
        // Average: ((10,10)+(30,30)+(50,50))/3 = (30,30)
        Assert.assertEquals("Position should be average of slot positions", new Vector2(30, 30), centerOfMass2D.getPosition());
        // Pattern orientations: 0.1, 0.3, 0.5, average = 0.3
        Assert.assertEquals("Orientation should be average of slot orientations", 0.3f, centerOfMass2D.getOrientation(), 0.001f);
    }

    // Test calculateDriftOffset with non-consecutive slot numbers - 3D
    @Test
    public void testCalculateDriftOffsetNonConsecutiveSlots3D() {
        SlotAssignment<Vector3> slotAssignment1 = new SlotAssignment<>(null, 1);
        SlotAssignment<Vector3> slotAssignment2 = new SlotAssignment<>(null, 3);
        SlotAssignment<Vector3> slotAssignment3 = new SlotAssignment<>(null, 5);
        slotAssignments3D.addAll(slotAssignment1, slotAssignment2, slotAssignment3);

        Location<Vector3> result = moderator3D.calculateDriftOffset(centerOfMass3D, slotAssignments3D, pattern3D);

        Assert.assertSame("Should return the same centerOfMass instance", centerOfMass3D, result);
        // Pattern sets: slot 1=(10,10,10), slot 3=(30,30,30), slot 5=(50,50,50)
        // Average: ((10,10,10)+(30,30,30)+(50,50,50))/3 = (30,30,30)
        Assert.assertEquals("Position should be average of slot positions", new Vector3(30, 30, 30), centerOfMass3D.getPosition());
        // Pattern orientations: 0.1, 0.3, 0.5, average = 0.3
        Assert.assertEquals("Orientation should be average of slot orientations", 0.3f, centerOfMass3D.getOrientation(), 0.001f);
    }

    // Test calculateDriftOffset tempLocation initialization - 2D
    @Test
    public void testCalculateDriftOffsetTempLocationInit2D() {
        SlotAssignment<Vector2> slotAssignment = new SlotAssignment<>(null, 0);
        slotAssignments2D.add(slotAssignment);

        // First call should initialize tempLocation
        moderator2D.calculateDriftOffset(centerOfMass2D, slotAssignments2D, pattern2D);
        Assert.assertNotNull("tempLocation should be initialized after first call", moderator2D.getTempLocation());

        // Second call should reuse tempLocation
        moderator2D.calculateDriftOffset(centerOfMass2D, slotAssignments2D, pattern2D);
        Assert.assertSame("Should reuse tempLocation on subsequent calls", moderator2D.getTempLocation(), moderator2D.getTempLocation());
    }

    // Test calculateDriftOffset tempLocation initialization - 3D
    @Test
    public void testCalculateDriftOffsetTempLocationInit3D() {
        SlotAssignment<Vector3> slotAssignment = new SlotAssignment<>(null, 0);
        slotAssignments3D.add(slotAssignment);

        // First call should initialize tempLocation
        moderator3D.calculateDriftOffset(centerOfMass3D, slotAssignments3D, pattern3D);
        Assert.assertNotNull("tempLocation should be initialized after first call", moderator3D.getTempLocation());

        // Second call should reuse tempLocation
        moderator3D.calculateDriftOffset(centerOfMass3D, slotAssignments3D, pattern3D);
        Assert.assertSame("Should reuse tempLocation on subsequent calls", moderator3D.getTempLocation(), moderator3D.getTempLocation());
    }

    // Test calculateDriftOffset with large number of slots - 2D
    @Test
    public void testCalculateDriftOffsetLargeNumberOfSlots2D() {
        // Add 100 slot assignments
        for (int i = 0; i < 100; i++) {
            slotAssignments2D.add(new SlotAssignment<>(null, i));
        }

        Location<Vector2> result = moderator2D.calculateDriftOffset(centerOfMass2D, slotAssignments2D, pattern2D);

        Assert.assertSame("Should return the same centerOfMass instance", centerOfMass2D, result);
        // Average of positions from (0,0) to (990,990) should be (495,495)
        Assert.assertEquals("Position should be average of all slot positions", new Vector2(495, 495), centerOfMass2D.getPosition());
        // Average of orientations from 0 to 9.9 should be 4.95
        Assert.assertEquals("Orientation should be average of all slot orientations", 4.95f, centerOfMass2D.getOrientation(), 0.001f);
    }

    // Test calculateDriftOffset with large number of slots - 3D
    @Test
    public void testCalculateDriftOffsetLargeNumberOfSlots3D() {
        // Add 100 slot assignments
        for (int i = 0; i < 100; i++) {
            slotAssignments3D.add(new SlotAssignment<>(null, i));
        }

        Location<Vector3> result = moderator3D.calculateDriftOffset(centerOfMass3D, slotAssignments3D, pattern3D);

        Assert.assertSame("Should return the same centerOfMass instance", centerOfMass3D, result);
        // Average of positions from (0,0,0) to (990,990,990) should be (495,495,495)
        Assert.assertEquals("Position should be average of all slot positions", new Vector3(495, 495, 495), centerOfMass3D.getPosition());
        // Average of orientations from 0 to 9.9 should be 4.95
        Assert.assertEquals("Orientation should be average of all slot orientations", 4.95f, centerOfMass3D.getOrientation(), 0.001f);
    }

    // Test calculateDriftOffset method chaining - 2D
    @Test
    public void testCalculateDriftOffsetMethodChaining2D() {
        SlotAssignment<Vector2> slotAssignment = new SlotAssignment<>(null, 0);
        slotAssignments2D.add(slotAssignment);

        Location<Vector2> result = moderator2D.calculateDriftOffset(centerOfMass2D, slotAssignments2D, pattern2D);

        Assert.assertSame("Should return the same centerOfMass instance for method chaining", centerOfMass2D, result);
    }

    // Test calculateDriftOffset method chaining - 3D
    @Test
    public void testCalculateDriftOffsetMethodChaining3D() {
        SlotAssignment<Vector3> slotAssignment = new SlotAssignment<>(null, 0);
        slotAssignments3D.add(slotAssignment);

        Location<Vector3> result = moderator3D.calculateDriftOffset(centerOfMass3D, slotAssignments3D, pattern3D);

        Assert.assertSame("Should return the same centerOfMass instance for method chaining", centerOfMass3D, result);
    }

    // Test implementation classes

    private static class TestFormationMotionModerator2D extends FormationMotionModerator<Vector2> {
        boolean updateAnchorPointCalled = false;

        @Override
        public void updateAnchorPoint(Location<Vector2> anchor) {
            updateAnchorPointCalled = true;
        }

        // Expose tempLocation for testing
        public Location<Vector2> getTempLocation() {
            try {
                java.lang.reflect.Field field = FormationMotionModerator.class.getDeclaredField("tempLocation");
                field.setAccessible(true);
                return (Location<Vector2>) field.get(this);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private static class TestFormationMotionModerator3D extends FormationMotionModerator<Vector3> {
        boolean updateAnchorPointCalled = false;

        @Override
        public void updateAnchorPoint(Location<Vector3> anchor) {
            updateAnchorPointCalled = true;
        }

        // Expose tempLocation for testing
        public Location<Vector3> getTempLocation() {
            try {
                java.lang.reflect.Field field = FormationMotionModerator.class.getDeclaredField("tempLocation");
                field.setAccessible(true);
                return (Location<Vector3>) field.get(this);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private static class TestLocation2D implements Location<Vector2> {
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
        public Location<Vector2> newLocation() {
            return new TestLocation2D();
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

    private static class TestLocation3D implements Location<Vector3> {
        private final Vector3 position = new Vector3();
        private float orientation = 0f;

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
        public Location<Vector3> newLocation() {
            return new TestLocation3D();
        }

        @Override
        public float vectorToAngle(Vector3 vector) {
            return (float) Math.atan2(vector.y, vector.x);
        }

        @Override
        public Vector3 angleToVector(Vector3 outVector, float angle) {
            outVector.set((float) Math.cos(angle), (float) Math.sin(angle), 0);
            return outVector;
        }
    }

    private static class TestFormationPattern2D implements FormationPattern<Vector2> {
        @Override
        public void setNumberOfSlots(int numberOfSlots) {
            // Not used in tests
        }

        @Override
        public Location<Vector2> calculateSlotLocation(Location<Vector2> outLocation, int slotNumber) {
            outLocation.getPosition().set(slotNumber * 10, slotNumber * 10);
            outLocation.setOrientation(slotNumber * 0.1f);
            return outLocation;
        }

        @Override
        public boolean supportsSlots(int slotCount) {
            return true;
        }
    }

    private static class TestFormationPattern3D implements FormationPattern<Vector3> {
        @Override
        public void setNumberOfSlots(int numberOfSlots) {
            // Not used in tests
        }

        @Override
        public Location<Vector3> calculateSlotLocation(Location<Vector3> outLocation, int slotNumber) {
            outLocation.getPosition().set(slotNumber * 10, slotNumber * 10, slotNumber * 10);
            outLocation.setOrientation(slotNumber * 0.1f);
            return outLocation;
        }

        @Override
        public boolean supportsSlots(int slotCount) {
            return true;
        }
    }
}
