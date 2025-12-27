package com.mbrlabs.mundus.editor.ui.modules.menu

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuItem
import com.mbrlabs.mundus.editor.ui.UI

class EnvironmentMenu : Menu("Environment") {

    val ambientLight = MenuItem("Ambient Light")
    val directionalLight = MenuItem("Directional Light")
    val skybox = MenuItem("Skybox")
    val fog = MenuItem("Fog")

    init {
        addItem(ambientLight)
        addItem(directionalLight)
        addItem(skybox)
        addItem(fog)

        setupListeners()
    }

    private fun setupListeners() {
        // ambient light
        ambientLight.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.ambientLightDialog)
            }
        })

        // directional light
        directionalLight.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.directionalLightDialog)
            }
        })

        // fog
        fog.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.fogDialog)
            }
        })

        // skybox
        skybox.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                UI.showDialog(UI.skyboxDialog)
            }
        })

    }

}
