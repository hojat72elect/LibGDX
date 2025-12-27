package com.mbrlabs.mundus.editor.ui.widgets

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisTable

open class Toolbar {

    val root = VisTable()

    private val left = VisTable()
    private val right = VisTable()

    init {
        root.setBackground("menu-bg")
        root.align(Align.left or Align.center)

        left.left().top()
        root.add(left).pad(2f).expandX().fillX()

        right.right().top()
        root.add(right).pad(2f).expandX().fillX().row()
        root.addSeparator().pad(0f).height(2f).colspan(2)
    }

    fun addItem(actor: Actor, addLeft: Boolean) {
        if (addLeft) {
            left.add(actor)
        } else {
            right.add(actor)
        }
    }

    fun addSeperator(addLeft: Boolean) {
        if (addLeft) {
            left.addSeparator(true)
        } else {
            right.addSeparator(true)
        }
    }

}
