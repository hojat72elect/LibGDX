package com.kotcrab.vis.ui.widget.spinner;

import com.kotcrab.vis.ui.util.NumberDigitsTextFieldFilter;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link SimpleFloatSpinnerModel}.
 */
public class SimpleFloatSpinnerModelTest {

    @Mock
    private Spinner mockSpinner;

    @Mock
    private VisValidatableTextField mockTextField;

    private SimpleFloatSpinnerModel model;

    @BeforeClass
    public static void setupGdx() {
        if (com.badlogic.gdx.Gdx.files == null) {
            com.badlogic.gdx.Gdx.files = (com.badlogic.gdx.Files) Proxy.newProxyInstance(
                    com.badlogic.gdx.Files.class.getClassLoader(),
                    new Class[]{com.badlogic.gdx.Files.class},
                    (proxy, method, args) -> {
                        if ("classpath".equals(method.getName())) {
                            return new com.badlogic.gdx.files.FileHandle("test");
                        }
                        return null;
                    });
        }
        if (com.badlogic.gdx.Gdx.app == null) {
            com.badlogic.gdx.Gdx.app = (com.badlogic.gdx.Application) Proxy.newProxyInstance(
                    com.badlogic.gdx.Application.class.getClassLoader(),
                    new Class[]{com.badlogic.gdx.Application.class},
                    (proxy, method, args) -> null);
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockSpinner.getTextField()).thenReturn(mockTextField);
        when(mockSpinner.isProgrammaticChangeEvents()).thenReturn(true);
        when(mockTextField.getValidators()).thenReturn(new com.badlogic.gdx.utils.Array<>());

        model = new SimpleFloatSpinnerModel(5.5f, 0f, 10f, 0.5f, 2);
    }

    @Test
    public void testConstructorWithDefaultStep() {
        SimpleFloatSpinnerModel defaultStepModel = new SimpleFloatSpinnerModel(5f, 0f, 10f);
        assertEquals("Initial value should be 5", 5f, defaultStepModel.getValue(), 0.001f);
        assertEquals("Min should be 0", 0f, defaultStepModel.getMin(), 0.001f);
        assertEquals("Max should be 10", 10f, defaultStepModel.getMax(), 0.001f);
        assertEquals("Step should be 1 by default", 1f, defaultStepModel.getStep(), 0.001f);
        assertEquals("Precision should be 1 by default", 1, defaultStepModel.getPrecision());
    }

    @Test
    public void testConstructorWithStep() {
        SimpleFloatSpinnerModel stepModel = new SimpleFloatSpinnerModel(5f, 0f, 10f, 0.5f);
        assertEquals("Step should be 0.5", 0.5f, stepModel.getStep(), 0.001f);
        assertEquals("Precision should be 1", 1, stepModel.getPrecision());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithMinGreaterThanMax() {
        new SimpleFloatSpinnerModel(5f, 10f, 0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonPositiveStep() {
        new SimpleFloatSpinnerModel(5f, 0f, 10f, 0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativePrecision() {
        new SimpleFloatSpinnerModel(5f, 0f, 10f, 1f, -1);
    }

    @Test
    public void testBind() {
        model.bind(mockSpinner);

        verify(mockTextField).getValidators();
        verify(mockTextField).setTextFieldFilter(any(NumberDigitsTextFieldFilter.class));
        verify(mockSpinner).notifyValueChanged(true);
    }

    @Test
    public void testTextChangedWithEmptyText() {
        model.bind(mockSpinner);
        when(mockTextField.getText()).thenReturn("");

        model.textChanged();

        assertEquals("Should set to min value for empty text", 0f, model.getValue(), 0.001f);
    }

    @Test
    public void testTextChangedWithValidText() {
        model.bind(mockSpinner);
        when(mockTextField.getText()).thenReturn("7.25");

        model.textChanged();

        assertEquals("Should set to parsed value", 7.25f, model.getValue(), 0.001f);
    }

    @Test
    public void testTextChangedWithInvalidText() {
        model.bind(mockSpinner);
        float originalValue = model.getValue();
        when(mockTextField.getText()).thenReturn("invalid");

        model.textChanged();

        assertEquals("Should not change value for invalid text", originalValue, model.getValue(), 0.001f);
    }

    @Test
    public void testTextChangedWithOutOfBoundsText() {
        model.bind(mockSpinner);
        float originalValue = model.getValue();
        when(mockTextField.getText()).thenReturn("15.0");

        model.textChanged();

        assertEquals("Should not change value for out of bounds text", originalValue, model.getValue(), 0.001f);
    }

    @Test
    public void testIncrementModel() {
        model.bind(mockSpinner);

        assertTrue("Should increment successfully", model.incrementModel());
        assertEquals("Should increase by step", 6.0f, model.getValue(), 0.001f);
    }

    @Test
    public void testIncrementModelBeyondMax() {
        model.bind(mockSpinner);
        model.setValue(9.8f);

        assertTrue("Should increment to max", model.incrementModel());
        assertEquals("Should clamp to max", 10f, model.getValue(), 0.001f);
    }

    @Test
    public void testIncrementModelAtMaxWithoutWrap() {
        model.bind(mockSpinner);
        model.setValue(model.getMax());

        assertFalse("Should not increment at max without wrap", model.incrementModel());
        assertEquals("Should stay at max", model.getMax(), model.getValue(), 0.001f);
    }

    @Test
    public void testIncrementModelAtMaxWithWrap() {
        model.bind(mockSpinner);
        model.setWrap(true);
        model.setValue(model.getMax());

        assertTrue("Should wrap to min", model.incrementModel());
        assertEquals("Should wrap to min value", 0f, model.getValue(), 0.001f);
    }

    @Test
    public void testDecrementModel() {
        model.bind(mockSpinner);

        assertTrue("Should decrement successfully", model.decrementModel());
        assertEquals("Should decrease by step", 5.0f, model.getValue(), 0.001f);
    }

    @Test
    public void testDecrementModelBeyondMin() {
        model.bind(mockSpinner);
        model.setValue(0.3f);

        assertTrue("Should decrement to min", model.decrementModel());
        assertEquals("Should clamp to min", 0f, model.getValue(), 0.001f);
    }

    @Test
    public void testDecrementModelAtMinWithoutWrap() {
        model.bind(mockSpinner);
        model.setValue(model.getMin());

        assertFalse("Should not decrement at min without wrap", model.decrementModel());
        assertEquals("Should stay at min", model.getMin(), model.getValue(), 0.001f);
    }

    @Test
    public void testDecrementModelAtMinWithWrap() {
        model.bind(mockSpinner);
        model.setWrap(true);
        model.setValue(model.getMin());

        assertTrue("Should wrap to max", model.decrementModel());
        assertEquals("Should wrap to max value", 10f, model.getValue(), 0.001f);
    }

    @Test
    public void testGetTextWithPrecision() {
        assertEquals("Should return current value with precision", "5.5", model.getText());

        model.bind(mockSpinner);
        model.setValue(5.0f);
        assertEquals("Should return value formatted with precision", "5.0", model.getText());
    }

    @Test
    public void testGetTextWithZeroPrecision() {
        SimpleFloatSpinnerModel zeroPrecisionModel = new SimpleFloatSpinnerModel(5.7f, 0f, 10f, 1f, 0);
        assertEquals("Should return truncated value for zero precision", "5", zeroPrecisionModel.getText());
    }

    @Test
    public void testGetAndSetPrecision() {
        assertEquals("Initial precision should be 2", 2, model.getPrecision());

        model.bind(mockSpinner);
        model.setPrecision(1);

        assertEquals("Precision should be updated", 1, model.getPrecision());
        verify(mockSpinner, times(2)).notifyValueChanged(true);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetNegativePrecision() {
        model.setPrecision(-1);
    }

    @Test
    public void testSetValue() {
        model.bind(mockSpinner);

        model.setValue(7.75f);

        assertEquals("Value should be set", 7.75f, model.getValue(), 0.001f);
        verify(mockSpinner, times(2)).notifyValueChanged(true);
    }

    @Test
    public void testSetValueWithFireEvent() {
        model.bind(mockSpinner);

        model.setValue(7.75f, false);

        assertEquals("Value should be set", 7.75f, model.getValue(), 0.001f);
        verify(mockSpinner).notifyValueChanged(false);
    }

    @Test
    public void testSetValueBeyondBounds() {
        model.bind(mockSpinner);

        model.setValue(15.0f);
        assertEquals("Should clamp to max", model.getMax(), model.getValue(), 0.001f);

        model.setValue(-5.0f);
        assertEquals("Should clamp to min", model.getMin(), model.getValue(), 0.001f);

        verify(mockSpinner, times(3)).notifyValueChanged(true);
    }

    @Test
    public void testGetAndSetMin() {
        assertEquals("Initial min should be 0", 0f, model.getMin(), 0.001f);

        model.bind(mockSpinner);
        model.setMin(-5f);

        assertEquals("Min should be updated", -5f, model.getMin(), 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMinGreaterThanMax() {
        model.setMin(15f);
    }

    @Test
    public void testSetMinAffectsCurrent() {
        model.bind(mockSpinner);
        model.setValue(2f);

        model.setMin(3f);

        assertEquals("Current should be adjusted to new min", 3f, model.getValue(), 0.001f);
        verify(mockSpinner, times(3)).notifyValueChanged(true);
    }

    @Test
    public void testGetAndSetMax() {
        assertEquals("Initial max should be 10", 10f, model.getMax(), 0.001f);

        model.bind(mockSpinner);
        model.setMax(20f);

        assertEquals("Max should be updated", 20f, model.getMax(), 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxLessThanMin() {
        model.setMax(-5f);
    }

    @Test
    public void testSetMaxAffectsCurrent() {
        model.bind(mockSpinner);
        model.setValue(8f);

        model.setMax(7f);

        assertEquals("Current should be adjusted to new max", 7f, model.getValue(), 0.001f);
        verify(mockSpinner, times(3)).notifyValueChanged(true);
    }

    @Test
    public void testGetAndSetStep() {
        assertEquals("Initial step should be 0.5", 0.5f, model.getStep(), 0.001f);

        model.setStep(1.5f);

        assertEquals("Step should be updated", 1.5f, model.getStep(), 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNonPositiveStep() {
        model.setStep(0f);
    }

    @Test
    public void testBoundsValidator() {
        model.bind(mockSpinner);

        // Test the bounds validator through textChanged behavior
        when(mockTextField.getText()).thenReturn("5.0");
        model.textChanged();
        assertEquals("Valid input should be accepted", 5.0f, model.getValue(), 0.001f);

        when(mockTextField.getText()).thenReturn("15.0");
        float beforeInvalid = model.getValue();
        model.textChanged();
        assertEquals("Invalid input should not change value", beforeInvalid, model.getValue(), 0.001f);
    }

    @Test
    public void testNegativeRangeModel() {
        SimpleFloatSpinnerModel negativeModel = new SimpleFloatSpinnerModel(-5.5f, -10f, -1f, 0.5f, 1);
        negativeModel.bind(mockSpinner);

        assertEquals("Initial value should be -5.5", -5.5f, negativeModel.getValue(), 0.001f);
        assertEquals("Min should be -10", -10f, negativeModel.getMin(), 0.001f);
        assertEquals("Max should be -1", -1f, negativeModel.getMax(), 0.001f);

        assertTrue("Should increment", negativeModel.incrementModel());
        assertEquals("Should increment to -5.0", -5.0f, negativeModel.getValue(), 0.001f);

        assertTrue("Should decrement", negativeModel.decrementModel());
        assertEquals("Should decrement back to -5.5", -5.5f, negativeModel.getValue(), 0.001f);
    }

    @Test
    public void testZeroRangeModel() {
        SimpleFloatSpinnerModel zeroModel = new SimpleFloatSpinnerModel(0f, 0f, 0f, 1f, 1);
        zeroModel.bind(mockSpinner);

        assertEquals("Value should be 0", 0f, zeroModel.getValue(), 0.001f);
        assertEquals("Min should be 0", 0f, zeroModel.getMin(), 0.001f);
        assertEquals("Max should be 0", 0f, zeroModel.getMax(), 0.001f);

        assertFalse("Should not increment", zeroModel.incrementModel());
        assertFalse("Should not decrement", zeroModel.decrementModel());
    }

    @Test
    public void testFloatRoundingInGetText() {
        // Test with a value that would have floating point precision issues
        SimpleFloatSpinnerModel precisionModel = new SimpleFloatSpinnerModel(0.1f, 0f, 1f, 0.1f, 2);
        precisionModel.bind(mockSpinner);
        precisionModel.setValue(0.3f);

        String text = precisionModel.getText();
        assertTrue("Should handle float rounding correctly", text.equals("0.3") || text.equals("0.30"));
    }
}
