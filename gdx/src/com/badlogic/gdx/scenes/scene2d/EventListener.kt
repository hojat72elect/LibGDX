package com.badlogic.gdx.scenes.scene2d

/**
 * Low level interface for receiving events. Typically there is a listener class for each specific event class.
 * @see InputListener
 * @see InputEvent
 */
interface EventListener {

    /**
     * Try to handle the given event, if it is applicable.
     * @return true if the event should be considered [handled][Event.handle] by scene2d.
     */
    fun handle(event: Event): Boolean
}
