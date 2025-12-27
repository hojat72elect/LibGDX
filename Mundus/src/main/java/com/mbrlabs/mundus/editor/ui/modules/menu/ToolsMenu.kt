package com.mbrlabs.mundus.editor.ui.modules.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuItem
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.ui.UI
import com.mbrlabs.mundus.editor.ui.modules.dialogs.tools.AssetCleanUpDialog

class ToolsMenu : Menu("Tools") {

    private val findUnusedAssets = MenuItem("Asset Clean Up")
    private val debugRendering = MenuItem("Debug Render Options")
    private val fixTerrainSeams = MenuItem("Fix Terrain Seams")

    val projectManager: ProjectManager = Mundus.inject()

    init {
        addItem(findUnusedAssets)
        addItem(debugRendering)
        addItem(fixTerrainSeams)

        findUnusedAssets.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val dialog = AssetCleanUpDialog()
                UI.showDialog(dialog)
                Thread {
                    val unusedAssets = projectManager.current().assetManager.findUnusedAssets(projectManager)
                    Gdx.app.postRunnable {
                        dialog.setAssetsToDelete(unusedAssets)
                    }
                }.start()
            }
        })

        debugRendering.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.debugRenderDialog)
            }
        })

        fixTerrainSeams.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.terrainStitcherDialog)
            }
        })
    }

}
