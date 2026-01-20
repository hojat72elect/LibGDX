package com.badlogic.gdx.graphics.g3d.particles.batches

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g3d.RenderableProvider
import com.badlogic.gdx.graphics.g3d.particles.ResourceData
import com.badlogic.gdx.graphics.g3d.particles.ResourceData.Configurable
import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderData

/**
 * Info : This interface was moved to Kerman game engine.
 *
 * Common interface to all the batches that render particles.
 */
interface ParticleBatch<T : ParticleControllerRenderData> : RenderableProvider, Configurable<Any> {

    /**
     * Must be called once before any drawing operation
     */
    fun begin()

    fun draw(controller: T)

    /**
     * Must be called after all the drawing operations
     */
    fun end()

    override fun save(manager: AssetManager, resources: ResourceData<Any>)

    override fun load(manager: AssetManager, resources: ResourceData<Any>)
}
