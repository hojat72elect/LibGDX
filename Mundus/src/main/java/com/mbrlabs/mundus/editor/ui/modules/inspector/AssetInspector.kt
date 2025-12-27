package com.mbrlabs.mundus.editor.ui.modules.inspector

import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisTable
import com.mbrlabs.mundus.commons.assets.Asset
import com.mbrlabs.mundus.commons.assets.MaterialAsset
import com.mbrlabs.mundus.commons.assets.ModelAsset
import com.mbrlabs.mundus.commons.assets.TerrainAsset
import com.mbrlabs.mundus.commons.assets.TerrainLayerAsset
import com.mbrlabs.mundus.commons.assets.TextureAsset
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.ui.modules.inspector.assets.MaterialAssetInspectorWidget
import com.mbrlabs.mundus.editor.ui.modules.inspector.assets.ModelAssetInspectorWidget
import com.mbrlabs.mundus.editor.ui.modules.inspector.assets.TerrainAssetInspectorWidget
import com.mbrlabs.mundus.editor.ui.modules.inspector.assets.TerrainLayerAssetInspectorWidget
import com.mbrlabs.mundus.editor.ui.modules.inspector.assets.TextureAssetInspectorWidget
import com.mbrlabs.mundus.editor.ui.modules.inspector.assets.UsedByAssetInspectorWidget

class AssetInspector : VisTable() {

    private val materialWidget = MaterialAssetInspectorWidget()
    private val modelWidget = ModelAssetInspectorWidget()
    private val textureWidget = TextureAssetInspectorWidget()
    private val terrainWidget = TerrainAssetInspectorWidget()
    private val terrainLayerWidget = TerrainLayerAssetInspectorWidget()
    private val usedByWidget = UsedByAssetInspectorWidget()
    private var projectManager: ProjectManager

    var asset: Asset? = null
        set(value) {
            field = value
            clear()

            if (value is MaterialAsset) {
                add(materialWidget).growX().row()
                materialWidget.setMaterial(value)
            } else if (value is ModelAsset) {
                add(modelWidget).growX().row()
                modelWidget.setModel(value)
            } else if (value is TextureAsset) {
                add(textureWidget).growX().row()
                textureWidget.setTextureAsset(value)
            } else if (value is TerrainAsset) {
                add(terrainWidget).growX().row()
                terrainWidget.setTerrainAsset(value)
            } else if (value is TerrainLayerAsset) {
                add(terrainLayerWidget).growX().row()
                terrainLayerWidget.setTerrainLayerAsset(value)
            }

            add(usedByWidget).padTop(10f).growX().row()
            usedByWidget.setAsset(asset, projectManager)
        }

    init {
        align(Align.top)
        pad(7f)
        projectManager = Mundus.inject()
    }

}
