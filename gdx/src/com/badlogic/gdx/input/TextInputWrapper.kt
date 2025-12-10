package com.badlogic.gdx.input

interface TextInputWrapper {

    fun getText(): String

    fun setText(text: String)

    fun getSelectionStart(): Int

    fun getSelectionEnd(): Int

    fun setPosition(position: Int)

    fun shouldClose(): Boolean
}
