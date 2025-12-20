package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;

/**
 * Similar to {@link VisCheckBox} however uses round (instead of square) button {@link Drawable}. Note that if you
 * want to achieve 'select only one option' behaviour you need to use {@link ButtonGroup}.
 * <p>
 * When listening for button press {@link ChangeListener} should be always preferred (instead of {@link ClickListener}).
 * {@link ClickListener} does not support disabling button and will still report button presses.
 *
 * @see VisCheckBox
 */
public class VisRadioButton extends VisCheckBox {
    public VisRadioButton(String text) {
        this(text, VisUI.getSkin().get("radio", VisCheckBoxStyle.class));
    }

    public VisRadioButton(String text, VisCheckBoxStyle style) {
        super(text, style);
    }
}
