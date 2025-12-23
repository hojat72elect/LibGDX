package com.badlogic.gdx.ai.tests.utils.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A label showing current FPS prefixed by a constant string.
 */
public class FpsLabel extends IntValueLabel {

    public FpsLabel(CharSequence text, Skin skin) {
        this(text, skin.get(LabelStyle.class));
    }

    public FpsLabel(CharSequence text, Skin skin, String styleName) {
        this(text, skin.get(styleName, LabelStyle.class));
    }

    public FpsLabel(CharSequence text, Skin skin, String fontName, Color color) {
        this(text, new LabelStyle(skin.getFont(fontName), color));
    }

    public FpsLabel(CharSequence text, Skin skin, String fontName, String colorName) {
        this(text, new LabelStyle(skin.getFont(fontName), skin.getColor(colorName)));
    }

    public FpsLabel(CharSequence text, LabelStyle style) {
        super(text, -1, style);
    }

    @Override
    public int getValue() {
        return Gdx.graphics.getFramesPerSecond();
    }
}
