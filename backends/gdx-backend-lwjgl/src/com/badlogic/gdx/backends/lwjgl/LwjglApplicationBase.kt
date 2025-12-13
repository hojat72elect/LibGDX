package com.badlogic.gdx.backends.lwjgl

import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.lwjgl.audio.LwjglAudio

interface LwjglApplicationBase : Application {
    fun createAudio(config: LwjglApplicationConfiguration): LwjglAudio
    fun createInput(config: LwjglApplicationConfiguration): LwjglInput
}
