package com.badlogic.gdx.ai.btree.decorator;

import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;
import com.badlogic.gdx.ai.btree.leaf.Failure;
import com.badlogic.gdx.ai.btree.leaf.Success;
import com.badlogic.gdx.ai.utils.random.ConstantFloatDistribution;
import com.badlogic.gdx.ai.utils.random.FloatDistribution;
import com.badlogic.gdx.math.MathUtils;

/**
 * The {@code Random} decorator succeeds with the specified probability, regardless of whether the wrapped task fails or succeeds.
 * Also, the wrapped task is optional, meaning that this decorator can act like a leaf task.
 * <p>
 * Notice that if success probability is 1 this task is equivalent to the decorator {@link AlwaysSucceed} and the leaf
 * {@link Success}. Similarly if success probability is 0 this task is equivalent to the decorator {@link AlwaysFail} and the leaf
 * {@link Failure}.
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
@TaskConstraint(minChildren = 0, maxChildren = 1)
public class Random<E> extends Decorator<E> {

    /**
     * Optional task attribute specifying the random distribution that determines the success probability. It defaults to
     * {@link ConstantFloatDistribution#ZERO_POINT_FIVE}.
     */
    @TaskAttribute
    public FloatDistribution success;

    private float p;

    /**
     * Creates a {@code Random} decorator with no child that succeeds or fails with equal probability.
     */
    public Random() {
        this(ConstantFloatDistribution.ZERO_POINT_FIVE);
    }

    /**
     * Creates a {@code Random} decorator with the given child that succeeds or fails with equal probability.
     *
     * @param task the child task to wrap
     */
    public Random(Task<E> task) {
        this(ConstantFloatDistribution.ZERO_POINT_FIVE, task);
    }

    /**
     * Creates a {@code Random} decorator with no child that succeeds with the specified probability.
     *
     * @param success the random distribution that determines success probability
     */
    public Random(FloatDistribution success) {
        super();
        this.success = success;
    }

    /**
     * Creates a {@code Random} decorator with the given child that succeeds with the specified probability.
     *
     * @param success the random distribution that determines success probability
     * @param task    the child task to wrap
     */
    public Random(FloatDistribution success, Task<E> task) {
        super(task);
        this.success = success;
    }

    /**
     * Draws a value from the distribution that determines the success probability.
     * <p>
     * This method is called when the task is entered.
     */
    @Override
    public void start() {
        p = success.nextFloat();
    }

    @Override
    public void run() {
        if (child != null)
            super.run();
        else
            decide();
    }

    @Override
    public void childFail(Task<E> runningTask) {
        decide();
    }

    @Override
    public void childSuccess(Task<E> runningTask) {
        decide();
    }

    private void decide() {
        if (MathUtils.random() <= p)
            success();
        else
            fail();
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        Random<E> random = (Random<E>) task;
        random.success = success; // no need to clone since it is immutable

        return super.copyTo(task);
    }

    @Override
    public void reset() {
        this.p = 0;
        this.success = ConstantFloatDistribution.ZERO_POINT_FIVE;
        super.reset();
    }
}
