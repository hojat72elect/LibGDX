package com.mbrlabs.mundus.editor.ui.widgets

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.Tooltip
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.mbrlabs.mundus.editor.utils.Fa

/**
 * A table containing both a label and tool tip icon.
 */
class ToolTipLabel(val label: String, toolTipText: String) : VisTable() {
    var fieldLabel = VisLabel(label)
    var fieldInfo = FaLabel(Fa.INFO_CIRCLE)

    init {
        defaults().padRight(4f)
        add(fieldLabel, fieldInfo).align(Align.left)
        Tooltip.Builder(toolTipText, Align.left).target(fieldInfo).build()

        fieldInfo.addListener(object : InputListener() {
            override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                super.enter(event, x, y, pointer, fromActor)
                fieldInfo.style = FaLabel.styleActive
            }

            override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                super.exit(event, x, y, pointer, toActor)
                fieldInfo.style = FaLabel.style
            }
        })
    }

}