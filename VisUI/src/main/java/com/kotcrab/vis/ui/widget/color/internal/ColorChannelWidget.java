package com.kotcrab.vis.ui.widget.color.internal;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.color.BasicColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerWidgetStyle;
import com.kotcrab.vis.ui.widget.color.internal.ColorInputField.ColorInputFieldListener;

/**
 * Used to display one color channel (hue, saturation etc.) with label, ColorInputField and ChannelBar.
 */
public class ColorChannelWidget extends VisTable {
    private final PickerCommons commons;
    private final ColorPickerWidgetStyle style;
    private final Sizes sizes;

    private final ChannelBar bar;
    private final ChangeListener barListener;
    private final ColorInputField inputField;

    private final int mode;
    private int value;
    private final int maxValue;

    public ColorChannelWidget(PickerCommons commons, String label, int mode, int maxValue, final ChannelBar.ChannelBarListener listener) {
        super(true);
        this.commons = commons;

        this.style = commons.style;
        this.sizes = commons.sizes;
        this.mode = mode;
        this.maxValue = maxValue;

        barListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                value = bar.getValue();
                listener.updateFields();
                inputField.setValue(value);
            }
        };

        add(new VisLabel(label)).width(10 * sizes.scaleFactor).center();
        add(inputField = new ColorInputField(maxValue, new ColorInputFieldListener() {
            @Override
            public void changed(int newValue) {
                value = newValue;
                listener.updateFields();
                bar.setValue(newValue);
            }
        })).width(BasicColorPicker.FIELD_WIDTH * sizes.scaleFactor);
        add(bar = createBarImage()).size(BasicColorPicker.BAR_WIDTH * sizes.scaleFactor, BasicColorPicker.BAR_HEIGHT * sizes.scaleFactor);
        bar.setChannelBarListener(listener);

        inputField.setValue(0);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        inputField.setValue(value);
        bar.setValue(value);
    }

    private ChannelBar createBarImage() {
        if (mode == ChannelBar.MODE_ALPHA)
            return new AlphaChannelBar(commons, mode, maxValue, barListener);
        else
            return new ChannelBar(commons, mode, maxValue, barListener);
    }

    public ChannelBar getBar() {
        return bar;
    }

    public boolean isInputValid() {
        return inputField.isInputValid();
    }
}
