package com.badlogic.ashley.signals;

/**
 * A simple Listener interface used to listen to a {@link Signal}.
 */
public interface Listener<T> {
    /**
     * @param signal The Signal that triggered event
     * @param object The object passed on dispatch
     */
    void receive(Signal<T> signal, T object);
}
