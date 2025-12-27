package com.mbrlabs.mundus.editor.ui.modules.dock

import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.tabbedpane.Tab
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneListener
import com.mbrlabs.mundus.editor.ui.modules.dock.assets.AssetsDock
import com.mbrlabs.mundus.editor.ui.widgets.MundusSplitPane

class DockBar(private val splitPane: MundusSplitPane) : VisTable(), TabbedPaneListener {

    val assetsDock = AssetsDock()
    private val logBar = LogBar()
    private val profilingBar = ProfilingBar()
    private val tabbedPane: TabbedPane


    init {
        val style = TabbedPane.TabbedPaneStyle(
            VisUI.getSkin().get(TabbedPane.TabbedPaneStyle::class.java)
        )
        style.buttonStyle = VisTextButton.VisTextButtonStyle(
            VisUI.getSkin().get("toggle", VisTextButton.VisTextButtonStyle::class.java)
        )

        tabbedPane = TabbedPane(style)
        tabbedPane.isAllowTabDeselect = true
        tabbedPane.addListener(this)

        tabbedPane.add(assetsDock)
        tabbedPane.add(logBar)
        tabbedPane.add(profilingBar)
        add(tabbedPane.table).expandX().fillX().left().bottom().height(30f).row()

        // Keeping asset tab the default active tab
        tabbedPane.switchTab(assetsDock)
    }

    override fun switchedTab(tab: Tab?) {
        if (tab != null) {
            splitPane.setSecondWidget(tab.contentTable)
            splitPane.setSplitAmount(0.8f)
        } else {
            splitPane.setSecondWidget(null)
            splitPane.setSplitAmount(1f)
        }
        splitPane.invalidate()
    }

    override fun removedTab(tab: Tab) {
        // user can't do that
    }

    override fun removedAllTabs() {
        // user can't do that
    }

    fun update() {
        tabbedPane.updateTabTitle(logBar)
        profilingBar.update()
    }

}
