package com.mbrlabs.mundus.editor.ui.widgets;

import com.badlogic.gdx.graphics.Color;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.mbrlabs.mundus.editor.Mundus;
import com.mbrlabs.mundus.editor.utils.Colors;

public class FaLabel extends VisLabel {

    public final static LabelStyle style = new LabelStyle();
    public final static LabelStyle styleActive = new LabelStyle();

    static {
        style.font = Mundus.INSTANCE.getFaSmall();
        style.fontColor = Colors.INSTANCE.getTEAL();
    }

    static {
        styleActive.font = Mundus.INSTANCE.getFaSmall();
        styleActive.fontColor = Color.WHITE;
    }

    public FaLabel(String text) {
        super(text);
        setStyle(style);
    }
}
