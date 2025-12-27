@file:JvmName("SkyboxBuilder")

package com.mbrlabs.mundus.editor.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.Shader
import com.mbrlabs.mundus.commons.skybox.Skybox

fun createDefaultSkybox(shader: Shader): Skybox {
    val texture = Gdx.files.internal("textures/skybox/default/skybox_default.png")
    return Skybox(
        texture, texture, texture, texture, texture, texture,
        shader
    )
}

fun createNightSkybox(shader: Shader): Skybox {
    val texture = Gdx.files.internal("textures/skybox/star_night.png")
    return Skybox(
        texture, texture, texture, texture, texture, texture,
        shader
    )
}


