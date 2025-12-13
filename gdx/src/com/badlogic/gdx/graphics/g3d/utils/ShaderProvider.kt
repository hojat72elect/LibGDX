package com.badlogic.gdx.graphics.g3d.utils

import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.utils.Disposable

/**
 * Returns [Shader] instances for a [Renderable] on request. Also responsible for disposing of any created
 * [Shader] instances on a call to [Disposable.dispose].
 *
 */
interface ShaderProvider : Disposable {
    /**
     * Returns a [Shader] for the given [Renderable]. The RenderInstance may already contain a Shader, in which case
     * the provider may decide to return that.
     *
     * @param renderable the Renderable.
     * @return the Shader to be used for the RenderInstance.
     */
    fun getShader(renderable: Renderable): Shader
}
