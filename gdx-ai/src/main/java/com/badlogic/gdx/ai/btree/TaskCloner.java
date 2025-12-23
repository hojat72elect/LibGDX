package com.badlogic.gdx.ai.btree;

/**
 * A {@code TaskCloner} allows you to use third-party libraries like Kryo to clone behavior trees. See {@link Task#TASK_CLONER}
 */
public interface TaskCloner {

    /**
     * Makes a deep copy of the given task.
     *
     * @param task the task to clone
     * @return the cloned task
     */
    <T> Task<T> cloneTask(Task<T> task);

    /**
     * Free task previously created by this {@link TaskCloner}
     *
     * @param task task to free
     */
    <T> void freeTask(Task<T> task);
}
