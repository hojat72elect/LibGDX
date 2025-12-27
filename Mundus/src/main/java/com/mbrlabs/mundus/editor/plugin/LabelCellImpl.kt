package com.mbrlabs.mundus.editor.plugin

import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.mbrlabs.mundus.pluginapi.ui.Label
import com.mbrlabs.mundus.pluginapi.ui.LabelCell

class LabelCellImpl(private val labelCell: Cell<LabelImpl>) : CellImpl(labelCell), LabelCell {
    override fun getLabel(): Label = labelCell.actor
}
