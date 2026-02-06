package com.kotcrab.vis.ui.widget.spinner;

import com.kotcrab.vis.ui.util.FloatDigitsOnlyFilter;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.IntDigitsOnlyFilter;
import com.kotcrab.vis.ui.util.NumberDigitsTextFieldFilter;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Spinner model allowing to select float values. Uses {@link BigDecimal} to support high precession and support large numbers.
 * Consider using {@link SimpleFloatSpinnerModel} when such high precision is not needed as it will be faster and simpler to use.
 *
 * @see FloatSpinnerModel
 * @see IntSpinnerModel
 * @since 1.0.2
 */
public class FloatSpinnerModel extends AbstractSpinnerModel {
    private final InputValidator boundsValidator = new BoundsValidator();
    private NumberDigitsTextFieldFilter textFieldFilter;

    private BigDecimal max;
    private BigDecimal min;
    private BigDecimal step;
    private BigDecimal current;
    private int scale = 0;

    public FloatSpinnerModel(String initialValue, String min, String max) {
        this(initialValue, min, max, "1", 1);
    }

    public FloatSpinnerModel(String initialValue, String min, String max, String step) {
        this(initialValue, min, max, step, 1);
    }

    public FloatSpinnerModel(String initialValue, String min, String max, String step, int scale) {
        this(new BigDecimal(initialValue), new BigDecimal(min), new BigDecimal(max), new BigDecimal(step), scale);
    }

    public FloatSpinnerModel(BigDecimal initialValue, BigDecimal min, BigDecimal max, BigDecimal step, int scale) {
        super(false);
        this.current = initialValue;
        this.max = max;
        this.min = min;
        this.step = step;
        this.scale = scale;

        if (this.min.compareTo(this.max) > 0) throw new IllegalArgumentException("min can't be > max");
        if (this.step.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("step must be > 0");
        if (scale < 0) throw new IllegalArgumentException("scale must be >= 0");
    }

    @Override
    public void bind(Spinner spinner) {
        super.bind(spinner);
        setScale(scale, false);
        spinner.notifyValueChanged(true);
    }

    @Override
    public void textChanged() {
        if (spinner == null) return;
        String text = spinner.getTextField().getText();
        if (text.equals("")) {
            current = min.setScale(scale, RoundingMode.HALF_UP);
        } else if (checkInputBounds(text)) {
            current = new BigDecimal(text).setScale(scale, RoundingMode.HALF_UP);
        }
    }

    @Override
    public boolean incrementModel() {
        if (current.add(step).compareTo(max) > 0) {
            if (current.compareTo(max) == 0) {
                if (isWrap()) {
                    current = min.setScale(scale, RoundingMode.HALF_UP);
                    return true;
                }

                return false;
            }
            current = max.setScale(scale, RoundingMode.HALF_UP);
        } else {
            current = current.add(step);
        }

        return true;
    }

    @Override
    public boolean decrementModel() {
        if (current.subtract(step).compareTo(min) < 0) {
            if (current.compareTo(min) == 0) {
                if (isWrap()) {
                    current = max.setScale(scale, RoundingMode.HALF_UP);
                    return true;
                }

                return false;
            }

            current = min.setScale(scale, RoundingMode.HALF_UP);
        } else {
            current = current.subtract(step);
        }

        return true;
    }

    @Override
    public String getText() {
        return current.setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }

    public int getScale() {
        return scale;
    }

    /**
     * Sets scale of this selector. Scale defines how many digits after decimal point can be entered. By default
     * this is set to 0, meaning that only integers are allowed. Setting scale to 1 would allow 0.0, scale = 2 would
     * allow 0.00 and etc.
     */
    public void setScale(final int scale) {
        setScale(scale, true);
    }

    private void setScale(final int scale, boolean notifySpinner) {
        if (scale < 0) throw new IllegalStateException("Scale can't be < 0");
        this.scale = scale;
        current = current.setScale(scale, RoundingMode.HALF_UP);

        if (spinner != null) {
            VisValidatableTextField valueText = spinner.getTextField();
            valueText.getValidators().clear();
            valueText.addValidator(boundsValidator); //Both need the bounds check
            if (scale == 0) {
                valueText.addValidator(Validators.INTEGERS);
                valueText.setTextFieldFilter(textFieldFilter = new IntDigitsOnlyFilter(true));
            } else {
                valueText.addValidator(Validators.FLOATS);
                valueText.addValidator(new InputValidator() {
                    @Override
                    public boolean validateInput(String input) {
                        int dotIndex = input.indexOf('.');
                        if (dotIndex == -1) return true;
                        return input.length() - input.indexOf('.') - 1 <= scale;
                    }
                });
                valueText.setTextFieldFilter(textFieldFilter = new FloatDigitsOnlyFilter(true));
            }

            textFieldFilter.setUseFieldCursorPosition(true);
            textFieldFilter.setAcceptNegativeValues(min.compareTo(BigDecimal.ZERO) < 0);

            if (notifySpinner) {
                spinner.notifyValueChanged(spinner.isProgrammaticChangeEvents());
            }
        }
    }

    public void setValue(BigDecimal newValue, boolean fireEvent) {
        BigDecimal oldValue = current;
        if (newValue.compareTo(max) > 0) {
            current = max;
        } else if (newValue.compareTo(min) < 0) {
            current = min;
        } else {
            current = newValue;
        }

        if (spinner != null && !current.equals(oldValue)) {
            spinner.notifyValueChanged(fireEvent);
        }
    }

    public BigDecimal getValue() {
        return current;
    }

    public void setValue(BigDecimal newValue) {
        if (spinner != null) {
            setValue(newValue, spinner.isProgrammaticChangeEvents());
        } else {
            setValue(newValue, false);
        }
    }

    public BigDecimal getMin() {
        return min;
    }

    /**
     * Sets min value. If current is lesser than min, the current value is set to min value
     */
    public void setMin(BigDecimal min) {
        if (min.compareTo(max) > 0) throw new IllegalArgumentException("min can't be > max");

        this.min = min;

        if (textFieldFilter != null) {
            textFieldFilter.setAcceptNegativeValues(min.compareTo(BigDecimal.ZERO) < 0);
        }

        if (current.compareTo(min) < 0) {
            current = min.setScale(scale, RoundingMode.HALF_UP);
            if (spinner != null) {
                spinner.notifyValueChanged(spinner.isProgrammaticChangeEvents());
            }
        }
    }

    public BigDecimal getMax() {
        return max;
    }

    /**
     * Sets max value. If current is greater than max, the current value is set to max value.
     */
    public void setMax(BigDecimal max) {
        if (min.compareTo(max) > 0) throw new IllegalArgumentException("min can't be > max");

        this.max = max;

        if (current.compareTo(max) > 0) {
            current = max.setScale(scale, RoundingMode.HALF_UP);
            if (spinner != null) {
                spinner.notifyValueChanged(spinner.isProgrammaticChangeEvents());
            }
        }
    }

    public BigDecimal getStep() {
        return step;
    }

    public void setStep(BigDecimal step) {
        if (step.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("step must be > 0");
        this.step = step;
    }

    private boolean checkInputBounds(String input) {
        try {
            BigDecimal x = new BigDecimal(input);
            return x.compareTo(min) >= 0 && x.compareTo(max) <= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private class BoundsValidator implements InputValidator {
        @Override
        public boolean validateInput(String input) {
            return checkInputBounds(input);
        }
    }
}
