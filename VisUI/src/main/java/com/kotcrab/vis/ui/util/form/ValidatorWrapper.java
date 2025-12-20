package com.kotcrab.vis.ui.util.form;

import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

/**
 * Allows standard {@link InputValidator} to be used with {@link SimpleFormValidator#custom(VisValidatableTextField, FormInputValidator)}
 * Wraps standard input validator and adds error message.
 */
public class ValidatorWrapper extends FormInputValidator {
    private final InputValidator validator;

    public ValidatorWrapper(String errorMsg, InputValidator validator) {
        super(errorMsg);
        this.validator = validator;
    }

    @Override
    protected boolean validate(String input) {
        return validator.validateInput(input);
    }
}
