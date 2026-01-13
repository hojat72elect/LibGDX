package com.badlogic.gdx.scenes.scene2d.utils

import com.badlogic.gdx.graphics.g2d.Batch

/**
 * Info : This interface was moved to Kerman game engine.
 *
 * A drawable that supports scale and rotation.
 */
interface TransformDrawable : Drawable {
    fun draw(batch: Batch, x: Float, y: Float, originX: Float, originY: Float, width: Float, height: Float, scaleX: Float, scaleY: Float, rotation: Float)
}
