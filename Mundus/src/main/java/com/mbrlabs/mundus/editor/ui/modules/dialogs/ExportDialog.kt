package com.mbrlabs.mundus.editor.ui.modules.dialogs

import com.kotcrab.vis.ui.util.async.AsyncTaskListener
import com.kotcrab.vis.ui.widget.VisDialog
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisProgressBar
import com.mbrlabs.mundus.editor.Mundus
import com.mbrlabs.mundus.editor.core.io.IOManager
import com.mbrlabs.mundus.editor.core.io.IOManagerProvider
import com.mbrlabs.mundus.editor.core.plugin.PluginManagerProvider
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.exporter.Exporter
import com.mbrlabs.mundus.editor.ui.UI
import com.mbrlabs.mundus.editor.utils.Log
import com.mbrlabs.mundus.editorcommons.types.ToastType
import org.pf4j.PluginManager

class ExportDialog : VisDialog("Exporting") {

    private var lastExport: Long = 0

    private val label = VisLabel()
    private val progressBar = VisProgressBar(0f, 100f, 1f, false)

    private val projectManager: ProjectManager = Mundus.inject()
    private val ioManager: IOManager = Mundus.inject<IOManagerProvider>().ioManager
    private val pluginManager: PluginManager = Mundus.inject<PluginManagerProvider>().pluginManager

    init {
        isModal = true
        isMovable = false

        contentTable.add(label).padBottom(UI.PAD_BOTTOM).padTop(UI.PAD_BOTTOM_X2).left().growX().row()
        contentTable.add(progressBar).width(300f).left().growX().row()
    }

    fun export() {
        // validate
        val export = projectManager.current().settings?.export
        if (export == null || export.outputFolder == null
            || export.outputFolder.path().isEmpty() || !export.outputFolder.exists()
        ) {
            UI.toaster.error(
                "Export Error\nYou have to supply a output folder in the export settings." +
                        "\nWindow -> Settings -> Export Settings"
            )
            return
        }

        // prevent from exporting to fast which sometimes results in the export dialog not closing correctly
        if (System.currentTimeMillis() - lastExport < 1000f) {
            UI.toaster.error("Export pending")
            return
        }

        show(UI)

        Exporter(ioManager, projectManager.current(), pluginManager).exportAsync(export.outputFolder, object : AsyncTaskListener {
            private var error = false

            override fun progressChanged(newProgressPercent: Int) {
                progressBar.value = newProgressPercent.toFloat()
            }

            override fun finished() {
                if (!error) {
                    UI.toaster.success("Project exported")
                }
                resetValues()
                close()
                lastExport = System.currentTimeMillis()
            }

            override fun messageChanged(message: String?) {
                label.setText(message)
            }

            override fun failed(message: String?, exception: Exception?) {
                Log.exception("Exporter", exception)
                UI.toaster.sticky(ToastType.ERROR, "Export failed: " + exception.toString())
                error = true
                resetValues()
                close()
            }
        })
    }

    private fun resetValues() {
        progressBar.value = 0f
        label.setText("")
    }

    override fun close() {
        super.close()
    }

}
