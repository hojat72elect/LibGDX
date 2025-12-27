package com.mbrlabs.mundus.editor.plugin

import com.kotcrab.vis.ui.widget.VisLabel
import com.mbrlabs.mundus.pluginapi.ui.Label

class LabelImpl(text: String) : VisLabel(text), Label {
    override fun setText(text: String) {
        setText(text as CharSequence)
    }
}
