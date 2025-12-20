package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;

/**
 * A separator widget (horizontal or vertical bar) that can be used in menus, tables or other widgets, typically added
 * to new row with growX() (if creating horizontal separator) OR growY() (if creating vertical separator)
 * {@link PopupMenu} and {@link VisTable} provides utilities addSeparator() methods that adds new separator.
 *
 * @since 0.1.0
 */
public class Separator extends Widget {
    private final SeparatorStyle style;

    /**
     * New separator with default style
     */
    public Separator() {
        style = VisUI.getSkin().get(SeparatorStyle.class);
    }

    public Separator(String styleName) {
        style = VisUI.getSkin().get(styleName, SeparatorStyle.class);
    }

    public Separator(SeparatorStyle style) {
        this.style = style;
    }

    @Override
    public float getPrefHeight() {
        return style.thickness;
    }

    @Override
    public float getPrefWidth() {
        return style.thickness;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
        style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
    }

    public SeparatorStyle getStyle() {
        return style;
    }

    static public class SeparatorStyle {
        public Drawable background;
        public int thickness;

        public SeparatorStyle() {
        }

        public SeparatorStyle(SeparatorStyle style) {
            this.background = style.background;
            this.thickness = style.thickness;
        }

        public SeparatorStyle(Drawable bg, int thickness) {
            this.background = bg;
            this.thickness = thickness;
        }
    }
}
