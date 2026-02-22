package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.LoopDecorator;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RepeatTest {
    private BehaviorTree<String> behaviorTree;
    private TestTask successTask;
    private TestTask failureTask;

    @Before
    public void setUp() {
        behaviorTree = new BehaviorTree<>();
        behaviorTree.setObject("test");
        successTask = new TestTask("successTask", Status.SUCCEEDED);
        failureTask = new TestTask("failureTask", Status.FAILED);
    }

    @After
    public void tearDown() {
        behaviorTree = null;
        successTask = null;
        failureTask = null;
    }

    @Test
    public void testConstructorNoArgs() {
        Repeat<String> repeat = new Repeat<>();
        Assert.assertEquals("Should have no children", 0, repeat.getChildCount());
        Assert.assertEquals("Should default to infinite repetitions",
                ConstantIntegerDistribution.NEGATIVE_ONE.getValue(), repeat.times.nextInt());
        try {
            repeat.getChild(0);
            Assert.fail("Should throw IndexOutOfBoundsException for index 0 with no children");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithChild() {
        Repeat<String> repeat = new Repeat<>(successTask);
        Assert.assertEquals("Should have one child", 1, repeat.getChildCount());
        Assert.assertEquals("Child should be successTask", successTask, repeat.getChild(0));
        Assert.assertEquals("Should default to infinite repetitions",
                ConstantIntegerDistribution.NEGATIVE_ONE.getValue(), repeat.times.nextInt());
    }

    @Test
    public void testConstructorWithDistributionAndChild() {
        ConstantIntegerDistribution times = new ConstantIntegerDistribution(3);
        Repeat<String> repeat = new Repeat<>(times, successTask);
        Assert.assertEquals("Should have one child", 1, repeat.getChildCount());
        Assert.assertEquals("Child should be successTask", successTask, repeat.getChild(0));
        Assert.assertEquals("Should use provided distribution", 3, repeat.times.nextInt());
    }

    @Test
    public void testConstructorWithDistributionAndNullChild() {
        ConstantIntegerDistribution times = new ConstantIntegerDistribution(2);
        Repeat<String> repeat = new Repeat<>(times, null);
        Assert.assertEquals("Should have no children", 0, repeat.getChildCount());
        Assert.assertEquals("Should use provided distribution", 2, repeat.times.nextInt());
    }

    @Test
    public void testFiniteRepeatWithSuccessChild() {
        ConstantIntegerDistribution times = new ConstantIntegerDistribution(3);
        Repeat<String> repeat = new Repeat<>(times, successTask);
        behaviorTree.addChild(repeat);

        // Run the behavior tree - Repeat executes all repetitions in one run() call
        behaviorTree.step();

        Assert.assertEquals("Should succeed after specified repetitions", Status.SUCCEEDED, repeat.getStatus());
        Assert.assertEquals("Should execute child exactly 3 times", 3, successTask.executions);
        Assert.assertFalse("Should not continue looping", repeat.condition());
    }

    @Test
    public void testFiniteRepeatWithFailureChild() {
        ConstantIntegerDistribution times = new ConstantIntegerDistribution(2);
        Repeat<String> repeat = new Repeat<>(times, failureTask);
        behaviorTree.addChild(repeat);

        // Run the behavior tree - Repeat executes all repetitions in one run() call
        behaviorTree.step();

        Assert.assertEquals("Should succeed even though child fails (Repeat always succeeds)", Status.SUCCEEDED, repeat.getStatus());
        Assert.assertEquals("Should execute child exactly 2 times", 2, failureTask.executions);
        Assert.assertFalse("Should not continue looping", repeat.condition());
    }

    @Test
    public void testStartMethod() {
        ConstantIntegerDistribution times = new ConstantIntegerDistribution(5);
        Repeat<String> repeat = new Repeat<>(times, successTask);

        // Call start directly
        repeat.start();

        Assert.assertEquals("Count should be set from distribution", 5, getPrivateField(repeat));
    }

    @Test
    public void testCondition() {
        ConstantIntegerDistribution times = new ConstantIntegerDistribution(3);
        Repeat<String> repeat = new Repeat<>(times, successTask);

        // Before start - count should be 0, loop should be false
        Assert.assertFalse("Should not loop before start", repeat.condition());

        // After start - count should be 3, but loop is still false until run() is called
        repeat.start();
        Assert.assertFalse("Should not loop when loop is false even if count > 0", repeat.condition());

        // Set loop to true manually to simulate run() behavior
        setPrivateField(repeat, "loop", true);
        Assert.assertTrue("Should loop when count > 0 and loop is true", repeat.condition());

        // Simulate completion - set count to 0
        setPrivateField(repeat, "count", 0);
        Assert.assertFalse("Should not loop when count is 0", repeat.condition());
    }

    @Test
    public void testChildFail() {
        ConstantIntegerDistribution times = new ConstantIntegerDistribution(2);
        Repeat<String> repeat = new Repeat<>(times, failureTask);
        repeat.start();
        // Set loop to true to simulate being in run() method
        setPrivateField(repeat, "loop", true);

        // Child failure should behave same as success
        repeat.childFail(failureTask);
        Assert.assertEquals("Count should decrement from 2 to 1", 1, getPrivateField(repeat));
        Assert.assertTrue("Should continue looping", getPrivateBooleanField(repeat));
    }

    @Test
    public void testMaxChildrenConstraint() {
        Repeat<String> repeat = new Repeat<>();
        repeat.addChild(successTask);

        try {
            repeat.addChild(failureTask);
            Assert.fail("Should throw IllegalStateException for adding second child");
        } catch (IllegalStateException e) {
            // Expected - Repeat can only have one child
        }
    }

    @Test
    public void testMultipleFiniteRepeats() {
        // Test multiple finite repeat scenarios
        ConstantIntegerDistribution times1 = new ConstantIntegerDistribution(1);
        ConstantIntegerDistribution times2 = new ConstantIntegerDistribution(4);

        Repeat<String> repeat1 = new Repeat<>(times1, successTask);
        Repeat<String> repeat2 = new Repeat<>(times2, failureTask);

        BehaviorTree<String> tree1 = new BehaviorTree<>();
        BehaviorTree<String> tree2 = new BehaviorTree<>();
        tree1.setObject("test");
        tree2.setObject("test");
        tree1.addChild(repeat1);
        tree2.addChild(repeat2);

        // Run both trees to completion - Repeat executes all repetitions in one run() call
        tree1.step();
        tree2.step();

        Assert.assertEquals("Repeat1 should succeed", Status.SUCCEEDED, repeat1.getStatus());
        Assert.assertEquals("Repeat2 should succeed", Status.SUCCEEDED, repeat2.getStatus());
        Assert.assertEquals("Repeat1 should execute child once", 1, successTask.executions);
        Assert.assertEquals("Repeat2 should execute child 4 times", 4, failureTask.executions);
    }

    @Test
    public void testCopyTo() {
        ConstantIntegerDistribution times = new ConstantIntegerDistribution(3);
        Repeat<String> repeat = new Repeat<>(times, successTask);

        Repeat<String> copy = new Repeat<>();
        repeat.copyTo(copy);

        Assert.assertEquals("Times should be copied", times, copy.times);
        Assert.assertEquals("Should have same number of children", repeat.getChildCount(), copy.getChildCount());
    }

    private int getPrivateField(Repeat<String> repeat) {
        try {
            java.lang.reflect.Field field;
            field = Repeat.class.getDeclaredField("count");
            field.setAccessible(true);
            return field.getInt(repeat);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access private field: " + "count", e);
        }
    }

    private boolean getPrivateBooleanField(Repeat<String> repeat) {
        try {
            java.lang.reflect.Field field;
            field = LoopDecorator.class.getDeclaredField("loop");
            field.setAccessible(true);
            return field.getBoolean(repeat);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access private boolean field: " + "loop", e);
        }
    }

    private void setPrivateField(Repeat<String> repeat, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field;
            if (fieldName.equals("loop")) {
                field = LoopDecorator.class.getDeclaredField(fieldName);
            } else {
                field = Repeat.class.getDeclaredField(fieldName);
            }
            field.setAccessible(true);
            if (value instanceof Integer) {
                field.setInt(repeat, (Integer) value);
            } else if (value instanceof Boolean) {
                field.setBoolean(repeat, (Boolean) value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set private field: " + fieldName, e);
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
