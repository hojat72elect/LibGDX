package com.mbrlabs.mundus.editor.ui.modules.inspector.components.terrain

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.mbrlabs.mundus.editor.tools.brushes.TerrainBrush

class TerrainUpDownTab(private val parent: TerrainComponentWidget) : BaseBrushTab(parent, TerrainBrush.BrushMode.RAISE_LOWER) {
    private val table = VisTable()

    init {
        table.align(Align.left)
        table.add(VisLabel("Hold shift to lower")).center().row()
        table.add(terrainBrushGrid).expandX().fillX().row()
    }

    override fun getTabTitle(): String {
        return "Up/Down"
    }

    override fun getContentTable(): Table {
        return table
    }

}
