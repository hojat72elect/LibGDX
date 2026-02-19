package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.utils.Array;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class DynamicGuardSelectorTest {
    private final BehaviorTree<String> behaviorTree = new BehaviorTree<>();
    private final TestTask task1 = new TestTask("task1");
    private final TestTask task2 = new TestTask("task2");
    private final TestTask task3 = new TestTask("task3");
    private final Array<Task<String>> tasks = new Array<>();

    @Before
    public void setUp() {
        behaviorTree.setObject("test"); // Set the blackboard object
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
    }

    // Helper methods to access protected/private fields for testing
    @SuppressWarnings("unchecked")
    private Task<String> getRunningChild(DynamicGuardSelector<String> selector) {
        try {
            Field field = DynamicGuardSelector.class.getDeclaredField("runningChild");
            field.setAccessible(true);
            return (Task<String>) field.get(selector);
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    public void testConstructorNoChildren() {
        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>();
        Assert.assertEquals(0, selector.getChildCount());
    }

    @Test
    public void testConstructorWithVarArgs() {
        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>(task1, task2);
        Assert.assertEquals(2, selector.getChildCount());
        Assert.assertEquals(task1, selector.getChild(0));
        Assert.assertEquals(task2, selector.getChild(1));
    }

    @Test
    public void testConstructorWithArray() {
        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>(tasks);
        Assert.assertEquals(3, selector.getChildCount());
        Assert.assertEquals(task1, selector.getChild(0));
        Assert.assertEquals(task2, selector.getChild(1));
        Assert.assertEquals(task3, selector.getChild(2));
    }

    @Test
    public void testNoGuardsPass() {
        // All guards return false
        task1.setGuardResult(false);
        task2.setGuardResult(false);
        task3.setGuardResult(false);

        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>(tasks);
        behaviorTree.addChild(selector);
        behaviorTree.step();

        Assert.assertEquals(Status.FAILED, selector.getStatus());
        Assert.assertEquals(0, task1.executions);
        Assert.assertEquals(0, task2.executions);
        Assert.assertEquals(0, task3.executions);
        Assert.assertNull(getRunningChild(selector));
    }

    @Test
    public void testFirstGuardPasses() {
        // First guard passes, others fail
        task1.setGuardResult(true);
        task2.setGuardResult(false);
        task3.setGuardResult(false);
        task1.setStatus(Status.SUCCEEDED);

        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>(tasks);
        behaviorTree.addChild(selector);
        behaviorTree.step();

        Assert.assertEquals(Status.SUCCEEDED, selector.getStatus());
        Assert.assertEquals(1, task1.executions);
        Assert.assertEquals(0, task2.executions);
        Assert.assertEquals(0, task3.executions);
        Assert.assertNull(getRunningChild(selector));
    }

    @Test
    public void testSecondGuardPasses() {
        // First guard fails, second passes
        task1.setGuardResult(false);
        task2.setGuardResult(true);
        task3.setGuardResult(false);
        task2.setStatus(Status.SUCCEEDED);

        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>(tasks);
        behaviorTree.addChild(selector);
        behaviorTree.step();

        Assert.assertEquals(Status.SUCCEEDED, selector.getStatus());
        Assert.assertEquals(0, task1.executions);
        Assert.assertEquals(1, task2.executions);
        Assert.assertEquals(0, task3.executions);
        Assert.assertNull(getRunningChild(selector));
    }

    @Test
    public void testChildRunning() {
        task1.setGuardResult(true);
        task2.setGuardResult(false);
        task3.setGuardResult(false);
        task1.setStatus(Status.RUNNING);

        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>(tasks);
        behaviorTree.addChild(selector);
        behaviorTree.step();

        Assert.assertEquals(Status.RUNNING, selector.getStatus());
        Assert.assertEquals(task1, getRunningChild(selector));
        Assert.assertEquals(1, task1.executions);
    }

    @Test
    public void testChildSuccess() {
        task1.setGuardResult(true);
        task2.setGuardResult(false);
        task3.setGuardResult(false);
        task1.setStatus(Status.SUCCEEDED);

        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>(tasks);
        behaviorTree.addChild(selector);
        behaviorTree.step();

        Assert.assertEquals(Status.SUCCEEDED, selector.getStatus());
        Assert.assertNull(getRunningChild(selector));
        Assert.assertEquals(1, task1.executions);
    }

    @Test
    public void testChildFailure() {
        task1.setGuardResult(true);
        task2.setGuardResult(false);
        task3.setGuardResult(false);
        task1.setStatus(Status.FAILED);

        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>(tasks);
        behaviorTree.addChild(selector);
        behaviorTree.step();

        Assert.assertEquals(Status.FAILED, selector.getStatus());
        Assert.assertNull(getRunningChild(selector));
        Assert.assertEquals(1, task1.executions);
    }

    @Test
    public void testMultipleGuardsPassFirstWins() {
        // Multiple guards pass, first should win
        task1.setGuardResult(true);
        task2.setGuardResult(true);
        task3.setGuardResult(true);
        task1.setStatus(Status.SUCCEEDED);

        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>(tasks);
        behaviorTree.addChild(selector);
        behaviorTree.step();

        Assert.assertEquals(Status.SUCCEEDED, selector.getStatus());
        Assert.assertEquals(1, task1.executions);
        Assert.assertEquals(0, task2.executions);
        Assert.assertEquals(0, task3.executions);
    }

    @Test
    public void testResetTask() {
        task1.setGuardResult(true);
        task1.setStatus(Status.RUNNING);

        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>(tasks);
        behaviorTree.addChild(selector);
        behaviorTree.step();

        Assert.assertEquals(Status.RUNNING, selector.getStatus());
        Assert.assertEquals(task1, getRunningChild(selector));

        selector.resetTask();

        Assert.assertEquals(Status.FRESH, selector.getStatus());
        Assert.assertNull(getRunningChild(selector));
    }

    @Test
    public void testReset() {
        task1.setGuardResult(true);
        task1.setStatus(Status.RUNNING);

        DynamicGuardSelector<String> selector = new DynamicGuardSelector<>(tasks);
        behaviorTree.addChild(selector);
        behaviorTree.step();

        Assert.assertEquals(Status.RUNNING, selector.getStatus());
        Assert.assertEquals(task1, getRunningChild(selector));

        selector.reset();

        Assert.assertEquals(Status.FRESH, selector.getStatus());
        Assert.assertNull(getRunningChild(selector));
    }

    private static class TestTask extends LeafTask<String> {
        String name;
        Status status = Status.RUNNING;
        int executions = 0;
        TestTask guard = null;

        public TestTask(String name) {
            this.name = name;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public void setGuardResult(boolean result) {
            if (result) {
                this.guard = new TestTask(name + "-guard") {
                    @Override
                    public Status execute() {
                        return Status.SUCCEEDED;
                    }
                };
            } else {
                this.guard = new TestTask(name + "-guard") {
                    @Override
                    public Status execute() {
                        return Status.FAILED;
                    }
                };
            }
            // Set the guard using the parent class method
            setGuard(this.guard);
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
            testTask.guard = this.guard;
            return task;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
