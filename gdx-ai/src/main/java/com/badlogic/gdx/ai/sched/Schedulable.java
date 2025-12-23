package com.badlogic.gdx.ai.sched;

/**
 * Anything that can be scheduled by a {@link Scheduler} must implement this interface.
 */
public interface Schedulable {

    /**
     * Method invoked by the {@link Scheduler} when this schedulable needs to be run.
     *
     * @param nanoTimeToRun the maximum time in nanoseconds this scheduler should run on the current frame.
     */
    void run(long nanoTimeToRun);
}
