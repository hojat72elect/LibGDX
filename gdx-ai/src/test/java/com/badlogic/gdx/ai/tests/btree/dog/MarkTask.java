package com.badlogic.gdx.ai.tests.btree.dog;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class MarkTask extends LeafTask<Dog> {

    int i;

    @Override
    public void start() {
        i = 0;
        getObject().log("Dog lifts a leg and pee!");
    }

    @Override
    public Status execute() {
        Dog dog = getObject();
        Boolean result = dog.markATree(i++);
        if (result == null) {
            return Status.RUNNING;
        }
        return result ? Status.SUCCEEDED : Status.FAILED;
    }

    @Override
    protected Task<Dog> copyTo(Task<Dog> task) {
        return task;
    }

    @Override
    public void reset() {
        i = 0;
        super.reset();
    }
}
