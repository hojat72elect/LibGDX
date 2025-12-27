package com.mbrlabs.mundus.editor.ui.modules.inspector.assets

import com.kotcrab.vis.ui.widget.VisLabel
import com.mbrlabs.mundus.commons.assets.TextureAsset
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.editor.ui.modules.inspector.BaseInspectorWidget

import org.apache.commons.io.FileUtils

class TextureAssetInspectorWidget : BaseInspectorWidget(TITLE) {

    companion object {
        private val TITLE = "Texture Asset"
    }

    private val name = VisLabel()
    private val width = VisLabel()
    private val height = VisLabel()
    private val fileSize = VisLabel()

    private var textureAsset: TextureAsset? = null

    init {
        collapsibleContent.add(name).growX().row()
        collapsibleContent.add(width).growX().row()
        collapsibleContent.add(height).growX().row()
    }

    fun setTextureAsset(texture: TextureAsset) {
        this.textureAsset = texture
        updateUI()
    }

    private fun updateUI() {
        name.setText("Name: " + textureAsset?.name)
        width.setText("Width: " + textureAsset?.texture?.width + " px")
        height.setText("Height: " + textureAsset?.texture?.height + " px")

        val mb = FileUtils.sizeOf(textureAsset?.file?.file()) / 1000000f
        fileSize.setText("Size: $mb mb")
    }

    override fun onDelete() {
        // nope
    }

    override fun setValues(go: GameObject) {
        // nope
    }

}
