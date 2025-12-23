package com.badlogic.gdx.ai.btree.branch;

import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.utils.Array;

/**
 * A {@code RandomSelector} is a selector task's variant that runs its children in a random order.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
public class RandomSelector<E> extends Selector<E> {

    /**
     * Creates a {@code RandomSelector} branch with no children.
     */
    public RandomSelector() {
        super();
    }

    /**
     * Creates a {@code RandomSelector} branch with the given children.
     *
     * @param tasks the children of this task
     */
    public RandomSelector(Task<E>... tasks) {
        super(new Array<Task<E>>(tasks));
    }

    /**
     * Creates a {@code RandomSelector} branch with the given children.
     *
     * @param tasks the children of this task
     */
    public RandomSelector(Array<Task<E>> tasks) {
        super(tasks);
    }

    @Override
    public void start() {
        super.start();
        if (randomChildren == null) randomChildren = createRandomChildren();
    }
}
