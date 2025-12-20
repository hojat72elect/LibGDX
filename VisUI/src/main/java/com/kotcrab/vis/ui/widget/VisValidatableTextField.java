package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;

/**
 * Text field that input can be validated by custom input validators.
 *
 * @see InputValidator
 * @see Validators
 */
public class VisValidatableTextField extends VisTextField {
    private final Array<InputValidator> validators = new Array<InputValidator>();
    private boolean validationEnabled = true;

    private LastValidFocusListener restoreFocusListener;
    private boolean restoreLastValid = false;
    private String lastValid;

    public VisValidatableTextField() {
        super();
        init();
    }

    public VisValidatableTextField(String text) {
        super(text);
        init();
    }

    public VisValidatableTextField(String text, String styleName) {
        super(text, styleName);
        init();
    }

    public VisValidatableTextField(String text, VisTextFieldStyle style) {
        super(text, style);
        init();
    }

    public VisValidatableTextField(InputValidator validator) {
        super();
        addValidator(validator);
        init();
    }

    public VisValidatableTextField(InputValidator... validators) {
        super();
        for (InputValidator validator : validators)
            addValidator(validator);

        init();
    }

    public VisValidatableTextField(boolean restoreLastValid, InputValidator validator) {
        super();
        addValidator(validator);
        init();
        setRestoreLastValid(restoreLastValid);
    }

    public VisValidatableTextField(boolean restoreLastValid, InputValidator... validators) {
        super();
        for (InputValidator validator : validators)
            addValidator(validator);

        init();
        setRestoreLastValid(restoreLastValid);
    }

    private void init() {
        setProgrammaticChangeEvents(true);
        setIgnoreEqualsTextChange(false);
    }

    @Override
    void beforeChangeEventFired() {
        validateInput();
    }

    @Override
    public void setText(String str) {
        super.setText(str);
        validateInput();
    }

    public void validateInput() {
        if (validationEnabled) {
            for (InputValidator validator : validators) {
                if (!validator.validateInput(getText())) {
                    setInputValid(false);
                    return;
                }
            }
        }

        // validation not enabled or validators does not returned false (input was valid)
        setInputValid(true);
    }

    public void addValidator(InputValidator validator) {
        validators.add(validator);
        validateInput();
    }

    public Array<InputValidator> getValidators() {
        return validators;
    }

    public boolean isValidationEnabled() {
        return validationEnabled;
    }

    /**
     * Enables or disables validation, after changing this setting validateInput() is called, if validationEnabled == false then
     * field is marked as valid otherwise standard validation is performed
     *
     * @param validationEnabled enable or disable validation
     */
    public void setValidationEnabled(boolean validationEnabled) {
        this.validationEnabled = validationEnabled;
        validateInput();
    }

    public boolean isRestoreLastValid() {
        return restoreLastValid;
    }

    /**
     * If true this field will automatically restore last valid text if it loses keyboard focus during text edition.
     * This can't be called while field is selected, doing so will result in IllegalStateException. Default is false.
     */
    public void setRestoreLastValid(boolean restoreLastValid) {
        if (hasSelection)
            throw new IllegalStateException("Last valid text restore can't be changed while filed has selection");

        this.restoreLastValid = restoreLastValid;

        if (restoreLastValid) {
            if (restoreFocusListener == null) restoreFocusListener = new LastValidFocusListener();
            addListener(restoreFocusListener);
        } else {
            removeListener(restoreFocusListener);
        }
    }

    public void restoreLastValidText() {
        if (!restoreLastValid)
            throw new IllegalStateException("Restore last valid is not enabled, see #setRestoreLastValid(boolean)");

        //use super.setText to skip input validation and do not fire programmatic change event
        VisValidatableTextField.super.setText(lastValid);
        setInputValid(true);
    }

    private class LastValidFocusListener extends FocusListener {
        @Override
        public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
            if (focused && restoreLastValid) {
                lastValid = getText();
            }

            if (!focused && !isInputValid() && restoreLastValid) {
                restoreLastValidText();
            }
        }
    }
}
