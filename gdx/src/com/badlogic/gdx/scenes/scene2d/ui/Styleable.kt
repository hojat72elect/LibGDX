package com.badlogic.gdx.scenes.scene2d.ui

/**
 * This interface marks an Actor as Styleable.
 * The generic <T> is the Style object type.
 */
interface Styleable<T> {

    /**
     * Set the current style of the actor.
     */
    fun setStyle(style: T)

    /**
     * Get the current style of the actor.
     */
    fun getStyle(): T
}
