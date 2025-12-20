package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.Focusable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.BorderOwner;

/**
 * Extends functionality of standard {@link TextButton}, supports focus border. Compatible with standard {@link TextButton}.
 * <p>
 * When listening for button press {@link ChangeListener} should be always preferred (instead of {@link ClickListener}).
 * {@link ClickListener} does not support disabling button and will still report button presses.
 *
 * @see TextButton
 */
public class VisTextButton extends TextButton implements Focusable, BorderOwner {
    private VisTextButtonStyle style;

    private boolean drawBorder;
    private boolean focusBorderEnabled = true;

    public VisTextButton(String text, String styleName) {
        super(text, VisUI.getSkin().get(styleName, VisTextButtonStyle.class));
        init();
    }

    public VisTextButton(String text) {
        super(text, VisUI.getSkin().get(VisTextButtonStyle.class));
        init();
    }

    public VisTextButton(String text, ChangeListener listener) {
        super(text, VisUI.getSkin().get(VisTextButtonStyle.class));
        init();
        addListener(listener);
    }

    public VisTextButton(String text, String styleName, ChangeListener listener) {
        super(text, VisUI.getSkin().get(styleName, VisTextButtonStyle.class));
        init();
        addListener(listener);
    }

    public VisTextButton(String text, VisTextButtonStyle buttonStyle) {
        super(text, buttonStyle);
        init();
    }

    private void init() {
        style = (VisTextButtonStyle) getStyle();

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!isDisabled()) FocusManager.switchFocus(getStage(), VisTextButton.this);
                return false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (focusBorderEnabled && drawBorder && style.focusBorder != null) {
            style.focusBorder.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public boolean isFocusBorderEnabled() {
        return focusBorderEnabled;
    }

    @Override
    public void setFocusBorderEnabled(boolean focusBorderEnabled) {
        this.focusBorderEnabled = focusBorderEnabled;
    }

    @Override
    public void focusLost() {
        drawBorder = false;
    }

    @Override
    public void focusGained() {
        drawBorder = true;
    }

    static public class VisTextButtonStyle extends TextButtonStyle {
        public Drawable focusBorder;

        public VisTextButtonStyle() {
            super();
        }

        public VisTextButtonStyle(Drawable up, Drawable down, Drawable checked, BitmapFont font) {
            super(up, down, checked, font);
        }

        public VisTextButtonStyle(VisTextButtonStyle style) {
            super(style);
            this.focusBorder = style.focusBorder;
        }
    }
}
