package com.mbrlabs.mundus.editor.ui.modules.dialogs

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextButton
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.project.ProjectManager

class ExitDialog : BaseDialog(TITLE) {

    companion object {
        private val TITLE = "Confirm exit"
    }

    private val exit = VisTextButton("Exit")
    private val saveExit = VisTextButton("Save and Exit")
    private val cancel = VisTextButton("Cancel")

    private val projectManager: ProjectManager = Mundus.inject()

    init {
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        val root = Table()
        root.padTop(6f).padRight(6f).padBottom(10f)
        add(root)

        root.add(VisLabel("Do you really want to close Mundus?")).grow().center().colspan(3).padBottom(10f).row()
        root.add<VisTextButton>(cancel).padRight(5f).grow()
        root.add<VisTextButton>(exit).padRight(5f).grow()
        root.add<VisTextButton>(saveExit).grow().row()
    }

    private fun setupListeners() {
        // cancel
        cancel.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                close()
            }
        })

        // exit
        exit.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                projectManager.current().assetManager.deleteNewUnsavedAssets()
                Gdx.app.exit()
            }
        })

        // save current project & exit
        saveExit.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                projectManager.saveCurrentProject()
                Gdx.app.exit()
            }
        })
    }

}
