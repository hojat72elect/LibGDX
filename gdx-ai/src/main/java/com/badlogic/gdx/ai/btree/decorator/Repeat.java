package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.LoopDecorator;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;

/**
 * A {@code Repeat} decorator will repeat the wrapped task a certain number of times, possibly infinite. This task always succeeds
 * when reaches the specified number of repetitions.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
public class Repeat<E> extends LoopDecorator<E> {

    /**
     * Optional task attribute specifying the integer distribution that determines how many times the wrapped task must be
     * repeated. Defaults to {@link ConstantIntegerDistribution#NEGATIVE_ONE} which indicates an infinite number of repetitions.
     *
     * @see #start()
     */
    @TaskAttribute
    public IntegerDistribution times;

    private int count;

    /**
     * Creates an infinite repeat decorator with no child task.
     */
    public Repeat() {
        this(null);
    }

    /**
     * Creates an infinite repeat decorator that wraps the given task.
     *
     * @param child the task that will be wrapped
     */
    public Repeat(Task<E> child) {
        this(ConstantIntegerDistribution.NEGATIVE_ONE, child);
    }

    /**
     * Creates a repeat decorator that executes the given task the number of times (possibly infinite) determined by the given
     * distribution. The number of times is drawn from the distribution by the {@link #start()} method. Any negative value means
     * forever.
     *
     * @param times the integer distribution specifying how many times the child must be repeated.
     * @param child the task that will be wrapped
     */
    public Repeat(IntegerDistribution times, Task<E> child) {
        super(child);
        this.times = times;
    }

    /**
     * Draws a value from the distribution that determines how many times the wrapped task must be repeated. Any negative value
     * means forever.
     * <p>
     * This method is called when the task is entered.
     */
    @Override
    public void start() {
        count = times.nextInt();
    }

    @Override
    public boolean condition() {
        return loop && count != 0;
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        if (count > 0) count--;
        if (count == 0) {
            super.childSuccess(runningTask);
            loop = false;
        } else
            loop = true;
    }

    @Override
    public void childFail(Task<E> runningTask) {
        childSuccess(runningTask);
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        Repeat<E> repeat = (Repeat<E>) task;
        repeat.times = times; // no need to clone since it is immutable

        return super.copyTo(task);
    }

    @Override
    public void reset() {
        count = 0;
        times = null;
        super.reset();
    }
}
