package com.badlogic.gdx.ai.btree.leaf;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

/**
 * {@code Success} is a leaf that immediately succeeds.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
public class Success<E> extends LeafTask<E> {

    /**
     * Creates a {@code Success} task.
     */
    public Success() {
    }

    /**
     * Executes this {@code Success} task.
     *
     * @return {@link Status#SUCCEEDED}.
     */
    @Override
    public Status execute() {
        return Status.SUCCEEDED;
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        return task;
    }
}
