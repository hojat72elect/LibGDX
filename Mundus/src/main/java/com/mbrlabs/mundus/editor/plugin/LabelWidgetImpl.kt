package com.mbrlabs.mundus.editor.plugin

import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisLabel
import com.mbrlabs.mundus.pluginapi.ui.LabelWidget
import com.mbrlabs.mundus.pluginapi.ui.WidgetAlign

class LabelWidgetImpl(val label: VisLabel) : LabelWidget {
    override fun setAlign(align: WidgetAlign): LabelWidget {
        when (align) {
            WidgetAlign.LEFT -> label.setAlignment(Align.left)
            WidgetAlign.CENTER -> label.setAlignment(Align.center)
            WidgetAlign.RIGHT -> label.setAlignment(Align.right)
        }

        return this
    }

    override fun setText(text: String): LabelWidget {
        label.setText(text)

        return this
    }
}
