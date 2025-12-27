package com.mbrlabs.mundus.editor.ui.widgets

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.mbrlabs.mundus.editor.utils.formatFloat

/**
 * Can be used inside a scroll pane & has the current value displayed on the
 * right.
 */
class ImprovedSlider(min: Float, max: Float, step: Float, digits: Int = 2) : VisTable() {

    private val currentValue = VisLabel("0")
    private val slider = ScrollPaneSlider(min, max, step, false)

    init {
        add(slider).expandX().fillX().left()
        add(currentValue).padLeft(10f).right()

        slider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                currentValue.setText(String.format(formatFloat(slider.value, digits)))
            }
        })
    }

    var value: Float
        get() = slider.value
        set(value) {
            slider.value = value
            currentValue.setText(formatFloat(value, 2))
        }

}
