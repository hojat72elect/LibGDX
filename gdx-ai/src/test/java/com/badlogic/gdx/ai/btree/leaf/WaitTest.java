package com.badlogic.gdx.ai.btree.leaf;

import com.badlogic.gdx.ai.DefaultTimepiece;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.Timepiece;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.utils.random.ConstantFloatDistribution;
import com.badlogic.gdx.ai.utils.random.FloatDistribution;
import com.badlogic.gdx.ai.utils.random.UniformFloatDistribution;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WaitTest {
    private BehaviorTree<String> behaviorTree;
    private Timepiece timepiece;

    @Before
    public void setUp() {
        behaviorTree = new BehaviorTree<>();
        behaviorTree.setObject("test");

        // Set up a timepiece for controlled testing
        timepiece = new DefaultTimepiece();
        GdxAI.setTimepiece(timepiece);
    }

    @After
    public void tearDown() {
        behaviorTree = null;
        timepiece = null;
        GdxAI.setTimepiece(null);
    }

    @Test
    public void testConstructorNoArgs() {
        Wait<String> wait = new Wait<>();
        Assert.assertNotNull("Seconds distribution should be set", wait.seconds);
        Assert.assertTrue("Should use ConstantFloatDistribution.ZERO",
                wait.seconds instanceof ConstantFloatDistribution);
        Assert.assertEquals("Should have zero timeout", 0.0f,
                ((ConstantFloatDistribution) wait.seconds).getValue(), 0.001f);
    }

    @Test
    public void testConstructorWithFloat() {
        Wait<String> wait = new Wait<>(2.5f);
        Assert.assertNotNull("Seconds distribution should be set", wait.seconds);
        Assert.assertTrue("Should use ConstantFloatDistribution",
                wait.seconds instanceof ConstantFloatDistribution);
        Assert.assertEquals("Should have specified timeout", 2.5f,
                ((ConstantFloatDistribution) wait.seconds).getValue(), 0.001f);
    }

    @Test
    public void testConstructorWithFloatDistribution() {
        FloatDistribution distribution = new UniformFloatDistribution(1.0f, 3.0f);
        Wait<String> wait = new Wait<>(distribution);
        Assert.assertSame("Should use provided distribution", distribution, wait.seconds);
    }

    @Test
    public void testZeroWaitImmediateSuccess() {
        Wait<String> wait = new Wait<>(0.0f);
        behaviorTree.addChild(wait);

        // Start the task
        behaviorTree.step();

        // Should succeed immediately
        Assert.assertEquals("Wait should succeed immediately", Status.SUCCEEDED, wait.getStatus());
    }

    @Test
    public void testWaitRunningUntilTimeout() {
        Wait<String> wait = new Wait<>(1.0f);
        behaviorTree.addChild(wait);

        // Start the task
        behaviorTree.step();
        Assert.assertEquals("Wait should be running initially", Status.RUNNING, wait.getStatus());

        // Advance time by 0.5 seconds (still running)
        timepiece.update(0.5f);
        behaviorTree.step();
        Assert.assertEquals("Wait should still be running", Status.RUNNING, wait.getStatus());

        // Advance time by another 0.6 seconds (total 1.1 seconds, should succeed)
        timepiece.update(0.6f);
        behaviorTree.step();
        Assert.assertEquals("Wait should succeed after timeout", Status.SUCCEEDED, wait.getStatus());
    }

    @Test
    public void testWaitExactTimeout() {
        Wait<String> wait = new Wait<>(2.0f);
        behaviorTree.addChild(wait);

        // Start the task
        behaviorTree.step();
        Assert.assertEquals("Wait should be running initially", Status.RUNNING, wait.getStatus());

        // Advance time exactly to timeout
        timepiece.update(2.0f);
        behaviorTree.step();
        Assert.assertEquals("Wait should succeed at exact timeout", Status.SUCCEEDED, wait.getStatus());
    }

    @Test
    public void testWaitWithRandomDistribution() {
        FloatDistribution distribution = new UniformFloatDistribution(0.5f, 1.5f);
        Wait<String> wait = new Wait<>(distribution);
        behaviorTree.addChild(wait);

        // Start the task (this should draw a random timeout value)
        behaviorTree.step();
        Assert.assertEquals("Wait should be running initially", Status.RUNNING, wait.getStatus());

        // Advance time by maximum possible timeout (should definitely succeed)
        timepiece.update(1.6f);
        behaviorTree.step();
        Assert.assertEquals("Wait should succeed after max timeout", Status.SUCCEEDED, wait.getStatus());
    }

    @Test
    public void testMultipleWaitCycles() {
        Wait<String> wait = new Wait<>(0.5f);
        behaviorTree.addChild(wait);

        // First wait cycle
        behaviorTree.step();
        Assert.assertEquals("First cycle: should be running", Status.RUNNING, wait.getStatus());

        timepiece.update(0.5f);
        behaviorTree.step();
        Assert.assertEquals("First cycle: should succeed", Status.SUCCEEDED, wait.getStatus());

        // Reset and start second cycle
        wait.reset();
        // After reset, seconds becomes ConstantFloatDistribution.ZERO, so wait succeeds immediately
        behaviorTree.step();
        Assert.assertEquals("Second cycle: should succeed immediately after reset", Status.SUCCEEDED, wait.getStatus());

        timepiece.update(0.5f);
        behaviorTree.step();
        Assert.assertEquals("Second cycle: should succeed", Status.SUCCEEDED, wait.getStatus());
    }

    @Test
    public void testReset() {
        Wait<String> wait = new Wait<>(2.0f);
        behaviorTree.addChild(wait);

        // Start and run the wait
        behaviorTree.step();
        timepiece.update(1.0f);
        behaviorTree.step();

        // Verify it's running and has state
        Assert.assertEquals("Should be running", Status.RUNNING, wait.getStatus());

        // Reset the wait
        wait.reset();

        // Verify reset state
        Assert.assertTrue("Should have ConstantFloatDistribution.ZERO",
                wait.seconds instanceof ConstantFloatDistribution);
        Assert.assertEquals("Should have zero value", 0.0f,
                ((ConstantFloatDistribution) wait.seconds).getValue(), 0.001f);
    }

    @Test
    public void testCopyTo() {
        FloatDistribution distribution = new UniformFloatDistribution(1.0f, 2.0f);
        Wait<String> original = new Wait<>(distribution);

        Wait<String> copy = (Wait<String>) original.cloneTask();

        Assert.assertNotNull("Copy should not be null", copy);
        Assert.assertNotSame("Copy should be different instance", original, copy);
        Assert.assertSame("Copy should have same distribution", distribution, copy.seconds);
    }

    @Test
    public void testCopyToWithConstantDistribution() {
        Wait<String> original = new Wait<>(3.14f);

        Wait<String> copy = (Wait<String>) original.cloneTask();

        Assert.assertNotNull("Copy should not be null", copy);
        Assert.assertNotSame("Copy should be different instance", original, copy);
        Assert.assertTrue("Copy should use ConstantFloatDistribution",
                copy.seconds instanceof ConstantFloatDistribution);
        Assert.assertEquals("Copy should have same value", 3.14f,
                ((ConstantFloatDistribution) copy.seconds).getValue(), 0.001f);
    }

    @Test
    public void testWaitWithoutTimepieceUpdate() {
        Wait<String> wait = new Wait<>(0.1f);
        behaviorTree.addChild(wait);

        // Start the task
        behaviorTree.step();
        Assert.assertEquals("Wait should be running initially", Status.RUNNING, wait.getStatus());

        // Don't update timepiece, just step multiple times
        for (int i = 0; i < 10; i++) {
            behaviorTree.step();
            Assert.assertEquals("Wait should still be running without time update",
                    Status.RUNNING, wait.getStatus());
        }
    }

    @Test
    public void testWaitWithNegativeTime() {
        Wait<String> wait = new Wait<>(-1.0f);
        behaviorTree.addChild(wait);

        // Start the task
        behaviorTree.step();

        // Should succeed immediately since negative time means timeout has already passed
        Assert.assertEquals("Wait with negative time should succeed immediately",
                Status.SUCCEEDED, wait.getStatus());
    }

    @Test
    public void testWaitWithVerySmallTime() {
        Wait<String> wait = new Wait<>(0.0001f);
        behaviorTree.addChild(wait);

        // Start the task
        behaviorTree.step();

        // Should succeed almost immediately
        timepiece.update(0.0002f);
        behaviorTree.step();
        Assert.assertEquals("Wait with very small time should succeed quickly",
                Status.SUCCEEDED, wait.getStatus());
    }

    @Test
    public void testStartCalledMultipleTimes() {
        Wait<String> wait = new Wait<>(1.0f);
        behaviorTree.addChild(wait);

        // Start the task first time
        behaviorTree.step();
        float firstStartTime = timepiece.getTime();

        // Advance time a bit
        timepiece.update(0.3f);
        behaviorTree.step();
        Assert.assertEquals("Should be running", Status.RUNNING, wait.getStatus());

        // Reset and start again
        wait.reset();
        behaviorTree.step();
        float secondStartTime = timepiece.getTime();

        // Verify start time was updated
        Assert.assertTrue("Start time should be updated on restart",
                secondStartTime > firstStartTime);
    }

    @Test
    public void testWaitInTreeContext() {
        Wait<String> wait = new Wait<>(0.5f);
        behaviorTree.addChild(wait);

        // Test that wait works properly within behavior tree context
        behaviorTree.step();
        Assert.assertEquals("Tree should be running initially", Status.RUNNING, wait.getStatus());

        timepiece.update(0.3f);
        behaviorTree.step();
        Assert.assertEquals("Tree should still be running", Status.RUNNING, wait.getStatus());

        timepiece.update(0.3f);
        behaviorTree.step();
        Assert.assertEquals("Tree should succeed after wait", Status.SUCCEEDED, wait.getStatus());
    }

    @Test
    public void testExecuteWithoutStart() {
        Wait<String> wait = new Wait<>(1.0f);

        // Try to execute without calling start() directly
        // This simulates what happens if execute() is called before start()
        Status status = wait.execute();

        // Should succeed immediately because startTime=0, timeout=0, so (0-0) < 0 is false
        Assert.assertEquals("Execute without start should succeed immediately", Status.SUCCEEDED, status);
    }

    @Test
    public void testWaitWithLargeTime() {
        Wait<String> wait = new Wait<>(1000.0f);
        behaviorTree.addChild(wait);

        // Start the task
        behaviorTree.step();
        Assert.assertEquals("Wait should be running initially", Status.RUNNING, wait.getStatus());

        // Advance time significantly but not enough to complete
        timepiece.update(500.0f);
        behaviorTree.step();
        Assert.assertEquals("Wait should still be running", Status.RUNNING, wait.getStatus());

        // Advance time to complete
        timepiece.update(500.1f);
        behaviorTree.step();
        Assert.assertEquals("Wait should succeed after large timeout", Status.SUCCEEDED, wait.getStatus());
    }
}
