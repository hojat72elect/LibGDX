package com.kotcrab.vis.ui.util.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

/**
 * Allows to use libGDX {@link Value} with lambdas. Using this on Java lower than 1.8 is pointless because lambadas are
 * not supported.
 *
 * @see VisWidgetValue
 * @since 0.9.3
 */
public class VisValue extends Value {
    private final ValueGetter getter;

    public VisValue(ValueGetter getter) {
        this.getter = getter;
    }

    @Override
    public float get(Actor context) {
        return getter.get(context);
    }

    public interface ValueGetter {
        float get(Actor context);
    }
}
