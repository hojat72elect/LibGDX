package com.mbrlabs.mundus.editor.ui.modules

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.util.dialog.Dialogs
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter
import com.kotcrab.vis.ui.widget.MenuItem
import com.kotcrab.vis.ui.widget.PopupMenu
import com.kotcrab.vis.ui.widget.Tooltip
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.events.AssetImportEvent
import com.mbrlabs.mundus.editor.events.AssetSelectedEvent
import com.mbrlabs.mundus.editor.events.FullScreenEvent
import com.mbrlabs.mundus.editor.events.GameObjectSelectedEvent
import com.mbrlabs.mundus.editor.events.ToolActivatedEvent
import com.mbrlabs.mundus.editor.tools.RotateTool
import com.mbrlabs.mundus.editor.tools.ScaleTool
import com.mbrlabs.mundus.editor.tools.SelectionTool
import com.mbrlabs.mundus.editor.tools.ToolManager
import com.mbrlabs.mundus.editor.tools.TranslateTool
import com.mbrlabs.mundus.editor.ui.UI
import com.mbrlabs.mundus.editor.ui.gizmos.GizmoManager
import com.mbrlabs.mundus.editor.ui.modules.outline.Outline
import com.mbrlabs.mundus.editor.ui.widgets.FaTextButton
import com.mbrlabs.mundus.editor.ui.widgets.ToggleButton
import com.mbrlabs.mundus.editor.ui.widgets.Toolbar
import com.mbrlabs.mundus.editor.utils.Fa
import com.mbrlabs.mundus.editor.utils.Log

class MundusToolbar(private val outline: Outline) : Toolbar(),
    FullScreenEvent.FullScreenEventListener,
    ToolActivatedEvent.ToolActivatedEventListener,
    GameObjectSelectedEvent.GameObjectSelectedListener,
    AssetSelectedEvent.AssetSelectedListener {

    companion object {
        private val TAG = MundusToolbar::class.java.simpleName
    }

    private val saveBtn = FaTextButton(Fa.SAVE)
    private val importBtn = FaTextButton(Fa.DOWNLOAD)
    private val exportBtn = FaTextButton(Fa.GIFT)

    private val selectBtn: FaTextButton
    private val translateBtn: FaTextButton
    private val rotateBtn: FaTextButton
    private val scaleBtn: FaTextButton
    private val fullScreenBtn: FaTextButton
    private val gizmoBtn: FaTextButton
    private val globalLocalSwitch = ToggleButton("Global space", "Local space")

    private val importMenu = PopupMenu()
    private val importMesh = MenuItem("Import 3D model")
    private val importTexture = MenuItem("Import texture")
    private val createMaterial = MenuItem("Create material")

    private val toolManager: ToolManager = Mundus.inject()
    private val projectManager: ProjectManager = Mundus.inject()
    private val gizmoManager: GizmoManager = Mundus.inject()

    init {
        Mundus.registerEventListener(this)

        importMenu.addItem(importMesh)
        importMenu.addItem(importTexture)
        importMenu.addItem(createMaterial)

        saveBtn.padRight(7f).padLeft(7f)
        Tooltip.Builder("Save project (Ctrl+S)").target(saveBtn).build()

        importBtn.padRight(7f).padLeft(7f)
        Tooltip.Builder("Import").target(importBtn).build()

        exportBtn.padRight(12f).padLeft(7f)
        Tooltip.Builder("Export project (F1)").target(exportBtn).build()

        selectBtn = FaTextButton(toolManager.selectionTool.iconFont)
        selectBtn.padRight(7f).padLeft(12f)
        Tooltip.Builder(toolManager.selectionTool.name).target(selectBtn).build()

        translateBtn = FaTextButton(toolManager.translateTool.iconFont)
        translateBtn.padRight(7f).padLeft(7f)
        Tooltip.Builder(toolManager.translateTool.name).target(translateBtn).build()

        rotateBtn = FaTextButton(toolManager.rotateTool.iconFont)
        rotateBtn.padRight(7f).padLeft(7f)
        Tooltip.Builder(toolManager.rotateTool.name).target(rotateBtn).build()

        scaleBtn = FaTextButton(toolManager.scaleTool.iconFont)
        scaleBtn.padRight(7f).padLeft(7f)
        Tooltip.Builder(toolManager.scaleTool.name).target(scaleBtn).build()

        fullScreenBtn = FaTextButton(Fa.ARROWS_ALT)
        fullScreenBtn.padRight(7f).padLeft(7f)
        Tooltip.Builder("Fullscreen view (F8)").target(fullScreenBtn).build()

        gizmoBtn = FaTextButton(Fa.EYE)
        gizmoBtn.padRight(7f).padLeft(7f)
        Tooltip.Builder("Toggle gizmos").target(gizmoBtn).build()

        addItem(saveBtn, true)
        addItem(importBtn, true)
        addItem(exportBtn, true)
        addSeperator(true)
        addItem(selectBtn, true)
        addItem(translateBtn, true)
        addItem(rotateBtn, true)
        addItem(scaleBtn, true)
        addSeperator(true)
        addItem(fullScreenBtn, true)
        addItem(gizmoBtn, true)
        //addItem(globalLocalSwitch, true);

        setButtonStyleToActive(translateBtn)
        updateGizmoIcon()

        // save btn
        saveBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                projectManager.saveCurrentProject()
                UI.toaster.success("Project saved")
            }
        })

        // export btn
        exportBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.exportDialog.export()
            }
        })

        // import btn
        importBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                importMenu.showMenu(UI, importBtn)
            }
        })

        // import mesh
        importMesh.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.importModelDialog)
            }
        })

        // import texture
        importTexture.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.importTextureDialog)
            }
        })

        // create material
        createMaterial.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Dialogs.showInputDialog(
                    UI, "Create new material", "Material name",
                    object : InputDialogAdapter() {
                        override fun finished(input: String?) {
                            val assetManager = projectManager.current().assetManager
                            try {
                                val mat = assetManager.createMaterialAsset(input!!)
                                Mundus.postEvent(AssetImportEvent(mat))
                            } catch (e: Exception) {
                                Log.exception(TAG, e)
                                UI.toaster.error(e.toString())
                            }

                        }

                        override fun canceled() {
                            super.canceled()
                        }
                    })
            }
        })

        // select tool
        selectBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                toolManager.activateTool(toolManager.selectionTool)
                val selectedGameObject = outline.getSelectedGameObject()
                projectManager.current().currScene.currentSelection = selectedGameObject
            }
        })

        // translate tool
        translateBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                toolManager.activateTool(toolManager.translateTool)
                val selectedGameObject = outline.getSelectedGameObject()
                projectManager.current().currScene.currentSelection = selectedGameObject
            }
        })

        // rotate tool
        rotateBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                toolManager.activateTool(toolManager.rotateTool)
                val selectedGameObject = outline.getSelectedGameObject()
                projectManager.current().currScene.currentSelection = selectedGameObject
            }
        })

        // scale tool
        scaleBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                toolManager.activateTool(toolManager.scaleTool)
                val selectedGameObject = outline.getSelectedGameObject()
                projectManager.current().currScene.currentSelection = selectedGameObject
            }
        })

        // full screen render
        fullScreenBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.toggleFullscreenRender()
            }
        })

        // gizmo visibility toggle
        gizmoBtn.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                gizmoManager.toggleRendering()
                updateGizmoIcon()

            }
        })

        // global / local space switching
        globalLocalSwitch.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                toolManager.translateTool.setGlobalSpace(globalLocalSwitch.isOn)
            }
        })

    }

    /**
     * Refresh the active button in UI with the currently active tool.
     */
    fun updateActiveToolButton() {
        when (toolManager.activeTool?.name) {
            SelectionTool.NAME -> setButtonStyleToActive(selectBtn)
            TranslateTool.NAME -> setButtonStyleToActive(translateBtn)
            RotateTool.NAME -> setButtonStyleToActive(rotateBtn)
            ScaleTool.NAME -> setButtonStyleToActive(scaleBtn)
        }
    }

    override fun onFullScreenEvent(event: FullScreenEvent) {
        if (event.isFullScreen) {
            fullScreenBtn.style = FaTextButton.styleActive
        } else {
            fullScreenBtn.style = FaTextButton.styleNoBg
        }
    }

    override fun onToolActivatedEvent(event: ToolActivatedEvent) {
        clearButtonsStyle()
        updateActiveToolButton()
    }

    override fun onGameObjectSelected(event: GameObjectSelectedEvent) {
        if (!event.activateSelection) return
        if (toolManager.activeTool !is SelectionTool) {
            toolManager.activateTool(toolManager.translateTool)
        }
        toolManager.translateTool.gameObjectSelected(event.gameObject)
        Mundus.postEvent(ToolActivatedEvent())
    }

    override fun onAssetSelected(event: AssetSelectedEvent) {
        toolManager.setDefaultTool()
    }

    private fun clearButtonsStyle() {
        selectBtn.style = FaTextButton.styleNoBg
        translateBtn.style = FaTextButton.styleNoBg
        rotateBtn.style = FaTextButton.styleNoBg
        scaleBtn.style = FaTextButton.styleNoBg
    }

    private fun setButtonStyleToActive(btn: FaTextButton) {
        clearButtonsStyle()
        btn.style = FaTextButton.styleActive
    }

    private fun updateGizmoIcon() {
        if (gizmoManager.isRenderEnabled()) {
            gizmoBtn.style = FaTextButton.styleActive
            gizmoBtn.setText(Fa.EYE)
        } else {
            gizmoBtn.style = FaTextButton.styleNoBg
            gizmoBtn.setText(Fa.EYE_SLASH)
        }
    }

}
