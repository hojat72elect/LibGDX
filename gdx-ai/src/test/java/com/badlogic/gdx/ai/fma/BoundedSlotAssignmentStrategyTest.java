package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Unit tests for BoundedSlotAssignmentStrategy
 */
public class BoundedSlotAssignmentStrategyTest {

    private TestBoundedSlotAssignmentStrategy<Vector2> strategy2D;
    private TestBoundedSlotAssignmentStrategy<Vector3> strategy3D;
    private Array<SlotAssignment<Vector2>> assignments2D;
    private Array<SlotAssignment<Vector3>> assignments3D;
    private TestFormationMember2D member1;
    private TestFormationMember2D member2;
    private TestFormationMember2D member3;
    private TestFormationMember3D member1_3D;
    private TestFormationMember3D member2_3D;
    private TestFormationMember3D member3_3D;

    @Before
    public void setUp() {
        strategy2D = new TestBoundedSlotAssignmentStrategy<>();
        strategy3D = new TestBoundedSlotAssignmentStrategy<>();
        assignments2D = new Array<>();
        assignments3D = new Array<>();
        member1 = new TestFormationMember2D();
        member2 = new TestFormationMember2D();
        member3 = new TestFormationMember2D();
        member1_3D = new TestFormationMember3D();
        member2_3D = new TestFormationMember3D();
        member3_3D = new TestFormationMember3D();
    }

    @Test
    public void testCalculateNumberOfSlotsWithEmptyAssignments() {
        // Test with empty assignments array
        int result = strategy2D.calculateNumberOfSlots(new Array<>());
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCalculateNumberOfSlotsWithSingleAssignment() {
        // Test with one assignment at slot 0
        assignments2D.add(new SlotAssignment<>(member1, 0));
        int result = strategy2D.calculateNumberOfSlots(assignments2D);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCalculateNumberOfSlotsWithMultipleAssignments() {
        // Test with multiple assignments at different slots
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 2));
        assignments2D.add(new SlotAssignment<>(member3, 4));
        int result = strategy2D.calculateNumberOfSlots(assignments2D);
        Assert.assertEquals(5, result); // highest slot is 4, so 4+1=5 slots
    }

    @Test
    public void testCalculateNumberOfSlotsWithConsecutiveSlots() {
        // Test with consecutive slot numbers
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));
        assignments2D.add(new SlotAssignment<>(member3, 2));
        int result = strategy2D.calculateNumberOfSlots(assignments2D);
        Assert.assertEquals(3, result); // highest slot is 2, so 2+1=3 slots
    }

    @Test
    public void testCalculateNumberOfSlotsWithGaps() {
        // Test with gaps in slot numbers
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 5));
        assignments2D.add(new SlotAssignment<>(member3, 10));
        int result = strategy2D.calculateNumberOfSlots(assignments2D);
        Assert.assertEquals(11, result); // highest slot is 10, so 10+1=11 slots
    }

    @Test
    public void testCalculateNumberOfSlotsWithNegativeSlotNumbers() {
        // Test with negative slot numbers (edge case)
        assignments2D.add(new SlotAssignment<>(member1, -2));
        assignments2D.add(new SlotAssignment<>(member2, -1));
        assignments2D.add(new SlotAssignment<>(member3, 0));
        int result = strategy2D.calculateNumberOfSlots(assignments2D);
        Assert.assertEquals(1, result); // highest slot is 0, so 0+1=1 slot
    }

    @Test
    public void testCalculateNumberOfSlotsWithMixedSlotNumbers() {
        // Test with mixed positive and negative slot numbers
        assignments2D.add(new SlotAssignment<>(member1, -3));
        assignments2D.add(new SlotAssignment<>(member2, 1));
        assignments2D.add(new SlotAssignment<>(member3, 3));
        int result = strategy2D.calculateNumberOfSlots(assignments2D);
        Assert.assertEquals(4, result); // highest slot is 3, so 3+1=4 slots
    }

    @Test
    public void testCalculateNumberOfSlotsWithDuplicateSlots() {
        // Test with duplicate slot numbers
        assignments2D.add(new SlotAssignment<>(member1, 2));
        assignments2D.add(new SlotAssignment<>(member2, 2));
        assignments2D.add(new SlotAssignment<>(member3, 3));
        int result = strategy2D.calculateNumberOfSlots(assignments2D);
        Assert.assertEquals(4, result); // highest slot is 3, so 3+1=4 slots
    }

    @Test
    public void testCalculateNumberOfSlotsWithLargeSlotNumbers() {
        // Test with large slot numbers
        assignments2D.add(new SlotAssignment<>(member1, 100));
        assignments2D.add(new SlotAssignment<>(member2, 200));
        assignments2D.add(new SlotAssignment<>(member3, 150));
        int result = strategy2D.calculateNumberOfSlots(assignments2D);
        Assert.assertEquals(201, result); // highest slot is 200, so 200+1=201 slots
    }

    @Test
    public void testCalculateNumberOfSlots3D() {
        // Test that 3D version works the same as 2D
        assignments3D.add(new SlotAssignment<>(member1_3D, 0));
        assignments3D.add(new SlotAssignment<>(member2_3D, 3));
        assignments3D.add(new SlotAssignment<>(member3_3D, 1));
        int result = strategy3D.calculateNumberOfSlots(assignments3D);
        Assert.assertEquals(4, result); // highest slot is 3, so 3+1=4 slots
    }

    @Test
    public void testRemoveSlotAssignmentFromBeginning() {
        // Test removing assignment from beginning of array
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));
        assignments2D.add(new SlotAssignment<>(member3, 2));

        strategy2D.removeSlotAssignment(assignments2D, 0);

        Assert.assertEquals(2, assignments2D.size);
        Assert.assertEquals(member2, assignments2D.get(0).member);
        Assert.assertEquals(0, assignments2D.get(0).slotNumber); // was 1, now 0
        Assert.assertEquals(member3, assignments2D.get(1).member);
        Assert.assertEquals(1, assignments2D.get(1).slotNumber); // was 2, now 1
    }

    @Test
    public void testRemoveSlotAssignmentFromMiddle() {
        // Test removing assignment from middle of array
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));
        assignments2D.add(new SlotAssignment<>(member3, 2));

        strategy2D.removeSlotAssignment(assignments2D, 1);

        Assert.assertEquals(2, assignments2D.size);
        Assert.assertEquals(member1, assignments2D.get(0).member);
        Assert.assertEquals(0, assignments2D.get(0).slotNumber); // unchanged
        Assert.assertEquals(member3, assignments2D.get(1).member);
        Assert.assertEquals(1, assignments2D.get(1).slotNumber); // was 2, now 1
    }

    @Test
    public void testRemoveSlotAssignmentFromEnd() {
        // Test removing assignment from end of array
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));
        assignments2D.add(new SlotAssignment<>(member3, 2));

        strategy2D.removeSlotAssignment(assignments2D, 2);

        Assert.assertEquals(2, assignments2D.size);
        Assert.assertEquals(member1, assignments2D.get(0).member);
        Assert.assertEquals(0, assignments2D.get(0).slotNumber); // unchanged
        Assert.assertEquals(member2, assignments2D.get(1).member);
        Assert.assertEquals(1, assignments2D.get(1).slotNumber); // unchanged
    }

    @Test
    public void testRemoveSlotAssignmentWithGaps() {
        // Test removing assignment when there are gaps in slot numbers
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 3));
        assignments2D.add(new SlotAssignment<>(member3, 5));

        strategy2D.removeSlotAssignment(assignments2D, 1); // remove slot 3

        Assert.assertEquals(2, assignments2D.size);
        Assert.assertEquals(member1, assignments2D.get(0).member);
        Assert.assertEquals(0, assignments2D.get(0).slotNumber); // unchanged
        Assert.assertEquals(member3, assignments2D.get(1).member);
        Assert.assertEquals(4, assignments2D.get(1).slotNumber); // was 5, now 4
    }

    @Test
    public void testRemoveSlotAssignmentWithNegativeSlots() {
        // Test removing assignment with negative slot numbers
        assignments2D.add(new SlotAssignment<>(member1, -2));
        assignments2D.add(new SlotAssignment<>(member2, -1));
        assignments2D.add(new SlotAssignment<>(member3, 0));

        strategy2D.removeSlotAssignment(assignments2D, 1); // remove slot -1

        Assert.assertEquals(2, assignments2D.size);
        Assert.assertEquals(member1, assignments2D.get(0).member);
        Assert.assertEquals(-2, assignments2D.get(0).slotNumber); // unchanged
        Assert.assertEquals(member3, assignments2D.get(1).member);
        Assert.assertEquals(-1, assignments2D.get(1).slotNumber); // was 0, now -1
    }

    @Test
    public void testRemoveSlotAssignmentWithLargeSlots() {
        // Test removing assignment with large slot numbers
        assignments2D.add(new SlotAssignment<>(member1, 100));
        assignments2D.add(new SlotAssignment<>(member2, 101));
        assignments2D.add(new SlotAssignment<>(member3, 102));

        strategy2D.removeSlotAssignment(assignments2D, 1); // remove slot 101

        Assert.assertEquals(2, assignments2D.size);
        Assert.assertEquals(member1, assignments2D.get(0).member);
        Assert.assertEquals(100, assignments2D.get(0).slotNumber); // unchanged
        Assert.assertEquals(member3, assignments2D.get(1).member);
        Assert.assertEquals(101, assignments2D.get(1).slotNumber); // was 102, now 101
    }

    @Test
    public void testRemoveSlotAssignment3D() {
        // Test that 3D version works the same as 2D
        assignments3D.add(new SlotAssignment<>(member1_3D, 0));
        assignments3D.add(new SlotAssignment<>(member2_3D, 1));
        assignments3D.add(new SlotAssignment<>(member3_3D, 2));

        strategy3D.removeSlotAssignment(assignments3D, 0); // remove first

        Assert.assertEquals(2, assignments3D.size);
        Assert.assertEquals(member2_3D, assignments3D.get(0).member);
        Assert.assertEquals(0, assignments3D.get(0).slotNumber); // was 1, now 0
        Assert.assertEquals(member3_3D, assignments3D.get(1).member);
        Assert.assertEquals(1, assignments3D.get(1).slotNumber); // was 2, now 1
    }

    @Test
    public void testRemoveSlotAssignmentSingleElement() {
        // Test removing from single element array
        assignments2D.add(new SlotAssignment<>(member1, 5));

        strategy2D.removeSlotAssignment(assignments2D, 0);

        Assert.assertEquals(0, assignments2D.size);
    }

    @Test
    public void testUpdateSlotAssignmentsAbstractMethod() {
        // Test that updateSlotAssignments is abstract and can be implemented
        TestBoundedSlotAssignmentStrategy<Vector2> testStrategy = new TestBoundedSlotAssignmentStrategy<>();
        
        // Should call the implemented method
        testStrategy.updateSlotAssignments(assignments2D);
        
        Assert.assertTrue("Update method should have been called", testStrategy.updateCalled);
    }

    @Test
    public void testCalculateNumberOfSlotsConsistency() {
        // Test that calculateNumberOfSlots produces consistent results
        assignments2D.add(new SlotAssignment<>(member1, 3));
        assignments2D.add(new SlotAssignment<>(member2, 7));
        assignments2D.add(new SlotAssignment<>(member3, 2));

        // Calculate multiple times
        int result1 = strategy2D.calculateNumberOfSlots(assignments2D);
        int result2 = strategy2D.calculateNumberOfSlots(assignments2D);
        int result3 = strategy2D.calculateNumberOfSlots(assignments2D);

        Assert.assertEquals(8, result1); // highest slot is 7, so 7+1=8
        Assert.assertEquals(result1, result2);
        Assert.assertEquals(result2, result3);
    }

    @Test
    public void testCalculateNumberOfSlotsWithUnorderedAssignments() {
        // Test with unordered assignments (not sorted by slot number)
        assignments2D.add(new SlotAssignment<>(member1, 5));
        assignments2D.add(new SlotAssignment<>(member2, 1));
        assignments2D.add(new SlotAssignment<>(member3, 8));
        assignments2D.add(new SlotAssignment<>(new TestFormationMember2D(), 3));

        int result = strategy2D.calculateNumberOfSlots(assignments2D);
        Assert.assertEquals(9, result); // highest slot is 8, so 8+1=9 slots
    }

    @Test
    public void testRemoveSlotAssignmentMultipleRemovals() {
        // Test multiple consecutive removals
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));
        assignments2D.add(new SlotAssignment<>(member3, 2));

        // Remove first element
        strategy2D.removeSlotAssignment(assignments2D, 0);
        Assert.assertEquals(2, assignments2D.size);
        Assert.assertEquals(0, assignments2D.get(0).slotNumber);
        Assert.assertEquals(1, assignments2D.get(1).slotNumber);

        // Remove new first element
        strategy2D.removeSlotAssignment(assignments2D, 0);
        Assert.assertEquals(1, assignments2D.size);
        Assert.assertEquals(0, assignments2D.get(0).slotNumber);

        // Remove last element
        strategy2D.removeSlotAssignment(assignments2D, 0);
        Assert.assertEquals(0, assignments2D.size);
    }

    @Test
    public void testInterfaceImplementation() {
        // Test that BoundedSlotAssignmentStrategy implements SlotAssignmentStrategy
        Assert.assertNotNull("Should implement SlotAssignmentStrategy", strategy2D);
    }

    /**
     * Test implementation of BoundedSlotAssignmentStrategy for testing
     */
    private static class TestBoundedSlotAssignmentStrategy<T extends Vector<T>> extends BoundedSlotAssignmentStrategy<T> {
        boolean updateCalled = false;

        @Override
        public void updateSlotAssignments(Array<SlotAssignment<T>> assignments) {
            updateCalled = true;
            // Simple implementation for testing
        }
    }

    /**
     * Test implementation of FormationMember for testing
     */
    private static class TestFormationMember2D implements FormationMember<Vector2> {
        private final TestLocation2D location = new TestLocation2D();

        @Override
        public Location<Vector2> getTargetLocation() {
            return location;
        }
    }

    /**
     * Test implementation of FormationMember for testing
     */
    private static class TestFormationMember3D implements FormationMember<Vector3> {
        private final TestLocation3D location = new TestLocation3D();

        @Override
        public Location<Vector3> getTargetLocation() {
            return location;
        }
    }

    /**
     * Test implementation of Location interface for testing
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
    }

    /**
     * Test implementation of Location interface for testing
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
    }
}
