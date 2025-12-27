package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.editorcommons.Subscribe

class AssetDeletedEvent {

    interface AssetDeletedListener {
        @Subscribe
        fun onAssetDeleted(event: AssetDeletedEvent)
    }

}