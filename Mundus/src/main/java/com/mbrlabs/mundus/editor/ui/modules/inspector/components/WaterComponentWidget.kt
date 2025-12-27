package com.mbrlabs.mundus.editor.ui.modules.inspector.components

import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.mbrlabs.mundus.commons.scene3d.GameObject
import com.mbrlabs.mundus.commons.scene3d.components.Component
import com.mbrlabs.mundus.commons.scene3d.components.WaterComponent
import com.mbrlabs.mundus.editor.ui.widgets.WaterWidget

class WaterComponentWidget(waterComponent: WaterComponent) :
    ComponentWidget<WaterComponent>("Water Component", waterComponent) {

    private val settingsContainer = VisTable()

    init {
        val label = VisLabel()
        label.wrap = true
        label.setText("NOTE: All water instances must have the same height (Y value) for proper reflections.")
        collapsibleContent.add(label).grow().padBottom(10f).row()

        settingsContainer.add(WaterWidget(waterComponent)).grow()
        collapsibleContent.add(settingsContainer).left().grow().row()
    }

    override fun setValues(go: GameObject) {
        val c: WaterComponent? = go.findComponentByType(Component.Type.WATER)
        if (c != null) {
            this.component = c
        }
    }

}
