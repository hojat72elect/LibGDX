package com.mbrlabs.mundus.editor.ui.widgets;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class ToggleButton extends VisTextButton {

    private String textOn;
    private String textOff;

    private boolean isOn;

    public ToggleButton(String textOn, String textOff) {
        super(textOn);
        this.textOn = textOn;
        this.textOff = textOff;
        this.isOn = true;

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleState();
            }
        });
    }

    public boolean isOn() {
        return this.isOn;
    }

    public void setOn(boolean on) {
        this.isOn = on;
        if (isOn) {
            setText(textOn);
        } else {
            setText(textOff);
        }
    }

    public boolean toggleState() {
        setOn(!isOn);
        return isOn;
    }

    public void setTextOn(String textOn) {
        this.textOn = textOn;
    }

    public void setTextOff(String textOff) {
        this.textOff = textOff;
    }
}
