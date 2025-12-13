package com.badlogic.gdx.backends.lwjgl

import com.badlogic.gdx.Input

interface LwjglInput : Input {
    /**
     * Records input events such as mouse and keyboard (usually called every frame by the [LwjglApplication])
     */
    fun update()

    /**
     * Process all recorded input events (usually called every frame by the [LwjglApplication])
     */
    fun processEvents()
}
