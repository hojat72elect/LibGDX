package com.mbrlabs.mundus.editor.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer

class InputManager : InputMultiplexer() {

    init {
        Gdx.input.setInputProcessor(this)
    }

}
