package com.badlogic.gdx.ai.sched;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.ai.sched.SchedulerBase.SchedulableRecord;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SchedulerBaseTest {

    private TestScheduler scheduler;
    private TestSchedulable testSchedulable1;
    private TestSchedulable testSchedulable2;
    private TestSchedulable testSchedulable3;

    @Before
    public void setUp() {
        scheduler = new TestScheduler(100);
        testSchedulable1 = new TestSchedulable("Task1");
        testSchedulable2 = new TestSchedulable("Task2");
        testSchedulable3 = new TestSchedulable("Task3");
    }

    @Test
    public void testConstructor() {
        assertEquals(100, scheduler.dryRunFrames);
        assertNotNull(scheduler.schedulableRecords);
        assertNotNull(scheduler.runList);
        assertNotNull(scheduler.phaseCounters);
        assertTrue(scheduler.schedulableRecords.isEmpty());
        assertTrue(scheduler.runList.isEmpty());
        assertTrue(scheduler.phaseCounters.isEmpty());
    }

    @Test
    public void testConstructorWithZeroDryRunFrames() {
        TestScheduler zeroScheduler = new TestScheduler(0);
        assertEquals(0, zeroScheduler.dryRunFrames);
        assertNotNull(zeroScheduler.schedulableRecords);
        assertNotNull(zeroScheduler.runList);
        assertNotNull(zeroScheduler.phaseCounters);
    }

    @Test
    public void testConstructorWithNegativeDryRunFrames() {
        TestScheduler negativeScheduler = new TestScheduler(-50);
        assertEquals(-50, negativeScheduler.dryRunFrames);
        assertNotNull(negativeScheduler.schedulableRecords);
        assertNotNull(negativeScheduler.runList);
        assertNotNull(negativeScheduler.phaseCounters);
    }

    @Test
    public void testCalculatePhaseWithNoExistingTasks() {
        int phase = scheduler.calculatePhase(5);
        assertTrue("Phase should be between 0 and frequency-1", phase >= 0 && phase < 5);
        assertEquals(0, phase);
    }

    @Test
    public void testCalculatePhaseWithSingleExistingTask() {

        scheduler.addDirect(testSchedulable1, 3, 0);
        int phase = scheduler.calculatePhase(4);
        assertTrue("Phase should be between 0 and 3", phase >= 0 && phase < 4);
    }

    @Test
    public void testCalculatePhaseWithMultipleExistingTasks() {

        scheduler.addDirect(testSchedulable1, 2, 0);
        scheduler.addDirect(testSchedulable2, 3, 1);

        int phase = scheduler.calculatePhase(4);
        assertTrue("Phase should be between 0 and 3", phase >= 0 && phase < 4);
    }

    @Test
    public void testCalculatePhaseWithFrequencyOne() {

        scheduler.addDirect(testSchedulable1, 5, 0);
        scheduler.addDirect(testSchedulable2, 3, 1);

        int phase = scheduler.calculatePhase(1);
        assertEquals(0, phase);
    }

    @Test
    public void testCalculatePhaseWithLargeFrequency() {

        scheduler.addDirect(testSchedulable1, 2, 0);
        scheduler.addDirect(testSchedulable2, 3, 1);

        int phase = scheduler.calculatePhase(100);
        assertTrue("Phase should be between 0 and 99", phase >= 0 && phase < 100);
    }

    @Test
    public void testCalculatePhaseExpandsPhaseCounters() {

        assertEquals(0, scheduler.phaseCounters.size);

        int phase = scheduler.calculatePhase(50);

        // Phase counters should be expanded to accommodate the frequency
        assertTrue("Phase counters should be expanded", scheduler.phaseCounters.size >= 50);
    }

    @Test
    public void testCalculatePhaseWithZeroDryRunFrames() {
        TestScheduler zeroDryRunScheduler = new TestScheduler(0);

        zeroDryRunScheduler.addDirect(testSchedulable1, 3, 0);

        int phase = zeroDryRunScheduler.calculatePhase(4);
        assertEquals(0, phase);
    }

    @Test
    public void testCalculatePhaseWithNegativeDryRunFrames() {
        TestScheduler negativeDryRunScheduler = new TestScheduler(-10);

        negativeDryRunScheduler.addDirect(testSchedulable1, 3, 0);

        int phase = negativeDryRunScheduler.calculatePhase(4);
        assertEquals(0, phase);
    }

    @Test
    public void testCalculatePhaseConsistency() {

        scheduler.addDirect(testSchedulable1, 2, 0);
        scheduler.addDirect(testSchedulable2, 3, 1);

        // Calculate phase multiple times for same frequency
        int phase1 = scheduler.calculatePhase(4);
        int phase2 = scheduler.calculatePhase(4);
        int phase3 = scheduler.calculatePhase(4);

        // Results should be consistent
        assertEquals(phase1, phase2);
        assertEquals(phase2, phase3);
    }

    @Test
    public void testCalculatePhaseWithDifferentFrequencies() {
        // Add existing tasks
        scheduler.addDirect(testSchedulable1, 2, 0);

        // Test with different frequencies
        int phase2 = scheduler.calculatePhase(2);
        int phase3 = scheduler.calculatePhase(3);
        int phase5 = scheduler.calculatePhase(5);
        int phase10 = scheduler.calculatePhase(10);

        // All should be within valid ranges
        assertTrue(phase2 >= 0 && phase2 < 2);
        assertTrue(phase3 >= 0 && phase3 < 3);
        assertTrue(phase5 >= 0 && phase5 < 5);
        assertTrue(phase10 >= 0 && phase10 < 10);
    }

    @Test
    public void testSchedulableRecordConstructor() {
        TestSchedulableRecord record = new TestSchedulableRecord(testSchedulable1, 5, 2);

        assertEquals(testSchedulable1, record.schedulable);
        assertEquals(5, record.frequency);
        assertEquals(2, record.phase);
    }

    @Test
    public void testSchedulableRecordFields() {
        TestSchedulableRecord record = new TestSchedulableRecord(testSchedulable1, 10, 7);

        // Test that fields are properly set
        assertNotNull(record.schedulable);
        assertEquals(10, record.frequency);
        assertEquals(7, record.phase);
    }

    @Test
    public void testAddDirect() {
        // Test the helper method that directly adds schedulable records
        scheduler.addDirect(testSchedulable1, 5, 2);

        assertEquals(1, scheduler.schedulableRecords.size);
        TestSchedulableRecord record = scheduler.schedulableRecords.get(0);
        assertEquals(testSchedulable1, record.schedulable);
        assertEquals(5, record.frequency);
        assertEquals(2, record.phase);
    }

    @Test
    public void testAddDirectMultiple() {
        scheduler.addDirect(testSchedulable1, 2, 0);
        scheduler.addDirect(testSchedulable2, 3, 1);
        scheduler.addDirect(testSchedulable3, 4, 2);

        assertEquals(3, scheduler.schedulableRecords.size);

        TestSchedulableRecord record1 = scheduler.schedulableRecords.get(0);
        TestSchedulableRecord record2 = scheduler.schedulableRecords.get(1);
        TestSchedulableRecord record3 = scheduler.schedulableRecords.get(2);

        assertEquals(testSchedulable1, record1.schedulable);
        assertEquals(2, record1.frequency);
        assertEquals(0, record1.phase);

        assertEquals(testSchedulable2, record2.schedulable);
        assertEquals(3, record2.frequency);
        assertEquals(1, record2.phase);

        assertEquals(testSchedulable3, record3.schedulable);
        assertEquals(4, record3.frequency);
        assertEquals(2, record3.phase);
    }

    @Test
    public void testCalculatePhaseOptimalDistribution() {
        // Create a scenario where optimal phase distribution is clear
        // Add tasks that all run on phase 0 of their frequencies
        scheduler.addDirect(testSchedulable1, 2, 0);  // Runs on frames 0, 2, 4, 6, 8...
        scheduler.addDirect(testSchedulable2, 3, 0);  // Runs on frames 0, 3, 6, 9...

        // For frequency 4, phase 0 will have conflicts (frames 0, 6, 12...)
        // Phase 1 will have conflicts (frames 1, 7, 13...) - none with existing tasks
        // Phase 2 will have conflicts (frames 2, 8, 14...) - conflicts with task 1
        // Phase 3 will have conflicts (frames 3, 9, 15...) - conflicts with task 2

        int phase = scheduler.calculatePhase(4);

        // Phase 1 should be optimal as it avoids conflicts with both existing tasks
        assertEquals(1, phase);
    }

    @Test
    public void testCalculatePhaseWithComplexScenario() {
        // Create a complex scenario with multiple tasks
        scheduler.addDirect(testSchedulable1, 2, 0);  // Even frames
        scheduler.addDirect(testSchedulable2, 4, 1);  // Frames 1, 5, 9, 13...
        scheduler.addDirect(testSchedulable3, 3, 2);  // Frames 2, 5, 8, 11...

        // Calculate phase for frequency 6
        int phase = scheduler.calculatePhase(6);

        // Should find the phase with minimum conflicts
        assertTrue("Phase should be between 0 and 5", phase >= 0 && phase < 6);
    }

    @Test
    public void testPhaseCountersReset() {
        // Add some tasks to populate phase counters
        scheduler.addDirect(testSchedulable1, 3, 0);
        scheduler.calculatePhase(5);

        // Phase counters should be reset to the requested frequency
        assertEquals(5, scheduler.phaseCounters.size);

        // After calculatePhase, counters contain conflict counts from the dry run simulation
        // They should not be negative and should be reasonable values
        for (int i = 0; i < scheduler.phaseCounters.size; i++) {
            int count = scheduler.phaseCounters.get(i);
            assertTrue("Phase counter should be non-negative", count >= 0);
            assertTrue("Phase counter should be reasonable", count <= scheduler.dryRunFrames);
        }
    }

    @Test
    public void testCalculatePhaseWithSameFrequency() {
        // Add task with frequency 3
        scheduler.addDirect(testSchedulable1, 3, 0);

        // Calculate phase for another task with same frequency
        int phase = scheduler.calculatePhase(3);

        // Should choose phase that minimizes conflicts
        assertTrue("Phase should be between 0 and 2", phase >= 0 && phase < 3);
        // Phase 1 or 2 should be chosen to avoid conflicts with existing task at phase 0
        assertTrue(phase != 0);
    }

    /**
     * Test implementation of SchedulerBase for testing purposes.
     */
    private static class TestScheduler extends SchedulerBase<TestSchedulableRecord> {

        public TestScheduler(int dryRunFrames) {
            super(dryRunFrames);
        }

        @Override
        public void addWithAutomaticPhasing(Schedulable schedulable, int frequency) {
            int phase = calculatePhase(frequency);
            addDirect((TestSchedulable) schedulable, frequency, phase);
        }

        @Override
        public void add(Schedulable schedulable, int frequency, int phase) {
            addDirect((TestSchedulable) schedulable, frequency, phase);
        }

        @Override
        public void run(long nanoTimeToRun) {
            // Empty implementation for testing
        }

        // Helper method to directly add schedulable records for testing
        public void addDirect(TestSchedulable schedulable, int frequency, int phase) {
            schedulableRecords.add(new TestSchedulableRecord(schedulable, frequency, phase));
        }
    }

    /**
     * Test implementation of SchedulableRecord for testing purposes.
     */
    private static class TestSchedulableRecord extends SchedulableRecord {

        TestSchedulableRecord(Schedulable schedulable, int frequency, int phase) {
            super(schedulable, frequency, phase);
        }
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
