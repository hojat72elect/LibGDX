package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.editor.core.registry.Settings
import com.mbrlabs.mundus.editorcommons.Subscribe

class SettingsChangedEvent(val settings: Settings) {

    interface SettingsChangedListener {
        @Subscribe
        fun onSettingsChanged(event: SettingsChangedEvent)
    }

}
