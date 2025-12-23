package com.badlogic.gdx.ai.tests.btree.dog;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;

public class CareTask extends LeafTask<Dog> {

    @TaskAttribute(required = true)
    public float urgentProb = 0.8f;

    @Override
    public Status execute() {
        if (Math.random() < urgentProb) {
            return Status.SUCCEEDED;
        }
        Dog dog = getObject();
        dog.brainLog("GASP - Something urgent :/");
        return Status.FAILED;
    }

    @Override
    protected Task<Dog> copyTo(Task<Dog> task) {
        CareTask care = (CareTask) task;
        care.urgentProb = urgentProb;

        return task;
    }

    @Override
    public void reset() {
        urgentProb = 0.8f;
        super.reset();
    }
}
