package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SoftRoleSlotAssignmentStrategyTest {

    private TestSlotCostProvider<Vector2> costProvider2D;
    private TestSlotCostProvider<Vector3> costProvider3D;
    private SoftRoleSlotAssignmentStrategy<Vector2> strategy2D;
    private SoftRoleSlotAssignmentStrategy<Vector3> strategy3D;
    private SoftRoleSlotAssignmentStrategy<Vector2> strategyWithThreshold;
    private Array<SlotAssignment<Vector2>> assignments2D;
    private Array<SlotAssignment<Vector3>> assignments3D;
    private TestFormationMember2D member1;
    private TestFormationMember2D member2;
    private TestFormationMember2D member3;
    private TestFormationMember3D member1_3D;
    private TestFormationMember3D member2_3D;

    @Before
    public void setUp() {
        costProvider2D = new TestSlotCostProvider<>();
        costProvider3D = new TestSlotCostProvider<>();
        strategy2D = new SoftRoleSlotAssignmentStrategy<>(costProvider2D);
        strategy3D = new SoftRoleSlotAssignmentStrategy<>(costProvider3D);
        strategyWithThreshold = new SoftRoleSlotAssignmentStrategy<>(costProvider2D, 5.0f);
        assignments2D = new Array<>();
        assignments3D = new Array<>();
        member1 = new TestFormationMember2D();
        member2 = new TestFormationMember2D();
        member3 = new TestFormationMember2D();
        member1_3D = new TestFormationMember3D();
        member2_3D = new TestFormationMember3D();
    }

    @Test
    public void testConstructorWithCostProvider() {
        // Test constructor with only cost provider (should use infinite threshold)
        SoftRoleSlotAssignmentStrategy<Vector2> strategy = new SoftRoleSlotAssignmentStrategy<>(costProvider2D);
        Assert.assertNotNull("Strategy should be created", strategy);
        Assert.assertEquals("Cost threshold should be infinite", Float.POSITIVE_INFINITY, strategy.costThreshold, 0.0f);
        Assert.assertEquals("Cost provider should be set", costProvider2D, strategy.slotCostProvider);
    }

    @Test
    public void testConstructorWithCostProviderAndThreshold() {
        // Test constructor with cost provider and threshold
        float threshold = 10.0f;
        SoftRoleSlotAssignmentStrategy<Vector2> strategy = new SoftRoleSlotAssignmentStrategy<>(costProvider2D, threshold);
        Assert.assertNotNull("Strategy should be created", strategy);
        Assert.assertEquals("Cost threshold should be set", threshold, strategy.costThreshold, 0.0f);
        Assert.assertEquals("Cost provider should be set", costProvider2D, strategy.slotCostProvider);
    }

    @Test
    public void testUpdateSlotAssignmentsWithEmptyAssignments() {
        // Test with empty assignments array
        strategy2D.updateSlotAssignments(new Array<>());
        // Should not throw exception
    }

    @Test
    public void testUpdateSlotAssignmentsWithSingleMember() {
        // Test with single member and single slot
        assignments2D.add(new SlotAssignment<>(member1, 0));
        costProvider2D.setCost(member1, 0, 1.0f);

        strategy2D.updateSlotAssignments(assignments2D);

        Assert.assertEquals("Member should be assigned to slot 0", member1, assignments2D.get(0).member);
        Assert.assertEquals("Slot number should be 0", 0, assignments2D.get(0).slotNumber);
    }

    @Test
    public void testUpdateSlotAssignmentsWithMultipleMembers() {
        // Test with multiple members and slots
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));
        assignments2D.add(new SlotAssignment<>(member3, 2));

        // Set costs - member1 prefers slot 0, member2 prefers slot 1, member3 prefers slot 2
        costProvider2D.setCost(member1, 0, 1.0f);
        costProvider2D.setCost(member1, 1, 10.0f);
        costProvider2D.setCost(member1, 2, 10.0f);
        costProvider2D.setCost(member2, 0, 10.0f);
        costProvider2D.setCost(member2, 1, 1.0f);
        costProvider2D.setCost(member2, 2, 10.0f);
        costProvider2D.setCost(member3, 0, 10.0f);
        costProvider2D.setCost(member3, 1, 10.0f);
        costProvider2D.setCost(member3, 2, 1.0f);

        strategy2D.updateSlotAssignments(assignments2D);

        Assert.assertEquals("Member1 should be assigned to slot 0", member1, assignments2D.get(0).member);
        Assert.assertEquals("Member2 should be assigned to slot 1", member2, assignments2D.get(1).member);
        Assert.assertEquals("Member3 should be assigned to slot 2", member3, assignments2D.get(2).member);
    }

    @Test
    public void testUpdateSlotAssignmentsWithCostThreshold() {
        // Test that slots with cost above threshold are ignored
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));

        // Set costs - member1 can only use slot 0 (cost 1), member2 can only use slot 1 (cost 3)
        costProvider2D.setCost(member1, 0, 1.0f);
        costProvider2D.setCost(member1, 1, 10.0f); // above threshold
        costProvider2D.setCost(member2, 0, 10.0f); // above threshold
        costProvider2D.setCost(member2, 1, 3.0f);

        strategyWithThreshold.updateSlotAssignments(assignments2D);

        Assert.assertEquals("Member1 should be assigned to slot 0", member1, assignments2D.get(0).member);
        Assert.assertEquals("Member2 should be assigned to slot 1", member2, assignments2D.get(1).member);
    }

    @Test
    public void testUpdateSlotAssignmentsWithCompetingPreferences() {
        // Test when multiple members prefer the same slot
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));
        assignments2D.add(new SlotAssignment<>(member3, 2));

        // All members prefer slot 0, but have different costs for other slots
        costProvider2D.setCost(member1, 0, 1.0f);
        costProvider2D.setCost(member1, 1, 2.0f);
        costProvider2D.setCost(member1, 2, 3.0f);
        costProvider2D.setCost(member2, 0, 1.0f);
        costProvider2D.setCost(member2, 1, 4.0f);
        costProvider2D.setCost(member2, 2, 5.0f);
        costProvider2D.setCost(member3, 0, 1.0f);
        costProvider2D.setCost(member3, 1, 6.0f);
        costProvider2D.setCost(member3, 2, 7.0f);

        strategy2D.updateSlotAssignments(assignments2D);

        // All members have same assignmentEase (1/2 + 1/3 + 1/4 = ~1.083), so order depends on sorting
        // But slot 0 should go to someone, and others should get remaining slots
        Assert.assertNotNull("Slot 0 should be assigned", assignments2D.get(0).member);
        Assert.assertNotNull("Slot 1 should be assigned", assignments2D.get(1).member);
        Assert.assertNotNull("Slot 2 should be assigned", assignments2D.get(2).member);
    }

    @Test
    public void testUpdateSlotAssignmentsWithNoValidAssignment() {
        // Test when a member has no valid slots (all costs above threshold)
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));

        // member1 has no valid slots
        costProvider2D.setCost(member1, 0, 10.0f); // above threshold
        costProvider2D.setCost(member1, 1, 10.0f); // above threshold
        costProvider2D.setCost(member2, 0, 1.0f);
        costProvider2D.setCost(member2, 1, 2.0f);

        try {
            strategyWithThreshold.updateSlotAssignments(assignments2D);
            Assert.fail("Should throw GdxRuntimeException when no valid assignment exists");
        } catch (GdxRuntimeException e) {
            Assert.assertTrue("Error message should mention member", e.getMessage().contains("cannot find valid slot assignment"));
        }
    }

    @Test
    public void testUpdateSlotAssignmentsWithZeroCost() {
        // Test with zero cost (ideal assignment)
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));

        costProvider2D.setCost(member1, 0, 0.0f); // ideal
        costProvider2D.setCost(member1, 1, 10.0f);
        costProvider2D.setCost(member2, 0, 10.0f);
        costProvider2D.setCost(member2, 1, 1.0f);

        strategy2D.updateSlotAssignments(assignments2D);

        Assert.assertEquals("Member1 should be assigned to slot 0", member1, assignments2D.get(0).member);
        Assert.assertEquals("Member2 should be assigned to slot 1", member2, assignments2D.get(1).member);
    }

    @Test
    public void testUpdateSlotAssignments3D() {
        // Test that 3D version works the same as 2D
        assignments3D.add(new SlotAssignment<>(member1_3D, 0));
        assignments3D.add(new SlotAssignment<>(member2_3D, 1));

        costProvider3D.setCost(member1_3D, 0, 1.0f);
        costProvider3D.setCost(member1_3D, 1, 10.0f);
        costProvider3D.setCost(member2_3D, 0, 10.0f);
        costProvider3D.setCost(member2_3D, 1, 1.0f);

        strategy3D.updateSlotAssignments(assignments3D);

        Assert.assertEquals("Member1 should be assigned to slot 0", member1_3D, assignments3D.get(0).member);
        Assert.assertEquals("Member2 should be assigned to slot 1", member2_3D, assignments3D.get(1).member);
    }

    @Test
    public void testUpdateSlotAssignmentsWithGaps() {
        // Test with gaps in slot numbers - need to create assignments for all slots up to the highest
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(new TestFormationMember2D(), 1)); // dummy for slot 1
        assignments2D.add(new SlotAssignment<>(member2, 2));
        assignments2D.add(new SlotAssignment<>(new TestFormationMember2D(), 3)); // dummy for slot 3
        assignments2D.add(new SlotAssignment<>(member3, 4));

        // Set costs for all members and slots
        for (int i = 0; i < assignments2D.size; i++) {
            FormationMember<Vector2> member = assignments2D.get(i).member;
            for (int j = 0; j < assignments2D.size; j++) {
                int slotNumber = assignments2D.get(j).slotNumber;
                // Each member prefers their "own" slot
                float cost = (i == j) ? 1.0f : 10.0f;
                costProvider2D.setCost(member, slotNumber, cost);
            }
        }

        strategy2D.updateSlotAssignments(assignments2D);

        Assert.assertEquals("Member1 should be assigned to slot 0", member1, assignments2D.get(0).member);
        Assert.assertEquals("Member2 should be assigned to slot 2", member2, assignments2D.get(2).member);
        Assert.assertEquals("Member3 should be assigned to slot 4", member3, assignments2D.get(4).member);
    }

    @Test
    public void testUpdateSlotAssignmentsWithNegativeCosts() {
        // Test behavior with negative costs (should work as per algorithm)
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));

        costProvider2D.setCost(member1, 0, -1.0f); // negative cost
        costProvider2D.setCost(member1, 1, 10.0f);
        costProvider2D.setCost(member2, 0, 10.0f);
        costProvider2D.setCost(member2, 1, 1.0f);

        strategy2D.updateSlotAssignments(assignments2D);

        Assert.assertEquals("Member1 should be assigned to slot 0", member1, assignments2D.get(0).member);
        Assert.assertEquals("Member2 should be assigned to slot 1", member2, assignments2D.get(1).member);
    }

    @Test
    public void testCalculateNumberOfSlotsInherited() {
        // Test that calculateNumberOfSlots works (inherited from BoundedSlotAssignmentStrategy)
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 3));
        assignments2D.add(new SlotAssignment<>(member3, 1));

        int result = strategy2D.calculateNumberOfSlots(assignments2D);
        Assert.assertEquals("Should calculate 4 slots (highest is 3, so 3+1=4)", 4, result);
    }

    @Test
    public void testRemoveSlotAssignmentInherited() {
        // Test that removeSlotAssignment works (inherited from BoundedSlotAssignmentStrategy)
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));
        assignments2D.add(new SlotAssignment<>(member3, 2));

        strategy2D.removeSlotAssignment(assignments2D, 1); // remove middle

        Assert.assertEquals(2, assignments2D.size);
        Assert.assertEquals(member1, assignments2D.get(0).member);
        Assert.assertEquals(0, assignments2D.get(0).slotNumber);
        Assert.assertEquals(member3, assignments2D.get(1).member);
        Assert.assertEquals(1, assignments2D.get(1).slotNumber); // was 2, now 1
    }

    @Test
    public void testCostAndSlotComparison() {
        // Test CostAndSlot compareTo method
        SoftRoleSlotAssignmentStrategy.CostAndSlot<Vector2> slot1 =
                new SoftRoleSlotAssignmentStrategy.CostAndSlot<>(1.0f, 0);
        SoftRoleSlotAssignmentStrategy.CostAndSlot<Vector2> slot2 =
                new SoftRoleSlotAssignmentStrategy.CostAndSlot<>(2.0f, 1);
        SoftRoleSlotAssignmentStrategy.CostAndSlot<Vector2> slot3 =
                new SoftRoleSlotAssignmentStrategy.CostAndSlot<>(1.0f, 2);

        Assert.assertTrue("slot1 should be less than slot2", slot1.compareTo(slot2) < 0);
        Assert.assertTrue("slot2 should be greater than slot1", slot2.compareTo(slot1) > 0);
        Assert.assertEquals("slot1 should equal slot3 (same cost)", 0, slot1.compareTo(slot3));
    }

    @Test
    public void testMemberAndSlotsComparison() {
        // Test MemberAndSlots compareTo method
        SoftRoleSlotAssignmentStrategy.MemberAndSlots<Vector2> member1 =
                new SoftRoleSlotAssignmentStrategy.MemberAndSlots<>(this.member1);
        SoftRoleSlotAssignmentStrategy.MemberAndSlots<Vector2> member2 =
                new SoftRoleSlotAssignmentStrategy.MemberAndSlots<>(this.member2);

        member1.assignmentEase = 1.0f;
        member2.assignmentEase = 2.0f;

        Assert.assertTrue("member1 should be less than member2", member1.compareTo(member2) < 0);
        Assert.assertTrue("member2 should be greater than member1", member2.compareTo(member1) > 0);
        Assert.assertEquals("member should equal self", 0, member1.compareTo(member1));
    }

    @Test
    public void testCostAndSlotConstructor() {
        // Test CostAndSlot constructor
        float cost = 5.0f;
        int slotNumber = 3;
        SoftRoleSlotAssignmentStrategy.CostAndSlot<Vector2> costAndSlot =
                new SoftRoleSlotAssignmentStrategy.CostAndSlot<>(cost, slotNumber);

        Assert.assertEquals("Cost should be set", cost, costAndSlot.cost, 0.0f);
        Assert.assertEquals("Slot number should be set", slotNumber, costAndSlot.slotNumber);
    }

    @Test
    public void testMemberAndSlotsConstructor() {
        // Test MemberAndSlots constructor
        TestFormationMember2D member = new TestFormationMember2D();
        SoftRoleSlotAssignmentStrategy.MemberAndSlots<Vector2> memberAndSlots =
                new SoftRoleSlotAssignmentStrategy.MemberAndSlots<>(member);

        Assert.assertEquals("Member should be set", member, memberAndSlots.member);
        Assert.assertEquals("Assignment ease should be 0", 0.0f, memberAndSlots.assignmentEase, 0.0f);
        Assert.assertNotNull("CostAndSlots array should be initialized", memberAndSlots.costAndSlots);
        Assert.assertEquals("CostAndSlots array should be empty", 0, memberAndSlots.costAndSlots.size);
    }

    @Test
    public void testSlotCostProviderInterface() {
        // Test that SlotCostProvider interface works
        TestSlotCostProvider<Vector2> provider = new TestSlotCostProvider<>();
        TestFormationMember2D member = new TestFormationMember2D();
        int slotNumber = 2;
        float expectedCost = 3.5f;

        provider.setCost(member, slotNumber, expectedCost);
        float actualCost = provider.getCost(member, slotNumber);

        Assert.assertEquals("Cost should match", expectedCost, actualCost, 0.0f);
    }

    @Test
    public void testUpdateSlotAssignmentsWithLargeNumberOfSlots() {
        // Test with large number of slots
        int numSlots = 100;
        Array<SlotAssignment<Vector2>> largeAssignments = new Array<>();
        Array<TestFormationMember2D> members = new Array<>();

        // Create assignments
        for (int i = 0; i < numSlots; i++) {
            TestFormationMember2D member = new TestFormationMember2D();
            members.add(member);
            largeAssignments.add(new SlotAssignment<>(member, i));
        }

        // Set costs - each member prefers their own slot
        for (int i = 0; i < numSlots; i++) {
            for (int j = 0; j < numSlots; j++) {
                float cost = (i == j) ? 1.0f : 100.0f;
                costProvider2D.setCost(members.get(i), j, cost);
            }
        }

        strategy2D.updateSlotAssignments(largeAssignments);

        // Verify each member got their preferred slot
        for (int i = 0; i < numSlots; i++) {
            Assert.assertEquals("Member " + i + " should be assigned to slot " + i,
                    members.get(i), largeAssignments.get(i).member);
            Assert.assertEquals("Slot " + i + " should have number " + i,
                    i, largeAssignments.get(i).slotNumber);
        }
    }

    @Test
    public void testUpdateSlotAssignmentsWithInfiniteCost() {
        // Test with infinite cost (should be ignored)
        assignments2D.add(new SlotAssignment<>(member1, 0));
        assignments2D.add(new SlotAssignment<>(member2, 1));

        costProvider2D.setCost(member1, 0, 1.0f);
        costProvider2D.setCost(member1, 1, Float.POSITIVE_INFINITY); // infinite cost
        costProvider2D.setCost(member2, 0, Float.POSITIVE_INFINITY); // infinite cost
        costProvider2D.setCost(member2, 1, 1.0f);

        strategy2D.updateSlotAssignments(assignments2D);

        Assert.assertEquals("Member1 should be assigned to slot 0", member1, assignments2D.get(0).member);
        Assert.assertEquals("Member2 should be assigned to slot 1", member2, assignments2D.get(1).member);
    }

    /**
     * Test implementation of SlotCostProvider for testing
     */
    private static class TestSlotCostProvider<T extends Vector<T>> implements SoftRoleSlotAssignmentStrategy.SlotCostProvider<T> {
        private final java.util.Map<String, Float> costMap = new java.util.HashMap<>();

        public void setCost(FormationMember<T> member, int slotNumber, float cost) {
            String key = member.toString() + "_" + slotNumber;
            costMap.put(key, cost);
        }

        @Override
        public float getCost(FormationMember<T> member, int slotNumber) {
            String key = member.toString() + "_" + slotNumber;
            return costMap.getOrDefault(key, 1.0f); // default cost of 1.0
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

        @Override
        public String toString() {
            return "TestFormationMember2D@" + System.identityHashCode(this);
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

        @Override
        public String toString() {
            return "TestFormationMember3D@" + System.identityHashCode(this);
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
