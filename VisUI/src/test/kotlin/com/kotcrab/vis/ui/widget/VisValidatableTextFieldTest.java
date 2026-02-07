package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link VisValidatableTextField}.
 */
public class VisValidatableTextFieldTest {

    @Mock
    private VisTextFieldStyle mockStyle;
    @Mock
    private Drawable mockDrawable;
    @Mock
    private BitmapFont mockFont;
    @Mock
    private InputValidator mockValidator1;
    @Mock
    private InputValidator mockValidator2;

    private VisValidatableTextField textField;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        // Setup mock style
        mockStyle.font = mockFont;
        mockStyle.background = mockDrawable;
        mockStyle.cursor = mockDrawable;
        mockStyle.selection = mockDrawable;
        
        // Create text field with mock style
        textField = new VisValidatableTextField("Test", mockStyle);
    }

    @Test
    public void testConstructorWithNoArgs() {
        VisValidatableTextField field = new VisValidatableTextField();
        assertEquals("", field.getText());
        assertTrue("Programmatic change events should be enabled by default", field.getProgrammaticChangeEvents());
    }

    @Test
    public void testConstructorWithText() {
        VisValidatableTextField field = new VisValidatableTextField("Hello");
        assertEquals("Hello", field.getText());
        assertTrue("Programmatic change events should be enabled by default", field.getProgrammaticChangeEvents());
    }

    @Test
    public void testConstructorWithTextAndStyleName() {
        VisValidatableTextField field = new VisValidatableTextField("Hello", "default");
        assertEquals("Hello", field.getText());
        assertTrue("Programmatic change events should be enabled by default", field.getProgrammaticChangeEvents());
    }

    @Test
    public void testConstructorWithTextAndStyle() {
        VisValidatableTextField field = new VisValidatableTextField("Hello", mockStyle);
        assertEquals("Hello", field.getText());
        assertEquals(mockStyle, field.getStyle());
        assertTrue("Programmatic change events should be enabled by default", field.getProgrammaticChangeEvents());
    }

    @Test
    public void testConstructorWithValidator() {
        VisValidatableTextField field = new VisValidatableTextField(mockValidator1);
        assertEquals("", field.getText());
        assertEquals("Should have 1 validator", 1, field.getValidators().size);
        assertTrue("Should contain the validator", field.getValidators().contains(mockValidator1, true));
    }

    @Test
    public void testConstructorWithMultipleValidators() {
        VisValidatableTextField field = new VisValidatableTextField(mockValidator1, mockValidator2);
        assertEquals("", field.getText());
        assertEquals("Should have 2 validators", 2, field.getValidators().size);
        assertTrue("Should contain validator1", field.getValidators().contains(mockValidator1, true));
        assertTrue("Should contain validator2", field.getValidators().contains(mockValidator2, true));
    }

    @Test
    public void testConstructorWithRestoreLastValidAndValidator() {
        VisValidatableTextField field = new VisValidatableTextField(true, mockValidator1);
        assertEquals("", field.getText());
        assertTrue("Restore last valid should be enabled", field.isRestoreLastValid());
        assertEquals("Should have 1 validator", 1, field.getValidators().size);
    }

    @Test
    public void testConstructorWithRestoreLastValidAndMultipleValidators() {
        VisValidatableTextField field = new VisValidatableTextField(true, mockValidator1, mockValidator2);
        assertEquals("", field.getText());
        assertTrue("Restore last valid should be enabled", field.isRestoreLastValid());
        assertEquals("Should have 2 validators", 2, field.getValidators().size);
    }

    @Test
    public void testAddValidator() {
        assertEquals("Should have 0 validators initially", 0, textField.getValidators().size);
        
        textField.addValidator(mockValidator1);
        assertEquals("Should have 1 validator", 1, textField.getValidators().size);
        assertTrue("Should contain the validator", textField.getValidators().contains(mockValidator1, true));
        
        textField.addValidator(mockValidator2);
        assertEquals("Should have 2 validators", 2, textField.getValidators().size);
        assertTrue("Should contain both validators", 
            textField.getValidators().contains(mockValidator1, true) && 
            textField.getValidators().contains(mockValidator2, true));
    }

    @Test
    public void testGetValidators() {
        textField.addValidator(mockValidator1);
        textField.addValidator(mockValidator2);
        
        assertEquals("Should return the validators array", 2, textField.getValidators().size);
        assertTrue("Should contain validator1", textField.getValidators().contains(mockValidator1, true));
        assertTrue("Should contain validator2", textField.getValidators().contains(mockValidator2, true));
    }

    @Test
    public void testValidationEnabledByDefault() {
        assertTrue("Validation should be enabled by default", textField.isValidationEnabled());
    }

    @Test
    public void testSetValidationEnabled() {
        textField.setValidationEnabled(false);
        assertFalse("Validation should be disabled", textField.isValidationEnabled());
        
        textField.setValidationEnabled(true);
        assertTrue("Validation should be enabled", textField.isValidationEnabled());
    }

    @Test
    public void testValidateInputWithAllValidValidators() {
        when(mockValidator1.validateInput(anyString())).thenReturn(true);
        when(mockValidator2.validateInput(anyString())).thenReturn(true);
        
        textField.addValidator(mockValidator1);
        textField.addValidator(mockValidator2);
        textField.setText("valid");
        
        assertTrue("Input should be valid when all validators pass", textField.isInputValid());
        verify(mockValidator1).validateInput("valid");
        verify(mockValidator2).validateInput("valid");
    }

    @Test
    public void testValidateInputWithFirstInvalidValidator() {
        when(mockValidator1.validateInput(anyString())).thenReturn(false);
        when(mockValidator2.validateInput(anyString())).thenReturn(true);
        
        textField.addValidator(mockValidator1);
        textField.addValidator(mockValidator2);
        textField.setText("invalid");
        
        assertFalse("Input should be invalid when first validator fails", textField.isInputValid());
        verify(mockValidator1).validateInput("invalid");
        verify(mockValidator2, never()).validateInput(anyString());
    }

    @Test
    public void testValidateInputWithSecondInvalidValidator() {
        when(mockValidator1.validateInput(anyString())).thenReturn(true);
        when(mockValidator2.validateInput(anyString())).thenReturn(false);
        
        textField.addValidator(mockValidator1);
        textField.addValidator(mockValidator2);
        textField.setText("invalid");
        
        assertFalse("Input should be invalid when second validator fails", textField.isInputValid());
        verify(mockValidator1).validateInput("invalid");
        verify(mockValidator2).validateInput("invalid");
    }

    @Test
    public void testValidateInputWhenDisabled() {
        when(mockValidator1.validateInput(anyString())).thenReturn(false);
        
        textField.addValidator(mockValidator1);
        textField.setValidationEnabled(false);
        textField.setText("any");
        
        assertTrue("Input should be valid when validation is disabled", textField.isInputValid());
        verify(mockValidator1, never()).validateInput(anyString());
    }

    @Test
    public void testValidateInputCalledOnSetText() {
        when(mockValidator1.validateInput(anyString())).thenReturn(true);
        
        textField.addValidator(mockValidator1);
        textField.setText("test");
        
        verify(mockValidator1).validateInput("test");
    }

    @Test
    public void testRestoreLastValidDisabledByDefault() {
        assertFalse("Restore last valid should be disabled by default", textField.isRestoreLastValid());
    }

    @Test
    public void testSetRestoreLastValid() {
        textField.setRestoreLastValid(true);
        assertTrue("Restore last valid should be enabled", textField.isRestoreLastValid());
        
        textField.setRestoreLastValid(false);
        assertFalse("Restore last valid should be disabled", textField.isRestoreLastValid());
    }

    @Test
    public void testSetRestoreLastValidWithSelection() {
        textField.setText("test");
        textField.setSelection(0, 2); // Create selection
        
        try {
            textField.setRestoreLastValid(true);
            fail("Should throw IllegalStateException when field has selection");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testRestoreLastValidTextWhenEnabled() {
        textField.setRestoreLastValid(true);
        textField.setText("valid");
        
        try {
            textField.restoreLastValidText();
            fail("Should throw IllegalStateException when restore last valid is enabled but no last valid text exists");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testRestoreLastValidTextWhenDisabled() {
        textField.setRestoreLastValid(false);
        
        try {
            textField.restoreLastValidText();
            fail("Should throw IllegalStateException when restore last valid is disabled");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testWithBuiltInValidators() {
        // Test with numeric validator
        textField.addValidator(Validators.NUMERIC);
        textField.setText("123");
        assertTrue("Numeric text should be valid", textField.isInputValid());
        
        textField.setText("abc");
        assertFalse("Non-numeric text should be invalid", textField.isInputValid());
        
        // Test with email validator
        textField.getValidators().clear();
        textField.addValidator(Validators.EMAIL);
        textField.setText("test@example.com");
        assertTrue("Valid email should be valid", textField.isInputValid());
        
        textField.setText("invalid-email");
        assertFalse("Invalid email should be invalid", textField.isInputValid());
    }

    @Test
    public void testValidationWithEmptyText() {
        when(mockValidator1.validateInput(anyString())).thenReturn(true);
        
        textField.addValidator(mockValidator1);
        textField.setText("");
        
        verify(mockValidator1).validateInput("");
        assertTrue("Empty text validation should work", textField.isInputValid());
    }

    @Test
    public void testValidationWithNullText() {
        when(mockValidator1.validateInput(anyString())).thenReturn(true);
        
        textField.addValidator(mockValidator1);
        textField.setText(null);
        
        verify(mockValidator1).validateInput("");
        assertTrue("Null text should be treated as empty string", textField.isInputValid());
    }

    @Test
    public void testProgrammaticChangeEventsEnabled() {
        assertTrue("Programmatic change events should be enabled by default", textField.getProgrammaticChangeEvents());
    }

    @Test
    public void testIgnoreEqualsTextChangeDisabled() {
        // This is tested indirectly through the constructor behavior
        // The field should validate even when setting the same text
        when(mockValidator1.validateInput(anyString())).thenReturn(true);
        
        textField.addValidator(mockValidator1);
        textField.setText("test");
        textField.setText("test"); // Same text
        
        // Validator should be called twice (once for each setText)
        verify(mockValidator1, times(2)).validateInput("test");
    }
}
