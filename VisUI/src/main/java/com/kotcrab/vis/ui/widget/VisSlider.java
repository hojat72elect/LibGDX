package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.kotcrab.vis.ui.VisUI;

/**
 * Compatible with {@link Slider}. Does not provide additional features.
 *
 * @see Slider
 */
public class VisSlider extends Slider {
    public VisSlider(float min, float max, float stepSize, boolean vertical) {
        super(min, max, stepSize, vertical, VisUI.getSkin());
    }

    public VisSlider(float min, float max, float stepSize, boolean vertical, String styleName) {
        super(min, max, stepSize, vertical, VisUI.getSkin(), styleName);
    }

    public VisSlider(float min, float max, float stepSize, boolean vertical, SliderStyle style) {
        super(min, max, stepSize, vertical, style);
    }
}
