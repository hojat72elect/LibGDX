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

public class UntilFailTest {
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
        UntilFail<String> untilFail = new UntilFail<>();
        Assert.assertEquals("Should have no children", 0, untilFail.getChildCount());
        try {
            untilFail.getChild(0);
            Assert.fail("Should throw IndexOutOfBoundsException for index 0 with no children");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithChild() {
        UntilFail<String> untilFail = new UntilFail<>(successTask);
        Assert.assertEquals("Should have one child", 1, untilFail.getChildCount());
        Assert.assertEquals("Child should be successTask", successTask, untilFail.getChild(0));
    }

    @Test
    public void testConstructorWithNullChild() {
        UntilFail<String> untilFail = new UntilFail<>(null);
        Assert.assertEquals("Should have no children", 0, untilFail.getChildCount());
    }

    @Test
    public void testUntilFailWithImmediateFailure() {
        UntilFail<String> untilFail = new UntilFail<>(failureTask);
        behaviorTree.addChild(untilFail);

        // Run the behavior tree
        behaviorTree.step();

        Assert.assertEquals("Should succeed when child fails immediately", Status.SUCCEEDED, untilFail.getStatus());
        Assert.assertEquals("Should execute child exactly once", 1, failureTask.executions);
        Assert.assertFalse("Should not continue looping", getLoopField(untilFail));
    }

    @Test
    public void testChildSuccess() {
        UntilFail<String> untilFail = new UntilFail<>(successTask);
        untilFail.start();
        setLoopField(untilFail, true);

        // Child success should keep looping
        untilFail.childSuccess(successTask);
        Assert.assertTrue("Should continue looping after child success", getLoopField(untilFail));
    }

    @Test
    public void testChildFail() {
        UntilFail<String> untilFail = new UntilFail<>(failureTask);
        behaviorTree.addChild(untilFail);

        // Start the behavior tree to set up proper context
        behaviorTree.step();

        // Don't call reset() as it nullifies the tree reference
        // Just start fresh and set loop field
        untilFail.start();
        setLoopField(untilFail, true);

        // Child failure should stop looping and succeed
        untilFail.childFail(failureTask);
        Assert.assertFalse("Should stop looping after child failure", getLoopField(untilFail));
        Assert.assertEquals("Should succeed when child fails", Status.SUCCEEDED, untilFail.getStatus());
    }

    @Test
    public void testCondition() {
        UntilFail<String> untilFail = new UntilFail<>(successTask);

        // Before start - loop should be false
        Assert.assertFalse("Should not loop before start", untilFail.condition());

        // After start - loop is still false until run() is called
        untilFail.start();
        Assert.assertFalse("Should not loop when loop is false", untilFail.condition());

        // Set loop to true manually to simulate run() behavior
        setLoopField(untilFail, true);
        Assert.assertTrue("Should loop when loop is true", untilFail.condition());
    }

    @Test
    public void testMaxChildrenConstraint() {
        UntilFail<String> untilFail = new UntilFail<>();
        untilFail.addChild(successTask);

        try {
            untilFail.addChild(failureTask);
            Assert.fail("Should throw IllegalStateException for adding second child");
        } catch (IllegalStateException e) {
            // Expected - UntilFail can only have one child
        }
    }

    @Test
    public void testReset() {
        UntilFail<String> untilFail = new UntilFail<>(successTask);
        untilFail.start();
        setLoopField(untilFail, true);

        // Reset should clear loop and child
        untilFail.reset();
        Assert.assertFalse("Loop should be false after reset", getLoopField(untilFail));
        Assert.assertEquals("Should have no children after reset", 0, untilFail.getChildCount());
    }

    @Test
    public void testResetTask() {
        UntilFail<String> untilFail = new UntilFail<>(successTask);
        untilFail.start();
        setLoopField(untilFail, true);

        // Reset task should clear loop but keep child
        untilFail.resetTask();
        // Note: resetTask() doesn't call reset(), so loop field remains unchanged
        // This is the actual behavior of resetTask() in the Task class
        Assert.assertTrue("Loop should remain true after resetTask (resetTask doesn't call reset)", getLoopField(untilFail));
        Assert.assertEquals("Should still have child after resetTask", 1, untilFail.getChildCount());
    }

    @Test
    public void testChildRunning() {
        UntilFail<String> untilFail = new UntilFail<>(runningTask);
        behaviorTree.addChild(untilFail);

        // Start the behavior tree to set up proper context
        behaviorTree.step();

        // Don't call reset() as it nullifies the tree reference
        // Just start fresh and set loop field
        untilFail.start();
        setLoopField(untilFail, true);

        // Child running should stop looping
        untilFail.childRunning(runningTask, runningTask);
        Assert.assertFalse("Should stop looping when child is running", getLoopField(untilFail));
        Assert.assertEquals("Should be running when child is running", Status.RUNNING, untilFail.getStatus());
    }

    private boolean getLoopField(UntilFail<String> untilFail) {
        try {
            java.lang.reflect.Field field = LoopDecorator.class.getDeclaredField("loop");
            field.setAccessible(true);
            return field.getBoolean(untilFail);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access private field: loop", e);
        }
    }

    private void setLoopField(UntilFail<String> untilFail, boolean value) {
        try {
            java.lang.reflect.Field field = LoopDecorator.class.getDeclaredField("loop");
            field.setAccessible(true);
            field.setBoolean(untilFail, value);
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
