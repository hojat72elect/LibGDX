package com.kotcrab.vis.ui.util.form;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.util.Validators.GreaterThanValidator;
import com.kotcrab.vis.ui.util.Validators.LesserThanValidator;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

/**
 * Utility class made for creating input forms that requires inputting various information and that information cannot be wrong.
 * For example user registration form.
 * <p>
 * SimpleFormValidator is GWT compatible and does not provide fileExists methods, if you are not using GWT use
 * {@link FormValidator}.
 */
public class SimpleFormValidator {
    private final FormValidatorStyle style;

    private final ChangeSharedListener changeListener = new ChangeSharedListener();
    private final Array<VisValidatableTextField> fields = new Array<>();
    private final Array<CheckedButtonWrapper> buttons = new Array<>();

    private String successMsg;

    private boolean formInvalid = false;
    private String errorMsgText = "";

    private final Array<Disableable> disableTargets = new Array<>();
    private Label messageLabel;

    private boolean treatDisabledFieldsAsValid = true;

    /**
     * @param targetToDisable target actor that will be disabled if form is invalid. Eg. you can pass form Confirm button.
     *                        May be null.
     */
    public SimpleFormValidator(Disableable targetToDisable) {
        this(targetToDisable, null, "default");
    }

    /**
     * @param targetToDisable target actor that will be disabled if form is invalid. Eg. you can pass form Confirm button.
     *                        May be null.
     * @param messageLabel    label that text will be changed if from is valid or invalid. May be null.
     */
    public SimpleFormValidator(Disableable targetToDisable, Label messageLabel) {
        this(targetToDisable, messageLabel, "default");
    }

    /**
     * @param targetToDisable target actor that will be disabled if form is invalid. Eg. you can pass form Confirm button.
     *                        May be null.
     * @param messageLabel    label that text will be changed if from is valid or invalid. May be null.
     */
    public SimpleFormValidator(Disableable targetToDisable, Label messageLabel, String styleName) {
        this(targetToDisable, messageLabel, VisUI.getSkin().get(styleName, FormValidatorStyle.class));
    }

    /**
     * @param targetToDisable target actor that will be disabled if form is invalid. Eg. you can pass form Confirm button.
     *                        May be null.
     * @param messageLabel    label that text will be changed if from is valid or invalid. May be null.
     */
    public SimpleFormValidator(Disableable targetToDisable, Label messageLabel, FormValidatorStyle style) {
        this.style = style;
        if (targetToDisable != null) disableTargets.add(targetToDisable);
        this.messageLabel = messageLabel;
    }

    /**
     * Validates if file is not empty
     */
    public FormInputValidator notEmpty(VisValidatableTextField field, String errorMsg) {
        EmptyInputValidator validator = new EmptyInputValidator(errorMsg);
        field.addValidator(validator);
        add(field);
        return validator;
    }

    /**
     * Validates if entered text is integer number
     */
    public FormInputValidator integerNumber(VisValidatableTextField field, String errorMsg) {
        ValidatorWrapper wrapper = new ValidatorWrapper(errorMsg, Validators.INTEGERS);
        field.addValidator(wrapper);
        add(field);
        return wrapper;
    }

    /**
     * Validates if entered text is float number
     */
    public FormInputValidator floatNumber(VisValidatableTextField field, String errorMsg) {
        ValidatorWrapper wrapper = new ValidatorWrapper(errorMsg, Validators.FLOATS);
        field.addValidator(wrapper);
        add(field);
        return wrapper;
    }

    /**
     * Validates if entered text is greater than entered number <p>
     * Can be used in combination with {@link #integerNumber(VisValidatableTextField, String)} to only allows integers.
     */
    public FormInputValidator valueGreaterThan(VisValidatableTextField field, String errorMsg, float value) {
        return valueGreaterThan(field, errorMsg, value, false);
    }

    /**
     * Validates if entered text is lesser than entered number <p>
     * Can be used in combination with {@link #integerNumber(VisValidatableTextField, String)} to only allows integers.
     */
    public FormInputValidator valueLesserThan(VisValidatableTextField field, String errorMsg, float value) {
        return valueLesserThan(field, errorMsg, value, false);
    }

    /**
     * Validates if entered text is greater than (or equal) entered number <p>
     * Can be used in combination with {@link #integerNumber(VisValidatableTextField, String)} to only allows integers.
     */
    public FormInputValidator valueGreaterThan(VisValidatableTextField field, String errorMsg, float value, boolean validIfEqualsValue) {
        ValidatorWrapper wrapper = new ValidatorWrapper(errorMsg, new GreaterThanValidator(value, validIfEqualsValue));
        field.addValidator(wrapper);
        add(field);
        return wrapper;
    }

    /**
     * Validates if entered text is lesser (or equal) than entered number <p>
     * Can be used in combination with {@link #integerNumber(VisValidatableTextField, String)} to only allows integers.
     */
    public FormInputValidator valueLesserThan(VisValidatableTextField field, String errorMsg, float value, boolean validIfEqualsValue) {
        ValidatorWrapper wrapper = new ValidatorWrapper(errorMsg, new LesserThanValidator(value, validIfEqualsValue));
        field.addValidator(wrapper);
        add(field);
        return wrapper;
    }

    /**
     * Allows to add custom validator to field
     */
    public FormInputValidator custom(VisValidatableTextField field, FormInputValidator customValidator) {
        field.addValidator(customValidator);
        add(field);
        return customValidator;
    }

    /**
     * Validates if given button (usually checkbox) is checked. Use VisCheckBox to additionally support error border around it.
     */
    public void checked(Button button, String errorMsg) {
        buttons.add(new CheckedButtonWrapper(button, true, errorMsg));
        button.addListener(changeListener);
        validate();
    }

    /**
     * Validates if given button (usually checkbox) is unchecked. Use VisCheckBox to additionally support error border around it.
     */
    public void unchecked(Button button, String errorMsg) {
        buttons.add(new CheckedButtonWrapper(button, false, errorMsg));
        button.addListener(changeListener);
        validate();
    }

    /**
     * Adds field to this form without attaching any {@link FormInputValidator} to it. This can be used when field
     * already has added all required validators.
     */
    public void add(VisValidatableTextField field) {
        if (!fields.contains(field, true)) fields.add(field);
        field.addListener(changeListener); //addListener won't allow to add same listener twice
        validate();
    }

    public void addDisableTarget(Disableable disableable) {
        disableTargets.add(disableable);
        updateWidgets();
    }

    public boolean removeDisableTarget(Disableable disableable) {
        boolean result = disableTargets.removeValue(disableable, true);
        updateWidgets();
        return result;
    }

    public void setMessageLabel(Label messageLabel) {
        this.messageLabel = messageLabel;
        updateWidgets();
    }

    /**
     * @param successMsg message that will be displayed on {@link #messageLabel} if all fields were valid. May be null.
     */
    public void setSuccessMessage(String successMsg) {
        this.successMsg = successMsg;
        updateWidgets();
    }

    public boolean isTreatDisabledFieldsAsValid() {
        return treatDisabledFieldsAsValid;
    }

    /**
     * If true then this FormValidator will treat disabled fields as valid regardless of their validator states.
     * Default is true. Changing this cause form to be revalidated.
     *
     * @since 1.0.2
     */
    public void setTreatDisabledFieldsAsValid(boolean treatDisabledFieldAsValid) {
        this.treatDisabledFieldsAsValid = treatDisabledFieldAsValid;
        validate();
    }

    /**
     * Performs full check of this form, typically there is no need to call this method manually since form will be automatically
     * validated upon field content change. However calling this might be required when change made to field state does not
     * cause change event to be fired. For example disabling or enabling field.
     */
    public void validate() {
        formInvalid = false;
        errorMsgText = null;

        for (CheckedButtonWrapper wrapper : buttons) {
            wrapper.setButtonStateInvalid(wrapper.button.isChecked() != wrapper.mustBeChecked);
        }

        for (CheckedButtonWrapper wrapper : buttons) {
            if (treatDisabledFieldsAsValid && wrapper.button.isDisabled()) {
                continue;
            }

            if (wrapper.button.isChecked() != wrapper.mustBeChecked) {
                errorMsgText = wrapper.errorMsg;
                formInvalid = true;
                break;
            }
        }

        for (VisValidatableTextField field : fields) {
            field.validateInput();
        }

        for (VisValidatableTextField field : fields) {
            if (treatDisabledFieldsAsValid && field.isDisabled()) {
                continue;
            }

            if (!field.isInputValid()) {
                Array<InputValidator> validators = field.getValidators();
                for (InputValidator v : validators) {
                    if (!(v instanceof FormInputValidator)) {
                        throw new IllegalStateException("Fields validated by FormValidator cannot have validators not added using FormValidator methods. " +
                                "Are you adding validators to field manually?");
                    }

                    FormInputValidator validator = (FormInputValidator) v;

                    if (!validator.getLastResult()) {
                        if (!(validator.isHideErrorOnEmptyInput() && field.getText().equals(""))) {
                            errorMsgText = validator.getErrorMsg();
                        }

                        formInvalid = true;
                        break;
                    }
                }

                break;
            }
        }

        updateWidgets();
    }

    private void updateWidgets() {
        for (Disableable disableable : disableTargets) {
            disableable.setDisabled(formInvalid);
        }

        if (messageLabel != null) {
            if (errorMsgText != null) {
                messageLabel.setText(errorMsgText);
            } else {
                messageLabel.setText(successMsg); //setText will default to "" if successMsg is null
            }

            Color targetColor = errorMsgText != null ? style.errorLabelColor : style.validLabelColor;
            if (targetColor != null && style.colorTransitionDuration != 0) {
                messageLabel.addAction(Actions.color(targetColor, style.colorTransitionDuration));
            } else {
                messageLabel.setColor(targetColor);
            }
        }
    }

    private static class CheckedButtonWrapper {
        public Button button;
        public boolean mustBeChecked;
        public String errorMsg;

        public CheckedButtonWrapper(Button button, boolean mustBeChecked, String errorMsg) {
            this.button = button;
            this.mustBeChecked = mustBeChecked;
            this.errorMsg = errorMsg;
        }

        public void setButtonStateInvalid(boolean state) {
            if (button instanceof VisCheckBox) {
                ((VisCheckBox) button).setStateInvalid(state);
            }
        }
    }

    public static class EmptyInputValidator extends FormInputValidator {
        public EmptyInputValidator(String errorMsg) {
            super(errorMsg);
        }

        @Override
        public boolean validate(String input) {
            return !input.isEmpty();
        }
    }

    public static class FormValidatorStyle {
        /**
         * Optional
         */
        public Color errorLabelColor;
        /**
         * Optional
         */
        public Color validLabelColor;

        public float colorTransitionDuration;

        public FormValidatorStyle() {
        }

        public FormValidatorStyle(Color errorLabelColor, Color validLabelColor) {
            this.errorLabelColor = errorLabelColor;
            this.validLabelColor = validLabelColor;
        }

        public FormValidatorStyle(FormValidatorStyle style) {
            this.errorLabelColor = style.errorLabelColor;
            this.validLabelColor = style.validLabelColor;
            this.colorTransitionDuration = style.colorTransitionDuration;
        }
    }

    private class ChangeSharedListener extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            validate();
        }
    }
}
