package com.mbrlabs.mundus.editor.ui.modules.dialogs

import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.VisDialog

/**
 * Base dialog to extend from. Tracks if it is currently open or not.
 */
open class BaseDialog(title: String) : VisDialog(title) {
    protected var dialogOpen = false

    override fun show(stage: Stage?): VisDialog {
        dialogOpen = true
        return super.show(stage)
    }

    override fun close() {
        super.close()
        dialogOpen = false
    }

    init {
        addCloseButton()
    }

}
