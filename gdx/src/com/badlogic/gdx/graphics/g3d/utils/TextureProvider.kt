package com.badlogic.gdx.graphics.g3d.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.Texture.TextureWrap
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.model.data.ModelData

/**
 * Used by [Model] to load textures from [ModelData].
 */
interface TextureProvider {

    fun load(fileName: String): Texture

    class FileTextureProvider @JvmOverloads constructor(
        private val minFilter: TextureFilter = TextureFilter.Linear,
        private val magFilter: TextureFilter = TextureFilter.Linear,
        private val uWrap: TextureWrap = TextureWrap.Repeat,
        private val vWrap: TextureWrap = TextureWrap.Repeat,
        private val useMipMaps: Boolean = false
    ) : TextureProvider {

        override fun load(fileName: String): Texture {
            val result = Texture(Gdx.files.internal(fileName), useMipMaps)
            result.setFilter(minFilter, magFilter)
            result.setWrap(uWrap, vWrap)
            return result
        }
    }

    class AssetTextureProvider(val assetManager: AssetManager) : TextureProvider {
        override fun load(fileName: String): Texture = assetManager.get(fileName, Texture::class.java)
    }
}
