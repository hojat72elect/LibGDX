package com.badlogic.gdx.graphics.g3d.environment

import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor
import com.badlogic.gdx.math.Matrix4

/**
 * Info : This interface was moved to Kerman game engine.
 */
interface ShadowMap {
    fun getProjViewTrans(): Matrix4
    fun getDepthMap(): TextureDescriptor<*>
}
