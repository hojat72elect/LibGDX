package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for Formation
 */
public class FormationTest {

    // Test implementations for 2D
    private TestLocation2D testAnchor2D;
    private TestFormationPattern2D testPattern2D;
    private TestSlotAssignmentStrategy2D testStrategy2D;
    private TestFormationMotionModerator2D testModerator2D;
    private Formation<Vector2> formation2D;
    private TestFormationMember2D member1_2D;
    private TestFormationMember2D member2_2D;
    private TestFormationMember2D member3_2D;

    // Test implementations for 3D
    private TestLocation3D testAnchor3D;
    private TestFormationPattern3D testPattern3D;
    private TestSlotAssignmentStrategy3D testStrategy3D;
    private TestFormationMotionModerator3D testModerator3D;
    private Formation<Vector3> formation3D;
    private TestFormationMember3D member1_3D;

    @Before
    public void setUp() {
        // Setup 2D test objects
        testAnchor2D = new TestLocation2D();
        testPattern2D = new TestFormationPattern2D();
        testStrategy2D = new TestSlotAssignmentStrategy2D();
        testModerator2D = new TestFormationMotionModerator2D();
        formation2D = new Formation<>(testAnchor2D, testPattern2D, testStrategy2D, testModerator2D);
        member1_2D = new TestFormationMember2D();
        member2_2D = new TestFormationMember2D();
        member3_2D = new TestFormationMember2D();

        // Setup 3D test objects
        testAnchor3D = new TestLocation3D();
        testPattern3D = new TestFormationPattern3D();
        testStrategy3D = new TestSlotAssignmentStrategy3D();
        testModerator3D = new TestFormationMotionModerator3D();
        formation3D = new Formation<>(testAnchor3D, testPattern3D, testStrategy3D, testModerator3D);
        member1_3D = new TestFormationMember3D();
    }

    // Constructor tests

    @Test
    public void testConstructorWithAnchorAndPattern() {
        Formation<Vector2> formation = new Formation<>(testAnchor2D, testPattern2D);
        Assert.assertNotNull(formation);
        Assert.assertEquals(testAnchor2D, formation.getAnchorPoint());
        Assert.assertEquals(testPattern2D, formation.getPattern());
        Assert.assertNotNull(formation.getSlotAssignmentStrategy());
        Assert.assertNull(formation.getMotionModerator());
        Assert.assertEquals(0, formation.getSlotAssignmentCount());
    }

    @Test
    public void testConstructorWithAnchorPatternAndStrategy() {
        Formation<Vector2> formation = new Formation<>(testAnchor2D, testPattern2D, testStrategy2D);
        Assert.assertNotNull(formation);
        Assert.assertEquals(testAnchor2D, formation.getAnchorPoint());
        Assert.assertEquals(testPattern2D, formation.getPattern());
        Assert.assertEquals(testStrategy2D, formation.getSlotAssignmentStrategy());
        Assert.assertNull(formation.getMotionModerator());
        Assert.assertEquals(0, formation.getSlotAssignmentCount());
    }

    @Test
    public void testConstructorWithAllParameters() {
        Formation<Vector2> formation = new Formation<>(testAnchor2D, testPattern2D, testStrategy2D, testModerator2D);
        Assert.assertNotNull(formation);
        Assert.assertEquals(testAnchor2D, formation.getAnchorPoint());
        Assert.assertEquals(testPattern2D, formation.getPattern());
        Assert.assertEquals(testStrategy2D, formation.getSlotAssignmentStrategy());
        Assert.assertEquals(testModerator2D, formation.getMotionModerator());
        Assert.assertEquals(0, formation.getSlotAssignmentCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullAnchor() {
        new Formation<>(null, testPattern2D);
    }

    // Getter/Setter tests

    @Test
    public void testGetAndSetAnchorPoint() {
        TestLocation2D newAnchor = new TestLocation2D();
        formation2D.setAnchorPoint(newAnchor);
        Assert.assertEquals(newAnchor, formation2D.getAnchorPoint());
    }

    @Test
    public void testGetAndSetPattern() {
        TestFormationPattern2D newPattern = new TestFormationPattern2D();
        formation2D.setPattern(newPattern);
        Assert.assertEquals(newPattern, formation2D.getPattern());
    }

    @Test
    public void testGetAndSetSlotAssignmentStrategy() {
        TestSlotAssignmentStrategy2D newStrategy = new TestSlotAssignmentStrategy2D();
        formation2D.setSlotAssignmentStrategy(newStrategy);
        Assert.assertEquals(newStrategy, formation2D.getSlotAssignmentStrategy());
    }

    @Test
    public void testGetAndSetMotionModerator() {
        TestFormationMotionModerator2D newModerator = new TestFormationMotionModerator2D();
        formation2D.setMotionModerator(newModerator);
        Assert.assertEquals(newModerator, formation2D.getMotionModerator());
    }

    @Test
    public void testSetMotionModeratorToNull() {
        formation2D.setMotionModerator(null);
        Assert.assertNull(formation2D.getMotionModerator());
    }

    // updateSlotAssignments tests

    @Test
    public void testUpdateSlotAssignments() {
        // Add some members first
        formation2D.addMember(member1_2D);
        formation2D.addMember(member2_2D);

        // Update slot assignments
        formation2D.updateSlotAssignments();

        // Verify strategy was called
        Assert.assertTrue(testStrategy2D.updateSlotAssignmentsCalled);
        Assert.assertTrue(testPattern2D.setNumberOfSlotsCalled);

        // Verify moderator was called
        Assert.assertTrue(testModerator2D.calculateDriftOffsetCalled);
    }

    @Test
    public void testUpdateSlotAssignmentsWithoutModerator() {
        Formation<Vector2> formationWithoutModerator = new Formation<>(testAnchor2D, testPattern2D, testStrategy2D);

        // Add some members first
        formationWithoutModerator.addMember(member1_2D);

        // Update slot assignments
        formationWithoutModerator.updateSlotAssignments();

        // Verify strategy was called
        Assert.assertTrue(testStrategy2D.updateSlotAssignmentsCalled);
        Assert.assertTrue(testPattern2D.setNumberOfSlotsCalled);
    }

    // changePattern tests

    @Test
    public void testChangePatternSuccessful() {
        // Add a member first
        formation2D.addMember(member1_2D);

        // Create a new pattern that supports 1 slot
        TestFormationPattern2D newPattern = new TestFormationPattern2D();
        newPattern.supportedSlots = 1;

        // Change pattern
        boolean result = formation2D.changePattern(newPattern);

        Assert.assertTrue(result);
        Assert.assertEquals(newPattern, formation2D.getPattern());
        Assert.assertTrue(testStrategy2D.updateSlotAssignmentsCalled);
    }

    @Test
    public void testChangePatternUnsuccessful() {
        // Add a member first
        formation2D.addMember(member1_2D);

        // Create a new pattern that doesn't support 1 slot
        TestFormationPattern2D newPattern = new TestFormationPattern2D();
        newPattern.supportedSlots = 0;

        // Change pattern
        boolean result = formation2D.changePattern(newPattern);

        Assert.assertFalse(result);
        Assert.assertEquals(testPattern2D, formation2D.getPattern()); // Should remain unchanged
    }

    // addMember tests

    @Test
    public void testAddMemberSuccessful() {
        boolean result = formation2D.addMember(member1_2D);

        Assert.assertTrue(result);
        Assert.assertEquals(1, formation2D.getSlotAssignmentCount());
        Assert.assertEquals(member1_2D, formation2D.getSlotAssignmentAt(0).member);
        Assert.assertEquals(0, formation2D.getSlotAssignmentAt(0).slotNumber);
        Assert.assertTrue(testStrategy2D.updateSlotAssignmentsCalled);
        Assert.assertTrue(testPattern2D.setNumberOfSlotsCalled);
    }

    @Test
    public void testAddMemberUnsuccessful() {
        // Create a pattern that doesn't support any slots
        testPattern2D.supportedSlots = 0;

        boolean result = formation2D.addMember(member1_2D);

        Assert.assertFalse(result);
        Assert.assertEquals(0, formation2D.getSlotAssignmentCount());
    }

    @Test
    public void testAddMultipleMembers() {
        formation2D.addMember(member1_2D);
        formation2D.addMember(member2_2D);
        formation2D.addMember(member3_2D);

        Assert.assertEquals(3, formation2D.getSlotAssignmentCount());
        Assert.assertEquals(member1_2D, formation2D.getSlotAssignmentAt(0).member);
        Assert.assertEquals(member2_2D, formation2D.getSlotAssignmentAt(1).member);
        Assert.assertEquals(member3_2D, formation2D.getSlotAssignmentAt(2).member);
    }

    // removeMember tests

    @Test
    public void testRemoveMemberExisting() {
        // Add members first
        formation2D.addMember(member1_2D);
        formation2D.addMember(member2_2D);
        formation2D.addMember(member3_2D);

        // Reset call flags
        testStrategy2D.resetCallFlags();

        // Remove middle member
        formation2D.removeMember(member2_2D);

        Assert.assertEquals(2, formation2D.getSlotAssignmentCount());
        Assert.assertEquals(member1_2D, formation2D.getSlotAssignmentAt(0).member);
        Assert.assertEquals(member3_2D, formation2D.getSlotAssignmentAt(1).member);
        Assert.assertTrue(testStrategy2D.removeSlotAssignmentCalled);
        Assert.assertTrue(testStrategy2D.updateSlotAssignmentsCalled);
    }

    @Test
    public void testRemoveMemberNonExisting() {
        // Add one member
        formation2D.addMember(member1_2D);

        // Reset call flags
        testStrategy2D.resetCallFlags();

        // Try to remove non-existing member
        formation2D.removeMember(member2_2D);

        Assert.assertEquals(1, formation2D.getSlotAssignmentCount());
        Assert.assertFalse(testStrategy2D.removeSlotAssignmentCalled);
        Assert.assertFalse(testStrategy2D.updateSlotAssignmentsCalled);
    }

    @Test
    public void testRemoveMemberFromEmptyFormation() {
        formation2D.removeMember(member1_2D);

        Assert.assertEquals(0, formation2D.getSlotAssignmentCount());
    }

    // updateSlots tests

    @Test
    public void testUpdateSlotsWithoutModerator() {
        Formation<Vector2> formationWithoutModerator = new Formation<>(testAnchor2D, testPattern2D, testStrategy2D);

        // Add a member
        formationWithoutModerator.addMember(member1_2D);

        // Update slots
        formationWithoutModerator.updateSlots();

        // Verify slot location was calculated
        Assert.assertTrue(testPattern2D.calculateSlotLocationCalled);

        // Verify member's target location was updated
        Assert.assertTrue(member1_2D.targetLocationUpdated);
    }

    @Test
    public void testUpdateSlotsWithModerator() {
        // Add a member
        formation2D.addMember(member1_2D);

        // Update slots
        formation2D.updateSlots();

        // Verify slot location was calculated
        Assert.assertTrue(testPattern2D.calculateSlotLocationCalled);

        // Verify member's target location was updated
        Assert.assertTrue(member1_2D.targetLocationUpdated);

        // Verify moderator's updateAnchorPoint was called
        Assert.assertTrue(testModerator2D.updateAnchorPointCalled);
    }

    @Test
    public void testUpdateSlotsWithMultipleMembers() {
        // Add multiple members
        formation2D.addMember(member1_2D);
        formation2D.addMember(member2_2D);
        formation2D.addMember(member3_2D);

        // Update slots
        formation2D.updateSlots();

        // Verify all members' target locations were updated
        Assert.assertTrue(member1_2D.targetLocationUpdated);
        Assert.assertTrue(member2_2D.targetLocationUpdated);
        Assert.assertTrue(member3_2D.targetLocationUpdated);
    }

    // 3D vector support tests

    @Test
    public void testFormationWithVector3() {
        Formation<Vector3> formation = new Formation<>(testAnchor3D, testPattern3D, testStrategy3D, testModerator3D);

        Assert.assertNotNull(formation);
        Assert.assertEquals(testAnchor3D, formation.getAnchorPoint());
        Assert.assertEquals(testPattern3D, formation.getPattern());
        Assert.assertEquals(testStrategy3D, formation.getSlotAssignmentStrategy());
        Assert.assertEquals(testModerator3D, formation.getMotionModerator());
    }

    @Test
    public void testAddMemberWithVector3() {
        boolean result = formation3D.addMember(member1_3D);

        Assert.assertTrue(result);
        Assert.assertEquals(1, formation3D.getSlotAssignmentCount());
        Assert.assertEquals(member1_3D, formation3D.getSlotAssignmentAt(0).member);
    }

    @Test
    public void testUpdateSlotsWithVector3() {
        // Add a member
        formation3D.addMember(member1_3D);

        // Update slots
        formation3D.updateSlots();

        // Verify slot location was calculated
        Assert.assertTrue(testPattern3D.calculateSlotLocationCalled);

        // Verify member's target location was updated
        Assert.assertTrue(member1_3D.targetLocationUpdated);
    }

    // Edge case tests

    @Test
    public void testFormationWithNullModerator() {
        Formation<Vector2> formation = new Formation<>(testAnchor2D, testPattern2D, testStrategy2D, null);

        Assert.assertNull(formation.getMotionModerator());

        // Add member and update slots - should work without moderator
        formation.addMember(member1_2D);
        formation.updateSlots();

        Assert.assertTrue(member1_2D.targetLocationUpdated);
    }

    @Test
    public void testGetSlotAssignmentAtInvalidIndex() {
        // Should throw exception when accessing invalid index
        try {
            formation2D.getSlotAssignmentAt(0);
            Assert.fail("Expected exception");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testMultipleOperations() {
        // Test multiple add/remove operations
        Assert.assertTrue(formation2D.addMember(member1_2D));
        Assert.assertTrue(formation2D.addMember(member2_2D));
        Assert.assertEquals(2, formation2D.getSlotAssignmentCount());

        formation2D.removeMember(member1_2D);
        Assert.assertEquals(1, formation2D.getSlotAssignmentCount());
        Assert.assertEquals(member2_2D, formation2D.getSlotAssignmentAt(0).member);

        Assert.assertTrue(formation2D.addMember(member3_2D));
        Assert.assertEquals(2, formation2D.getSlotAssignmentCount());
    }

    // Test implementation classes

    private static class TestLocation2D implements Location<Vector2> {
        private final Vector2 position = new Vector2(1, 2);
        private float orientation = 0.5f;

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
            return 0;
        }

        @Override
        public Vector2 angleToVector(Vector2 outVector, float angle) {
            return outVector.set(1, 0);
        }
    }

    private static class TestLocation3D implements Location<Vector3> {
        private final Vector3 position = new Vector3(1, 2, 3);
        private float orientation = 0.5f;

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
            return 0;
        }

        @Override
        public Vector3 angleToVector(Vector3 outVector, float angle) {
            return outVector.set(1, 0, 0);
        }
    }

    private static class TestFormationPattern2D implements FormationPattern<Vector2> {
        int supportedSlots = Integer.MAX_VALUE;
        boolean setNumberOfSlotsCalled = false;
        boolean calculateSlotLocationCalled = false;

        @Override
        public void setNumberOfSlots(int numberOfSlots) {
            setNumberOfSlotsCalled = true;
        }

        @Override
        public Location<Vector2> calculateSlotLocation(Location<Vector2> outLocation, int slotNumber) {
            calculateSlotLocationCalled = true;
            outLocation.getPosition().set(slotNumber * 10, slotNumber * 10);
            outLocation.setOrientation(slotNumber * 0.1f);
            return outLocation;
        }

        @Override
        public boolean supportsSlots(int slotCount) {
            return slotCount <= supportedSlots;
        }
    }

    private static class TestFormationPattern3D implements FormationPattern<Vector3> {
        int supportedSlots = Integer.MAX_VALUE;
        boolean setNumberOfSlotsCalled = false;
        boolean calculateSlotLocationCalled = false;

        @Override
        public void setNumberOfSlots(int numberOfSlots) {
            setNumberOfSlotsCalled = true;
        }

        @Override
        public Location<Vector3> calculateSlotLocation(Location<Vector3> outLocation, int slotNumber) {
            calculateSlotLocationCalled = true;
            outLocation.getPosition().set(slotNumber * 10, slotNumber * 10, slotNumber * 10);
            outLocation.setOrientation(slotNumber * 0.1f);
            return outLocation;
        }

        @Override
        public boolean supportsSlots(int slotCount) {
            return slotCount <= supportedSlots;
        }
    }

    private static class TestSlotAssignmentStrategy2D implements SlotAssignmentStrategy<Vector2> {
        boolean updateSlotAssignmentsCalled = false;
        boolean removeSlotAssignmentCalled = false;

        @Override
        public void updateSlotAssignments(Array<SlotAssignment<Vector2>> assignments) {
            updateSlotAssignmentsCalled = true;
        }

        @Override
        public int calculateNumberOfSlots(Array<SlotAssignment<Vector2>> assignments) {
            return assignments.size;
        }

        @Override
        public void removeSlotAssignment(Array<SlotAssignment<Vector2>> assignments, int index) {
            removeSlotAssignmentCalled = true;
            assignments.removeIndex(index);
        }

        public void resetCallFlags() {
            updateSlotAssignmentsCalled = false;
            removeSlotAssignmentCalled = false;
        }
    }

    private static class TestSlotAssignmentStrategy3D implements SlotAssignmentStrategy<Vector3> {
        boolean updateSlotAssignmentsCalled = false;
        boolean removeSlotAssignmentCalled = false;

        @Override
        public void updateSlotAssignments(Array<SlotAssignment<Vector3>> assignments) {
            updateSlotAssignmentsCalled = true;
        }

        @Override
        public int calculateNumberOfSlots(Array<SlotAssignment<Vector3>> assignments) {
            return assignments.size;
        }

        @Override
        public void removeSlotAssignment(Array<SlotAssignment<Vector3>> assignments, int index) {
            removeSlotAssignmentCalled = true;
            assignments.removeIndex(index);
        }
    }

    private static class TestFormationMotionModerator2D extends FormationMotionModerator<Vector2> {
        boolean calculateDriftOffsetCalled = false;
        boolean updateAnchorPointCalled = false;

        @Override
        public void updateAnchorPoint(Location<Vector2> anchor) {
            updateAnchorPointCalled = true;
        }

        @Override
        public Location<Vector2> calculateDriftOffset(Location<Vector2> centerOfMass, Array<SlotAssignment<Vector2>> slotAssignments, FormationPattern<Vector2> pattern) {
            calculateDriftOffsetCalled = true;
            return super.calculateDriftOffset(centerOfMass, slotAssignments, pattern);
        }
    }

    private static class TestFormationMotionModerator3D extends FormationMotionModerator<Vector3> {
        boolean calculateDriftOffsetCalled = false;
        boolean updateAnchorPointCalled = false;

        @Override
        public void updateAnchorPoint(Location<Vector3> anchor) {
            updateAnchorPointCalled = true;
        }

        @Override
        public Location<Vector3> calculateDriftOffset(Location<Vector3> centerOfMass, Array<SlotAssignment<Vector3>> slotAssignments, FormationPattern<Vector3> pattern) {
            calculateDriftOffsetCalled = true;
            return super.calculateDriftOffset(centerOfMass, slotAssignments, pattern);
        }
    }

    private static class TestFormationMember2D implements FormationMember<Vector2> {
        private final TestLocation2D targetLocation = new TestLocation2D();
        boolean targetLocationUpdated = false;

        @Override
        public Location<Vector2> getTargetLocation() {
            targetLocationUpdated = true;
            return targetLocation;
        }
    }

    private static class TestFormationMember3D implements FormationMember<Vector3> {
        private final TestLocation3D targetLocation = new TestLocation3D();
        boolean targetLocationUpdated = false;

        @Override
        public Location<Vector3> getTargetLocation() {
            targetLocationUpdated = true;
            return targetLocation;
        }
    }
}
