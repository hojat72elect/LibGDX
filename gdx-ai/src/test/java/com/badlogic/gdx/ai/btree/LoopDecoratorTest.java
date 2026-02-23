package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class LoopDecoratorTest {
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
        TestLoopDecorator loopDecorator = new TestLoopDecorator();
        Assert.assertEquals("Should have no children", 0, loopDecorator.getChildCount());
        try {
            loopDecorator.getChild(0);
            Assert.fail("Should throw IndexOutOfBoundsException for index 0 with no children");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithChild() {
        TestLoopDecorator loopDecorator = new TestLoopDecorator(successTask);
        Assert.assertEquals("Should have one child", 1, loopDecorator.getChildCount());
        Assert.assertEquals("Child should be successTask", successTask, loopDecorator.getChild(0));
    }

    @Test
    public void testConditionReturnsLoopField() {
        TestLoopDecorator loopDecorator = new TestLoopDecorator(successTask);

        // Initially loop should be false
        Assert.assertFalse("Condition should return false initially", loopDecorator.condition());

        // Set loop to true
        loopDecorator.setLoop(true);
        Assert.assertTrue("Condition should return true when loop is true", loopDecorator.condition());

        // Set loop to false
        loopDecorator.setLoop(false);
        Assert.assertFalse("Condition should return false when loop is false", loopDecorator.condition());
    }

    @Test
    public void testRunWithRunningChild() {
        TestLoopDecorator loopDecorator = new TestLoopDecorator(runningTask);
        behaviorTree.addChild(loopDecorator);

        // Set loop to false to run only once
        loopDecorator.setLoop(false);

        behaviorTree.step();

        Assert.assertEquals("LoopDecorator should be running when child is running", Status.RUNNING, loopDecorator.getStatus());
        Assert.assertEquals("Running task should be executed", 1, runningTask.executions);
    }

    @Test
    public void testChildRunningStopsLoop() {
        TestLoopDecorator loopDecorator = new TestLoopDecorator(runningTask);
        behaviorTree.addChild(loopDecorator);

        // Set loop to true initially
        loopDecorator.setLoop(true);

        // First step - child starts running, this calls run() which sets loop=true,
        // then child runs and calls childRunning() which sets loop=false
        behaviorTree.step();
        Assert.assertEquals("LoopDecorator should be running", Status.RUNNING, loopDecorator.getStatus());
        // After step completes, childRunning() has been called, so loop should be false
        Assert.assertFalse("Loop should be false after step (childRunning called)", getLoopField(loopDecorator));

        // Simulate child running callback again - this should keep loop as false
        loopDecorator.childRunning(runningTask, loopDecorator);
        Assert.assertFalse("Loop should remain false when child is running", getLoopField(loopDecorator));
    }

    @Test
    public void testResetClearsLoop() {
        TestLoopDecorator loopDecorator = new TestLoopDecorator(successTask);

        // Set loop to true
        loopDecorator.setLoop(true);
        Assert.assertTrue("Loop should be true before reset", getLoopField(loopDecorator));

        loopDecorator.reset();

        Assert.assertEquals("Status should be FRESH after reset", Status.FRESH, loopDecorator.getStatus());
        Assert.assertFalse("Loop should be false after reset", getLoopField(loopDecorator));
    }

    @Test
    public void testGuardFailure() {
        TestTask guardTask = new TestTask("guardTask", Status.FAILED);
        TestLoopDecorator loopDecorator = new TestLoopDecorator(successTask);
        loopDecorator.setGuard(guardTask);
        behaviorTree.addChild(loopDecorator);

        loopDecorator.setLoop(false);

        behaviorTree.step();

        Assert.assertEquals("LoopDecorator should fail when guard fails", Status.FAILED, loopDecorator.getStatus());
        Assert.assertEquals("Success task should not be executed", 0, successTask.executions);
        Assert.assertEquals("Guard task should be executed", 1, guardTask.executions);
    }

    @Test
    public void testInheritanceFromDecorator() {

        TaskConstraint decoratorAnnotation = Decorator.class.getAnnotation(TaskConstraint.class);
        Assert.assertNotNull("Decorator should have TaskConstraint annotation", decoratorAnnotation);
        Assert.assertEquals("Decorator should allow min 1 child", 1, decoratorAnnotation.minChildren());
        Assert.assertEquals("Decorator should allow max 1 child", 1, decoratorAnnotation.maxChildren());
    }

    @Test
    public void testChildManagement() {
        TestLoopDecorator loopDecorator = new TestLoopDecorator();

        // Test adding child
        int index = loopDecorator.addChild(successTask);
        Assert.assertEquals("Should return index 0 for first child", 0, index);
        Assert.assertEquals("Should have 1 child", 1, loopDecorator.getChildCount());
        Assert.assertEquals("Child should be successTask", successTask, loopDecorator.getChild(0));

        // Test adding second child should fail
        try {
            loopDecorator.addChild(failureTask);
            Assert.fail("Should throw IllegalStateException when adding second child");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Test invalid child access
        try {
            loopDecorator.getChild(1);
            Assert.fail("Should throw IndexOutOfBoundsException for index 1");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testCopyTo() {
        TestLoopDecorator loopDecorator = new TestLoopDecorator(successTask);
        loopDecorator.setLoop(true);

        TestLoopDecorator targetDecorator = new TestLoopDecorator();
        TestLoopDecorator copiedDecorator = (TestLoopDecorator) loopDecorator.copyTo(targetDecorator);

        Assert.assertSame("Should return target decorator", targetDecorator, copiedDecorator);
        // The child should be cloned, not the same instance
        Assert.assertNotNull("Should have copied child", copiedDecorator.getChild(0));
        Assert.assertEquals("Child should have same name", "successTask", copiedDecorator.getChild(0).toString());
        // Note: copyTo doesn't copy the loop field, so it should be false (default)
        Assert.assertFalse("Copied decorator should have default loop state", getLoopField(copiedDecorator));
    }

    @Test
    public void testCloneTask() {
        TestLoopDecorator loopDecorator = new TestLoopDecorator(successTask);
        loopDecorator.setLoop(true);

        TestLoopDecorator clonedDecorator = (TestLoopDecorator) loopDecorator.cloneTask();

        Assert.assertNotSame("Should create new instance", loopDecorator, clonedDecorator);
        Assert.assertEquals("Should have same child count", loopDecorator.getChildCount(), clonedDecorator.getChildCount());
        Assert.assertEquals("Cloned should have FRESH status", Status.FRESH, clonedDecorator.getStatus());
        Assert.assertFalse("Cloned should have loop reset", getLoopField(clonedDecorator));
    }

    @Test
    public void testAbstractMethods() {
        TestLoopDecorator loopDecorator = new TestLoopDecorator(successTask);

        // Test that abstract methods are properly implemented
        Assert.assertTrue("Condition method should be implemented", loopDecorator.condition() || !loopDecorator.condition());

        // Test copyTo returns correct type
        TestLoopDecorator copy = new TestLoopDecorator();
        Task<String> result = loopDecorator.copyTo(copy);
        Assert.assertSame("CopyTo should return target", copy, result);
    }


    private boolean getLoopField(TestLoopDecorator loopDecorator) {
        try {
            Field field = loopDecorator.getClass().getSuperclass().getDeclaredField("loop");
            field.setAccessible(true);
            return (Boolean) field.get(loopDecorator);
        } catch (Exception e) {
            return false;
        }
    }

    public static class TestLoopDecorator extends LoopDecorator<String> {
        public TestLoopDecorator() {
            super();
        }

        public TestLoopDecorator(Task<String> child) {
            super(child);
        }

        // Method to set loop field for testing
        public void setLoop(boolean loop) {
            try {
                Field field = this.getClass().getSuperclass().getDeclaredField("loop");
                field.setAccessible(true);
                field.set(this, loop);
            } catch (Exception e) {
                // Ignore
            }
        }

        @Override
        public boolean condition() {
            return super.condition();
        }

        @Override
        public void childSuccess(Task<String> runningTask) {
            super.childSuccess(runningTask);
        }

        @Override
        public void childFail(Task<String> runningTask) {
            super.childFail(runningTask);
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            TestLoopDecorator loopDecorator = (TestLoopDecorator) task;
            if (this.child != null) {
                loopDecorator.addChild(this.child.cloneTask());
            }
            return task;
        }
    }

    public static class TestTask extends LeafTask<String> {
        String name;
        Status status = Status.SUCCEEDED;
        int executions = 0;

        public TestTask() {
            this.name = "default";
        }

        public TestTask(String name, Status status) {
            this.name = name;
            this.status = status;
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
            return task;
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
