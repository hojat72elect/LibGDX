package com.badlogic.gdx.ai.btree.leaf;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

/**
 * {@code Failure} is a leaf that immediately fails.
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
public class Failure<E> extends LeafTask<E> {

    /**
     * Creates a {@code Failure} task.
     */
    public Failure() {
    }

    /**
     * Executes this {@code Failure} task.
     *
     * @return {@link Status#FAILED}.
     */
    @Override
    public Status execute() {
        return Status.FAILED;
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        return task;
    }
}
