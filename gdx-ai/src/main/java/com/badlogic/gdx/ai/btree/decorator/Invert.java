package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;

/**
 * An {@code Invert} decorator will succeed if the wrapped task fails and will fail if the wrapped task succeeds.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
public class Invert<E> extends Decorator<E> {

    /**
     * Creates an {@code Invert} decorator with no child.
     */
    public Invert() {
    }

    /**
     * Creates an {@code Invert} decorator with the given child.
     *
     * @param task the child task to wrap
     */
    public Invert(Task<E> task) {
        super(task);
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        super.childFail(runningTask);
    }

    @Override
    public void childFail(Task<E> runningTask) {
        super.childSuccess(runningTask);
    }
}
