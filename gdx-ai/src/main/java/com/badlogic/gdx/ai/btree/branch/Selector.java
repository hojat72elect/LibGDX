package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.SingleRunningChildBranch;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.utils.Array;

/**
 * A {@code Selector} is a branch task that runs every children until one of them succeeds. If a child task fails, the selector
 * will start and run the next child task.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state.
 */
public class Selector<E> extends SingleRunningChildBranch<E> {

    /**
     * Creates a {@code Selector} branch with no children.
     */
    public Selector() {
        super();
    }

    /**
     * Creates a {@code Selector} branch with the given children.
     *
     * @param tasks the children of this task
     */
    public Selector(Task<E>... tasks) {
        super(new Array<Task<E>>(tasks));
    }

    /**
     * Creates a {@code Selector} branch with the given children.
     *
     * @param tasks the children of this task
     */
    public Selector(Array<Task<E>> tasks) {
        super(tasks);
    }

    @Override
    public void childFail(Task<E> runningTask) {
        super.childFail(runningTask);
        if (++currentChildIndex < children.size) {
            run(); // Run next child
        } else {
            fail(); // All children processed, return failure status
        }
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        super.childSuccess(runningTask);
        success(); // Return success status when a child says it succeeded
    }
}
