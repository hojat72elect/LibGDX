package com.mbrlabs.mundus.editor.assets

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.mbrlabs.mundus.commons.assets.ModelAsset
import com.mbrlabs.mundus.commons.assets.meta.Meta
import com.mbrlabs.mundus.commons.utils.TextureProvider

class EditorModelAsset(meta: Meta, assetFile: FileHandle) : ModelAsset(meta, assetFile), TextureProvider {

    lateinit var thumbnail: Texture
    override fun getTexture(): Texture = thumbnail

    override fun dispose() {
        super.dispose()
        thumbnail.dispose()
    }

}
