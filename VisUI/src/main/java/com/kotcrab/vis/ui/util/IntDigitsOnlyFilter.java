package com.kotcrab.vis.ui.util;

import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldFilter;

/**
 * {@link TextFieldFilter} that only allows digits for integer values.
 */
public class IntDigitsOnlyFilter extends NumberDigitsTextFieldFilter {
    public IntDigitsOnlyFilter(boolean acceptNegativeValues) {
        super(acceptNegativeValues);
    }

    @Override
    public boolean acceptChar(VisTextField field, char c) {
        if (isAcceptNegativeValues()) {
            if (isUseFieldCursorPosition()) {
                if (c == '-' && (field.getCursorPosition() > 0 || field.getText().startsWith("-"))) return false;
            } else {
                if (c == '-' && field.getText().startsWith("-")) return false;
            }

            if (c == '-') return true;
        }
        return Character.isDigit(c);
    }
}
