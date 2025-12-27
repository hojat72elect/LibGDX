package com.mbrlabs.mundus.editor.ui.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.kotcrab.vis.ui.widget.VisRadioButton;
import com.kotcrab.vis.ui.widget.VisTable;

public class RadioButtonGroup<T> extends VisTable {

    private final ButtonGroup<RadioButton> buttonGroup;

    public RadioButtonGroup() {
        super();
        buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        pad(5);
    }

    public void add(RadioButton radioButton) {
        buttonGroup.add(radioButton);
        super.add(radioButton).left().row();
    }

    public ButtonGroup<RadioButton> getButtonGroup() {
        return buttonGroup;
    }

    /**
     * A checkbox with a reference object.
     */
    public static class RadioButton extends VisRadioButton {

        private final Object refObject;

        public RadioButton(String text, Object refObject) {
            super(text);
            this.refObject = refObject;
        }

        public Object getRefObject() {
            return refObject;
        }
    }
}
