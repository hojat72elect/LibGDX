package com.mbrlabs.mundus.editor.plugin

import com.mbrlabs.mundus.editor.ui.UI
import com.mbrlabs.mundus.editorcommons.types.ToastType
import com.mbrlabs.mundus.pluginapi.manager.ToasterManager

class ToasterManagerImpl : ToasterManager {

    override fun info(text: String) {
        UI.toaster.info(text)
    }

    override fun error(text: String) {
        UI.toaster.error(text)
    }

    override fun success(text: String) {
        UI.toaster.success(text)
    }

    override fun sticky(type: ToastType, text: String) {
        UI.toaster.sticky(type, text)
    }
}
