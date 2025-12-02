
package com.badlogic.gdx.scenes.scene2d.utils;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * A drawable knows how to draw itself at a given rectangular size. It provides padding sizes and a minimum size so that other
 * code can determine how to size and position content.
 *
 *  */
public interface Drawable {
    /**
     * Draws this drawable at the specified bounds. The drawable should be tinted with {@link Batch#getColor()}, possibly by
     * mixing its own color.
     */
    void draw(Batch batch, float x, float y, float width, float height);

    float getLeftWidth();

    void setLeftWidth(float leftWidth);

    float getRightWidth();

    void setRightWidth(float rightWidth);

    float getTopHeight();

    void setTopHeight(float topHeight);

    float getBottomHeight();

    void setBottomHeight(float bottomHeight);

    default void setPadding(float topHeight, float leftWidth, float bottomHeight, float rightWidth) {
        setTopHeight(topHeight);
        setLeftWidth(leftWidth);
        setBottomHeight(bottomHeight);
        setRightWidth(rightWidth);
    }

    default void setPadding(float padding) {
        setPadding(padding, padding, padding, padding);
    }

    default void setPadding(Drawable from) {
        setPadding(from.getTopHeight(), from.getLeftWidth(), from.getBottomHeight(), from.getRightWidth());
    }

    float getMinWidth();

    void setMinWidth(float minWidth);

    float getMinHeight();

    void setMinHeight(float minHeight);

    default void setMinSize(float minWidth, float minHeight) {
        setMinWidth(minWidth);
        setMinHeight(minHeight);
    }
}
