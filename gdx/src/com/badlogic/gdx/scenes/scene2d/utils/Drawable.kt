package com.badlogic.gdx.scenes.scene2d.utils

import com.badlogic.gdx.graphics.g2d.Batch

/**
 * A drawable knows how to draw itself at a given rectangular size. It provides padding sizes and a minimum size so that other
 * code can determine how to size and position content.
 */
interface Drawable {

    /**
     * Draws this drawable at the specified bounds. The drawable should be tinted with [Batch.getColor], possibly by
     * mixing its own color.
     */
    fun draw(batch: Batch, x: Float, y: Float, width: Float, height: Float)

    fun getLeftWidth(): Float

    fun setLeftWidth(leftWidth: Float)

    fun getRightWidth(): Float

    fun setRightWidth(rightWidth: Float)

    fun getTopHeight(): Float

    fun setTopHeight(topHeight: Float)

    fun getBottomHeight(): Float

    fun setBottomHeight(bottomHeight: Float)

    fun setPadding(topHeight: Float, leftWidth: Float, bottomHeight: Float, rightWidth: Float) {
        setTopHeight(topHeight)
        setLeftWidth(leftWidth)
        setBottomHeight(bottomHeight)
        setRightWidth(rightWidth)
    }

    fun setPadding(padding: Float) {
        setPadding(padding, padding, padding, padding)
    }

    fun setPadding(from: Drawable) {
        setPadding(from.getTopHeight(), from.getLeftWidth(), from.getBottomHeight(), from.getRightWidth())
    }

    fun getMinWidth(): Float

    fun setMinWidth(minWidth: Float)

    fun getMinHeight(): Float

    fun setMinHeight(minHeight: Float)

    fun setMinSize(minWidth: Float, minHeight: Float) {
        setMinWidth(minWidth)
        setMinHeight(minHeight)
    }
}
