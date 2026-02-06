package com.kotcrab.vis.ui.widget.spinner;

import com.kotcrab.vis.ui.util.NumberDigitsTextFieldFilter;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link FloatSpinnerModel}.
 */
public class FloatSpinnerModelTest {

    @Mock
    private Spinner mockSpinner;

    @Mock
    private VisValidatableTextField mockTextField;

    private FloatSpinnerModel model;

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

        model = new FloatSpinnerModel("5.5", "0", "10", "0.5", 2);
    }

    @Test
    public void testConstructorWithStrings() {
        FloatSpinnerModel stringModel = new FloatSpinnerModel("5", "0", "10");
        assertEquals("Initial value should be 5", new BigDecimal("5"), stringModel.getValue());
        assertEquals("Min should be 0", new BigDecimal("0"), stringModel.getMin());
        assertEquals("Max should be 10", new BigDecimal("10"), stringModel.getMax());
        assertEquals("Step should be 1", new BigDecimal("1"), stringModel.getStep());
        assertEquals("Scale should be 1", 1, stringModel.getScale());
    }

    @Test
    public void testConstructorWithStringsAndStep() {
        FloatSpinnerModel stringModel = new FloatSpinnerModel("5", "0", "10", "0.5");
        assertEquals("Step should be 0.5", new BigDecimal("0.5"), stringModel.getStep());
        assertEquals("Scale should be 1", 1, stringModel.getScale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithMinGreaterThanMax() {
        new FloatSpinnerModel("5", "10", "0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNonPositiveStep() {
        new FloatSpinnerModel("5", "0", "10", "0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeScale() {
        new FloatSpinnerModel("5", "0", "10", "1", -1);
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

        assertEquals("Should set to min value for empty text", new BigDecimal("0.00"), model.getValue());
    }

    @Test
    public void testTextChangedWithValidText() {
        model.bind(mockSpinner);
        when(mockTextField.getText()).thenReturn("7.25");

        model.textChanged();

        assertEquals("Should set to parsed value", new BigDecimal("7.25"), model.getValue());
    }

    @Test
    public void testTextChangedWithInvalidText() {
        model.bind(mockSpinner);
        BigDecimal originalValue = model.getValue();
        when(mockTextField.getText()).thenReturn("invalid");

        model.textChanged();

        assertEquals("Should not change value for invalid text", originalValue, model.getValue());
    }

    @Test
    public void testIncrementModel() {
        model.bind(mockSpinner);

        assertTrue("Should increment successfully", model.incrementModel());
        assertEquals("Should increase by step", new BigDecimal("6.00"), model.getValue());
    }

    @Test
    public void testIncrementModelBeyondMax() {
        model.bind(mockSpinner);
        model.setValue(new BigDecimal("9.8"));

        assertTrue("Should increment to max", model.incrementModel());
        assertEquals("Should clamp to max", new BigDecimal("10.00"), model.getValue());
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
        assertEquals("Should wrap to min value", new BigDecimal("0.00"), model.getValue());
    }

    @Test
    public void testDecrementModel() {
        model.bind(mockSpinner);

        assertTrue("Should decrement successfully", model.decrementModel());
        assertEquals("Should decrease by step", new BigDecimal("5.00"), model.getValue());
    }

    @Test
    public void testDecrementModelBeyondMin() {
        model.bind(mockSpinner);
        model.setValue(new BigDecimal("0.3"));

        assertTrue("Should decrement to min", model.decrementModel());
        assertEquals("Should clamp to min", new BigDecimal("0.00"), model.getValue());
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
        assertEquals("Should wrap to max value", new BigDecimal("10.00"), model.getValue());
    }

    @Test
    public void testGetText() {
        assertEquals("Should return current value as plain string", "5.50", model.getText());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetNegativeScale() {
        model.setScale(-1);
    }

    @Test
    public void testSetValueWithFireEvent() {
        model.bind(mockSpinner);

        model.setValue(new BigDecimal("7.75"), false);

        assertEquals("Value should be set", new BigDecimal("7.75"), model.getValue());
        verify(mockSpinner).notifyValueChanged(true);
        verify(mockSpinner).notifyValueChanged(false);
    }

    @Test
    public void testSetValueBeyondBounds() {
        model.bind(mockSpinner);

        model.setValue(new BigDecimal("15.0"));
        assertEquals("Should clamp to max", model.getMax(), model.getValue());

        model.setValue(new BigDecimal("-5.0"));
        assertEquals("Should clamp to min", model.getMin(), model.getValue());

        verify(mockSpinner, times(3)).notifyValueChanged(true);
    }

    @Test
    public void testGetAndSetMin() {
        assertEquals("Initial min should be 0", new BigDecimal("0"), model.getMin());

        model.bind(mockSpinner);
        model.setMin(new BigDecimal("-5"));

        assertEquals("Min should be updated", new BigDecimal("-5"), model.getMin());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMinGreaterThanMax() {
        model.setMin(new BigDecimal("15"));
    }

    @Test
    public void testSetMinAffectsCurrent() {
        model.bind(mockSpinner);
        model.setValue(new BigDecimal("2"));

        model.setMin(new BigDecimal("3"));

        assertEquals("Current should be adjusted to new min", new BigDecimal("3.00"), model.getValue());
        verify(mockSpinner, times(3)).notifyValueChanged(true);
    }

    @Test
    public void testGetAndSetMax() {
        assertEquals("Initial max should be 10", new BigDecimal("10"), model.getMax());

        model.bind(mockSpinner);
        model.setMax(new BigDecimal("20"));

        assertEquals("Max should be updated", new BigDecimal("20"), model.getMax());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxLessThanMin() {
        model.setMax(new BigDecimal("-5"));
    }

    @Test
    public void testSetMaxAffectsCurrent() {
        model.bind(mockSpinner);
        model.setValue(new BigDecimal("8"));

        model.setMax(new BigDecimal("7"));

        assertEquals("Current should be adjusted to new max", new BigDecimal("7.00"), model.getValue());
        verify(mockSpinner, times(3)).notifyValueChanged(true);
    }

    @Test
    public void testGetAndSetStep() {
        assertEquals("Initial step should be 0.5", new BigDecimal("0.5"), model.getStep());

        model.setStep(new BigDecimal("1.5"));

        assertEquals("Step should be updated", new BigDecimal("1.5"), model.getStep());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNonPositiveStep() {
        model.setStep(BigDecimal.ZERO);
    }

    @Test
    public void testBoundsValidator() {
        model.bind(mockSpinner);

        // Test the bounds validator through reflection or by testing textChanged behavior
        when(mockTextField.getText()).thenReturn("5.0");
        model.textChanged();
        assertEquals("Valid input should be accepted", new BigDecimal("5.00"), model.getValue());

        when(mockTextField.getText()).thenReturn("15.0");
        BigDecimal beforeInvalid = model.getValue();
        model.textChanged();
        assertEquals("Invalid input should not change value", beforeInvalid, model.getValue());
    }
}
