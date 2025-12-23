package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.utils.Array;

/**
 * A {@code RandomSequence} is a sequence task's variant that runs its children in a random order.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
public class RandomSequence<E> extends Sequence<E> {

    /**
     * Creates a {@code RandomSequence} branch with no children.
     */
    public RandomSequence() {
        super();
    }

    /**
     * Creates a {@code RandomSequence} branch with the given children.
     *
     * @param tasks the children of this task
     */
    public RandomSequence(Array<Task<E>> tasks) {
        super(tasks);
    }

    /**
     * Creates a {@code RandomSequence} branch with the given children.
     *
     * @param tasks the children of this task
     */
    public RandomSequence(Task<E>... tasks) {
        super(new Array<Task<E>>(tasks));
    }

    @Override
    public void start() {
        super.start();
        if (randomChildren == null) randomChildren = createRandomChildren();
    }
}
