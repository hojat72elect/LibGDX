package com.badlogic.gdx.input

/**
 * This interface was moved to kerman game engine.
 */
interface TextInputWrapper {

    fun getText(): String

    fun setText(text: String)

    fun getSelectionStart(): Int

    fun getSelectionEnd(): Int

    fun setPosition(position: Int)

    fun shouldClose(): Boolean
}
