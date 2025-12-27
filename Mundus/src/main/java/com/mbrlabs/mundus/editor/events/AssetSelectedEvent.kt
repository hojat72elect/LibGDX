package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.commons.assets.Asset
import com.mbrlabs.mundus.editorcommons.Subscribe

class AssetSelectedEvent(val asset: Asset) {

    interface AssetSelectedListener {
        @Subscribe
        fun onAssetSelected(event: AssetSelectedEvent)
    }
}
