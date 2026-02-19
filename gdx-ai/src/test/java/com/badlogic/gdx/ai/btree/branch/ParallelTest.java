package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.Task.Status;
import com.badlogic.gdx.ai.btree.branch.Parallel.Orchestrator;
import com.badlogic.gdx.ai.btree.branch.Parallel.Policy;
import com.badlogic.gdx.utils.Array;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class ParallelTest {
    private final BehaviorTree<String> behaviorTree = new BehaviorTree<>();
    private final TestTask task1 = new TestTask();
    private final TestTask task2 = new TestTask();
    private final Array<Task<String>> tasks = new Array<>();

    @Before
    public void setUp() {
        tasks.add(task1);
        tasks.add(task2);
    }

    /**
     * Resume orchestrator - all tasks start or run on each step<br>
     * Sequence policy - all tasks have to succeed for the parallel task to succeed
     */
    @Test
    public void testResumeOrchestratorSequencePolicy() {
        Parallel<String> parallel = new Parallel<>(Policy.Sequence, Orchestrator.Resume, tasks);
        behaviorTree.addChild(parallel);
        behaviorTree.step();

        Assert.assertEquals(1, task1.executions);
        Assert.assertEquals(1, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        task2.status = Status.SUCCEEDED;
        behaviorTree.step();

        Assert.assertEquals(2, task1.executions);
        Assert.assertEquals(2, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        behaviorTree.step();

        Assert.assertEquals(3, task1.executions);
        Assert.assertEquals(3, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        task1.status = Status.SUCCEEDED;
        behaviorTree.step();

        Assert.assertEquals(4, task1.executions);
        Assert.assertEquals(4, task2.executions);
        Assert.assertEquals(Status.SUCCEEDED, parallel.getStatus());
    }

    /**
     * Resume orchestrator - all tasks start or run on each step<br>
     * Selector policy - only one task has to succeed for the parallel task to succeed
     */
    @Test
    public void testResumeOrchestratorSelectorPolicy() {
        Parallel<String> parallel = new Parallel<>(Policy.Selector, Orchestrator.Resume, tasks);
        behaviorTree.addChild(parallel);
        behaviorTree.step();

        Assert.assertEquals(1, task1.executions);
        Assert.assertEquals(1, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        behaviorTree.step();

        Assert.assertEquals(2, task1.executions);
        Assert.assertEquals(2, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        task1.status = Status.SUCCEEDED;
        behaviorTree.step();

        Assert.assertEquals(3, task1.executions);
        Assert.assertEquals(2, task2.executions);
        Assert.assertEquals(Status.SUCCEEDED, parallel.getStatus());

        behaviorTree.step();

        // Resume strategy - all tasks start/run
        Assert.assertEquals(4, task1.executions);
        Assert.assertEquals(2, task2.executions);
        Assert.assertEquals(Status.SUCCEEDED, parallel.getStatus());
    }

    /**
     * Join orchestrator - all tasks run until success/failure then don't run again
     * until the parallel task has succeeded or failed<br>
     * Sequence policy - all tasks have to succeed for the parallel task to succeed
     */
    @Test
    public void testJoinOrchestratorSequencePolicySequentialOrder() {
        Parallel<String> parallel = new Parallel<>(Policy.Sequence, Orchestrator.Join, tasks);
        behaviorTree.addChild(parallel);
        behaviorTree.step();

        Assert.assertEquals(1, task1.executions);
        Assert.assertEquals(1, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        task1.status = Status.SUCCEEDED;
        behaviorTree.step();

        Assert.assertEquals(2, task1.executions);
        Assert.assertEquals(2, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        behaviorTree.step();

        // Join strategy - task 1 will not execute again until the parallel task
        // succeeds or fails
        Assert.assertEquals(2, task1.executions);
        Assert.assertEquals(3, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        task2.status = Status.SUCCEEDED;
        behaviorTree.step();

        Assert.assertEquals(2, task1.executions);
        Assert.assertEquals(4, task2.executions);
        Assert.assertEquals(Status.SUCCEEDED, parallel.getStatus());

        task1.status = Status.RUNNING;
        task2.status = Status.RUNNING;

        behaviorTree.step();
        Assert.assertEquals(3, task1.executions);
        Assert.assertEquals(5, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());
    }

    /**
     * Join orchestrator - all tasks run until success/failure then don't run again
     * until the parallel task has succeeded or failed<br>
     * Sequence policy - all tasks have to succeed for the parallel task to succeed
     */
    @Test
    public void testJoinOrchestratorSequencePolicyInverseOrder() {
        Parallel<String> parallel = new Parallel<>(Policy.Sequence, Orchestrator.Join, tasks);
        behaviorTree.addChild(parallel);
        behaviorTree.step();

        Assert.assertEquals(1, task1.executions);
        Assert.assertEquals(1, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        task2.status = Status.SUCCEEDED;
        behaviorTree.step();

        Assert.assertEquals(2, task1.executions);
        Assert.assertEquals(2, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        behaviorTree.step();

        // Join strategy - task 2 will not execute again until the parallel task
        // succeeds or fails
        Assert.assertEquals(3, task1.executions);
        Assert.assertEquals(2, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        task1.status = Status.SUCCEEDED;
        behaviorTree.step();

        Assert.assertEquals(4, task1.executions);
        Assert.assertEquals(2, task2.executions);
        Assert.assertEquals(Status.SUCCEEDED, parallel.getStatus());

        task1.status = Status.RUNNING;
        task2.status = Status.RUNNING;

        behaviorTree.step();
        Assert.assertEquals(5, task1.executions);
        Assert.assertEquals(3, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());
    }

    /**
     * Join orchestrator - all tasks run until success/failure then don't run again
     * until the parallel task has succeeded or failed<br>
     * Selector policy - only one task has to succeed for the parallel task to succeed
     */
    @Test
    public void testJoinOrchestratorSelectorPolicy() {
        Parallel<String> parallel = new Parallel<>(Policy.Selector, Orchestrator.Join, tasks);
        behaviorTree.addChild(parallel);
        behaviorTree.step();

        Assert.assertEquals(1, task1.executions);
        Assert.assertEquals(1, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        task1.status = Status.FAILED;
        behaviorTree.step();

        Assert.assertEquals(2, task1.executions);
        Assert.assertEquals(2, task2.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        // Join strategy - task 1 will not execute again until the parallel task
        // succeeds or fails
        task2.status = Status.SUCCEEDED;
        behaviorTree.step();

        Assert.assertEquals(2, task1.executions);
        Assert.assertEquals(3, task2.executions);
        Assert.assertEquals(Status.SUCCEEDED, parallel.getStatus());

        behaviorTree.step();

        Assert.assertEquals(3, task1.executions);
        Assert.assertEquals(4, task2.executions);
        Assert.assertEquals(Status.SUCCEEDED, parallel.getStatus());
    }

    // Constructor tests
    @Test
    public void testConstructorNoChildren() {
        Parallel<String> parallel = new Parallel<>();
        Assert.assertEquals(0, parallel.getChildCount());
        Assert.assertEquals(Policy.Sequence, parallel.policy);
        Assert.assertEquals(Orchestrator.Resume, parallel.orchestrator);
    }

    @Test
    public void testConstructorWithVarArgs() {
        Parallel<String> parallel = new Parallel<>(task1, task2);
        Assert.assertEquals(2, parallel.getChildCount());
        Assert.assertEquals(task1, parallel.getChild(0));
        Assert.assertEquals(task2, parallel.getChild(1));
        Assert.assertEquals(Policy.Sequence, parallel.policy);
        Assert.assertEquals(Orchestrator.Resume, parallel.orchestrator);
    }

    @Test
    public void testConstructorWithArray() {
        Parallel<String> parallel = new Parallel<>(tasks);
        Assert.assertEquals(2, parallel.getChildCount());
        Assert.assertEquals(task1, parallel.getChild(0));
        Assert.assertEquals(task2, parallel.getChild(1));
        Assert.assertEquals(Policy.Sequence, parallel.policy);
        Assert.assertEquals(Orchestrator.Resume, parallel.orchestrator);
    }

    @Test
    public void testConstructorWithPolicy() {
        Parallel<String> parallel = new Parallel<>(Policy.Selector);
        Assert.assertEquals(0, parallel.getChildCount());
        Assert.assertEquals(Policy.Selector, parallel.policy);
        Assert.assertEquals(Orchestrator.Resume, parallel.orchestrator);
    }

    @Test
    public void testConstructorWithPolicyAndVarArgs() {
        Parallel<String> parallel = new Parallel<>(Policy.Selector, task1, task2);
        Assert.assertEquals(2, parallel.getChildCount());
        Assert.assertEquals(Policy.Selector, parallel.policy);
        Assert.assertEquals(Orchestrator.Resume, parallel.orchestrator);
    }

    @Test
    public void testConstructorWithPolicyAndArray() {
        Parallel<String> parallel = new Parallel<>(Policy.Selector, tasks);
        Assert.assertEquals(2, parallel.getChildCount());
        Assert.assertEquals(Policy.Selector, parallel.policy);
        Assert.assertEquals(Orchestrator.Resume, parallel.orchestrator);
    }

    @Test
    public void testConstructorWithOrchestratorAndArray() {
        Parallel<String> parallel = new Parallel<>(Orchestrator.Join, tasks);
        Assert.assertEquals(2, parallel.getChildCount());
        Assert.assertEquals(Policy.Sequence, parallel.policy);
        Assert.assertEquals(Orchestrator.Join, parallel.orchestrator);
    }

    @Test
    public void testConstructorWithOrchestratorAndVarArgs() {
        Parallel<String> parallel = new Parallel<>(Orchestrator.Join, task1, task2);
        Assert.assertEquals(2, parallel.getChildCount());
        Assert.assertEquals(Policy.Sequence, parallel.policy);
        Assert.assertEquals(Orchestrator.Join, parallel.orchestrator);
    }

    @Test
    public void testConstructorWithPolicyOrchestratorAndArray() {
        Parallel<String> parallel = new Parallel<>(Policy.Selector, Orchestrator.Join, tasks);
        Assert.assertEquals(2, parallel.getChildCount());
        Assert.assertEquals(Policy.Selector, parallel.policy);
        Assert.assertEquals(Orchestrator.Join, parallel.orchestrator);
    }

    // Child callback tests
    @Test
    public void testChildRunning() {
        Parallel<String> parallel = new Parallel<>(tasks);

        // Initially noRunningTasks should be true
        Assert.assertTrue(getNoRunningTasks(parallel));

        parallel.childRunning(task1, parallel);

        // After childRunning, noRunningTasks should be false
        Assert.assertFalse(getNoRunningTasks(parallel));
    }

    @Test
    public void testChildSuccessSequencePolicy() {
        Parallel<String> parallel = new Parallel<>(Policy.Sequence, tasks);

        parallel.childSuccess(task1);

        // For sequence policy, childSuccess should not immediately set lastResult
        // unless all children have succeeded and no tasks are running
        Assert.assertNull(getLastResult(parallel));
    }

    @Test
    public void testChildSuccessSelectorPolicy() {
        Parallel<String> parallel = new Parallel<>(Policy.Selector, tasks);

        parallel.childSuccess(task1);

        // For selector policy, childSuccess should immediately set lastResult to true
        Assert.assertEquals(Boolean.TRUE, getLastResult(parallel));
    }

    @Test
    public void testChildFailSequencePolicy() {
        Parallel<String> parallel = new Parallel<>(Policy.Sequence, tasks);

        parallel.childFail(task1);

        // For sequence policy, childFail should immediately set lastResult to false
        Assert.assertEquals(Boolean.FALSE, getLastResult(parallel));
    }

    @Test
    public void testChildFailSelectorPolicy() {
        Parallel<String> parallel = new Parallel<>(Policy.Selector, tasks);

        parallel.childFail(task1);

        // For selector policy, childFail should not immediately set lastResult
        // unless all children have failed and no tasks are running
        Assert.assertNull(getLastResult(parallel));
    }

    // Reset tests
    @Test
    public void testResetTask() {
        Parallel<String> parallel = new Parallel<>(tasks);

        // Modify internal state
        parallel.childRunning(task1, parallel);

        // Verify state is modified
        Assert.assertFalse(getNoRunningTasks(parallel));

        parallel.resetTask();

        // resetTask should reset noRunningTasks to true but not other fields
        Assert.assertTrue(getNoRunningTasks(parallel));
    }

    @Test
    public void testReset() {
        Parallel<String> parallel = new Parallel<>(Policy.Selector, Orchestrator.Join, tasks);

        // Modify internal state
        parallel.childRunning(task1, parallel);
        parallel.policy = Policy.Selector;
        parallel.orchestrator = Orchestrator.Join;

        parallel.reset();

        // reset should restore all fields to defaults
        Assert.assertEquals(Policy.Sequence, parallel.policy);
        Assert.assertEquals(Orchestrator.Resume, parallel.orchestrator);
        Assert.assertTrue(getNoRunningTasks(parallel));
        Assert.assertNull(getLastResult(parallel));
        Assert.assertEquals(0, getCurrentChildIndex(parallel));
    }

    @Test
    public void testResetAllChildren() {
        TestTask task3 = new TestTask();
        tasks.add(task3);

        Parallel<String> parallel = new Parallel<>(tasks);
        behaviorTree.addChild(parallel);
        behaviorTree.step();

        // Verify tasks have been executed
        Assert.assertTrue(task1.executions > 0);
        Assert.assertTrue(task2.executions > 0);
        Assert.assertTrue(task3.executions > 0);

        parallel.resetAllChildren();

        // After resetAllChildren, all children should be in FRESH status
        Assert.assertEquals(Status.FRESH, task1.getStatus());
        Assert.assertEquals(Status.FRESH, task2.getStatus());
        Assert.assertEquals(Status.FRESH, task3.getStatus());
    }

    @Test
    public void testSingleChildParallel() {
        Array<Task<String>> singleTask = new Array<>();
        singleTask.add(task1);

        Parallel<String> parallel = new Parallel<>(singleTask);
        behaviorTree.addChild(parallel);
        behaviorTree.step();

        Assert.assertEquals(1, task1.executions);
        Assert.assertEquals(Status.RUNNING, parallel.getStatus());

        task1.status = Status.SUCCEEDED;
        behaviorTree.step();

        Assert.assertEquals(Status.SUCCEEDED, parallel.getStatus());
    }

    @Test
    public void testAllChildrenFailSequencePolicy() {
        Parallel<String> parallel = new Parallel<>(Policy.Sequence, Orchestrator.Resume, tasks);
        behaviorTree.addChild(parallel);

        task1.status = Status.FAILED;
        task2.status = Status.FAILED;

        behaviorTree.step();

        // Sequence policy should fail as soon as first child fails
        Assert.assertEquals(Status.FAILED, parallel.getStatus());
    }

    @Test
    public void testPolicySequenceOnChildFail() {
        Boolean result = Policy.Sequence.onChildFail(null);
        // Should return false (fail immediately)
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testPolicySelectorOnChildSuccess() {
        Boolean result = Policy.Selector.onChildSuccess(null);
        // Should return true (succeed immediately)
        Assert.assertEquals(Boolean.TRUE, result);
    }

    // Helper methods to access private fields for testing
    private boolean getNoRunningTasks(Parallel<String> parallel) {
        try {
            Field field = Parallel.class.getDeclaredField("noRunningTasks");
            field.setAccessible(true);
            return field.getBoolean(parallel);
        } catch (Exception e) {
            return false;
        }
    }

    private Boolean getLastResult(Parallel<String> parallel) {
        try {
            Field field = Parallel.class.getDeclaredField("lastResult");
            field.setAccessible(true);
            return (Boolean) field.get(parallel);
        } catch (Exception e) {
            return null;
        }
    }

    private int getCurrentChildIndex(Parallel<String> parallel) {
        try {
            Field field = Parallel.class.getDeclaredField("currentChildIndex");
            field.setAccessible(true);
            return field.getInt(parallel);
        } catch (Exception e) {
            return -1;
        }
    }

    private static class TestTask extends LeafTask<String> {
        Status status = Status.RUNNING;
        int executions = 0;

        @Override
        public Status execute() {
            executions++;
            return status;
        }

        @Override
        protected Task<String> copyTo(Task<String> task) {
            return task;
        }
    }
}
