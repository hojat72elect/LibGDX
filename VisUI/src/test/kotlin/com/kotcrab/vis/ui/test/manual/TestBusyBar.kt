package com.kotcrab.vis.ui.test.manual

import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.BusyBar
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisWindow

class TestBusyBar : VisWindow("busybar") {

    init {
        TableUtils.setSpacingDefaults(this)
        columnDefaults(0).left()

        addCloseButton()
        addVisWidgets()

        setResizable(true)
        setSize(320F, 170F)
        centerWindow()
    }

    private fun addVisWidgets() {
        val busyBar = BusyBar()
        add(busyBar).top().space(0F).growX().row()
        add(VisLabel("Working...", Align.center)).grow().center()
    }
}