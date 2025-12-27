package com.mbrlabs.mundus.editor.ui.modules.dialogs.settings

import com.kotcrab.vis.ui.widget.VisTable

abstract class BaseSettingsTable : VisTable() {

    init {
        top().left()
        padRight(5f).padLeft(6f)
    }

    open fun onInit() {
        // NOOP
    }

    abstract fun onSave()

}