package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.LoopDecorator;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.Task.Status;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UntilSuccessTest {
    private BehaviorTree<String> behaviorTree;
    private TestTask successTask;
    private TestTask failureTask;
    private TestTask runningTask;

    @Before
    public void setUp() {
        behaviorTree = new BehaviorTree<>();
        behaviorTree.setObject("test");
        successTask = new TestTask("successTask", Status.SUCCEEDED);
        failureTask = new TestTask("failureTask", Status.FAILED);
        runningTask = new TestTask("runningTask", Status.RUNNING);
    }

    @After
    public void tearDown() {
        behaviorTree = null;
        successTask = null;
        failureTask = null;
        runningTask = null;
    }

    @Test
    public void testConstructorNoArgs() {
        UntilSuccess<String> untilSuccess = new UntilSuccess<>();
        Assert.assertEquals("Should have no children", 0, untilSuccess.getChildCount());
        try {
            untilSuccess.getChild(0);
            Assert.fail("Should throw IndexOutOfBoundsException for index 0 with no children");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithChild() {
        UntilSuccess<String> untilSuccess = new UntilSuccess<>(successTask);
        Assert.assertEquals("Should have one child", 1, untilSuccess.getChildCount());
        Assert.assertEquals("Child should be successTask", successTask, untilSuccess.getChild(0));
    }

    @Test
    public void testConstructorWithNullChild() {
        UntilSuccess<String> untilSuccess = new UntilSuccess<>(null);
        Assert.assertEquals("Should have no children", 0, untilSuccess.getChildCount());
    }

    @Test
    public void testUntilSuccessWithImmediateSuccess() {
        UntilSuccess<String> untilSuccess = new UntilSuccess<>(successTask);
        behaviorTree.addChild(untilSuccess);

        // Run the behavior tree
        behaviorTree.step();

        Assert.assertEquals("Should succeed when child succeeds immediately", Status.SUCCEEDED, untilSuccess.getStatus());
        Assert.assertEquals("Should execute child exactly once", 1, successTask.executions);
        Assert.assertFalse("Should not continue looping", getLoopField(untilSuccess));
    }

    @Test
    public void testChildSuccess() {
        UntilSuccess<String> untilSuccess = new UntilSuccess<>(successTask);
        behaviorTree.addChild(untilSuccess);
        behaviorTree.step();

        // Don't call reset() as it nullifies the tree reference
        // Just start fresh and set loop field
        untilSuccess.start();
        setLoopField(untilSuccess, true);

        // Child success should stop looping and succeed
        untilSuccess.childSuccess(successTask);
        Assert.assertFalse("Should stop looping after child success", getLoopField(untilSuccess));
        Assert.assertEquals("Should succeed when child succeeds", Status.SUCCEEDED, untilSuccess.getStatus());
    }

    @Test
    public void testCondition() {
        UntilSuccess<String> untilSuccess = new UntilSuccess<>(successTask);

        // Before start - loop should be false
        Assert.assertFalse("Should not loop before start", untilSuccess.condition());

        // After start - loop is still false until run() is called
        untilSuccess.start();
        Assert.assertFalse("Should not loop when loop is false", untilSuccess.condition());

        // Set loop to true manually to simulate run() behavior
        setLoopField(untilSuccess, true);
        Assert.assertTrue("Should loop when loop is true", untilSuccess.condition());
    }

    @Test
    public void testMaxChildrenConstraint() {
        UntilSuccess<String> untilSuccess = new UntilSuccess<>();
        untilSuccess.addChild(successTask);

        try {
            untilSuccess.addChild(failureTask);
            Assert.fail("Should throw IllegalStateException for adding second child");
        } catch (IllegalStateException e) {
            // Expected - UntilSuccess can only have one child
        }
    }

    @Test
    public void testReset() {
        UntilSuccess<String> untilSuccess = new UntilSuccess<>(successTask);
        untilSuccess.start();
        setLoopField(untilSuccess, true);

        // Reset should clear loop and child
        untilSuccess.reset();
        Assert.assertFalse("Loop should be false after reset", getLoopField(untilSuccess));
        Assert.assertEquals("Should have no children after reset", 0, untilSuccess.getChildCount());
    }

    @Test
    public void testResetTask() {
        UntilSuccess<String> untilSuccess = new UntilSuccess<>(successTask);
        untilSuccess.start();
        setLoopField(untilSuccess, true);

        // Reset task should clear loop but keep child
        untilSuccess.resetTask();
        // Note: resetTask() doesn't call reset(), so loop field remains unchanged
        // This is the actual behavior of resetTask() in the Task class
        Assert.assertTrue("Loop should remain true after resetTask (resetTask doesn't call reset)", getLoopField(untilSuccess));
        Assert.assertEquals("Should still have child after resetTask", 1, untilSuccess.getChildCount());
    }

    @Test
    public void testChildRunning() {
        UntilSuccess<String> untilSuccess = new UntilSuccess<>(runningTask);
        behaviorTree.addChild(untilSuccess);

        // Start the behavior tree to set up proper context
        behaviorTree.step();

        // Don't call reset() as it nullifies the tree reference
        // Just start fresh and set loop field
        untilSuccess.start();
        setLoopField(untilSuccess, true);

        // Child running should stop looping
        untilSuccess.childRunning(runningTask, runningTask);
        Assert.assertFalse("Should stop looping when child is running", getLoopField(untilSuccess));
        Assert.assertEquals("Should be running when child is running", Status.RUNNING, untilSuccess.getStatus());
    }

    @Test
    public void testLoopBehaviorWithRunningChild() {
        TestTask runningThenSuccessTask = new TestTask("runningThenSuccessTask", Status.RUNNING);
        UntilSuccess<String> untilSuccess = new UntilSuccess<>(runningThenSuccessTask);
        behaviorTree.addChild(untilSuccess);

        // First step - child starts running
        behaviorTree.step();
        Assert.assertEquals("Should be running when child is running", Status.RUNNING, untilSuccess.getStatus());
        Assert.assertFalse("Should not loop when child is running", getLoopField(untilSuccess));
        Assert.assertEquals("Child should execute once", 1, runningThenSuccessTask.executions);

        // Make child succeed on next execution
        runningThenSuccessTask.setStatus(Status.SUCCEEDED);
        behaviorTree.step();

        Assert.assertEquals("Should succeed when child succeeds", Status.SUCCEEDED, untilSuccess.getStatus());
        Assert.assertFalse("Should not continue looping after success", getLoopField(untilSuccess));
        Assert.assertEquals("Child should execute twice", 2, runningThenSuccessTask.executions);
    }

    private boolean getLoopField(UntilSuccess<String> untilSuccess) {
        try {
            java.lang.reflect.Field field = LoopDecorator.class.getDeclaredField("loop");
            field.setAccessible(true);
            return field.getBoolean(untilSuccess);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access private field: loop", e);
        }
    }

    private void setLoopField(UntilSuccess<String> untilSuccess, boolean value) {
        try {
            java.lang.reflect.Field field = LoopDecorator.class.getDeclaredField("loop");
            field.setAccessible(true);
            field.setBoolean(untilSuccess, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set private field: loop", e);
        }
    }

    private static class TestTask extends LeafTask<String> {
        String name;
        Status status;
        int executions = 0;

        public TestTask(String name, Status status) {
            this.name = name;
            this.status = status;
        }

        public TestTask() {
            // No-arg constructor for cloning
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        @Override
        public Status execute() {
            executions++;
            return status;
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            TestTask testTask = (TestTask) task;
            testTask.name = this.name;
            testTask.status = this.status;
            return testTask;
        }

        @Override
        public Task<String> cloneTask() {
            TestTask clone = new TestTask();
            copyTo(clone);
            return clone;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
