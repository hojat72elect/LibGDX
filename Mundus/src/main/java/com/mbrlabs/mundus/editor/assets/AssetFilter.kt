package com.mbrlabs.mundus.editor.assets

import com.mbrlabs.mundus.commons.assets.Asset
import com.mbrlabs.mundus.commons.assets.MaterialAsset
import com.mbrlabs.mundus.commons.assets.TerrainLayerAsset
import com.mbrlabs.mundus.commons.assets.TextureAsset

interface AssetFilter {
    fun ignore(asset: Asset): Boolean
}

class AssetMaterialFilter : AssetFilter {
    override fun ignore(asset: Asset): Boolean {
        return asset !is MaterialAsset
    }
}

class AssetTextureFilter : AssetFilter {
    override fun ignore(asset: Asset): Boolean {
        return asset !is TextureAsset
    }
}


class AssetTerrainLayerFilter : AssetFilter {
    override fun ignore(asset: Asset): Boolean {
        return asset !is TerrainLayerAsset
    }
}