package com.badlogic.gdx.backends.lwjgl3

import com.badlogic.gdx.Input
import com.badlogic.gdx.utils.Disposable

interface Lwjgl3Input : Input, Disposable {
    fun windowHandleChanged(windowHandle: Long)
    fun update()
    fun prepareNext()
    fun resetPollingStates()
}
