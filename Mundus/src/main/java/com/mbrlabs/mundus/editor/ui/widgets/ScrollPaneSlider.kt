package com.mbrlabs.mundus.editor.ui.widgets

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.kotcrab.vis.ui.widget.VisSlider

/**
 * Makes it possible to use a slider inside a scroll pane.
 */
class ScrollPaneSlider(min: Float, max: Float, stepSize: Float, vertical: Boolean) : VisSlider(min, max, stepSize, vertical) {

    init {
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                event?.stop()
                return true
            }
        })
    }
}
