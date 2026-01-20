package com.badlogic.gdx.graphics.g3d

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool

/**
 * Info : This interface was moved to Kerman game engine.
 *
 * Returns a list of [Renderable] instances to be rendered by a [ModelBatch].
 */
interface RenderableProvider {
    /**
     * Returns [Renderable] instances. Renderables are obtained from the provided [Pool] and added to the provided
     * array. The Renderables obtained using [Pool.obtain] will later be put back into the pool, do not store them
     * internally. The resulting array can be rendered via a [ModelBatch].
     *
     * @param renderables the output array.
     * @param pool the pool to obtain Renderables from.
     */
    fun getRenderables(renderables: Array<Renderable>, pool: Pool<Renderable>)
}
