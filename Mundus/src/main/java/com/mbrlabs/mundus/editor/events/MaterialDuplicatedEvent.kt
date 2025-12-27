package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.editorcommons.Subscribe

class MaterialDuplicatedEvent {
    interface MaterialDuplicatedEventListener {
        @Subscribe
        fun onMaterialDuplicated(event: MaterialDuplicatedEvent)
    }
}
