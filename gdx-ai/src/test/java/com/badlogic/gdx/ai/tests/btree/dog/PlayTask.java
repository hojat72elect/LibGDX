package com.badlogic.gdx.ai.tests.btree.dog;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class PlayTask extends LeafTask<Dog> {

    public void start() {
        Dog dog = getObject();
        dog.brainLog("WOW - Lets play!");
    }

    @Override
    public Status execute() {
        Dog dog = getObject();
        dog.brainLog("PANT PANT - So fun");
        return Status.RUNNING;
    }

    @Override
    public void end() {
        Dog dog = getObject();
        dog.brainLog("SIC - No time to play :(");
    }

    @Override
    protected Task<Dog> copyTo(Task<Dog> task) {
        return task;
    }
}
