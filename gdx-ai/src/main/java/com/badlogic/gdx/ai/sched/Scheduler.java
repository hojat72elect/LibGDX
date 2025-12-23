package com.badlogic.gdx.ai.sched;

/**
 * A {@code Scheduler} works by assigning a pot of execution time among a variety of tasks, based on which ones need the time.
 * <p>
 * Different AI tasks can and should be run at different frequencies. You can simply schedule some tasks to run every few frames
 * and other tasks to run more frequently, slicing up the overall AI and distributing it over time. It is a powerful technique for
 * making sure that the game doesn't take too much AI time overall.
 * <p>
 * The tasks that get called are passed timing information so they can decide when to stop running and return. However, note that
 * there is nothing to stop a task from running for as long as it wants. The scheduler trusts that they will be well behaved.
 * <p>
 * Notes:
 * <ul>
 * <li><b>Hierarchical Scheduling</b>: {@code Scheduler} extends the {@link Schedulable} interface, allowing a scheduling system to
 * be run as a task by another scheduler. This technique is known as hierarchical scheduling. Also, it's worth noting that with a
 * hierarchical approach, there's no reason why the schedulers at different levels should be of the same kind. For instance, it is
 * possible to use a frequency-based scheduler for the whole game and priority-based schedulers for individual characters.</li>
 * <li><b>Level of Detail</b>: On its own there is nothing that hierarchical scheduling provides that a single scheduler cannot
 * handle. It comes into its own when used in combination with level of detail (LOD) systems. Level of detail systems are behavior
 * selectors; they choose only one behavior to run. In a hierarchical structure this means that schedulers running the whole game
 * don't need to know which behavior each character is running. A flat structure would mean removing and registering behaviors
 * with the main scheduler each time.</li>
 * </ul>
 */
public interface Scheduler extends Schedulable {

    /**
     * Adds the {@code schedulable} to the list using the given {@code frequency} and a phase calculated by this scheduler.
     *
     * @param schedulable the task to schedule
     * @param frequency   the frequency
     */
    void addWithAutomaticPhasing(Schedulable schedulable, int frequency);

    /**
     * Adds the {@code schedulable} to the list using the given {@code frequency} and {@code phase}
     *
     * @param schedulable the task to schedule
     * @param frequency   the frequency
     * @param phase       the phase
     */
    void add(Schedulable schedulable, int frequency, int phase);
}
