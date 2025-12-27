package com.mbrlabs.mundus.editor.ui.modules.inspector.components.terrain

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.mbrlabs.mundus.editor.tools.brushes.TerrainBrush

class TerrainFlattenTab(parent: TerrainComponentWidget) : BaseBrushTab(parent, TerrainBrush.BrushMode.FLATTEN) {

    private val table = VisTable()

    init {
        table.align(Align.left)
        table.add(VisLabel("Hold shift to sample a height")).center().row()

        table.add(terrainBrushGrid).expand().fill().row()
    }

    override fun getTabTitle(): String {
        return "Flatten"
    }

    override fun getContentTable(): Table {
        return table
    }

}
