package com.kotcrab.vis.ui.util.form;

import com.kotcrab.vis.ui.util.InputValidator;

/**
 * Base class for all validators used in {@link SimpleFormValidator}. Implementing custom {@link FormInputValidator} doesn't
 * differ from creating standard {@link InputValidator}. You just need to supply error message which will be displayed
 * when form validation failed on this validator. Because implementing custom {@link FormInputValidator} does not require
 * any more changes you can use {@link ValidatorWrapper} for existing {@link InputValidator}s.
 *
 * @see InputValidator
 * @see ValidatorWrapper
 */
public abstract class FormInputValidator implements InputValidator {
    private String errorMsg;
    private boolean result;
    private boolean hideErrorOnEmptyInput = false;

    public FormInputValidator(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public final boolean validateInput(String input) {
        result = validate(input);
        return result;
    }

    /**
     * Called by FormInputValidator when input should be validated, for proper validator behaviour this must be used
     * instead of {@link #validateInput(String)}.
     * Last result of this function will be stored because it is required by FromValidator.
     *
     * @param input that should be validated.
     * @return if input is valid, false otherwise.
     */
    protected abstract boolean validate(String input);

    /**
     * When called, error message of this validator won't be displayed if input field is empty, however from still will
     * be treated as invalid (confirm button won't be enabled). This is UX improvement feature, simply don't display
     * error before user typed in something.
     */
    public FormInputValidator hideErrorOnEmptyInput() {
        hideErrorOnEmptyInput = true;
        return this;
    }

    public boolean isHideErrorOnEmptyInput() {
        return hideErrorOnEmptyInput;
    }

    /**
     * @see #hideErrorOnEmptyInput()
     */
    public void setHideErrorOnEmptyInput(boolean hideErrorOnEmptyInput) {
        this.hideErrorOnEmptyInput = hideErrorOnEmptyInput;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    boolean getLastResult() {
        return result;
    }
}
