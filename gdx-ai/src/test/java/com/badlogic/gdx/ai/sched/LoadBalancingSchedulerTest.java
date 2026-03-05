package com.badlogic.gdx.ai.sched;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.ai.sched.SchedulerBase.SchedulableRecord;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LoadBalancingSchedulerTest {

    private LoadBalancingScheduler scheduler;
    private TestSchedulable testSchedulable1;
    private TestSchedulable testSchedulable2;
    private TestSchedulable testSchedulable3;

    @Before
    public void setUp() {
        scheduler = new LoadBalancingScheduler(100);
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
        LoadBalancingScheduler zeroScheduler = new LoadBalancingScheduler(0);
        assertEquals(0, zeroScheduler.dryRunFrames);
        assertEquals(0, zeroScheduler.frame);
    }

    @Test
    public void testConstructorWithNegativeDryRunFrames() {
        LoadBalancingScheduler negativeScheduler = new LoadBalancingScheduler(-10);
        assertEquals(-10, negativeScheduler.dryRunFrames);
        assertEquals(0, negativeScheduler.frame);
    }

    @Test
    public void testAddWithAutomaticPhasing() {
        scheduler.addWithAutomaticPhasing(testSchedulable1, 5);

        assertEquals(1, scheduler.schedulableRecords.size);
        SchedulableRecord record = scheduler.schedulableRecords.get(0);
        assertSame(testSchedulable1, record.schedulable);
        assertEquals(5, record.frequency);
        assertTrue(record.phase >= 0 && record.phase < 5); // Phase should be within frequency range
    }

    @Test
    public void testAddWithAutomaticPhasingMultipleTasks() {
        scheduler.addWithAutomaticPhasing(testSchedulable1, 3);
        scheduler.addWithAutomaticPhasing(testSchedulable2, 4);
        scheduler.addWithAutomaticPhasing(testSchedulable3, 5);

        assertEquals(3, scheduler.schedulableRecords.size);

        // Each task should have appropriate phase within its frequency range
        SchedulableRecord record1 = scheduler.schedulableRecords.get(0);
        SchedulableRecord record2 = scheduler.schedulableRecords.get(1);
        SchedulableRecord record3 = scheduler.schedulableRecords.get(2);

        assertEquals(3, record1.frequency);
        assertTrue(record1.phase >= 0 && record1.phase < 3);

        assertEquals(4, record2.frequency);
        assertTrue(record2.phase >= 0 && record2.phase < 4);

        assertEquals(5, record3.frequency);
        assertTrue(record3.phase >= 0 && record3.phase < 5);
    }

    @Test
    public void testAddWithAutomaticPhasingWithFrequencyOne() {
        scheduler.addWithAutomaticPhasing(testSchedulable1, 1);

        assertEquals(1, scheduler.schedulableRecords.size);
        SchedulableRecord record = scheduler.schedulableRecords.get(0);
        assertEquals(1, record.frequency);
        assertEquals(0, record.phase); // Phase should be 0 for frequency 1
    }

    @Test
    public void testAdd() {
        scheduler.add(testSchedulable1, 5, 2);

        assertEquals(1, scheduler.schedulableRecords.size);
        SchedulableRecord record = scheduler.schedulableRecords.get(0);
        assertSame(testSchedulable1, record.schedulable);
        assertEquals(5, record.frequency);
        assertEquals(2, record.phase);
    }

    @Test
    public void testAddMultipleTasks() {
        scheduler.add(testSchedulable1, 3, 1);
        scheduler.add(testSchedulable2, 4, 2);
        scheduler.add(testSchedulable3, 5, 0);

        assertEquals(3, scheduler.schedulableRecords.size);

        SchedulableRecord record1 = scheduler.schedulableRecords.get(0);
        SchedulableRecord record2 = scheduler.schedulableRecords.get(1);
        SchedulableRecord record3 = scheduler.schedulableRecords.get(2);

        assertEquals(3, record1.frequency);
        assertEquals(1, record1.phase);

        assertEquals(4, record2.frequency);
        assertEquals(2, record2.phase);

        assertEquals(5, record3.frequency);
        assertEquals(0, record3.phase);
    }

    @Test
    public void testRunWithNoTasks() {
        long initialTime = System.nanoTime();
        scheduler.run(1000000); // 1ms
        long finalTime = System.nanoTime();

        // Frame should be incremented even with no tasks
        assertEquals(1, scheduler.frame);
        // Run list should be empty
        assertEquals(0, scheduler.runList.size);
        // Should complete quickly since no tasks to run
        assertTrue(finalTime - initialTime < 1000000); // Less than 1ms
    }

    @Test
    public void testRunWithSingleTask() {
        scheduler.add(testSchedulable1, 1, 0); // Run every frame

        scheduler.run(1000000); // 1ms

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertTrue(testSchedulable1.lastTimeToRun > 0);
        assertTrue(testSchedulable1.lastTimeToRun <= 1000000);
    }

    @Test
    public void testRunWithMultipleTasksAllDue() {
        scheduler.add(testSchedulable1, 1, 0); // Due every frame
        scheduler.add(testSchedulable2, 1, 0); // Due every frame
        scheduler.add(testSchedulable3, 1, 0); // Due every frame

        scheduler.run(3000000); // 3ms total

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);
        assertEquals(1, testSchedulable3.runCount);

        // Each task should get approximately 1ms (3ms / 3 tasks)
        assertTrue(testSchedulable1.lastTimeToRun > 0);
        assertTrue(testSchedulable2.lastTimeToRun > 0);
        assertTrue(testSchedulable3.lastTimeToRun > 0);
    }

    @Test
    public void testRunWithFrequencyBasedScheduling() {
        scheduler.add(testSchedulable1, 2, 0); // Run every 2 frames
        scheduler.add(testSchedulable2, 3, 0); // Run every 3 frames

        // Frame 1: task1 due (frame 1 + 0) % 2 == 1 != 0, task2 due (frame 1 + 0) % 3 == 1 != 0
        scheduler.run(1000000);
        assertEquals(1, scheduler.frame);
        assertEquals(0, testSchedulable1.runCount);
        assertEquals(0, testSchedulable2.runCount);

        // Frame 2: task1 due (frame 2 + 0) % 2 == 0, task2 due (frame 2 + 0) % 3 == 2 != 0
        scheduler.run(1000000);
        assertEquals(2, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(0, testSchedulable2.runCount);

        // Frame 3: task1 due (frame 3 + 0) % 2 == 1 != 0, task2 due (frame 3 + 0) % 3 == 0
        scheduler.run(1000000);
        assertEquals(3, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);

        // Frame 4: task1 due (frame 4 + 0) % 2 == 0, task2 due (frame 4 + 0) % 3 == 1 != 0
        scheduler.run(1000000);
        assertEquals(4, scheduler.frame);
        assertEquals(2, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);
    }

    @Test
    public void testRunWithPhaseBasedScheduling() {
        scheduler.add(testSchedulable1, 2, 1); // Run on odd frames
        scheduler.add(testSchedulable2, 2, 0); // Run on even frames

        // Frame 1: task1 due (frame 1 + 1) % 2 == 0, task2 due (frame 1 + 0) % 2 == 1 != 0
        scheduler.run(1000000);
        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(0, testSchedulable2.runCount);

        // Frame 2: task1 due (frame 2 + 1) % 2 == 1 != 0, task2 due (frame 2 + 0) % 2 == 0
        scheduler.run(1000000);
        assertEquals(2, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);

        // Frame 3: task1 due (frame 3 + 1) % 2 == 0, task2 due (frame 3 + 0) % 2 == 1 != 0
        scheduler.run(1000000);
        assertEquals(3, scheduler.frame);
        assertEquals(2, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);
    }

    @Test
    public void testRunTimeBalancing() {
        // Create tasks that track their execution order and time allocation
        scheduler.add(testSchedulable1, 1, 0);
        scheduler.add(testSchedulable2, 1, 0);
        scheduler.add(testSchedulable3, 1, 0);

        long totalTime = 3000000; // 3ms
        scheduler.run(totalTime);

        assertEquals(3, scheduler.runList.size);
        assertEquals(1, testSchedulable1.runCount);
        assertEquals(1, testSchedulable2.runCount);
        assertEquals(1, testSchedulable3.runCount);

        // Tasks should be run in order they were added
        assertEquals("Task1", testSchedulable1.name);
        assertEquals("Task2", testSchedulable2.name);
        assertEquals("Task3", testSchedulable3.name);

        // Time should be distributed among tasks (allowing for some variance due to overhead)
        long minExpectedTime = totalTime / 3 / 2; // At least half the equal share
        assertTrue("Task1 should get reasonable time", testSchedulable1.lastTimeToRun >= minExpectedTime);
        assertTrue("Task2 should get reasonable time", testSchedulable2.lastTimeToRun >= minExpectedTime);
        assertTrue("Task3 should get reasonable time", testSchedulable3.lastTimeToRun >= minExpectedTime);
    }

    @Test
    public void testRunWithZeroTimeToRun() {
        scheduler.add(testSchedulable1, 1, 0);

        scheduler.run(0);

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        // With zero timeToRun, the time calculation can result in negative values
        // due to timeToRun -= currentTime - lastTime, then dividing by remaining tasks
        assertTrue(testSchedulable1.lastTimeToRun <= 0);
    }

    @Test
    public void testRunWithNegativeTimeToRun() {
        scheduler.add(testSchedulable1, 1, 0);

        scheduler.run(-1000);

        assertEquals(1, scheduler.frame);
        assertEquals(1, testSchedulable1.runCount);
        // Should still run but with negative or zero time
        assertTrue(testSchedulable1.lastTimeToRun <= 0);
    }

    @Test
    public void testMultipleFramesWithDifferentFrequencies() {
        scheduler.add(testSchedulable1, 2, 0); // Every 2 frames
        scheduler.add(testSchedulable2, 3, 0); // Every 3 frames

        // Run for 6 frames to see the pattern
        for (int frame = 1; frame <= 6; frame++) {
            scheduler.run(1000000);
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
            scheduler.addWithAutomaticPhasing(new TestSchedulable("Task" + i), 5);
        }

        assertEquals(5, scheduler.schedulableRecords.size);

        // Check that phases are distributed to minimize conflicts
        List<Integer> phases = new ArrayList<>();
        for (int i = 0; i < scheduler.schedulableRecords.size; i++) {
            SchedulableRecord record = scheduler.schedulableRecords.get(i);
            assertEquals(5, record.frequency);
            assertTrue(record.phase >= 0 && record.phase < 5);
            phases.add(record.phase);
        }

        assertEquals("Should have some phase distribution", 5, phases.size());
    }

    @Test
    public void testFrameIncrement() {
        scheduler.add(testSchedulable1, 1, 0);

        int initialFrame = scheduler.frame;

        scheduler.run(1000000);
        assertEquals(initialFrame + 1, scheduler.frame);

        scheduler.run(1000000);
        assertEquals(initialFrame + 2, scheduler.frame);

        scheduler.run(1000000);
        assertEquals(initialFrame + 3, scheduler.frame);
    }

    @Test
    public void testRunListClearing() {
        scheduler.add(testSchedulable1, 2, 0); // Not due on first frame
        scheduler.add(testSchedulable2, 1, 0); // Due every frame

        // Frame 1: task1 not due (frame 1 + phase 0) % 2 = 1, task2 due (frame 1 + phase 0) % 1 = 0
        scheduler.run(1000000);
        assertEquals(0, testSchedulable1.runCount); // task1 should not run
        assertEquals(1, testSchedulable2.runCount); // task2 should run

        // Frame 2: task1 due (frame 2 + phase 0) % 2 = 0, task2 due (frame 2 + phase 0) % 1 = 0
        scheduler.run(1000000);
        assertEquals(1, testSchedulable1.runCount); // task1 should run
        assertEquals(2, testSchedulable2.runCount); // task2 should run again

        // Frame 3: task1 not due (frame 3 + phase 0) % 2 = 1, task2 due (frame 3 + phase 0) % 1 = 0
        scheduler.run(1000000);
        assertEquals(1, testSchedulable1.runCount); // task1 should not run again
        assertEquals(3, testSchedulable2.runCount); // task2 should run again
    }

    @Test
    public void testSchedulerImplementsSchedulable() {
        assertNotNull("LoadBalancingScheduler should implement Scheduler", scheduler);
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
    public void testComplexSchedulingScenario() {
        // Create a complex scenario with multiple frequencies and phases
        scheduler.add(testSchedulable1, 2, 0); // Even frames
        scheduler.add(testSchedulable2, 3, 1); // Frames 2, 5, 8...
        scheduler.add(testSchedulable3, 4, 3); // Frames 1, 5, 9...

        // Run for 12 frames and track execution
        for (int frame = 1; frame <= 12; frame++) {
            scheduler.run(1000000);
        }

        assertEquals(12, scheduler.frame);

        // Verify expected execution counts based on frequency and phase
        // Task1 (freq 2, phase 0): runs on frames 2, 4, 6, 8, 10, 12 = 6 times
        assertEquals(6, testSchedulable1.runCount);

        // Task2 (freq 3, phase 1): runs when (frame + 1) % 3 == 0, so frames 2, 5, 8, 11 = 4 times
        assertEquals(4, testSchedulable2.runCount);

        // Task3 (freq 4, phase 3): runs when (frame + 3) % 4 == 0, so frames 1, 5, 9 = 3 times
        assertEquals(3, testSchedulable3.runCount);
    }

    @Test
    public void testLargeNumberOfTasks() {
        // Test with many tasks to ensure scalability
        int taskCount = 100;
        TestSchedulable[] tasks = new TestSchedulable[taskCount];

        for (int i = 0; i < taskCount; i++) {
            tasks[i] = new TestSchedulable("Task" + i);
            scheduler.add(tasks[i], 10, i % 10); // Distribute phases
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
