package com.kotcrab.vis.ui.util.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Allows to use libGDX {@link Value} with lambdas for scene2d.ui widgets. Note that this cannot be added to actors,
 * only widgets are supported, if you try to do so you will get {@link ClassCastException} when this Value has been invoked.
 * Using this on Java lower than 1.8 is pointless because lambadas are not supported.
 *
 * @see VisValue
 * @see PrefHeightIfVisibleValue
 * @since 0.9.3
 */
public class VisWidgetValue extends Value {
    protected WidgetValueGetter getter;

    public VisWidgetValue(WidgetValueGetter getter) {
        this.getter = getter;
    }

    @Override
    public float get(Actor context) {
        return getter.get((Widget) context);
    }

    public interface WidgetValueGetter {
        float get(Widget context);
    }
}
