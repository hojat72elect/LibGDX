package com.mbrlabs.mundus.editor.events

import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.editorcommons.Subscribe

class ComponentAddedEvent(val component: Component) {

    interface ComponentAddedListener {
        @Subscribe
        fun onComponentAdded(event: ComponentAddedEvent)
    }
}
