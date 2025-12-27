package com.mbrlabs.mundus.editor.ui.modules.inspector.components

import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.events.ComponentRemovedEvent
import com.mbrlabs.mundus.editor.ui.modules.inspector.BaseInspectorWidget

abstract class ComponentWidget<T : Component>(title: String, var component: T) : BaseInspectorWidget(title) {

    init {
        isDeletable = true
    }

    override fun onDelete() {
        component.remove()
        remove()
        Mundus.postEvent(ComponentRemovedEvent())
    }

}
