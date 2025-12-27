package com.mbrlabs.mundus.editor.ui.modules.dialogs.settings

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.JsonWriter
import com.kotcrab.vis.ui.widget.VisCheckBox
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisSelectBox
import com.kotcrab.vis.ui.widget.file.FileChooser
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.io.IOManager
import com.mbrlabs.mundus.editor.core.io.IOManagerProvider
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.ui.UI
import com.mbrlabs.mundus.editor.ui.widgets.FileChooserField
import com.mbrlabs.mundus.editorcommons.events.ProjectChangedEvent

class ExportSettingsTable : BaseSettingsTable(), ProjectChangedEvent.ProjectChangedListener {

    private val fileChooserField = FileChooserField(500)

    private val jsonType = VisSelectBox<JsonWriter.OutputType>()
    private val allAssets = VisCheckBox("Export unused assets [will be ignored for now]")
    private val compression = VisCheckBox("Compress scenes [will be ignored for now]")

    private val projectManager: ProjectManager = Mundus.inject()
    private val ioManager: IOManager = Mundus.inject<IOManagerProvider>().ioManager

    init {
        Mundus.registerEventListener(this)
        top().left()
        padRight(UI.PAD_SIDE).padLeft(UI.PAD_SIDE)

        jsonType.setItems(
            JsonWriter.OutputType.javascript,
            JsonWriter.OutputType.json,
            JsonWriter.OutputType.minimal
        )

        add(VisLabel("Export Settings")).left().row()
        addSeparator().padBottom(UI.PAD_SIDE * 2)

        add(VisLabel("Output folder")).growX().row()
        add(fileChooserField).growX().padBottom(UI.PAD_BOTTOM).row()
        add(VisLabel("Scene json format")).growX().row()
        add(jsonType).growX().padBottom(UI.PAD_BOTTOM).row()
        add(VisLabel("Flags")).growX().row()
        add(allAssets).left().row()
        add(compression).left().row()

        fileChooserField.setFileMode(FileChooser.SelectionMode.DIRECTORIES)
    }

    override fun onProjectChanged(event: ProjectChangedEvent) {
        updateValues()
    }

    private fun updateValues() {
        val exportSettings = projectManager.current().settings?.export
        if (exportSettings?.outputFolder != null) {
            fileChooserField.setText(exportSettings.outputFolder?.path())
        }
        allAssets.isChecked = exportSettings?.allAssets!!
        compression.isChecked = exportSettings.compressScenes
        jsonType.selected = exportSettings.jsonType
    }

    override fun onSave() {
        val exportSettings = projectManager.current().settings?.export ?: return
        exportSettings.allAssets = allAssets.isChecked
        exportSettings.compressScenes = compression.isChecked
        exportSettings.jsonType = jsonType.selected
        exportSettings.outputFolder = FileHandle(fileChooserField.path)

        ioManager.saveProjectContext(projectManager.current())
        UI.toaster.success("Settings saved")
    }

}