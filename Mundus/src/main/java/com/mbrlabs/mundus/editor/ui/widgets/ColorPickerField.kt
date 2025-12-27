package com.mbrlabs.mundus.editor.ui.widgets

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisTextField
import com.kotcrab.vis.ui.widget.color.ColorPicker
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter
import com.kotcrab.vis.ui.widget.color.ColorPickerListener
import com.mbrlabs.mundus.editor.ui.UI

/**
 * An un-editable text field with a color picker.
 * The text field shows the color hex value, the button launches a color picker dialog.
 */
class ColorPickerField() : VisTable() {

    /**
     * The currently selected color.
     */
    var selectedColor: Color = Color.WHITE.cpy()
        set(value) {
            field.set(value)
            textField.text = "#" + value.toString()
        }

    /**
     * An optional color picker listener.
     * Will be called if user changed color.
     */
    var colorAdapter: ColorPickerAdapter? = null

    private val colorPickerListenerInternal: ColorPickerListener
    private val textField: VisTextField = VisTextField()
    private val cpBtn: VisTextButton = VisTextButton("Select")

    init {
        // setup internal color picker listener
        colorPickerListenerInternal = object : ColorPickerListener {
            override fun canceled(oldColor: Color?) {
                colorAdapter?.canceled(oldColor)
            }

            override fun reset(previousColor: Color?, newColor: Color?) {
                colorAdapter?.reset(previousColor, newColor)
            }

            override fun changed(newColor: Color?) {
                colorAdapter?.changed(newColor)
            }

            override fun finished(newColor: Color) {
                selectedColor = newColor
                colorAdapter?.finished(newColor)
            }
        }

        textField.isDisabled = true
        setupUI()
        setupListeners()
    }

    /**
     * Disables the button for the color picker.
     */
    fun disable(disable: Boolean) {
        cpBtn.isDisabled = disable
        if (disable) {
            cpBtn.touchable = Touchable.disabled
        } else {
            cpBtn.touchable = Touchable.enabled
        }
    }

    private fun setupUI() {
        add<VisTextField>(textField).padRight(5f).fillX().expandX()
        add(cpBtn).row()
    }

    private fun setupListeners() {
        // selectedColor chooser button
        cpBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                val colorPicker = ColorPicker()
                colorPicker.color = selectedColor
                colorPicker.listener = colorPickerListenerInternal
                UI.addActor(colorPicker.fadeIn())
            }
        })

    }

}
