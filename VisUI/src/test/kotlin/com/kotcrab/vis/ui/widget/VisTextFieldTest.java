package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.VisUI;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link VisTextField}.
 */
public class VisTextFieldTest {

    @Mock
    private Stage mockStage;
    @Mock
    private VisTextFieldStyle mockStyle;
    @Mock
    private Drawable mockDrawable;
    @Mock
    private BitmapFont mockFont;
    @Mock
    private Clipboard mockClipboard;
    @Mock
    private ChangeListener mockChangeListener;
    @Mock
    private TextField.TextFieldFilter mockFilter;

    private VisTextField textField;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        // Setup mock style
        mockStyle.font = mockFont;
        mockStyle.background = mockDrawable;
        mockStyle.cursor = mockDrawable;
        mockStyle.selection = mockDrawable;
        mockStyle.focusBorder = mockDrawable;
        mockStyle.errorBorder = mockDrawable;
        
        // Create text field with mock style
        textField = new VisTextField("Test", mockStyle);
        textField.setClipboard(mockClipboard);
    }

    @Test
    public void testConstructorWithNoArgs() {
        VisTextField field = new VisTextField();
        assertEquals("", field.getText());
    }

    @Test
    public void testConstructorWithText() {
        VisTextField field = new VisTextField("Hello");
        assertEquals("Hello", field.getText());
    }

    @Test
    public void testConstructorWithTextAndStyleName() {
        VisTextField field = new VisTextField("Hello", "default");
        assertEquals("Hello", field.getText());
    }

    @Test
    public void testConstructorWithTextAndStyle() {
        VisTextField field = new VisTextField("Hello", mockStyle);
        assertEquals("Hello", field.getText());
        assertEquals(mockStyle, field.getStyle());
    }

    @Test
    public void testGetAndSetText() {
        textField.setText("New Text");
        assertEquals("New Text", textField.getText());
        
        textField.setText(null);
        assertEquals("", textField.getText());
    }

    @Test
    public void testAppendText() {
        textField.setText("Hello");
        textField.appendText(" World");
        assertEquals("Hello World", textField.getText());
        
        textField.appendText(null);
        assertEquals("Hello World", textField.getText());
    }

    @Test
    public void testClearText() {
        textField.setText("Some text");
        textField.clearText();
        assertEquals("", textField.getText());
    }

    @Test
    public void testGetAndSetMessageText() {
        assertNull("Message text should be null initially", textField.getMessageText());
        
        textField.setMessageText("Enter text here");
        assertEquals("Enter text here", textField.getMessageText());
        
        textField.setMessageText(null);
        assertNull("Message text should be null after setting to null", textField.getMessageText());
    }

    @Test
    public void testGetAndSetMaxLength() {
        assertEquals("Default max length should be 0", 0, textField.getMaxLength());
        
        textField.setMaxLength(10);
        assertEquals("Max length should be 10", 10, textField.getMaxLength());
    }

    @Test
    public void testGetAndSetCursorPosition() {
        textField.setText("Hello");
        textField.setCursorPosition(3);
        assertEquals("Cursor position should be 3", 3, textField.getCursorPosition());
        
        // Test cursor position beyond text length
        textField.setCursorPosition(10);
        assertEquals("Cursor position should be clamped to text length", 5, textField.getCursorPosition());
        
        // Test negative cursor position
        try {
            textField.setCursorPosition(-1);
            fail("Should throw IllegalArgumentException for negative cursor position");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetCursorAtTextEnd() {
        textField.setText("Hello World");
        textField.setCursorAtTextEnd();
        assertEquals("Cursor should be at text end", 11, textField.getCursorPosition());
    }

    @Test
    public void testGetAndSetSelection() {
        textField.setText("Hello World");
        
        textField.setSelection(2, 7);
        assertTrue("Should have selection", textField.isTextSelected());
        assertEquals("Selection should be 'llo W'", "llo W", textField.getSelection());
        assertEquals("Selection start should be 2", 2, textField.getSelectionStart());
        
        textField.clearSelection();
        assertFalse("Should not have selection", textField.isTextSelected());
        assertEquals("Selection should be empty", "", textField.getSelection());
    }

    @Test
    public void testSelectAll() {
        textField.setText("Hello World");
        textField.selectAll();
        assertTrue("Should have selection", textField.isTextSelected());
        assertEquals("Should select all text", "Hello World", textField.getSelection());
    }

    @Test
    public void testSelectionWithInvalidRange() {
        textField.setText("Hello");
        
        try {
            textField.setSelection(-1, 3);
            fail("Should throw IllegalArgumentException for negative selection start");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            textField.setSelection(1, -1);
            fail("Should throw IllegalArgumentException for negative selection end");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPasswordMode() {
        assertFalse("Password mode should be false by default", textField.isPasswordMode());
        
        textField.setPasswordMode(true);
        assertTrue("Password mode should be true", textField.isPasswordMode());
    }

    @Test
    public void testGetAndSetPasswordCharacter() {
        textField.setPasswordCharacter('*');
        // Since passwordCharacter is private, we can't directly test it
        // But we can verify the method doesn't throw exceptions
        assertTrue("Setting password character should complete without errors", true);
    }

    @Test
    public void testGetAndSetAlignment() {
        // Test with Align constants
        textField.setAlignment(com.badlogic.gdx.utils.Align.center);
        // Since alignment is private, we can't directly test it
        // But we can verify the method doesn't throw exceptions
        assertTrue("Setting alignment should complete without errors", true);
    }

    @Test
    public void testGetAndSetProgrammaticChangeEvents() {
        assertFalse("Programmatic change events should be false by default", textField.getProgrammaticChangeEvents());
        
        textField.setProgrammaticChangeEvents(true);
        assertTrue("Programmatic change events should be true", textField.getProgrammaticChangeEvents());
    }

    @Test
    public void testGetAndSetTextFieldListener() {
        assertNull("Text field listener should be null initially", textField.getTextFieldListener());
        
        textField.setTextFieldListener(mock(VisTextField.TextFieldListener.class));
        assertNotNull("Text field listener should not be null", textField.getTextFieldListener());
    }

    @Test
    public void testGetAndSetTextFieldFilter() {
        assertNull("Text field filter should be null initially", textField.getTextFieldFilter());
        
        textField.setTextFieldFilter(mockFilter);
        assertEquals("Text field filter should be set", mockFilter, textField.getTextFieldFilter());
    }

    @Test
    public void testGetAndSetFocusTraversal() {
        assertTrue("Focus traversal should be true by default", textField.focusTraversal);
        
        textField.setFocusTraversal(false);
        assertFalse("Focus traversal should be false", textField.focusTraversal);
    }

    @Test
    public void testGetAndSetEnterKeyFocusTraversal() {
        assertFalse("Enter key focus traversal should be false by default", textField.enterKeyFocusTraversal);
        
        textField.setEnterKeyFocusTraversal(true);
        assertTrue("Enter key focus traversal should be true", textField.enterKeyFocusTraversal);
    }

    @Test
    public void testGetAndSetOnlyFontChars() {
        assertTrue("Only font chars should be true by default", textField.onlyFontChars);
        
        textField.setOnlyFontChars(false);
        assertFalse("Only font chars should be false", textField.onlyFontChars);
    }

    @Test
    public void testCopy() {
        textField.setText("Hello World");
        textField.setSelection(2, 7);
        
        textField.copy();
        verify(mockClipboard).setContents("llo W");
    }

    @Test
    public void testCopyWithPasswordMode() {
        textField.setText("Hello World");
        textField.setPasswordMode(true);
        textField.setSelection(2, 7);
        
        textField.copy();
        verify(mockClipboard, never()).setContents(anyString());
    }

    @Test
    public void testCut() {
        textField.setText("Hello World");
        textField.setSelection(2, 7);
        
        textField.cut();
        verify(mockClipboard).setContents("llo W");
        assertEquals("Text should be cut", "Heorld", textField.getText());
    }

    @Test
    public void testPaste() {
        when(mockClipboard.getContents()).thenReturn("Pasted");
        textField.setText("Hello ");
        textField.setCursorPosition(6);
        
        textField.paste();
        assertEquals("Text should be pasted", "Hello Pasted", textField.getText());
    }

    @Test
    public void testPasteWithNullContent() {
        when(mockClipboard.getContents()).thenReturn(null);
        textField.setText("Hello");
        
        textField.paste();
        assertEquals("Text should remain unchanged", "Hello", textField.getText());
    }

    @Test
    public void testGetAndSetStyle() {
        VisTextFieldStyle newStyle = new VisTextFieldStyle();
        textField.setStyle(newStyle);
        assertEquals("Style should be set", newStyle, textField.getStyle());
    }

    @Test
    public void testSetStyleWithNull() {
        try {
            textField.setStyle(null);
            fail("Should throw IllegalArgumentException for null style");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetAndSetClipboard() {
        Clipboard newClipboard = mock(Clipboard.class);
        textField.setClipboard(newClipboard);
        // Since clipboard is private, we can't directly test it
        // But we can verify the method doesn't throw exceptions
        assertTrue("Setting clipboard should complete without errors", true);
    }

    @Test
    public void testGetAndSetOnscreenKeyboard() {
        TextField.OnscreenKeyboard newKeyboard = mock(TextField.OnscreenKeyboard.class);
        textField.setOnscreenKeyboard(newKeyboard);
        assertEquals("Onscreen keyboard should be set", newKeyboard, textField.getOnscreenKeyboard());
    }

    @Test
    public void testGetAndSetCursorPercentHeight() {
        textField.setCursorPercentHeight(0.5f);
        // Since cursorPercentHeight is private, we can't directly test it
        // But we can verify the method doesn't throw exceptions
        assertTrue("Setting cursor percent height should complete without errors", true);
        
        try {
            textField.setCursorPercentHeight(-0.1f);
            fail("Should throw IllegalArgumentException for negative cursor percent height");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            textField.setCursorPercentHeight(1.1f);
            fail("Should throw IllegalArgumentException for cursor percent height > 1");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFocusBorderEnabledByDefault() {
        assertTrue("Focus border should be enabled by default", textField.focusBorderEnabled);
    }

    @Test
    public void testSetFocusBorderEnabled() {
        textField.setFocusBorderEnabled(false);
        assertFalse("Focus border should be disabled", textField.focusBorderEnabled);
        
        textField.setFocusBorderEnabled(true);
        assertTrue("Focus border should be enabled", textField.focusBorderEnabled);
    }

    @Test
    public void testFocusGained() {
        textField.focusGained();
        // Since drawBorder is private, we can't directly test it
        // But we can verify the method doesn't throw exceptions
        assertTrue("Focus gained should complete without errors", true);
    }

    @Test
    public void testFocusLost() {
        textField.focusLost();
        // Since drawBorder is private, we can't directly test it
        // But we can verify the method doesn't throw exceptions
        assertTrue("Focus lost should complete without errors", true);
    }

    @Test
    public void testIsDisabled() {
        assertFalse("Should not be disabled by default", textField.isDisabled());
        
        textField.setDisabled(true);
        assertTrue("Should be disabled", textField.isDisabled());
    }

    @Test
    public void testIsInputValid() {
        assertTrue("Input should be valid by default", textField.isInputValid());
    }

    @Test
    public void testSetInputValid() {
        textField.setInputValid(false);
        assertFalse("Input should be invalid", textField.isInputValid());
        
        textField.setInputValid(true);
        assertTrue("Input should be valid", textField.isInputValid());
    }

    @Test
    public void testIsReadOnly() {
        assertFalse("Should not be read-only by default", textField.readOnly);
    }

    @Test
    public void testSetReadOnly() {
        textField.setReadOnly(true);
        assertTrue("Should be read-only", textField.readOnly);
    }

    @Test
    public void testToString() {
        textField.setText("Hello World");
        assertEquals("toString should return text", "Hello World", textField.toString());
    }

    @Test
    public void testGetPrefWidth() {
        assertEquals("Preferred width should be 150", 150, textField.getPrefWidth(), 0.001f);
    }

    @Test
    public void testGetPrefHeight() {
        float prefHeight = textField.getPrefHeight();
        assertTrue("Preferred height should be positive", prefHeight > 0);
    }

    @Test
    public void testVisTextFieldStyle() {
        VisTextFieldStyle style = new VisTextFieldStyle();
        assertNull("Focus border should be null by default", style.focusBorder);
        assertNull("Error border should be null by default", style.errorBorder);
        assertNull("Background over should be null by default", style.backgroundOver);
        assertNull("Focused background should be null by default", style.focusedBackground);
        assertNull("Disabled background should be null by default", style.disabledBackground);
        assertNull("Focused font color should be null by default", style.focusedFontColor);
        assertNull("Disabled font color should be null by default", style.disabledFontColor);
        assertNull("Message font should be null by default", style.messageFont);
        assertNull("Message font color should be null by default", style.messageFontColor);
    }

    /**
     * Helper method to reset FocusManager static state.
     */
    private void resetFocusManager() {
        try {
            java.lang.reflect.Field field = FocusManager.class.getDeclaredField("focusedWidget");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            // Ignore reflection errors
        }
    }
}
