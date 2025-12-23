package com.badlogic.gdx.ai.tests.utils.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A label continuously showing the most up-to-date float value prefixed by a constant string.
 */
public abstract class FloatValueLabel extends Label {

    float oldValue;
    int appendIndex;

    public FloatValueLabel(CharSequence text, float initialValue, Skin skin) {
        this(text, initialValue, skin.get(LabelStyle.class));
    }

    public FloatValueLabel(CharSequence text, float initialValue, Skin skin, String styleName) {
        this(text, initialValue, skin.get(styleName, LabelStyle.class));
    }

    public FloatValueLabel(CharSequence text, float initialValue, Skin skin, String fontName, Color color) {
        this(text, initialValue, new LabelStyle(skin.getFont(fontName), color));
    }

    public FloatValueLabel(CharSequence text, float initialValue, Skin skin, String fontName, String colorName) {
        this(text, initialValue, new LabelStyle(skin.getFont(fontName), skin.getColor(colorName)));
    }

    public FloatValueLabel(CharSequence text, float initialValue, LabelStyle style) {
        super(text.toString() + initialValue, style);
        this.oldValue = initialValue;
        this.appendIndex = text.length();
    }

    public abstract float getValue();

    @Override
    public void act(float delta) {
        float newValue = getValue();
        if (oldValue != newValue) {
            oldValue = newValue;
            StringBuilder sb = new StringBuilder(getText());
            sb.setLength(appendIndex);
            sb.append(oldValue);
            invalidateHierarchy();
        }
        super.act(delta);
    }
}
