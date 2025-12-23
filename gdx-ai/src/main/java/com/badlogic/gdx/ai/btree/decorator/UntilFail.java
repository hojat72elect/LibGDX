package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.LoopDecorator;
import com.badlogic.gdx.ai.btree.Task;

/**
 * The {@code UntilFail} decorator will repeat the wrapped task until that task fails, which makes the decorator succeed.
 * <p>
 * Notice that a wrapped task that always succeeds without entering the running status will cause an infinite loop in the current
 * frame.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
public class UntilFail<E> extends LoopDecorator<E> {

    /**
     * Creates an {@code UntilFail} decorator with no child.
     */
    public UntilFail() {
    }

    /**
     * Creates an {@code UntilFail} decorator with the given child.
     *
     * @param task the child task to wrap
     */
    public UntilFail(Task<E> task) {
        super(task);
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        loop = true;
    }

    @Override
    public void childFail(Task<E> runningTask) {
        success();
        loop = false;
    }
}
