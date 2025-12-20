package com.kotcrab.vis.ui.widget.color;

import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;

public class ColorPickerStyle extends WindowStyle {
    public ColorPickerWidgetStyle pickerStyle;

    public ColorPickerStyle() {
    }

    public ColorPickerStyle(ColorPickerStyle style) {
        super(style);
        if (style.pickerStyle != null) this.pickerStyle = new ColorPickerWidgetStyle(style.pickerStyle);
    }
}
