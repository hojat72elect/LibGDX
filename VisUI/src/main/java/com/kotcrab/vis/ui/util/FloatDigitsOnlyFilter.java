package com.kotcrab.vis.ui.util;

import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldFilter;

/**
 * {@link TextFieldFilter} that only allows digits for float values.
 */
public class FloatDigitsOnlyFilter extends IntDigitsOnlyFilter {
    public FloatDigitsOnlyFilter(boolean acceptNegativeValues) {
        super(acceptNegativeValues);
    }

    @Override
    public boolean acceptChar(VisTextField field, char c) {
        int selectionStart = field.getSelectionStart();
        int cursorPos = field.getCursorPosition();
        String text;
        if (field.isTextSelected()) { //issue #131
            String beforeSelection = field.getText().substring(0, Math.min(selectionStart, cursorPos));
            String afterSelection = field.getText().substring(Math.max(selectionStart, cursorPos));
            text = beforeSelection + afterSelection;
        } else {
            text = field.getText();
        }

        if (c == '.' && !text.contains(".")) return true;
        return super.acceptChar(field, c);
    }
}
