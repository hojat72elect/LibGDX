package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.commons.assets.Asset
import com.mbrlabs.mundus.editorcommons.Subscribe

class AssetImportEvent(val asset: Asset) {

    interface AssetImportListener {
        @Subscribe
        fun onAssetImported(event: AssetImportEvent)
    }

}
