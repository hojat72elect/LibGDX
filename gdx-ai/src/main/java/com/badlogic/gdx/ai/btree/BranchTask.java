package com.badlogic.gdx.ai.btree;

import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;
import com.badlogic.gdx.utils.Array;

/**
 * A branch task defines a behavior tree branch, contains logic of starting or running sub-branches and leaves
 *
 * @param <E> type of the blackboard object that tasks use to read or modify game state
 */
@TaskConstraint(minChildren = 1)
public abstract class BranchTask<E> extends Task<E> {

    /**
     * The children of this branch task.
     */
    protected Array<Task<E>> children;

    /**
     * Create a branch task with no children
     */
    public BranchTask() {
        this(new Array<Task<E>>());
    }

    /**
     * Create a branch task with a list of children
     *
     * @param tasks list of this task's children, can be empty
     */
    public BranchTask(Array<Task<E>> tasks) {
        this.children = tasks;
    }

    @Override
    protected int addChildToTask(Task<E> child) {
        children.add(child);
        return children.size - 1;
    }

    @Override
    public int getChildCount() {
        return children.size;
    }

    @Override
    public Task<E> getChild(int i) {
        return children.get(i);
    }

    @Override
    protected Task<E> copyTo(Task<E> task) {
        BranchTask<E> branch = (BranchTask<E>) task;
        if (children != null) {
            for (int i = 0; i < children.size; i++) {
                branch.children.add(children.get(i).cloneTask());
            }
        }

        return task;
    }

    @Override
    public void reset() {
        children.clear();
        super.reset();
    }
}
