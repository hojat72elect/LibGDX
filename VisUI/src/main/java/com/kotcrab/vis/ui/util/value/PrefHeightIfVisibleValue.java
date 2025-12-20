package com.kotcrab.vis.ui.util.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Value that returns widget preferred height if it's visible. If widget is invisible then returns 0.
 * This can be only added to classes extending {@link Widget} or {@link Table}, if you try to add it to any other class
 * you will get {@link IllegalStateException} during runtime.
 *
 *  @since 0.9.3
 */
public class PrefHeightIfVisibleValue extends Value {
    public static final PrefHeightIfVisibleValue INSTANCE = new PrefHeightIfVisibleValue();

    @Override
    public float get(Actor actor) {
        if (actor instanceof Widget widget) {
            return widget.isVisible() ? widget.getPrefHeight() : 0;
        }

        if (actor instanceof Table table) {
            return table.isVisible() ? table.getPrefHeight() : 0;
        }

        throw new IllegalStateException("Unsupported actor type for PrefHeightIfVisibleValue: " + actor.getClass());
    }
}
