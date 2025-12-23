package com.badlogic.gdx.ai.tests.btree.dog;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class RestTask extends LeafTask<Dog> {

    @Override
    public void start() {
        getObject().brainLog("YAWN - So tired...");
    }

    @Override
    public Status execute() {
        getObject().brainLog("zz zz zz");
        return Status.RUNNING;
    }

    @Override
    public void end() {
        getObject().brainLog("SOB - Time to wake up");
    }

    @Override
    protected Task<Dog> copyTo(Task<Dog> task) {
        return task;
    }
}
