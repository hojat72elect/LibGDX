package com.kotcrab.vis.ui.util.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

/**
 * Value that returns given fixed constant value if widget is visible. If actor is invisible then returns 0.
 *
 *  @since 1.1.0
 */
public class ConstantIfVisibleValue extends Value {
    private Actor actor;
    private final float constant;

    public ConstantIfVisibleValue(float constant) {
        this.constant = constant;
    }

    public ConstantIfVisibleValue(Actor actor, float constant) {
        this.actor = actor;
        this.constant = constant;
    }

    @Override
    public float get(Actor context) {
        if (actor != null) context = actor;
        return context.isVisible() ? constant : 0;
    }
}
