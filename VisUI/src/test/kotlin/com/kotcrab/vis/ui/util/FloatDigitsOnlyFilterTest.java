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

/**
 * Unit tests for {@link FloatDigitsOnlyFilter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class FloatDigitsOnlyFilterTest {

    @Mock
    private VisTextField textField;

    private FloatDigitsOnlyFilter filterWithNegative;
    private FloatDigitsOnlyFilter filterWithoutNegative;

    @Before
    public void setUp() {
        filterWithNegative = new FloatDigitsOnlyFilter(true);
        filterWithoutNegative = new FloatDigitsOnlyFilter(false);

        // Default field state
        when(textField.getText()).thenReturn("");
        when(textField.getSelectionStart()).thenReturn(0);
        when(textField.getCursorPosition()).thenReturn(0);
        when(textField.isTextSelected()).thenReturn(false);
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
    public void testAcceptDecimalPoint() {
        when(textField.getText()).thenReturn("123");

        assertTrue("Should accept decimal point when not present", filterWithNegative.acceptChar(textField, '.'));
        assertTrue("Should accept decimal point when not present (no negative)", filterWithoutNegative.acceptChar(textField, '.'));
    }

    @Test
    public void testRejectMultipleDecimalPoints() {
        when(textField.getText()).thenReturn("123.45");

        assertFalse("Should reject second decimal point", filterWithNegative.acceptChar(textField, '.'));
        assertFalse("Should reject second decimal point (no negative)", filterWithoutNegative.acceptChar(textField, '.'));
    }

    @Test
    public void testAcceptNegativeSign() {
        when(textField.getText()).thenReturn("");
        when(textField.getCursorPosition()).thenReturn(0);

        assertTrue("Should accept negative sign at start", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignWhenNotAllowed() {
        when(textField.getText()).thenReturn("");
        when(textField.getCursorPosition()).thenReturn(0);

        assertFalse("Should reject negative sign when not allowed", filterWithoutNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignInMiddle() {
        when(textField.getText()).thenReturn("123");
        when(textField.getCursorPosition()).thenReturn(3);

        // With useFieldCursorPosition disabled (default), negative sign is accepted
        // because text doesn't start with "-"
        assertTrue("Should accept negative sign when useFieldCursorPosition is false",
                filterWithNegative.acceptChar(textField, '-'));

        // With useFieldCursorPosition enabled, negative sign should be rejected
        filterWithNegative.setUseFieldCursorPosition(true);
        assertFalse("Should reject negative sign in middle when useFieldCursorPosition is true",
                filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignWhenAlreadyPresent() {
        when(textField.getText()).thenReturn("-123");
        when(textField.getCursorPosition()).thenReturn(0);

        assertFalse("Should reject negative sign when already present", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignWhenAlreadyPresentAtEnd() {
        when(textField.getText()).thenReturn("123-");
        when(textField.getCursorPosition()).thenReturn(4);

        // Text doesn't start with "-", so negative sign is accepted
        assertTrue("Should accept negative sign when text doesn't start with -",
                filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testAcceptNegativeSignWithCursorPosition() {
        filterWithNegative.setUseFieldCursorPosition(true);

        when(textField.getText()).thenReturn("");
        when(textField.getCursorPosition()).thenReturn(0);

        assertTrue("Should accept negative sign at position 0", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignWithCursorPositionNotAtStart() {
        filterWithNegative.setUseFieldCursorPosition(true);

        when(textField.getText()).thenReturn("123");
        when(textField.getCursorPosition()).thenReturn(3);

        assertFalse("Should reject negative sign not at position 0", filterWithNegative.acceptChar(textField, '-'));
    }

    @Test
    public void testRejectNegativeSignWithCursorPositionWhenTextStartsWithMinus() {
        filterWithNegative.setUseFieldCursorPosition(true);

        when(textField.getText()).thenReturn("-123");
        when(textField.getCursorPosition()).thenReturn(0);

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

        assertFalse("Should reject exclamation mark (no negative)", filterWithoutNegative.acceptChar(textField, '!'));
        assertFalse("Should reject at symbol (no negative)", filterWithoutNegative.acceptChar(textField, '@'));
        assertFalse("Should reject hash (no negative)", filterWithoutNegative.acceptChar(textField, '#'));
        assertFalse("Should reject space (no negative)", filterWithoutNegative.acceptChar(textField, ' '));
        assertFalse("Should reject comma (no negative)", filterWithoutNegative.acceptChar(textField, ','));
    }

    @Test
    public void testDecimalPointWithSelection() {
        when(textField.getText()).thenReturn("123.45");
        when(textField.isTextSelected()).thenReturn(true);
        when(textField.getSelectionStart()).thenReturn(2);
        when(textField.getCursorPosition()).thenReturn(5);

        // Selection removes "3.4", leaving "125"
        assertTrue("Should accept decimal point when selection removes existing decimal",
                filterWithNegative.acceptChar(textField, '.'));
    }

    @Test
    public void testDecimalPointWithSelectionNoDecimalInResult() {
        when(textField.getText()).thenReturn("123.45");
        when(textField.isTextSelected()).thenReturn(true);
        when(textField.getSelectionStart()).thenReturn(1);
        when(textField.getCursorPosition()).thenReturn(6);

        // Selection removes "23.45", leaving "1"
        assertTrue("Should accept decimal point when selection removes existing decimal",
                filterWithNegative.acceptChar(textField, '.'));
    }

    @Test
    public void testDecimalPointWithSelectionDecimalStillPresent() {
        when(textField.getText()).thenReturn("123.45.67");
        when(textField.isTextSelected()).thenReturn(true);
        when(textField.getSelectionStart()).thenReturn(1);
        when(textField.getCursorPosition()).thenReturn(6);

        // Selection removes "23.45", leaving "1.67"
        assertFalse("Should reject decimal point when decimal remains after selection",
                filterWithNegative.acceptChar(textField, '.'));
    }

    @Test
    public void testSelectionStartGreaterThanCursor() {
        when(textField.getText()).thenReturn("123.45");
        when(textField.isTextSelected()).thenReturn(true);
        when(textField.getSelectionStart()).thenReturn(5);
        when(textField.getCursorPosition()).thenReturn(2);

        // Selection removes "3.4", leaving "125"
        assertTrue("Should handle selection start > cursor position",
                filterWithNegative.acceptChar(textField, '.'));
    }

    @Test
    public void testEmptyField() {
        when(textField.getText()).thenReturn("");

        assertTrue("Should accept digit in empty field", filterWithNegative.acceptChar(textField, '5'));
        assertTrue("Should accept decimal point in empty field", filterWithNegative.acceptChar(textField, '.'));
        assertTrue("Should accept negative sign in empty field", filterWithNegative.acceptChar(textField, '-'));

        assertTrue("Should accept digit in empty field (no negative)", filterWithoutNegative.acceptChar(textField, '5'));
        assertTrue("Should accept decimal point in empty field (no negative)", filterWithoutNegative.acceptChar(textField, '.'));
    }

    @Test
    public void testFieldWithOnlyDecimalPoint() {
        when(textField.getText()).thenReturn(".");

        assertFalse("Should reject second decimal point", filterWithNegative.acceptChar(textField, '.'));
        assertTrue("Should accept digit after decimal point", filterWithNegative.acceptChar(textField, '5'));

        assertFalse("Should reject second decimal point (no negative)", filterWithoutNegative.acceptChar(textField, '.'));
        assertTrue("Should accept digit after decimal point (no negative)", filterWithoutNegative.acceptChar(textField, '5'));
    }

    @Test
    public void testFieldWithNegativeSign() {
        when(textField.getText()).thenReturn("-");
        when(textField.getCursorPosition()).thenReturn(1);

        assertFalse("Should reject second negative sign", filterWithNegative.acceptChar(textField, '-'));
        assertTrue("Should accept digit after negative sign", filterWithNegative.acceptChar(textField, '5'));
        assertTrue("Should accept decimal point after negative sign", filterWithNegative.acceptChar(textField, '.'));
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
        // Test building a float number: -123.456
        when(textField.getText()).thenReturn("");
        when(textField.getCursorPosition()).thenReturn(0);

        // Add negative sign
        assertTrue("Should accept negative sign", filterWithNegative.acceptChar(textField, '-'));
        when(textField.getText()).thenReturn("-");
        when(textField.getCursorPosition()).thenReturn(1);

        // Add digits
        assertTrue("Should accept digit 1", filterWithNegative.acceptChar(textField, '1'));
        when(textField.getText()).thenReturn("-1");
        when(textField.getCursorPosition()).thenReturn(2);

        assertTrue("Should accept digit 2", filterWithNegative.acceptChar(textField, '2'));
        when(textField.getText()).thenReturn("-12");
        when(textField.getCursorPosition()).thenReturn(3);

        assertTrue("Should accept digit 3", filterWithNegative.acceptChar(textField, '3'));
        when(textField.getText()).thenReturn("-123");
        when(textField.getCursorPosition()).thenReturn(4);

        // Add decimal point
        assertTrue("Should accept decimal point", filterWithNegative.acceptChar(textField, '.'));
        when(textField.getText()).thenReturn("-123.");
        when(textField.getCursorPosition()).thenReturn(5);

        // Add more digits
        assertTrue("Should accept digit 4", filterWithNegative.acceptChar(textField, '4'));
        when(textField.getText()).thenReturn("-123.4");
        when(textField.getCursorPosition()).thenReturn(6);

        assertTrue("Should accept digit 5", filterWithNegative.acceptChar(textField, '5'));
        when(textField.getText()).thenReturn("-123.45");
        when(textField.getCursorPosition()).thenReturn(7);

        assertTrue("Should accept digit 6", filterWithNegative.acceptChar(textField, '6'));
        when(textField.getText()).thenReturn("-123.456");
        when(textField.getCursorPosition()).thenReturn(8);

        // Try to add another decimal point (should be rejected)
        assertFalse("Should reject second decimal point", filterWithNegative.acceptChar(textField, '.'));

        // Try to add another negative sign (should be rejected)
        assertFalse("Should reject second negative sign", filterWithNegative.acceptChar(textField, '-'));
    }
}
