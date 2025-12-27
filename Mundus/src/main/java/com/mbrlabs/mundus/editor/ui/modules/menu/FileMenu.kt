package com.mbrlabs.mundus.editor.ui.modules.menu

import com.badlogic.gdx.Input
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.util.dialog.Dialogs
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuItem
import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.file.FileChooser
import com.kotcrab.vis.ui.widget.file.SingleFileChooserListener
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.project.ProjectAlreadyImportedException
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.core.project.ProjectOpenException
import com.mbrlabs.mundus.editor.core.registry.Registry
import com.mbrlabs.mundus.editor.ui.UI

class FileMenu : Menu("File") {

    private val newProject = MenuItem("New Project")
    private val importProject = MenuItem("Import Project")
    private val recentProjects = MenuItem("Recent Projects")
    private val saveProject = MenuItem("Save Project")
    private val exit = MenuItem("Exit")

    private val registry: Registry = Mundus.inject()
    private val projectManager: ProjectManager = Mundus.inject()

    init {
        newProject.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.N)
        importProject.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.O)
        saveProject.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.S)

        // setup recent projects
        val recentProjectsPopup = PopupMenu()
        for (ref in registry.projects) {
            val pro = MenuItem(ref.name + " - [" + ref.path + "]")
            pro.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    try {
                        projectManager.startAsyncProjectLoad(ref)
                        UI.toggleLoadingScreen(true, ref.name)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Dialogs.showErrorDialog(UI, "Could not open project")
                    }

                }
            })
            recentProjectsPopup.addItem(pro)
        }
        recentProjects.subMenu = recentProjectsPopup

        addItem(newProject)
        addItem(importProject)
        addItem(saveProject)
        addItem(recentProjects)
        addSeparator()
        addItem(exit)

        setupListeners()
    }

    private fun setupListeners() {
        newProject.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.newProjectDialog)
            }
        })

        importProject.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.fileChooser.setListener(object : SingleFileChooserListener() {
                    override fun selected(file: FileHandle) {
                        importNewProject(file)
                    }
                })
                UI.fileChooser.selectionMode = FileChooser.SelectionMode.DIRECTORIES
                UI.addActor(UI.fileChooser.fadeIn())
            }
        })

        saveProject.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                projectManager.saveCurrentProject()
                UI.toaster.success("Project saved")
            }
        })

        exit.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.exitDialog)
            }
        })
    }

    fun importNewProject(projectDir: FileHandle) {
        try {
            projectManager.importProject(projectDir.path())
        } catch (e: ProjectAlreadyImportedException) {
            e.printStackTrace()
            Dialogs.showErrorDialog(UI, "This Project is already imported.")
        } catch (e: ProjectOpenException) {
            e.printStackTrace()
            Dialogs.showErrorDialog(UI, "This Project can't be opened.")
        }

    }

}
