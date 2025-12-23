package com.rockbite.bongo.engine.events.internal;

import net.mostlyoriginal.api.event.common.Event;

/**
 * Listener registration and event dispatch.
 * <p>
 * Wrapper for the complete listener registration and event dispatching
 * strategy used by {CustomCustomEventSystem}.
 * <p>
 * Make sure your strategy supports multiple instances if you want to run
 * multiple artemis worlds in parallel.
 */
public interface CustomEventDispatchStrategy {

    /**
     * Subscribe listener to events.
     */
    void register(Object owner, CustomEventListenerAbstraction listener);

    void unregisterEventsForOwner(Object owner);

    /**
     * Dispatch event to registered listeners.
     */
    void dispatch(Event event);

    /**
     * Dispatch event of given type to registered listeners.
     * <p>
     * Implementations should assume event is not safe to dispatch
     * until the current artemis system has finished processing.
     */
    <T extends Event> T dispatch(Class<T> type);

    /**
     * Process your own business.
     */
    void process();
}
