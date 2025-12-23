package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.BranchTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.utils.Array;

/**
 * A {@code DynamicGuardSelector} is a branch task that executes the first child whose guard is evaluated to {@code true}. At
 * every AI cycle, the children's guards are re-evaluated, so if the guard of the running child is evaluated to {@code false}, it
 * is cancelled, and the child with the highest priority starts running. The {@code DynamicGuardSelector} task finishes when no
 * guard is evaluated to {@code true} (thus failing) or when its active child finishes (returning the active child's termination
 * status).
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
public class DynamicGuardSelector<E> extends BranchTask<E> {

    /**
     * The child in the running status or {@code null} if no child is running.
     */
    protected Task<E> runningChild;

    /**
     * Creates a {@code DynamicGuardSelector} branch with no children.
     */
    public DynamicGuardSelector() {
        super();
    }

    /**
     * Creates a {@code DynamicGuardSelector} branch with the given children.
     *
     * @param tasks the children of this task
     */
    public DynamicGuardSelector(Task<E>... tasks) {
        super(new Array<Task<E>>(tasks));
    }

    /**
     * Creates a {@code DynamicGuardSelector} branch with the given children.
     *
     * @param tasks the children of this task
     */
    public DynamicGuardSelector(Array<Task<E>> tasks) {
        super(tasks);
    }

    @Override
    public void childRunning(Task<E> task, Task<E> reporter) {
        runningChild = task;
        running(); // Return a running status when a child says it's running
    }

    @Override
    public void childSuccess(Task<E> task) {
        this.runningChild = null;
        success();
    }

    @Override
    public void childFail(Task<E> task) {
        this.runningChild = null;
        fail();
    }

    @Override
    public void run() {
        // Check guards
        Task<E> childToRun = null;
        for (int i = 0, n = children.size; i < n; i++) {
            Task<E> child = children.get(i);
            if (child.checkGuard(this)) {
                childToRun = child;
                break;
            }
        }

        if (runningChild != null && runningChild != childToRun) {
            runningChild.cancel();
            runningChild = null;
        }
        if (childToRun == null) {
            fail();
        } else {
            if (runningChild == null) {
                runningChild = childToRun;
                runningChild.setControl(this);
                runningChild.start();
            }
            runningChild.run();
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.runningChild = null;
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        DynamicGuardSelector<E> branch = (DynamicGuardSelector<E>) task;
        branch.runningChild = null;

        return super.copyTo(task);
    }

    @Override
    public void reset() {
        runningChild = null;
        super.reset();
    }
}
