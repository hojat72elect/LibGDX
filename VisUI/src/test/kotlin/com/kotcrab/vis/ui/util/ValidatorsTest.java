package com.kotcrab.vis.ui.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link Validators}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidatorsTest {

    @Test
    public void testIntegerValidatorValidInputs() {
        Validators.IntegerValidator validator = new Validators.IntegerValidator();

        assertTrue("Should accept positive integer", validator.validateInput("123"));
        assertTrue("Should accept zero", validator.validateInput("0"));
        assertTrue("Should accept negative integer", validator.validateInput("-456"));
        assertTrue("Should accept max integer", validator.validateInput(String.valueOf(Integer.MAX_VALUE)));
        assertTrue("Should accept min integer", validator.validateInput(String.valueOf(Integer.MIN_VALUE)));
    }

    @Test
    public void testIntegerValidatorInvalidInputs() {
        Validators.IntegerValidator validator = new Validators.IntegerValidator();

        assertFalse("Should reject decimal number", validator.validateInput("123.45"));
        assertFalse("Should reject floating point", validator.validateInput("0.0"));
        assertFalse("Should reject text", validator.validateInput("abc"));
        assertFalse("Should reject mixed alphanumeric", validator.validateInput("123abc"));
        assertFalse("Should reject empty string", validator.validateInput(""));
        assertFalse("Should reject whitespace", validator.validateInput("   "));
        assertFalse("Should reject special characters", validator.validateInput("!@#$"));
        // Note: Current implementation doesn't handle null gracefully, so we skip null test
    }

    @Test
    public void testIntegerValidatorSharedInstance() {
        // Test that shared instance works correctly
        assertTrue("Shared instance should accept valid integer", Validators.INTEGERS.validateInput("42"));
        assertFalse("Shared instance should reject invalid input", Validators.INTEGERS.validateInput("abc"));
    }

    // ========== FloatValidator Tests ==========

    @Test
    public void testFloatValidatorValidInputs() {
        Validators.FloatValidator validator = new Validators.FloatValidator();

        assertTrue("Should accept positive float", validator.validateInput("123.45"));
        assertTrue("Should accept negative float", validator.validateInput("-67.89"));
        assertTrue("Should accept integer as float", validator.validateInput("42"));
        assertTrue("Should accept zero", validator.validateInput("0"));
        assertTrue("Should accept zero with decimal", validator.validateInput("0.0"));
        assertTrue("Should accept scientific notation", validator.validateInput("1.23e4"));
        assertTrue("Should accept max float", validator.validateInput(String.valueOf(Float.MAX_VALUE)));
        assertTrue("Should accept min float", validator.validateInput(String.valueOf(Float.MIN_VALUE)));
    }

    @Test
    public void testFloatValidatorInvalidInputs() {
        Validators.FloatValidator validator = new Validators.FloatValidator();

        assertFalse("Should reject text", validator.validateInput("abc"));
        assertFalse("Should reject mixed alphanumeric", validator.validateInput("123abc"));
        assertFalse("Should reject empty string", validator.validateInput(""));
        assertFalse("Should reject whitespace", validator.validateInput("   "));
        assertFalse("Should reject special characters", validator.validateInput("!@#$"));
        // Note: Current implementation doesn't handle null gracefully, so we skip null test
        assertFalse("Should reject multiple decimals", validator.validateInput("123.45.67"));
    }

    @Test
    public void testFloatValidatorSharedInstance() {
        // Test that shared instance works correctly
        assertTrue("Shared instance should accept valid float", Validators.FLOATS.validateInput("3.14"));
        assertFalse("Shared instance should reject invalid input", Validators.FLOATS.validateInput("abc"));
    }

    // ========== LesserThanValidator Tests ==========

    @Test
    public void testLesserThanValidatorStrictLess() {
        Validators.LesserThanValidator validator = new Validators.LesserThanValidator(100.0f);

        assertTrue("Should accept value less than threshold", validator.validateInput("50"));
        assertTrue("Should accept negative value less than threshold", validator.validateInput("-10"));
        assertTrue("Should accept zero less than positive threshold", validator.validateInput("0"));
        assertFalse("Should reject value equal to threshold", validator.validateInput("100"));
        assertFalse("Should reject value greater than threshold", validator.validateInput("150"));
        assertFalse("Should reject invalid input", validator.validateInput("abc"));
    }

    @Test
    public void testLesserThanValidatorWithEquals() {
        Validators.LesserThanValidator validator = new Validators.LesserThanValidator(100.0f, true);

        assertTrue("Should accept value less than threshold", validator.validateInput("50"));
        assertTrue("Should accept value equal to threshold", validator.validateInput("100"));
        assertFalse("Should reject value greater than threshold", validator.validateInput("150"));
        assertFalse("Should reject invalid input", validator.validateInput("abc"));
    }

    @Test
    public void testLesserThanValidatorWithoutEquals() {
        Validators.LesserThanValidator validator = new Validators.LesserThanValidator(100.0f, false);

        assertTrue("Should accept value less than threshold", validator.validateInput("50"));
        assertFalse("Should reject value equal to threshold", validator.validateInput("100"));
        assertFalse("Should reject value greater than threshold", validator.validateInput("150"));
        assertFalse("Should reject invalid input", validator.validateInput("abc"));
    }

    @Test
    public void testLesserThanValidatorEdgeCases() {
        Validators.LesserThanValidator validator = new Validators.LesserThanValidator(0.0f);

        assertTrue("Should accept negative value", validator.validateInput("-1"));
        assertFalse("Should reject zero", validator.validateInput("0"));
        assertFalse("Should reject positive value", validator.validateInput("1"));
    }

    @Test
    public void testLesserThanValidatorSetters() {
        Validators.LesserThanValidator validator = new Validators.LesserThanValidator(50.0f);

        // Test initial state
        assertFalse("Should reject 50 initially", validator.validateInput("50"));

        // Test setUseEquals
        validator.setUseEquals(true);
        assertTrue("Should accept 50 after setting equals", validator.validateInput("50"));

        // Test setLesserThan
        validator.setLesserThan(25.0f);
        assertFalse("Should reject 50 after changing threshold", validator.validateInput("50"));
        assertTrue("Should accept 20 after changing threshold", validator.validateInput("20"));
    }

    @Test
    public void testLesserThanValidatorWithFloats() {
        Validators.LesserThanValidator validator = new Validators.LesserThanValidator(10.5f);

        assertTrue("Should accept float less than threshold", validator.validateInput("10.4"));
        assertTrue("Should accept integer less than threshold", validator.validateInput("10"));
        assertFalse("Should reject float greater than threshold", validator.validateInput("10.6"));
        assertFalse("Should reject integer greater than threshold", validator.validateInput("11"));
    }

    @Test
    public void testGreaterThanValidatorStrictGreater() {
        Validators.GreaterThanValidator validator = new Validators.GreaterThanValidator(-100.0f);

        assertTrue("Should accept value greater than threshold", validator.validateInput("150"));
        assertTrue("Should accept positive value greater than negative threshold", validator.validateInput("50"));
        assertFalse("Should reject value equal to threshold", validator.validateInput("-100"));
        assertFalse("Should reject value less than threshold", validator.validateInput("-150"));
        assertFalse("Should reject invalid input", validator.validateInput("abc"));
    }

    @Test
    public void testGreaterThanValidatorWithEquals() {
        Validators.GreaterThanValidator validator = new Validators.GreaterThanValidator(100.0f, true);

        assertTrue("Should accept value greater than threshold", validator.validateInput("150"));
        assertTrue("Should accept value equal to threshold", validator.validateInput("100"));
        assertFalse("Should reject value less than threshold", validator.validateInput("50"));
        assertFalse("Should reject invalid input", validator.validateInput("abc"));
    }

    @Test
    public void testGreaterThanValidatorWithoutEquals() {
        Validators.GreaterThanValidator validator = new Validators.GreaterThanValidator(100.0f, false);

        assertTrue("Should accept value greater than threshold", validator.validateInput("150"));
        assertFalse("Should reject value equal to threshold", validator.validateInput("100"));
        assertFalse("Should reject value less than threshold", validator.validateInput("50"));
        assertFalse("Should reject invalid input", validator.validateInput("abc"));
    }

    @Test
    public void testGreaterThanValidatorEdgeCases() {
        Validators.GreaterThanValidator validator = new Validators.GreaterThanValidator(0.0f);

        assertTrue("Should accept positive value", validator.validateInput("1"));
        assertFalse("Should reject zero", validator.validateInput("0"));
        assertFalse("Should reject negative value", validator.validateInput("-1"));
    }

    @Test
    public void testGreaterThanValidatorSetters() {
        Validators.GreaterThanValidator validator = new Validators.GreaterThanValidator(50.0f);

        // Test initial state
        assertFalse("Should reject 50 initially", validator.validateInput("50"));

        // Test setUseEquals
        validator.setUseEquals(true);
        assertTrue("Should accept 50 after setting equals", validator.validateInput("50"));

        // Test setGreaterThan
        validator.setGreaterThan(25.0f);
        validator.setUseEquals(false); // Reset to strict comparison
        assertTrue("Should accept 50 after changing threshold (50 > 25)", validator.validateInput("50"));
        assertTrue("Should accept 30 after changing threshold (30 > 25)", validator.validateInput("30"));
        assertFalse("Should reject 20 after changing threshold (20 < 25)", validator.validateInput("20"));
    }

    @Test
    public void testGreaterThanValidatorWithFloats() {
        Validators.GreaterThanValidator validator = new Validators.GreaterThanValidator(10.5f);

        assertTrue("Should accept float greater than threshold", validator.validateInput("10.6"));
        assertTrue("Should accept integer greater than threshold", validator.validateInput("11"));
        assertFalse("Should reject float less than threshold", validator.validateInput("10.4"));
        assertFalse("Should reject integer less than threshold", validator.validateInput("10"));
    }

    @Test
    public void testValidatorsWithExtremeValues() {
        // Test with extreme numeric values
        Validators.LesserThanValidator lessValidator = new Validators.LesserThanValidator(Float.MAX_VALUE);
        Validators.GreaterThanValidator greaterValidator = new Validators.GreaterThanValidator(Float.MIN_VALUE);

        assertTrue("Less validator should accept normal value", lessValidator.validateInput("1000000"));
        // Note: Float.MIN_VALUE is a very small positive number, so most values are greater than it
        assertTrue("Greater validator should accept normal value", greaterValidator.validateInput("0.1"));
    }

    @Test
    public void testValidatorsWithScientificNotation() {
        Validators.LesserThanValidator lessValidator = new Validators.LesserThanValidator(1000.0f);
        Validators.GreaterThanValidator greaterValidator = new Validators.GreaterThanValidator(0.001f);

        assertTrue("Less validator should handle scientific notation", lessValidator.validateInput("1e2")); // 100
        assertTrue("Greater validator should handle scientific notation", greaterValidator.validateInput("1e-2")); // 0.01
        assertFalse("Less validator should reject large scientific notation", lessValidator.validateInput("1e4")); // 10000
        assertFalse("Greater validator should reject small scientific notation", greaterValidator.validateInput("1e-4")); // 0.0001
    }

    @Test
    public void testValidatorsWithNegativeThresholds() {
        Validators.LesserThanValidator lessValidator = new Validators.LesserThanValidator(-100.0f);
        Validators.GreaterThanValidator greaterValidator = new Validators.GreaterThanValidator(-50.0f);

        // Test LesserThanValidator with negative threshold
        assertTrue("Less validator should accept more negative", lessValidator.validateInput("-200"));
        assertFalse("Less validator should reject less negative", lessValidator.validateInput("-50"));

        // Test GreaterThanValidator with negative threshold
        assertTrue("Greater validator should accept less negative", greaterValidator.validateInput("-25"));
        assertFalse("Greater validator should reject more negative", greaterValidator.validateInput("-75"));
    }

    @Test
    public void testValidatorsConsistency() {
        // Test that validators are consistent with each other
        float threshold = 100.0f;
        Validators.LesserThanValidator lessValidator = new Validators.LesserThanValidator(threshold, true);
        Validators.GreaterThanValidator greaterValidator = new Validators.GreaterThanValidator(threshold, true);

        String testValue = "100";

        assertTrue("Less validator should accept threshold value", lessValidator.validateInput(testValue));
        assertTrue("Greater validator should accept threshold value", greaterValidator.validateInput(testValue));

        String lowerValue = "50";
        assertTrue("Less validator should accept lower value", lessValidator.validateInput(lowerValue));
        assertFalse("Greater validator should reject lower value", greaterValidator.validateInput(lowerValue));

        String higherValue = "150";
        assertFalse("Less validator should reject higher value", lessValidator.validateInput(higherValue));
        assertTrue("Greater validator should accept higher value", greaterValidator.validateInput(higherValue));
    }
}
