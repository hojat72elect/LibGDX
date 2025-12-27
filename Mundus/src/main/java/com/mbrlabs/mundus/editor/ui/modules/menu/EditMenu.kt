package com.mbrlabs.mundus.editor.ui.modules.menu

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuItem
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.history.CommandHistory

class EditMenu : Menu("Edit") {

    private val copy = MenuItem("Copy")
    private val paste = MenuItem("Paste")
    private val undo = MenuItem("Undo")
    private val redo = MenuItem("Redo")

    private val history: CommandHistory = Mundus.inject()

    init {
        copy.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.C)
        paste.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.P)
        undo.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.Z)
        redo.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.Y)

        addItem(copy)
        addItem(paste)
        addItem(undo)
        addItem(redo)

        setupListeners()
    }

    private fun setupListeners() {
        // undo
        undo.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                history.goBack()
            }
        })

        // redo
        redo.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                history.goForward()
            }
        })

    }

}
