package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.editorcommons.Subscribe

class ComponentRemovedEvent {
    interface ComponentRemovedListener {
        @Subscribe
        fun onComponentRemoved(event: ComponentRemovedEvent)
    }
}