package com.badlogic.gdx.backends.lwjgl.audio

import com.badlogic.gdx.Audio
import com.badlogic.gdx.backends.lwjgl.LwjglApplication

interface LwjglAudio : Audio {
    /**
     * Updates audio state (usually called every frame by the [LwjglApplication])
     */
    fun update()
}