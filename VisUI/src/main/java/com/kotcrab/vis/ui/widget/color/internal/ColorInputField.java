package com.kotcrab.vis.ui.widget.color.internal;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

/**
 * Fields used to enter color numbers in color picker, verifies max allowed value
 * provides quick increment/decrement of current value by pressing [shift +] plus or minus on numpad
 */
public class ColorInputField extends VisValidatableTextField {
    private int value;
    private final int maxValue;

    public ColorInputField(final int maxValue, final ColorInputFieldListener listener) {
        super(new ColorFieldValidator(maxValue));
        this.value = 0;
        this.maxValue = maxValue;

        setProgrammaticChangeEvents(false);
        setMaxLength(3);
        setTextFieldFilter(new NumberFilter());

        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (getText().length() > 0)
                    value = Integer.valueOf(getText());
            }
        });

        addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                ColorInputField field = (ColorInputField) event.getListenerActor();
                if (character == '+') field.changeValue(UIUtils.shift() ? 10 : 1);
                if (character == '-') field.changeValue(UIUtils.shift() ? -10 : -1);

                if (character != 0) listener.changed(getValue());

                return true;
            }
        });

        addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                if (!focused && !isInputValid())
                    setValue(maxValue); //only possibility on invalid field is that entered value will be bigger than maxValue so we set field value to maxValue
            }
        });
    }

    public void changeValue(int byValue) {
        this.value += byValue;

        if (value > maxValue) value = maxValue;
        if (value < 0) value = 0;

        updateUI();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        updateUI();
    }

    private void updateUI() {
        setText(String.valueOf(value));
        setCursorPosition(getMaxLength());
    }

    public interface ColorInputFieldListener {
        void changed(int newValue);
    }

    private static class NumberFilter implements TextFieldFilter {
        @Override
        public boolean acceptChar(VisTextField textField, char c) {
            return Character.isDigit(c);
        }
    }

    private static class ColorFieldValidator implements InputValidator {
        private final int maxValue;

        public ColorFieldValidator(int maxValue) {
            this.maxValue = maxValue;
        }

        @Override
        public boolean validateInput(String input) {
            if (input.equals("")) return false;

            Integer number = Integer.parseInt(input);
            return number <= maxValue;
        }
    }
}
