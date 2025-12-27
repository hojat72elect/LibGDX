package com.mbrlabs.mundus.editor.utils

import com.badlogic.gdx.Input
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.registry.KeyboardLayout
import com.mbrlabs.mundus.editor.core.registry.Registry

object KeyboardLayoutUtils {

    fun convertKeycode(code: Int): Int {
        if (Mundus.inject<Registry>().settings.keyboardLayout == KeyboardLayout.QWERTZ) {
            if (code == Input.Keys.Z) {
                return Input.Keys.Y
            } else if (code == Input.Keys.Y) {
                return Input.Keys.Z
            }
        }

        // return default QWERTY
        return code
    }
}
