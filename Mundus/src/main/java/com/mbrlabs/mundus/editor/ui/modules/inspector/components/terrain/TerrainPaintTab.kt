package com.mbrlabs.mundus.editor.ui.modules.inspector.components.terrain

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisTable
import com.mbrlabs.mundus.commons.assets.TerrainLayerAsset
import com.mbrlabs.mundus.commons.terrain.SplatTexture
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.assets.MetaSaver
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.tools.brushes.TerrainBrush
import com.mbrlabs.mundus.editor.ui.widgets.TerrainTextureLayerWidget

class TerrainPaintTab(private val parentWidget: TerrainComponentWidget) : BaseBrushTab(parentWidget, TerrainBrush.BrushMode.PAINT) {

    companion object {
        private val TAG = TerrainPaintTab::class.java.simpleName
    }

    private val root = VisTable()
    private val terrainTextureLayerWidget = TerrainTextureLayerWidget(this.parentWidget.component.terrainAsset.terrainLayerAsset)

    private val projectManager: ProjectManager = Mundus.inject()
    private val metaSaver: MetaSaver = Mundus.inject()

    init {
        root.align(Align.left)

        // brushes
        root.add(terrainBrushGrid).expand().fill().padBottom(5f).row()
        root.add(terrainTextureLayerWidget).expand().fill().padBottom(5f).row()

        terrainTextureLayerWidget.layerChangedListener = object : TerrainTextureLayerWidget.LayerChangedListener {
            override fun layerChanged(terrainLayerAsset: TerrainLayerAsset) {
                // Assign the new layer asset to the terrain
                parentWidget.component.terrainAsset.terrainLayerAsset = terrainLayerAsset
                parentWidget.component.applyMaterial()
                metaSaver.save(parentWidget.component.terrainAsset.meta)
                projectManager.current().assetManager.addModifiedAsset(parentWidget.component.terrainAsset)
            }
        }

        terrainTextureLayerWidget.layerUpdatedListener = object : TerrainTextureLayerWidget.LayerUpdatedListener {
            override fun layerUpdated(terrainLayerAsset: TerrainLayerAsset) {
                parentWidget.component.terrainAsset.updateTerrainMaterial()
                parentWidget.component.applyMaterial()
                projectManager.current().assetManager.addModifiedAsset(parentWidget.component.terrainAsset)
            }
        }

    }

    override fun onShow() {
        super.onShow()

        // At tab open the first (base) texture will be selected
        TerrainBrush.setPaintChannel(SplatTexture.Channel.BASE)
        terrainTextureLayerWidget.textureGrid.highlightFirst()
    }

    override fun getTabTitle(): String {
        return "Paint"
    }

    override fun getContentTable(): Table {
        return root
    }
}
