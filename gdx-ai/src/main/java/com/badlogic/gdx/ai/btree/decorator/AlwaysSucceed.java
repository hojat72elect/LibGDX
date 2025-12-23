package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;

/**
 * An {@code AlwaysSucceed} decorator will succeed no matter the wrapped task succeeds or fails.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
public class AlwaysSucceed<E> extends Decorator<E> {

    /**
     * Creates an {@code AlwaysSucceed} decorator with no child.
     */
    public AlwaysSucceed() {
    }

    /**
     * Creates an {@code AlwaysSucceed} decorator with the given child.
     *
     * @param task the child task to wrap
     */
    public AlwaysSucceed(Task<E> task) {
        super(task);
    }

    @Override
    public void childFail(Task<E> runningTask) {
        childSuccess(runningTask);
    }
}
