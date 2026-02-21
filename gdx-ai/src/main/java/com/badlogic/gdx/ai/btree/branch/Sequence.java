package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.SingleRunningChildBranch;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.utils.Array;

/**
 * A {@code Sequence} is a branch task that runs every child until one of them fails. If a child task succeeds, the selector
 * will start and run the next child task.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state.
 */
public class Sequence<E> extends SingleRunningChildBranch<E> {

    /**
     * Creates a {@code Sequence} branch with no children.
     */
    public Sequence() {
        super();
    }

    /**
     * Creates a {@code Sequence} branch with the given children.
     *
     * @param tasks the children of this task
     */
    public Sequence(Array<Task<E>> tasks) {
        super(tasks);
    }

    /**
     * Creates a {@code Sequence} branch with the given children.
     *
     * @param tasks the children of this task
     */
    public Sequence(Task<E>... tasks) {
        super(new Array<Task<E>>(tasks));
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        super.childSuccess(runningTask);
        if (++currentChildIndex < children.size) {
            run(); // Run next child
        } else {
            success(); // All children processed, return success status
        }
    }

    @Override
    public void childFail(Task<E> runningTask) {
        super.childFail(runningTask);
        fail(); // Return failure status when a child says it failed
    }
}
