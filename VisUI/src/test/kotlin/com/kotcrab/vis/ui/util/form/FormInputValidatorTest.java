package com.kotcrab.vis.ui.util.form;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link FormInputValidator}.
 */
@RunWith(MockitoJUnitRunner.class)
public class FormInputValidatorTest {

    private TestFormInputValidator validator;
    private static final String ERROR_MESSAGE = "Test error message";

    @Before
    public void setUp() {
        validator = new TestFormInputValidator(ERROR_MESSAGE);
    }

    @Test
    public void testConstructor() {
        TestFormInputValidator testValidator = new TestFormInputValidator("Custom error");
        
        assertEquals("Error message should be set", "Custom error", testValidator.getErrorMsg());
        assertFalse("Hide error on empty input should be false by default", testValidator.isHideErrorOnEmptyInput());
    }

    @Test
    public void testConstructorWithNullMessage() {
        TestFormInputValidator testValidator = new TestFormInputValidator(null);
        
        assertNull("Error message should be null", testValidator.getErrorMsg());
    }

    @Test
    public void testValidateInputCallsValidateMethod() {
        validator.validateInput("test");
        
        assertTrue("Validate method should be called", validator.isValidateCalled());
        assertEquals("Input should be passed to validate method", "test", validator.getLastValidatedInput());
    }

    @Test
    public void testValidateInputReturnsResult() {
        validator.setValidationResult(true);
        assertTrue("Should return true when validation result is true", validator.validateInput("test"));
        
        validator.setValidationResult(false);
        assertFalse("Should return false when validation result is false", validator.validateInput("test"));
    }

    @Test
    public void testValidateInputStoresResult() {
        validator.setValidationResult(true);
        validator.validateInput("test");
        assertTrue("Last result should be stored as true", validator.getLastResult());
        
        validator.setValidationResult(false);
        validator.validateInput("test2");
        assertFalse("Last result should be stored as false", validator.getLastResult());
    }

    @Test
    public void testValidateInputWithEmptyString() {
        validator.validateInput("");
        
        assertTrue("Validate method should be called even with empty string", validator.isValidateCalled());
        assertEquals("Empty string should be passed to validate method", "", validator.getLastValidatedInput());
    }

    @Test
    public void testValidateInputWithNullInput() {
        validator.validateInput(null);
        
        assertTrue("Validate method should be called even with null input", validator.isValidateCalled());
        assertEquals("Null input should be passed to validate method", null, validator.getLastValidatedInput());
    }

    @Test
    public void testHideErrorOnEmptyInput() {
        FormInputValidator result = validator.hideErrorOnEmptyInput();
        
        assertSame("Should return same instance", validator, result);
        assertTrue("Hide error on empty input should be true", validator.isHideErrorOnEmptyInput());
    }

    @Test
    public void testHideErrorOnEmptyInputChaining() {
        FormInputValidator result = validator.hideErrorOnEmptyInput().hideErrorOnEmptyInput();
        
        assertSame("Should return same instance", validator, result);
        assertTrue("Hide error on empty input should be true", validator.isHideErrorOnEmptyInput());
    }

    @Test
    public void testSetHideErrorOnEmptyInput() {
        validator.setHideErrorOnEmptyInput(true);
        assertTrue("Hide error on empty input should be true", validator.isHideErrorOnEmptyInput());
        
        validator.setHideErrorOnEmptyInput(false);
        assertFalse("Hide error on empty input should be false", validator.isHideErrorOnEmptyInput());
    }

    @Test
    public void testGetErrorMsg() {
        assertEquals("Should return error message", ERROR_MESSAGE, validator.getErrorMsg());
    }

    @Test
    public void testSetErrorMsg() {
        String newMessage = "New error message";
        validator.setErrorMsg(newMessage);
        
        assertEquals("Error message should be updated", newMessage, validator.getErrorMsg());
    }

    @Test
    public void testSetErrorMsgToNull() {
        validator.setErrorMsg(null);
        
        assertNull("Error message should be null", validator.getErrorMsg());
    }

    @Test
    public void testGetLastResultBeforeValidation() {
        // Before any validation, last result should be false (default)
        assertFalse("Last result should be false by default", validator.getLastResult());
    }

    @Test
    public void testGetLastResultAfterValidation() {
        validator.setValidationResult(true);
        validator.validateInput("test");
        
        assertTrue("Last result should be true after validation", validator.getLastResult());
    }

    @Test
    public void testMultipleValidations() {
        // First validation
        validator.setValidationResult(true);
        validator.validateInput("test1");
        assertTrue("First validation should be true", validator.getLastResult());
        
        // Second validation
        validator.setValidationResult(false);
        validator.validateInput("test2");
        assertFalse("Second validation should be false", validator.getLastResult());
        
        // Third validation
        validator.setValidationResult(true);
        validator.validateInput("test3");
        assertTrue("Third validation should be true", validator.getLastResult());
    }

    @Test
    public void testImplementsInputValidator() {
        assertTrue("FormInputValidator should implement InputValidator", validator instanceof com.kotcrab.vis.ui.util.InputValidator);
    }

    @Test
    public void testAbstractValidateMethod() {
        // Test that the abstract validate method is properly abstract
        // This is more of a compilation test, but we can verify the behavior
        TestFormInputValidator testValidator = new TestFormInputValidator(ERROR_MESSAGE) {
            @Override
            protected boolean validate(String input) {
                return input != null && input.length() > 5;
            }
        };
        
        assertFalse("Short input should be invalid", testValidator.validateInput("abc"));
        assertTrue("Long input should be valid", testValidator.validateInput("abcdef"));
        assertFalse("Null input should be invalid", testValidator.validateInput(null));
    }

    @Test
    public void testValidationResultPersistence() {
        // Test that the validation result persists correctly
        validator.setValidationResult(true);
        validator.validateInput("persistent");
        
        // Multiple calls to getLastResult should return the same value
        assertTrue("First call should return true", validator.getLastResult());
        assertTrue("Second call should return true", validator.getLastResult());
        assertTrue("Third call should return true", validator.getLastResult());
    }

    @Test
    public void testErrorMessageIndependence() {
        // Test that error message is independent of validation result
        validator.setErrorMsg("Independent message");
        
        validator.setValidationResult(false);
        validator.validateInput("test");
        
        assertEquals("Error message should remain unchanged", "Independent message", validator.getErrorMsg());
    }

    /**
     * Test implementation of FormInputValidator for testing purposes.
     */
    private static class TestFormInputValidator extends FormInputValidator {
        private boolean validateCalled = false;
        private String lastValidatedInput;
        private boolean validationResult = false;

        public TestFormInputValidator(String errorMsg) {
            super(errorMsg);
        }

        @Override
        protected boolean validate(String input) {
            validateCalled = true;
            lastValidatedInput = input;
            return validationResult;
        }

        public boolean isValidateCalled() {
            return validateCalled;
        }

        public String getLastValidatedInput() {
            return lastValidatedInput;
        }

        public void setValidationResult(boolean result) {
            this.validationResult = result;
        }

        public void resetValidationState() {
            validateCalled = false;
            lastValidatedInput = null;
        }
    }
}
