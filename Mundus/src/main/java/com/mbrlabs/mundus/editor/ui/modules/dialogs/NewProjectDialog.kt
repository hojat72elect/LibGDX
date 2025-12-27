package com.mbrlabs.mundus.editor.ui.modules.dialogs

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisTextField
import com.kotcrab.vis.ui.widget.file.FileChooser
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.ui.UI
import com.mbrlabs.mundus.editor.ui.widgets.FileChooserField

class NewProjectDialog : BaseDialog("Create New Project") {

    private val projectName = VisTextField()
    private val createBtn = VisTextButton("Create project")
    private val locationPath = FileChooserField(300)

    private val projectManager: ProjectManager = Mundus.inject()

    init {
        isModal = true
        locationPath.setFileMode(FileChooser.SelectionMode.DIRECTORIES)


        contentTable.add(VisLabel("Project Name")).left().row()
        contentTable.add(this.projectName).padBottom(UI.PAD_BOTTOM).width(300f).fillX().row()
        contentTable.add(VisLabel("Location")).left().row()
        contentTable.add(locationPath).padBottom(UI.PAD_BOTTOM_X2).growX().row()
        contentTable.add(createBtn).growX().row()

        setupListeners()

    }

    private fun setupListeners() {

        createBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val name = projectName.text
                var path = locationPath.path
                if (validateInput(name, path)) {
                    if (!path.endsWith("/")) {
                        path += "/"
                    }
                    val projectContext = projectManager.createProject(name, path)
                    close()
                    projectManager.changeProject(projectContext)
                }

            }
        })

    }

    private fun validateInput(name: String?, path: String?): Boolean {
        return name != null && name.isNotEmpty() && path != null && path.isNotEmpty()
    }

}
