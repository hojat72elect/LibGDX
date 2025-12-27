package com.mbrlabs.mundus.editor.ui.modules.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.MenuBar
import com.kotcrab.vis.ui.widget.VisImage
import com.kotcrab.vis.ui.widget.VisTable

class MundusMenuBar : MenuBar() {

    val fileMenu = FileMenu()
    val editMenu = EditMenu()
    val windowMenu = WindowMenu()
    val assetsMenu = AssetsMenu()
    val environmentMenu = EnvironmentMenu()
    val toolsMenu = ToolsMenu()
    val pluginsMenu = PluginsMenu()
    private val sceneMenu = SceneMenu()

    init {
        addMenu(fileMenu)
        addMenu(editMenu)
        addMenu(assetsMenu)
        addMenu(environmentMenu)
        addMenu(sceneMenu)
        addMenu(toolsMenu)
        addMenu(windowMenu)
        addMenu(pluginsMenu)
    }

    override fun getTable(): Table {
        val root = VisTable()
        root.setBackground("menu-bg")
        val menuTable = super.getTable()

        val icon = VisImage(Texture(Gdx.files.internal("ui/menu_icon.png")))
        root.add(icon).center().left().pad(5f).height(icon.height)
        root.add(menuTable).expand().fill().left().center().row()
        val sep = VisTable()
        sep.background = VisUI.getSkin().getDrawable("mundus-separator-green")
        root.add(sep).expandX().fillX().height(1f).colspan(2).row()

        return root
    }

}
