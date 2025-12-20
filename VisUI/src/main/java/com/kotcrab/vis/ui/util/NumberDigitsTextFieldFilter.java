package com.kotcrab.vis.ui.util;

import com.kotcrab.vis.ui.widget.VisTextField.TextFieldFilter;

/**
 * Base class for number digits text field filters. Filters extending this class must handle disabling entering
 * negative number values and using cursor position to prevent typing minus in wrong place.
 *
 * @see IntDigitsOnlyFilter
 * @see FloatDigitsOnlyFilter
 */
public abstract class NumberDigitsTextFieldFilter implements TextFieldFilter {
    private boolean acceptNegativeValues;
    private boolean useFieldCursorPosition;

    public NumberDigitsTextFieldFilter(boolean acceptNegativeValues) {
        this.acceptNegativeValues = acceptNegativeValues;
    }

    public boolean isAcceptNegativeValues() {
        return acceptNegativeValues;
    }

    public void setAcceptNegativeValues(boolean acceptNegativeValues) {
        this.acceptNegativeValues = acceptNegativeValues;
    }

    public boolean isUseFieldCursorPosition() {
        return useFieldCursorPosition;
    }

    /**
     * @param useFieldCursorPosition if true this filter will use current field cursor position to prevent typing minus sign
     *                               in wrong place. This is disabled by default. If you enable this feature you must ensure that field cursor position is
     *                               set to 0 when you change text programmatically. Non zero cursor position can happen when you are changing text when
     *                               field still has user focus.
     */
    public void setUseFieldCursorPosition(boolean useFieldCursorPosition) {
        this.useFieldCursorPosition = useFieldCursorPosition;
    }
}
