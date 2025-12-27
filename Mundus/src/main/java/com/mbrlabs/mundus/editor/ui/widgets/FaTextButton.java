package com.mbrlabs.mundus.editor.ui.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.mbrlabs.mundus.editor.Mundus;
import com.mbrlabs.mundus.editor.utils.Colors;

public class FaTextButton extends VisTextButton {

    public final static TextButton.TextButtonStyle styleNoBg = new TextButton.TextButtonStyle();
    public final static TextButton.TextButtonStyle styleBg = new TextButton.TextButtonStyle();
    public final static TextButton.TextButtonStyle styleActive = new TextButton.TextButtonStyle();

    static {
        styleNoBg.font = Mundus.INSTANCE.getFa();
        styleNoBg.fontColor = Color.WHITE;
        styleNoBg.pressedOffsetX = 1;
        styleNoBg.unpressedOffsetX = 0;
        styleNoBg.pressedOffsetY = -1;
        styleNoBg.fontColor = Colors.INSTANCE.getTEAL();
    }

    static {
        styleBg.font = Mundus.INSTANCE.getFa();
        styleBg.pressedOffsetX = 1;
        styleBg.unpressedOffsetX = 0;
        styleBg.pressedOffsetY = -1;
        styleBg.fontColor = Colors.INSTANCE.getTEAL();
        styleBg.up = VisUI.getSkin().getDrawable("menu-bg");
        styleBg.down = VisUI.getSkin().getDrawable("menu-bg");
    }

    static {
        styleActive.font = Mundus.INSTANCE.getFa();
        styleActive.pressedOffsetX = 1;
        styleActive.unpressedOffsetX = 0;
        styleActive.pressedOffsetY = -1;
        styleActive.fontColor = Color.WHITE;
    }

    public FaTextButton(String text) {
        this(text, styleBg);
    }

    public FaTextButton(String text, TextButtonStyle style) {
        super(text);
        setStyle(style);
        setFocusBorderEnabled(false);
    }
}
