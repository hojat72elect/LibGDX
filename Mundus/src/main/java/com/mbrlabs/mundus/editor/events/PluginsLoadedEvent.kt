package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.editorcommons.Subscribe

class PluginsLoadedEvent {

    interface PluginsLoadedEventListener {

        @Subscribe
        fun onPluginsLoaded(event: PluginsLoadedEvent)
    }
}
