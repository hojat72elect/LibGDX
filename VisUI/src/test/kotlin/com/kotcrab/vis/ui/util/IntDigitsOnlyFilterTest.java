package com.kotcrab.vis.ui.util;

import com.kotcrab.vis.ui.widget.VisTextField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

/**
 * Unit tests for {@link IntDigitsOnlyFilter}.
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class IntDigitsOnlyFilterTest {

    @Mock
    private VisTextField textField;

    private IntDigitsOnlyFilter filterWithNegative;
    private IntDigitsOnlyFilter filterWithoutNegative;

    @Before
    public void setUp() {
        filterWithNegative = new IntDigitsOnlyFilter(true);
        filterWithoutNegative = new IntDigitsOnlyFilter(false);

        // Default field state - only stub what's always used
        when(textField.getText()).thenReturn("");
    }

    @Test
    public void testAcceptDigits() {
        assertTrue("Should accept digit 0", filterWithNegative.acceptChar(textField, '0'));
        assertTrue("Should accept digit 5", filterWithNegative.acceptChar(textField, '5'));
        assertTrue("Should accept digit 9", filterWithNegative.acceptChar(textField, '9'));

        assertTrue("Should accept digit 0 (no negative)", filterWithoutNegative.acceptChar(textField, '0'));
        assertTrue("Should accept digit 5 (no negative)", filterWithoutNegative.acceptChar(textField, '5'));
        assertTrue("Should accept digit 9 (no negative)", filterWithoutNegative.acceptChar(textField, '9'));
    }

    @Test
    public void testAcceptNegativeSign() {
        when(textField.getText()).thenReturn("");

        assertTrue("Should accept negative sign at start", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignWhenNotAllowed() {
        when(textField.getText()).thenReturn("");

        assertFalse("Should reject negative sign when not allowed", filterWithoutNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignInMiddle() {
        when(textField.getText()).thenReturn("123");

        // With useFieldCursorPosition disabled (default), negative sign is accepted
        // because text doesn't start with "-"
        assertTrue("Should accept negative sign when useFieldCursorPosition is false",
                filterWithNegative.acceptChar(textField, '-'));

        // With useFieldCursorPosition enabled, negative sign should be rejected
        filterWithNegative.setUseFieldCursorPosition(true);
        lenient().when(textField.getCursorPosition()).thenReturn(3);
        assertFalse("Should reject negative sign in middle when useFieldCursorPosition is true",
                filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignWhenAlreadyPresent() {
        when(textField.getText()).thenReturn("-123");

        assertFalse("Should reject negative sign when already present", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignWhenAlreadyPresentAtEnd() {
        when(textField.getText()).thenReturn("123-");

        // Text doesn't start with "-", so negative sign is accepted
        assertTrue("Should accept negative sign when text doesn't start with -",
                filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testAcceptNegativeSignWithCursorPosition() {
        filterWithNegative.setUseFieldCursorPosition(true);

        when(textField.getText()).thenReturn("");
        lenient().when(textField.getCursorPosition()).thenReturn(0);

        assertTrue("Should accept negative sign at position 0", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignWithCursorPositionNotAtStart() {
        filterWithNegative.setUseFieldCursorPosition(true);

        when(textField.getText()).thenReturn("123");
        lenient().when(textField.getCursorPosition()).thenReturn(3);

        assertFalse("Should reject negative sign not at position 0", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignWithCursorPositionWhenTextStartsWithMinus() {
        filterWithNegative.setUseFieldCursorPosition(true);

        when(textField.getText()).thenReturn("-123");
        lenient().when(textField.getCursorPosition()).thenReturn(0);

        assertFalse("Should reject negative sign when text starts with minus", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectLetters() {
        assertFalse("Should reject letter a", filterWithNegative.acceptChar(textField, 'a'));
        assertFalse("Should reject letter Z", filterWithNegative.acceptChar(textField, 'Z'));
        assertFalse("Should reject letter a (no negative)", filterWithoutNegative.acceptChar(textField, 'a'));
        assertFalse("Should reject letter Z (no negative)", filterWithoutNegative.acceptChar(textField, 'Z'));
    }

    @Test
    public void testRejectSpecialCharacters() {
        assertFalse("Should reject exclamation mark", filterWithNegative.acceptChar(textField, '!'));
        assertFalse("Should reject at symbol", filterWithNegative.acceptChar(textField, '@'));
        assertFalse("Should reject hash", filterWithNegative.acceptChar(textField, '#'));
        assertFalse("Should reject space", filterWithNegative.acceptChar(textField, ' '));
        assertFalse("Should reject comma", filterWithNegative.acceptChar(textField, ','));
        assertFalse("Should reject decimal point", filterWithNegative.acceptChar(textField, '.'));

        assertFalse("Should reject exclamation mark (no negative)", filterWithoutNegative.acceptChar(textField, '!'));
        assertFalse("Should reject at symbol (no negative)", filterWithoutNegative.acceptChar(textField, '@'));
        assertFalse("Should reject hash (no negative)", filterWithoutNegative.acceptChar(textField, '#'));
        assertFalse("Should reject space (no negative)", filterWithoutNegative.acceptChar(textField, ' '));
        assertFalse("Should reject comma (no negative)", filterWithoutNegative.acceptChar(textField, ','));
        assertFalse("Should reject decimal point (no negative)", filterWithoutNegative.acceptChar(textField, '.'));
    }

    @Test
    public void testEmptyField() {
        when(textField.getText()).thenReturn("");

        assertTrue("Should accept digit in empty field", filterWithNegative.acceptChar(textField, '5'));
        assertTrue("Should accept negative sign in empty field", filterWithNegative.acceptChar(textField, '-'));

        assertTrue("Should accept digit in empty field (no negative)", filterWithoutNegative.acceptChar(textField, '5'));
    }

    @Test
    public void testFieldWithNegativeSign() {
        when(textField.getText()).thenReturn("-");

        assertFalse("Should reject second negative sign", filterWithNegative.acceptChar(textField, '-'));
        assertTrue("Should accept digit after negative sign", filterWithNegative.acceptChar(textField, '5'));
    }

    @Test
    public void testInheritedMethods() {
        assertTrue("Should inherit acceptNegativeValues setting", filterWithNegative.isAcceptNegativeValues());
        assertFalse("Should inherit acceptNegativeValues setting", filterWithoutNegative.isAcceptNegativeValues());

        filterWithNegative.setAcceptNegativeValues(false);
        assertFalse("Should allow changing acceptNegativeValues", filterWithNegative.isAcceptNegativeValues());

        assertFalse("Should inherit useFieldCursorPosition default", filterWithNegative.isUseFieldCursorPosition());

        filterWithNegative.setUseFieldCursorPosition(true);
        assertTrue("Should allow changing useFieldCursorPosition", filterWithNegative.isUseFieldCursorPosition());
    }

    @Test
    public void testComplexScenario() {
        // Test building an integer number: -123456
        when(textField.getText()).thenReturn("");

        // Add negative sign
        assertTrue("Should accept negative sign", filterWithNegative.acceptChar(textField, '-'));
        when(textField.getText()).thenReturn("-");

        // Add digits
        assertTrue("Should accept digit 1", filterWithNegative.acceptChar(textField, '1'));
        when(textField.getText()).thenReturn("-1");

        assertTrue("Should accept digit 2", filterWithNegative.acceptChar(textField, '2'));
        when(textField.getText()).thenReturn("-12");

        assertTrue("Should accept digit 3", filterWithNegative.acceptChar(textField, '3'));
        when(textField.getText()).thenReturn("-123");

        assertTrue("Should accept digit 4", filterWithNegative.acceptChar(textField, '4'));
        when(textField.getText()).thenReturn("-1234");

        assertTrue("Should accept digit 5", filterWithNegative.acceptChar(textField, '5'));
        when(textField.getText()).thenReturn("-12345");

        assertTrue("Should accept digit 6", filterWithNegative.acceptChar(textField, '6'));
        when(textField.getText()).thenReturn("-123456");

        // Try to add another negative sign (should be rejected)
        assertFalse("Should reject second negative sign", filterWithNegative.acceptChar(textField, '-'));

        // Try to add decimal point (should be rejected)
        assertFalse("Should reject decimal point", filterWithNegative.acceptChar(textField, '.'));
    }

    @Test
    public void testCursorPositionEdgeCases() {
        filterWithNegative.setUseFieldCursorPosition(true);

        // Test cursor at position 0 with empty text
        when(textField.getText()).thenReturn("");
        lenient().when(textField.getCursorPosition()).thenReturn(0);
        assertTrue("Should accept negative sign at position 0 with empty text", filterWithNegative.acceptChar(textField, '-'));

        // Test cursor at position 0 with text starting with -
        when(textField.getText()).thenReturn("-123");
        lenient().when(textField.getCursorPosition()).thenReturn(0);
        assertFalse("Should reject negative sign when text starts with -", filterWithNegative.acceptChar(textField, '-'));

        // Test cursor at position 1 with text starting with -
        when(textField.getText()).thenReturn("-123");
        lenient().when(textField.getCursorPosition()).thenReturn(1);
        assertFalse("Should reject negative sign when cursor > 0 and text starts with -", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testCursorPositionDisabledBehavior() {
        filterWithNegative.setUseFieldCursorPosition(false);

        // Even with cursor at position 3, negative sign is accepted because text doesn't start with "-"
        when(textField.getText()).thenReturn("123");
        assertTrue("Should accept negative sign when useFieldCursorPosition is false", filterWithNegative.acceptChar(textField, '-'));

        // But if text starts with "-", negative sign is rejected regardless of cursor position
        when(textField.getText()).thenReturn("-123");
        assertFalse("Should reject negative sign when text starts with -", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testNegativeSignAtVariousPositions() {
        // Test different text scenarios with negative signs
        when(textField.getText()).thenReturn("abc");
        assertTrue("Should accept negative sign in non-numeric text", filterWithNegative.acceptChar(textField, '-'));

        when(textField.getText()).thenReturn("123abc");
        assertTrue("Should accept negative sign in mixed text", filterWithNegative.acceptChar(textField, '-'));

        when(textField.getText()).thenReturn("-abc");
        assertFalse("Should reject negative sign when text starts with -", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testDigitAcceptanceInAllScenarios() {
        // Digits should always be accepted regardless of text content
        String[] testTexts = {"", "123", "-123", "abc", "123abc", "-abc", "123.45"};

        for (String text : testTexts) {
            when(textField.getText()).thenReturn(text);

            assertTrue("Should accept digit 5 with text '" + text + "'",
                    filterWithNegative.acceptChar(textField, '5'));
            assertTrue("Should accept digit 5 with text '" + text + "' (no negative)",
                    filterWithoutNegative.acceptChar(textField, '5'));
        }
    }
}
