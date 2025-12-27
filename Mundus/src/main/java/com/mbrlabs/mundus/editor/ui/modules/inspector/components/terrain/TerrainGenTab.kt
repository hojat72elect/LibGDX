package com.mbrlabs.mundus.editor.ui.modules.inspector.components.terrain

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.tabbedpane.Tab
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneListener
import com.mbrlabs.mundus.editor.ui.modules.inspector.components.terrain.generation.HeightmapTab
import com.mbrlabs.mundus.editor.ui.modules.inspector.components.terrain.generation.PerlinNoiseTab

/**
 * @Deprecated
 */
class TerrainGenTab(parent: TerrainComponentWidget) : Tab(false, false), TabbedPaneListener {
    private val root = VisTable()

    private val tabbedPane = TabbedPane()
    private val tabContainer = VisTable()

    private val heightmapTab = HeightmapTab(parent.component)
    private val perlinNoiseTab = PerlinNoiseTab(parent.component)

    init {
        tabbedPane.addListener(this)

        tabbedPane.add(heightmapTab)
        tabbedPane.add(perlinNoiseTab)

        root.add(tabbedPane.table).growX().row()
        root.add(tabContainer).expand().fill().row()
        tabbedPane.switchTab(0)
    }


    override fun getTabTitle(): String = "Gen"

    override fun getContentTable(): Table = root

    override fun switchedTab(tab: Tab) {
        tabContainer.clearChildren()
        tabContainer.add(tab.contentTable).expand().fill()
    }

    override fun removedTab(tab: Tab) {
        // no
    }

    override fun removedAllTabs() {
        // noop
    }

}
