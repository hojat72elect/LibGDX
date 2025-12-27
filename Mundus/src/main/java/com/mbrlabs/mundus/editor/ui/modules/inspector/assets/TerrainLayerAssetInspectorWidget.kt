package com.mbrlabs.mundus.editor.ui.modules.inspector.assets

import com.mbrlabs.mundus.commons.assets.TerrainLayerAsset
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.editor.ui.modules.inspector.BaseInspectorWidget
import com.mbrlabs.mundus.editor.ui.widgets.TerrainTextureLayerWidget

class TerrainLayerAssetInspectorWidget() : BaseInspectorWidget(TITLE) {
    companion object {
        private val TITLE = "Terrain Layer Asset"
    }

    private var terrainTextureLayerWidget: TerrainTextureLayerWidget? = null
    private var layerAsset: TerrainLayerAsset? = null

    init {
        isDeletable = false
    }

    fun setTerrainLayerAsset(layerAsset: TerrainLayerAsset) {
        this.layerAsset = layerAsset
        if (terrainTextureLayerWidget == null) {
            terrainTextureLayerWidget = TerrainTextureLayerWidget(layerAsset, false)
            collapsibleContent.add(terrainTextureLayerWidget).growX().row()
        } else {
            terrainTextureLayerWidget!!.setTerrainLayerAsset(layerAsset)
        }
    }

    override fun onDelete() {

    }

    override fun setValues(go: GameObject) {

    }

}