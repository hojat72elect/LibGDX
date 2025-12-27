package com.mbrlabs.mundus.editor.shader

import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Disposable
import com.mbrlabs.mundus.commons.shaders.DepthShader
import com.mbrlabs.mundus.commons.shaders.SkyboxShader
import com.mbrlabs.mundus.editor.tools.picker.PickerShader

object Shaders : Disposable {

    val wireframeShader: WireframeShader
    val skyboxShader: SkyboxShader
    val pickerShader: PickerShader
    val depthShader: DepthShader

    init {
        ShaderProgram.pedantic = false
        wireframeShader = WireframeShader()
        wireframeShader.init()
        skyboxShader = SkyboxShader()
        skyboxShader.init()
        pickerShader = PickerShader()
        pickerShader.init()
        depthShader = DepthShader()
        depthShader.init()
    }

    override fun dispose() {
        wireframeShader.dispose()
        skyboxShader.dispose()
        pickerShader.dispose()
        depthShader.dispose()
    }

}
