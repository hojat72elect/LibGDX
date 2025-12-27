package com.mbrlabs.mundus.editor.tools

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Disposable
import com.mbrlabs.mundus.editor.core.project.ProjectManager
import com.mbrlabs.mundus.editor.history.CommandHistory
import com.mbrlabs.mundus.editor.shader.Shaders

abstract class Tool(
    var projectManager: ProjectManager,
    protected var history: CommandHistory
) : InputAdapter(), Disposable {

    protected var shader: Shader = Shaders.wireframeShader

    abstract val name: String
    abstract val icon: Drawable
    abstract val iconFont: String
    val environment: Environment = Environment()

    abstract fun render()
    abstract fun act()
    abstract fun onActivated()
    abstract fun onDisabled()

}
