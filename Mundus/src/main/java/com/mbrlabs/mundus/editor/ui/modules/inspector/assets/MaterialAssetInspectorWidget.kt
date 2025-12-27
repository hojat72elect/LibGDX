package com.mbrlabs.mundus.editor.ui.modules.inspector.assets

import com.mbrlabs.mundus.commons.assets.MaterialAsset
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.editor.ui.modules.inspector.BaseInspectorWidget
import com.mbrlabs.mundus.editor.ui.widgets.MaterialWidget

class MaterialAssetInspectorWidget : BaseInspectorWidget(TITLE) {

    companion object {
        private val TITLE = "Material Asset"
    }

    private var material: MaterialAsset? = null
    private val materialWidget = MaterialWidget()

    init {
        isDeletable = false
        collapsibleContent.add(materialWidget).grow().row()

        materialWidget.material = material
        materialWidget.setupWidgets()
    }

    fun setMaterial(material: MaterialAsset) {
        this.material = material
        materialWidget.material = this.material
    }

    override fun onDelete() {
        // can't be deleted
    }

    override fun setValues(go: GameObject) {
        // nope
    }

}
