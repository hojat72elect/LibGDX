package com.kotcrab.vis.ui.util.form;

import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ValidatorWrapper}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidatorWrapperTest {

    @Mock
    private InputValidator mockValidator;
    
    private ValidatorWrapper validatorWrapper;
    private static final String ERROR_MESSAGE = "Test error message";

    @Before
    public void setUp() {
        validatorWrapper = new ValidatorWrapper(ERROR_MESSAGE, mockValidator);
    }

    @Test
    public void testConstructor() {
        ValidatorWrapper wrapper = new ValidatorWrapper("Custom error", mockValidator);
        
        assertEquals("Error message should be set", "Custom error", wrapper.getErrorMsg());
        assertTrue("Should extend FormInputValidator", wrapper instanceof FormInputValidator);
    }

    @Test
    public void testConstructorWithNullValidator() {
        ValidatorWrapper wrapper = new ValidatorWrapper(ERROR_MESSAGE, null);
        
        assertEquals("Error message should be set", ERROR_MESSAGE, wrapper.getErrorMsg());
        assertTrue("Should extend FormInputValidator", wrapper instanceof FormInputValidator);
    }

    @Test
    public void testConstructorWithNullMessage() {
        ValidatorWrapper wrapper = new ValidatorWrapper(null, mockValidator);
        
        assertNull("Error message should be null", wrapper.getErrorMsg());
        assertTrue("Should extend FormInputValidator", wrapper instanceof FormInputValidator);
    }

    @Test
    public void testValidateInputDelegatesToWrappedValidator() {
        String testInput = "test input";
        boolean expectedResult = true;
        
        when(mockValidator.validateInput(testInput)).thenReturn(expectedResult);
        
        boolean result = validatorWrapper.validateInput(testInput);
        
        assertEquals("Should delegate to wrapped validator", expectedResult, result);
        verify(mockValidator).validateInput(testInput);
    }

    @Test
    public void testValidateInputWithNullInput() {
        String testInput = null;
        boolean expectedResult = false;
        
        when(mockValidator.validateInput(testInput)).thenReturn(expectedResult);
        
        boolean result = validatorWrapper.validateInput(testInput);
        
        assertEquals("Should delegate null input to wrapped validator", expectedResult, result);
        verify(mockValidator).validateInput(testInput);
    }

    @Test
    public void testValidateInputWithEmptyString() {
        String testInput = "";
        boolean expectedResult = true;
        
        when(mockValidator.validateInput(testInput)).thenReturn(expectedResult);
        
        boolean result = validatorWrapper.validateInput(testInput);
        
        assertEquals("Should delegate empty string to wrapped validator", expectedResult, result);
        verify(mockValidator).validateInput(testInput);
    }

    @Test
    public void testMultipleValidations() {
        String input1 = "input1";
        String input2 = "input2";
        String input3 = "input3";
        
        when(mockValidator.validateInput(input1)).thenReturn(true);
        when(mockValidator.validateInput(input2)).thenReturn(false);
        when(mockValidator.validateInput(input3)).thenReturn(true);
        
        assertTrue("First validation should be true", validatorWrapper.validateInput(input1));
        assertFalse("Second validation should be false", validatorWrapper.validateInput(input2));
        assertTrue("Third validation should be true", validatorWrapper.validateInput(input3));
        
        verify(mockValidator).validateInput(input1);
        verify(mockValidator).validateInput(input2);
        verify(mockValidator).validateInput(input3);
    }

    @Test
    public void testInheritanceFromFormInputValidator() {
        // Test that ValidatorWrapper inherits FormInputValidator behavior
        assertTrue("Should extend FormInputValidator", validatorWrapper instanceof FormInputValidator);
        
        // Test hideErrorOnEmptyInput functionality
        FormInputValidator result = validatorWrapper.hideErrorOnEmptyInput();
        assertSame("Should return same instance", validatorWrapper, result);
        assertTrue("Hide error on empty input should be true", validatorWrapper.isHideErrorOnEmptyInput());
    }

    @Test
    public void testGetErrorMsg() {
        assertEquals("Should return error message", ERROR_MESSAGE, validatorWrapper.getErrorMsg());
    }

    @Test
    public void testSetErrorMsg() {
        String newMessage = "New error message";
        validatorWrapper.setErrorMsg(newMessage);
        
        assertEquals("Error message should be updated", newMessage, validatorWrapper.getErrorMsg());
    }

    @Test
    public void testGetLastResult() {
        // Setup mock validator to return specific result
        when(mockValidator.validateInput(anyString())).thenReturn(true);
        
        validatorWrapper.validateInput("test");
        
        assertTrue("Last result should be true", validatorWrapper.getLastResult());
        
        // Change mock behavior
        when(mockValidator.validateInput(anyString())).thenReturn(false);
        validatorWrapper.validateInput("test2");
        
        assertFalse("Last result should be false", validatorWrapper.getLastResult());
    }

    @Test
    public void testWithRealValidator() {
        // Test with a real validator to ensure integration works
        ValidatorWrapper integerWrapper = new ValidatorWrapper("Must be integer", Validators.INTEGERS);
        
        assertTrue("Valid integer should pass", integerWrapper.validateInput("123"));
        assertFalse("Invalid integer should fail", integerWrapper.validateInput("abc"));
        assertFalse("Empty string should fail", integerWrapper.validateInput(""));
        assertEquals("Error message should be preserved", "Must be integer", integerWrapper.getErrorMsg());
    }

    @Test
    public void testWithFloatsValidator() {
        // Test with another real validator
        ValidatorWrapper floatWrapper = new ValidatorWrapper("Must be float", Validators.FLOATS);
        
        assertTrue("Valid float should pass", floatWrapper.validateInput("123.45"));
        assertTrue("Integer should pass as float", floatWrapper.validateInput("123"));
        assertFalse("Invalid float should fail", floatWrapper.validateInput("abc"));
        assertEquals("Error message should be preserved", "Must be float", floatWrapper.getErrorMsg());
    }

    @Test
    public void testWrapperDoesNotInterfereWithOriginalValidator() {
        // Test that wrapping doesn't affect the original validator
        String testInput = "test";
        
        when(mockValidator.validateInput(testInput)).thenReturn(true);
        
        // Test through wrapper
        boolean wrapperResult = validatorWrapper.validateInput(testInput);
        
        // Test original validator directly
        boolean originalResult = mockValidator.validateInput(testInput);
        
        assertEquals("Wrapper and original should return same result", wrapperResult, originalResult);
        verify(mockValidator, times(2)).validateInput(testInput);
    }
}
