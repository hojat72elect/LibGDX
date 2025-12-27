package com.mbrlabs.mundus.editor.ui.modules.menu

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuItem
import com.mbrlabs.mundus.editor.ui.UI

class AssetsMenu : Menu("Assets") {

    private val importMesh = MenuItem("Import Mesh")

    init {
        addItem(importMesh)
        addListeners()
    }

    private fun addListeners() {
        importMesh.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.importModelDialog)
            }
        })
    }

}
