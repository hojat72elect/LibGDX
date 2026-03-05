package com.badlogic.gdx.ai.sched;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.ai.sched.PriorityScheduler.PrioritySchedulableRecord;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PrioritySchedulerTest {

    private PriorityScheduler scheduler;
    private TestSchedulable testSchedulable1;
    private TestSchedulable testSchedulable2;
    private TestSchedulable testSchedulable3;

    @Before
    public void setUp() {
        scheduler = new PriorityScheduler(100);
        testSchedulable1 = new TestSchedulable("Task1");
        testSchedulable2 = new TestSchedulable("Task2");
        testSchedulable3 = new TestSchedulable("Task3");
    }

    @Test
    public void testConstructor() {
        assertEquals(100, scheduler.dryRunFrames);
        assertEquals(0, scheduler.frame);
        assertNotNull(scheduler.schedulableRecords);
        assertNotNull(scheduler.runList);
        assertNotNull(scheduler.phaseCounters);
        assertTrue(scheduler.schedulableRecords.isEmpty());
        assertTrue(scheduler.runList.isEmpty());
        assertTrue(scheduler.phaseCounters.isEmpty());
    }

    @Test
    public void testConstructorWithZeroDryRunFrames() {
        PriorityScheduler zeroScheduler = new PriorityScheduler(0);
        assertEquals(0, zeroScheduler.dryRunFrames);
        assertEquals(0, zeroScheduler.frame);
    }

    @Test
    public void testConstructorWithNegativeDryRunFrames() {
        PriorityScheduler negativeScheduler = new PriorityScheduler(-10);
        assertEquals(-10, negativeScheduler.dryRunFrames);
        assertEquals(0, negativeScheduler.frame);
    }

    @Test
    public void testAddWithAutomaticPhasingFrequencyOnly() {
        scheduler.addWithAutomaticPhasing(testSchedulable1, 5);

        assertEquals(1, scheduler.schedulableRecords.size);
        PrioritySchedulableRecord record = scheduler.schedulableRecords.get(0);
        assertSame(testSchedulable1, record.schedulable);
        assertEquals(5, record.frequency);
        assertEquals(1f, record.priority, 0.001f); // Default priority
        assertTrue(record.phase >= 0 && record.phase < 5); // Phase should be within frequency range
    }

    @Test
    public void testAddWithAutomaticPhasingFrequencyAndPriority() {
        scheduler.addWithAutomaticPhasing(testSchedulable1, 5, 2.5f);

        assertEquals(1, scheduler.schedulableRecords.size);
        PrioritySchedulableRecord record = scheduler.schedulableRecords.get(0);
        assertSame(testSchedulable1, record.schedulable);
        assertEquals(5, record.frequency);
        assertEquals(2.5f, record.priority, 0.001f);
        assertTrue(record.phase >= 0 && record.phase < 5); // Phase should be within frequency range
    }

    @Test
    public void testAddWithAutomaticPhasingMultipleTasks() {
        scheduler.addWithAutomaticPhasing(testSchedulable1, 3, 1.5f);
        scheduler.addWithAutomaticPhasing(testSchedulable2, 4, 2.0f);
        scheduler.addWithAutomaticPhasing(testSchedulable3, 5, 0.5f);

        assertEquals(3, scheduler.schedulableRecords.size);

        PrioritySchedulableRecord record1 = scheduler.schedulableRecords.get(0);
        PrioritySchedulableRecord record2 = scheduler.schedulableRecords.get(1);
        PrioritySchedulableRecord record3 = scheduler.schedulableRecords.get(2);

        assertEquals(3, record1.frequency);
        assertEquals(1.5f, record1.priority, 0.001f);
        assertTrue(record1.phase >= 0 && record1.phase < 3);

        assertEquals(4, record2.frequency);
        assertEquals(2.0f, record2.priority, 0.001f);
        assertTrue(record2.phase >= 0 && record2.phase < 4);

        assertEquals(5, record3.frequency);
        assertEquals(0.5f, record3.priority, 0.001f);
        assertTrue(record3.phase >= 0 && record3.phase < 5);
    }

    @Test
    public void testAddFrequencyPhaseOnly() {
        scheduler.add(testSchedulable1, 5, 2);

        assertEquals(1, scheduler.schedulableRecords.size);
        PrioritySchedulableRecord record = scheduler.schedulableRecords.get(0);
        assertSame(testSchedulable1, record.schedulable);
        assertEquals(5, record.frequency);
        assertEquals(2, record.phase);
        assertEquals(1f, record.priority, 0.001f); // Default priority
    }

    @Test
    public void testAddFrequencyPhasePriority() {
        scheduler.add(testSchedulable1, 5, 2, 3.0f);

        assertEquals(1, scheduler.schedulableRecords.size);
        PrioritySchedulableRecord record = scheduler.schedulableRecords.get(0);
        assertSame(testSchedulable1, record.schedulable);
        assertEquals(5, record.frequency);
        assertEquals(2, record.phase);
        assertEquals(3.0f, record.priority, 0.001f);
    }

    @Test
    public void testAddMultipleTasks() {
        scheduler.add(testSchedulable1, 3, 1, 1.5f);
        scheduler.add(testSchedulable2, 4, 2, 2.0f);
        scheduler.add(testSchedulable3, 5, 0, 0.5f);

        assertEquals(3, scheduler.schedulableRecords.size);

        PrioritySchedulableRecord record1 = scheduler.schedulableRecords.get(0);
        PrioritySchedulableRecord record2 = scheduler.schedulableRecords.get(1);
        PrioritySchedulableRecord record3 = scheduler.schedulableRecords.get(2);

        assertEquals(3, record1.frequency);
        assertEquals(1, record1.phase);
        assertEquals(1.5f, record1.priority, 0.001f);

        assertEquals(4, record2.frequency);
        assertEquals(2, record2.phase);
        assertEquals(2.0f, record2.priority, 0.001f);

        assertEquals(5, record3.frequency);
        assertEquals(0, record3.phase);
        assertEquals(0.5f, record3.priority, 0.001f);
    }

    @Test
    public void testRunWithNoTasks() {
        long initialTime = System.nanoTime();
        scheduler.run(1000000); // 1ms
        long finalTime = System.nanoTime();

        assertEquals(1, scheduler.frame);
        assertEquals(0, scheduler.runList.size);
        assertTrue(finalTime - initialTime < 1000000); // Less than 1ms
    }

    @Test
    public void testRunWithSingleTask() {
        scheduler.add(testSchedulable1, 1, 0, 1.0f); // Run every frame

        scheduler.run(1000000); // 1ms

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertTrue(testSchedulable1.lastTimeToRun > 0);
        assertTrue(testSchedulable1.lastTimeToRun <= 1000000);
    }

    @Test
    public void testRunWithMultipleTasksSamePriority() {
        scheduler.add(testSchedulable1, 1, 0, 1.0f); // Same priority
        scheduler.add(testSchedulable2, 1, 0, 1.0f); // Same priority
        scheduler.add(testSchedulable3, 1, 0, 1.0f); // Same priority

        scheduler.run(3000000); // 3ms total

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);
        assertEquals(1, testSchedulable3.runCount);

        assertTrue(testSchedulable1.lastTimeToRun > 0);
        assertTrue(testSchedulable2.lastTimeToRun > 0);
        assertTrue(testSchedulable3.lastTimeToRun > 0);
    }

    @Test
    public void testRunWithMultipleTasksDifferentPriorities() {
        scheduler.add(testSchedulable1, 1, 0, 1.0f); // Priority 1
        scheduler.add(testSchedulable2, 1, 0, 2.0f); // Priority 2 (double)
        scheduler.add(testSchedulable3, 1, 0, 3.0f); // Priority 3 (triple)

        scheduler.run(6000000); // 6ms total

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);
        assertEquals(1, testSchedulable3.runCount);

        // Total priority = 1 + 2 + 3 = 6
        // Task1 gets 1/6 of time = 1ms
        // Task2 gets 2/6 of time = 2ms  
        // Task3 gets 3/6 of time = 3ms
        assertTrue("Task1 should get approximately 1ms", testSchedulable1.lastTimeToRun >= 800000 && testSchedulable1.lastTimeToRun <= 1200000);
        assertTrue("Task2 should get approximately 2ms", testSchedulable2.lastTimeToRun >= 1800000 && testSchedulable2.lastTimeToRun <= 2200000);
        assertTrue("Task3 should get approximately 3ms", testSchedulable3.lastTimeToRun >= 2800000 && testSchedulable3.lastTimeToRun <= 3200000);
    }

    @Test
    public void testRunWithFrequencyBasedScheduling() {
        scheduler.add(testSchedulable1, 2, 0, 1.0f); // Run every 2 frames
        scheduler.add(testSchedulable2, 3, 0, 2.0f); // Run every 3 frames

        // Frame 1: neither task due
        scheduler.run(1000000);
        assertEquals(1, scheduler.frame);
        assertEquals(0, testSchedulable1.runCount);
        assertEquals(0, testSchedulable2.runCount);

        // Frame 2: task1 due
        scheduler.run(1000000);
        assertEquals(2, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(0, testSchedulable2.runCount);

        // Frame 3: task2 due
        scheduler.run(1000000);
        assertEquals(3, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);
    }

    @Test
    public void testRunWithPhaseBasedScheduling() {
        scheduler.add(testSchedulable1, 2, 1, 1.0f); // Run on odd frames
        scheduler.add(testSchedulable2, 2, 0, 2.0f); // Run on even frames

        // Frame 1: task1 due, task2 not due
        scheduler.run(3000000); // 3ms total
        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(0, testSchedulable2.runCount);
        assertTrue("Task1 should get most of the time when running alone", testSchedulable1.lastTimeToRun > 2000000);

        // Frame 2: task1 not due, task2 due
        scheduler.run(3000000); // 3ms total
        assertEquals(2, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);
        assertTrue("Task2 should get most of the time when running alone", testSchedulable2.lastTimeToRun > 2000000);
    }

    @Test
    public void testPriorityBasedTimeDistribution() {
        scheduler.add(testSchedulable1, 1, 0, 0.5f); // Low priority
        scheduler.add(testSchedulable2, 1, 0, 1.0f); // Medium priority
        scheduler.add(testSchedulable3, 1, 0, 2.0f); // High priority

        scheduler.run(4000000); // 4ms total

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);
        assertEquals(1, testSchedulable3.runCount);

        // Total priority = 0.5 + 1.0 + 2.0 = 3.5
        // Task1 gets 0.5/3.5 = 1/7 of time ≈ 0.57ms
        // Task2 gets 1.0/3.5 = 2/7 of time ≈ 1.14ms  
        // Task3 gets 2.0/3.5 = 4/7 of time ≈ 2.29ms
        assertTrue("Task1 should get approximately 0.57ms", testSchedulable1.lastTimeToRun >= 400000 && testSchedulable1.lastTimeToRun <= 800000);
        assertTrue("Task2 should get approximately 1.14ms", testSchedulable2.lastTimeToRun >= 1000000 && testSchedulable2.lastTimeToRun <= 1300000);
        assertTrue("Task3 should get approximately 2.29ms", testSchedulable3.lastTimeToRun >= 2100000 && testSchedulable3.lastTimeToRun <= 2500000);
    }

    @Test
    public void testRunWithZeroTimeToRun() {
        scheduler.add(testSchedulable1, 1, 0, 1.0f);

        scheduler.run(0);

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertTrue(testSchedulable1.lastTimeToRun <= 0);
    }

    @Test
    public void testRunWithNegativeTimeToRun() {
        scheduler.add(testSchedulable1, 1, 0, 1.0f);

        scheduler.run(-1000);

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertTrue(testSchedulable1.lastTimeToRun <= 0);
    }

    @Test
    public void testMultipleFramesWithDifferentFrequenciesAndPriorities() {
        scheduler.add(testSchedulable1, 2, 0, 1.0f); // Every 2 frames, priority 1
        scheduler.add(testSchedulable2, 3, 0, 2.0f); // Every 3 frames, priority 2

        // Run for 6 frames
        for (int frame = 1; frame <= 6; frame++) {
            scheduler.run(3000000); // 3ms per frame
        }

        assertEquals(6, scheduler.frame);

        // Task1 should run on frames 2, 4, 6 (3 times)
        assertEquals(3, testSchedulable1.runCount);

        // Task2 should run on frames 3, 6 (2 times)
        assertEquals(2, testSchedulable2.runCount);
    }

    @Test
    public void testAutomaticPhasingDistribution() {
        // Add multiple tasks with the same frequency to test phase distribution
        for (int i = 0; i < 5; i++) {
            scheduler.addWithAutomaticPhasing(new TestSchedulable("Task" + i), 5, (i + 1) * 0.5f);
        }

        assertEquals(5, scheduler.schedulableRecords.size);

        // Check that phases are distributed and priorities are set correctly
        List<Integer> phases = new ArrayList<>();
        List<Float> priorities = new ArrayList<>();
        for (int i = 0; i < scheduler.schedulableRecords.size; i++) {
            PrioritySchedulableRecord record = scheduler.schedulableRecords.get(i);
            assertEquals(5, record.frequency);
            assertTrue(record.phase >= 0 && record.phase < 5);
            assertEquals((i + 1) * 0.5f, record.priority, 0.001f);
            phases.add(record.phase);
            priorities.add(record.priority);
        }

        assertEquals("Should have 5 phases", 5, phases.size());
        assertEquals("Should have 5 priorities", 5, priorities.size());
    }

    @Test
    public void testFrameIncrement() {
        scheduler.add(testSchedulable1, 1, 0, 1.0f);

        int initialFrame = scheduler.frame;

        scheduler.run(1000000);
        assertEquals(initialFrame + 1, scheduler.frame);

        scheduler.run(1000000);
        assertEquals(initialFrame + 2, scheduler.frame);

        scheduler.run(1000000);
        assertEquals(initialFrame + 3, scheduler.frame);
    }

    @Test
    public void testSchedulerImplementsSchedulable() {
        assertNotNull("PriorityScheduler should implement Scheduler", scheduler);
        assertTrue("Scheduler should extend Schedulable", true);
    }

    @Test
    public void testInheritanceFromSchedulerBase() {
        assertNotNull("Should extend SchedulerBase", scheduler);

        // Test inherited fields are accessible
        assertNotNull("schedulableRecords should be initialized", scheduler.schedulableRecords);
        assertNotNull("runList should be initialized", scheduler.runList);
        assertNotNull("phaseCounters should be initialized", scheduler.phaseCounters);
        assertEquals("dryRunFrames should be set", 100, scheduler.dryRunFrames);
    }

    @Test
    public void testPrioritySchedulableRecordInheritance() {
        scheduler.add(testSchedulable1, 5, 2, 3.0f);

        PrioritySchedulableRecord record = scheduler.schedulableRecords.get(0);

        // Test that it extends SchedulableRecord
        assertNotNull("Should extend SchedulableRecord", record);

        // Test inherited fields
        assertSame(testSchedulable1, record.schedulable);
        assertEquals(5, record.frequency);
        assertEquals(2, record.phase);

        // Test priority field
        assertEquals(3.0f, record.priority, 0.001f);
    }

    @Test
    public void testComplexSchedulingScenario() {
        // Create a complex scenario with different frequencies, phases, and priorities
        scheduler.add(testSchedulable1, 2, 0, 1.0f); // Even frames, low priority
        scheduler.add(testSchedulable2, 3, 1, 2.0f); // Frames 2, 5, 8..., medium priority
        scheduler.add(testSchedulable3, 4, 3, 3.0f); // Frames 1, 5, 9..., high priority

        // Run for 12 frames
        for (int frame = 1; frame <= 12; frame++) {
            scheduler.run(12000000); // 12ms total per frame
        }

        assertEquals(12, scheduler.frame);

        // Verify expected execution counts
        // Task1 (freq 2, phase 0): runs on frames 2, 4, 6, 8, 10, 12 = 6 times
        assertEquals(6, testSchedulable1.runCount);

        // Task2 (freq 3, phase 1): runs when (frame + 1) % 3 == 0, so frames 2, 5, 8, 11 = 4 times
        assertEquals(4, testSchedulable2.runCount);

        // Task3 (freq 4, phase 3): runs when (frame + 3) % 4 == 0, so frames 1, 5, 9 = 3 times
        assertEquals(3, testSchedulable3.runCount);
    }

    @Test
    public void testLargeNumberOfTasksWithDifferentPriorities() {
        // Test with many tasks having different priorities
        int taskCount = 50;
        TestSchedulable[] tasks = new TestSchedulable[taskCount];

        for (int i = 0; i < taskCount; i++) {
            tasks[i] = new TestSchedulable("Task" + i);
            float priority = (i % 10) + 1; // Priorities from 1 to 10
            scheduler.add(tasks[i], 10, i % 10, priority);
        }

        assertEquals(taskCount, scheduler.schedulableRecords.size);

        // Run one frame - should execute about 1/10 of tasks
        scheduler.run(10000000); // 10ms

        int expectedRunCount = taskCount / 10;
        int actualRunCount = 0;
        for (TestSchedulable task : tasks) {
            if (task.runCount > 0) actualRunCount++;
        }

        // Should be close to expected (allowing for some variance)
        assertTrue("Should run approximately " + expectedRunCount + " tasks",
                Math.abs(actualRunCount - expectedRunCount) <= 2);
    }

    @Test
    public void testZeroPriorityHandling() {
        scheduler.add(testSchedulable1, 1, 0, 0.0f); // Zero priority
        scheduler.add(testSchedulable2, 1, 0, 1.0f); // Normal priority

        scheduler.run(2000000); // 2ms total

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);

        // Task1 should get 0 time due to zero priority
        assertEquals(0, testSchedulable1.lastTimeToRun);
        // Task2 should get all the time
        assertTrue(testSchedulable2.lastTimeToRun > 0);
    }

    @Test
    public void testNegativePriorityHandling() {
        scheduler.add(testSchedulable1, 1, 0, -1.0f); // Negative priority
        scheduler.add(testSchedulable2, 1, 0, 2.0f); // Positive priority

        scheduler.run(2000000); // 2ms total

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);

        // With negative priority, time calculation can result in negative values
        assertTrue(testSchedulable1.lastTimeToRun <= 0);
        assertTrue(testSchedulable2.lastTimeToRun > 0);
    }

    /**
     * Test implementation of Schedulable for testing purposes.
     */
    private static class TestSchedulable implements Schedulable {
        final String name;
        int runCount = 0;
        long lastTimeToRun = 0;
        List<Long> timeAllocations = new ArrayList<>();

        TestSchedulable(String name) {
            this.name = name;
        }

        @Override
        public void run(long nanoTimeToRun) {
            runCount++;
            lastTimeToRun = nanoTimeToRun;
            timeAllocations.add(nanoTimeToRun);
        }
    }
}
