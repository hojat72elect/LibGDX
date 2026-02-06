package com.kotcrab.vis.ui.widget.spinner;

import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.IntDigitsOnlyFilter;
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
 * Unit tests for {@link IntSpinnerModel}.
 */
public class IntSpinnerModelTest {

    @Mock
    private Spinner mockSpinner;

    @Mock
    private VisValidatableTextField mockTextField;

    private IntSpinnerModel model;

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

        model = new IntSpinnerModel(5, 0, 10, 2);
    }

    @Test
    public void testConstructorWithDefaultStep() {
        IntSpinnerModel defaultStepModel = new IntSpinnerModel(5, 0, 10);
        assertEquals("Initial value should be 5", 5, defaultStepModel.getValue());
        assertEquals("Min should be 0", 0, defaultStepModel.getMin());
        assertEquals("Max should be 10", 10, defaultStepModel.getMax());
        assertEquals("Step should be 1 by default", 1, defaultStepModel.getStep());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithMinGreaterThanMax() {
        new IntSpinnerModel(5, 10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonPositiveStep() {
        new IntSpinnerModel(5, 0, 10, 0);
    }

    @Test
    public void testBind() {
        model.bind(mockSpinner);

        verify(mockTextField).getValidators();
        verify(mockTextField, times(2)).addValidator(any(InputValidator.class));
        verify(mockTextField).setTextFieldFilter(any(IntDigitsOnlyFilter.class));
        verify(mockSpinner).notifyValueChanged(true);
    }

    @Test
    public void testBindWithNegativeValues() {
        IntSpinnerModel negativeModel = new IntSpinnerModel(-5, -10, 0);
        negativeModel.bind(mockSpinner);

        verify(mockTextField).setTextFieldFilter(any(IntDigitsOnlyFilter.class));
    }

    @Test
    public void testTextChangedWithEmptyText() {
        model.bind(mockSpinner);
        when(mockTextField.getText()).thenReturn("");

        model.textChanged();

        assertEquals("Should set to min value for empty text", 0, model.getValue());
    }

    @Test
    public void testTextChangedWithValidText() {
        model.bind(mockSpinner);
        when(mockTextField.getText()).thenReturn("7");

        model.textChanged();

        assertEquals("Should set to parsed value", 7, model.getValue());
    }

    @Test
    public void testTextChangedWithInvalidText() {
        model.bind(mockSpinner);
        int originalValue = model.getValue();
        when(mockTextField.getText()).thenReturn("invalid");

        model.textChanged();

        assertEquals("Should not change value for invalid text", originalValue, model.getValue());
    }

    @Test
    public void testTextChangedWithOutOfBoundsText() {
        model.bind(mockSpinner);
        int originalValue = model.getValue();
        when(mockTextField.getText()).thenReturn("15");

        model.textChanged();

        assertEquals("Should not change value for out of bounds text", originalValue, model.getValue());
    }

    @Test
    public void testIncrementModel() {
        model.bind(mockSpinner);

        assertTrue("Should increment successfully", model.incrementModel());
        assertEquals("Should increase by step", 7, model.getValue());
    }

    @Test
    public void testIncrementModelBeyondMax() {
        model.bind(mockSpinner);
        model.setValue(9);

        assertTrue("Should increment to max", model.incrementModel());
        assertEquals("Should clamp to max", 10, model.getValue());
    }

    @Test
    public void testIncrementModelAtMaxWithoutWrap() {
        model.bind(mockSpinner);
        model.setValue(model.getMax());

        assertFalse("Should not increment at max without wrap", model.incrementModel());
        assertEquals("Should stay at max", model.getMax(), model.getValue());
    }

    @Test
    public void testIncrementModelAtMaxWithWrap() {
        model.bind(mockSpinner);
        model.setWrap(true);
        model.setValue(model.getMax());

        assertTrue("Should wrap to min", model.incrementModel());
        assertEquals("Should wrap to min value", model.getMin(), model.getValue());
    }

    @Test
    public void testDecrementModel() {
        model.bind(mockSpinner);

        assertTrue("Should decrement successfully", model.decrementModel());
        assertEquals("Should decrease by step", 3, model.getValue());
    }

    @Test
    public void testDecrementModelBeyondMin() {
        model.bind(mockSpinner);
        model.setValue(1);

        assertTrue("Should decrement to min", model.decrementModel());
        assertEquals("Should clamp to min", 0, model.getValue());
    }

    @Test
    public void testDecrementModelAtMinWithoutWrap() {
        model.bind(mockSpinner);
        model.setValue(model.getMin());

        assertFalse("Should not decrement at min without wrap", model.decrementModel());
        assertEquals("Should stay at min", model.getMin(), model.getValue());
    }

    @Test
    public void testDecrementModelAtMinWithWrap() {
        model.bind(mockSpinner);
        model.setWrap(true);
        model.setValue(model.getMin());

        assertTrue("Should wrap to max", model.decrementModel());
        assertEquals("Should wrap to max value", model.getMax(), model.getValue());
    }

    @Test
    public void testGetText() {
        assertEquals("Should return current value as string", "5", model.getText());

        model.bind(mockSpinner);
        model.setValue(7);
        assertEquals("Should return updated value as string", "7", model.getText());
    }

    @Test
    public void testSetValue() {
        model.bind(mockSpinner);

        model.setValue(7);

        assertEquals("Value should be set", 7, model.getValue());
        verify(mockSpinner, times(2)).notifyValueChanged(true);
    }

    @Test
    public void testSetValueWithFireEvent() {
        model.bind(mockSpinner);

        model.setValue(7, false);

        assertEquals("Value should be set", 7, model.getValue());
        verify(mockSpinner).notifyValueChanged(false);
    }

    @Test
    public void testSetValueBeyondBounds() {
        model.bind(mockSpinner);

        model.setValue(15);
        assertEquals("Should clamp to max", model.getMax(), model.getValue());

        model.setValue(-5);
        assertEquals("Should clamp to min", model.getMin(), model.getValue());

        verify(mockSpinner, times(3)).notifyValueChanged(true);
    }

    @Test
    public void testGetAndSetMin() {
        assertEquals("Initial min should be 0", 0, model.getMin());

        model.bind(mockSpinner);
        model.setMin(-5);

        assertEquals("Min should be updated", -5, model.getMin());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMinGreaterThanMax() {
        model.setMin(15);
    }

    @Test
    public void testSetMinAffectsCurrent() {
        model.bind(mockSpinner);
        model.setValue(2);

        model.setMin(3);

        assertEquals("Current should be adjusted to new min", 3, model.getValue());
        verify(mockSpinner, times(3)).notifyValueChanged(true);
    }

    @Test
    public void testGetAndSetMax() {
        assertEquals("Initial max should be 10", 10, model.getMax());

        model.bind(mockSpinner);
        model.setMax(20);

        assertEquals("Max should be updated", 20, model.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxLessThanMin() {
        model.setMax(-5);
    }

    @Test
    public void testSetMaxAffectsCurrent() {
        model.bind(mockSpinner);
        model.setValue(8);

        model.setMax(7);

        assertEquals("Current should be adjusted to new max", 7, model.getValue());
        verify(mockSpinner, times(3)).notifyValueChanged(true);
    }

    @Test
    public void testGetAndSetStep() {
        assertEquals("Initial step should be 2", 2, model.getStep());

        model.setStep(3);

        assertEquals("Step should be updated", 3, model.getStep());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNonPositiveStep() {
        model.setStep(0);
    }

    @Test
    public void testBoundsValidator() {
        model.bind(mockSpinner);

        // Test the bounds validator through textChanged behavior
        when(mockTextField.getText()).thenReturn("5");
        model.textChanged();
        assertEquals("Valid input should be accepted", 5, model.getValue());

        when(mockTextField.getText()).thenReturn("15");
        int beforeInvalid = model.getValue();
        model.textChanged();
        assertEquals("Invalid input should not change value", beforeInvalid, model.getValue());
    }

    @Test
    public void testNegativeRangeModel() {
        IntSpinnerModel negativeModel = new IntSpinnerModel(-5, -10, -1, 1);
        negativeModel.bind(mockSpinner);

        assertEquals("Initial value should be -5", -5, negativeModel.getValue());
        assertEquals("Min should be -10", -10, negativeModel.getMin());
        assertEquals("Max should be -1", -1, negativeModel.getMax());

        assertTrue("Should increment", negativeModel.incrementModel());
        assertEquals("Should increment to -4", -4, negativeModel.getValue());

        assertTrue("Should decrement", negativeModel.decrementModel());
        assertEquals("Should decrement back to -5", -5, negativeModel.getValue());
    }

    @Test
    public void testZeroRangeModel() {
        IntSpinnerModel zeroModel = new IntSpinnerModel(0, 0, 0);
        zeroModel.bind(mockSpinner);

        assertEquals("Value should be 0", 0, zeroModel.getValue());
        assertEquals("Min should be 0", 0, zeroModel.getMin());
        assertEquals("Max should be 0", 0, zeroModel.getMax());

        assertFalse("Should not increment", zeroModel.incrementModel());
        assertFalse("Should not decrement", zeroModel.decrementModel());
    }
}
