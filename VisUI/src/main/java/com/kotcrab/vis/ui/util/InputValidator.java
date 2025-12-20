package com.kotcrab.vis.ui.util;

import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.form.SimpleFormValidator;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

/**
 * Interface implemented by classes that can validate whether user input is right or wrong, typically used by {@link VisValidatableTextField}
 * {@link SimpleFormValidator} and {@link Dialogs} input dialogs.
 *
 *  @since 0.0.3
 */
public interface InputValidator {
    /**
     * Called when input must be validated.
     *
     * @param input text that should be validated
     * @return true if input is valid, false otherwise
     */
    boolean validateInput(String input);
}
