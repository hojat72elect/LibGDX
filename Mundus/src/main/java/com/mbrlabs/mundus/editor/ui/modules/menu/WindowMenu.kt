package com.mbrlabs.mundus.editor.ui.modules.menu

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuItem
import com.mbrlabs.mundus.editor.ui.UI

class WindowMenu : Menu("Window") {

    val settings = MenuItem("Settings")
    private val versionInfo = MenuItem("Version Info")
    private val keyboardShortcuts = MenuItem("Keyboard Shortcuts")

    init {
        settings.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.ALT_LEFT, Input.Keys.S)
        addItem(settings)
        addItem(keyboardShortcuts)
        addItem(versionInfo)

        settings.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.settingsDialog)
            }
        })

        versionInfo.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.versionDialog)
            }
        })

        keyboardShortcuts.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.keyboardShortcuts)
            }
        })
    }

}
