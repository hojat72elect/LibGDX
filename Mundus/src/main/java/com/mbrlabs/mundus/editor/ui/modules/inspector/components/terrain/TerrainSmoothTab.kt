package com.mbrlabs.mundus.editor.ui.modules.inspector.components.terrain

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisTable
import com.mbrlabs.mundus.editor.tools.brushes.TerrainBrush

class TerrainSmoothTab(parent: TerrainComponentWidget) : BaseBrushTab(parent, TerrainBrush.BrushMode.SMOOTH) {

    private val table = VisTable()

    init {
        table.align(Align.left)

        table.add(terrainBrushGrid).expand().fill().row()
    }

    override fun getTabTitle(): String {
        return "Smooth"
    }

    override fun getContentTable(): Table {
        return table
    }

}