package com.mbrlabs.mundus.editor.utils

import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.util.ToastManager
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.toast.Toast
import com.mbrlabs.mundus.editorcommons.types.ToastType
import java.util.Locale

/**
 * Displays Android-like toasts at top right corner of the screen.
 */
class Toaster(stage: Stage) {

    private val toastManager: ToastManager = ToastManager(stage)

    /**
     * Amber background.
     */
    fun info(msg: String) {
        val table = newTable(msg)
        val toast = Toast(ToastType.INFO.name.lowercase(Locale.getDefault()), table)
        toastManager.show(toast, 3f)
        toastManager.toFront()
    }

    /**
     * Red background
     *
     * @param msg
     */
    fun error(msg: String) {
        val table = newTable(msg)
        val toast = Toast(ToastType.ERROR.name.lowercase(Locale.getDefault()), table)
        toastManager.show(toast, 5f)
        toastManager.toFront()
    }

    /**
     * Teal background.
     *
     * @param msg
     */
    fun success(msg: String) {
        val table = newTable(msg)
        val toast = Toast(ToastType.SUCCESS.name.lowercase(Locale.getDefault()), table)
        toastManager.show(toast, 3f)
        toastManager.toFront()
    }

    /**
     * Does not close itself. User has to
     *
     * @param type
     * @param msg
     */
    fun sticky(type: ToastType, msg: String) {
        val table = newTable(msg)
        var toast: Toast? = null
        if (type == ToastType.SUCCESS) {
            toast = Toast(ToastType.SUCCESS.name.lowercase(Locale.getDefault()), table)
        } else if (type == ToastType.INFO) {
            toast = Toast(ToastType.INFO.name.lowercase(Locale.getDefault()), table)
        } else if (type == ToastType.ERROR) {
            toast = Toast(ToastType.ERROR.name.lowercase(Locale.getDefault()), table)
        }

        toastManager.show(toast)
        toastManager.toFront()
    }

    private fun newTable(text: String): VisTable {
        val table = VisTable()
        table.add(VisLabel(text))
        table.pad(5f)
        return table
    }

}
