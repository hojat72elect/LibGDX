package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.LoopDecorator;
import com.badlogic.gdx.ai.btree.Task;

/**
 * The {@code UntilSuccess} decorator will repeat the wrapped task until that task succeeds, which makes the decorator succeed.
 * <p>
 * Notice that a wrapped task that always fails without entering the running status will cause an infinite loop in the current
 * frame.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
public class UntilSuccess<E> extends LoopDecorator<E> {

    /**
     * Creates an {@code UntilSuccess} decorator with no child.
     */
    public UntilSuccess() {
    }

    /**
     * Creates an {@code UntilSuccess} decorator with the given child.
     *
     * @param task the child task to wrap
     */
    public UntilSuccess(Task<E> task) {
        super(task);
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        success();
        loop = false;
    }

    @Override
    public void childFail(Task<E> runningTask) {
        loop = true;
    }
}
