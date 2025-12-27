package com.mbrlabs.mundus.editor.ui.modules.dialogs.settings

import com.kotcrab.vis.ui.util.FloatDigitsOnlyFilter
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextField
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.events.LogEvent
import com.mbrlabs.mundus.editor.events.LogType
import com.mbrlabs.mundus.editor.preferences.MundusPreferencesManager
import com.mbrlabs.mundus.editor.ui.UI

class AppearanceSettingsTable : BaseSettingsTable() {

    private val globalPreferencesManager: MundusPreferencesManager = Mundus.inject()

    private val selectionLineWidth = VisTextField("0")
    private val wireframeLineWidth = VisTextField("0")

    init {
        top().left()
        defaults().left()//.pad(4f)
        padRight(5f).padLeft(6f)

        selectionLineWidth.textFieldFilter = FloatDigitsOnlyFilter(false)
        wireframeLineWidth.textFieldFilter = FloatDigitsOnlyFilter(false)

        add(VisLabel("Appearance Settings")).left().row()
        addSeparator().padBottom(10f)

        add(VisLabel("Selection line width")).row()
        add(selectionLineWidth).padBottom(4f).row()

        add(VisLabel("Wireframe line width")).row()
        add(wireframeLineWidth).padBottom(4f).row()

        loadValues()
    }

    private fun loadValues() {
        selectionLineWidth.text = globalPreferencesManager.getFloat(MundusPreferencesManager.GLOB_LINE_WIDTH_SELECTION, MundusPreferencesManager.GLOB_LINE_WIDTH_DEFAULT_VALUE).toString()
        wireframeLineWidth.text = globalPreferencesManager.getFloat(MundusPreferencesManager.GLOB_LINE_WIDTH_WIREFRAME, MundusPreferencesManager.GLOB_LINE_WIDTH_DEFAULT_VALUE).toString()
    }

    override fun onSave() {
        try {
            globalPreferencesManager.set(MundusPreferencesManager.GLOB_LINE_WIDTH_SELECTION, selectionLineWidth.text.toFloat())
        } catch (ex: NumberFormatException) {
            Mundus.postEvent(LogEvent(LogType.ERROR, "Error parsing field " + selectionLineWidth.name))
        }

        try {
            globalPreferencesManager.set(MundusPreferencesManager.GLOB_LINE_WIDTH_WIREFRAME, wireframeLineWidth.text.toFloat())
        } catch (ex: NumberFormatException) {
            Mundus.postEvent(LogEvent(LogType.ERROR, "Error parsing field " + wireframeLineWidth.name))
        }

        UI.toaster.success("Settings saved")
    }

}