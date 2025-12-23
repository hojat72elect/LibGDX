package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;

/**
 * An {@code AlwaysFail} decorator will fail no matter the wrapped task fails or succeeds.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state.
 */
public class AlwaysFail<E> extends Decorator<E> {

    /**
     * Creates an {@code AlwaysFail} decorator with no child.
     */
    public AlwaysFail() {
    }

    /**
     * Creates an {@code AlwaysFail} decorator with the given child.
     *
     * @param task the child task to wrap
     */
    public AlwaysFail(Task<E> task) {
        super(task);
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        childFail(runningTask);
    }
}
