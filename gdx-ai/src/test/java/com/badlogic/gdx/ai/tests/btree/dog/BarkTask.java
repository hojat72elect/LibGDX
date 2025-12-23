package com.badlogic.gdx.ai.tests.btree.dog;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;

public class BarkTask extends LeafTask<Dog> {

    @TaskAttribute
    public IntegerDistribution times = ConstantIntegerDistribution.ONE;

    private int t;

    @Override
    public void start() {
        super.start();
        t = times.nextInt();
    }

    @Override
    public Status execute() {
        Dog dog = getObject();
        for (int i = 0; i < t; i++)
            dog.bark();
        return Status.SUCCEEDED;
    }

    @Override
    protected Task<Dog> copyTo(Task<Dog> task) {
        BarkTask bark = (BarkTask) task;
        bark.times = times;

        return task;
    }

    @Override
    public void reset() {
        times = ConstantIntegerDistribution.ONE;
        t = 0;
        super.reset();
    }
}
