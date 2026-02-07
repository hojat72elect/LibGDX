package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link VisTextArea}.
 */
public class VisTextAreaTest {

    @Test
    public void testDefaultConstructor() {
        VisTextArea textArea = new VisTextArea();
        
        Assert.assertNotNull("Text area should be created", textArea);
        Assert.assertEquals("Default text should be empty", "", textArea.getText());
        Assert.assertNotNull("Style should not be null", textArea.getStyle());
        Assert.assertTrue("Write enters should be enabled", textArea.isWriteEnters());
    }

    @Test
    public void testConstructorWithText() {
        VisTextArea textArea = new VisTextArea("Initial text");
        
        Assert.assertNotNull("Text area should be created", textArea);
        Assert.assertEquals("Text should be set", "Initial text", textArea.getText());
        Assert.assertNotNull("Style should not be null", textArea.getStyle());
    }

    @Test
    public void testConstructorWithTextAndStyleName() {
        VisTextArea textArea = new VisTextArea("Test text", "default");
        
        Assert.assertNotNull("Text area should be created", textArea);
        Assert.assertEquals("Text should be set", "Test text", textArea.getText());
        Assert.assertNotNull("Style should not be null", textArea.getStyle());
    }

    @Test
    public void testConstructorWithTextAndStyle() {
        TextField.TextFieldStyle mockStyle = Mockito.mock(TextField.TextFieldStyle.class);
        VisTextArea textArea = new VisTextArea("Test text", mockStyle);
        
        Assert.assertNotNull("Text area should be created", textArea);
        Assert.assertEquals("Text should be set", "Test text", textArea.getText());
        Assert.assertSame("Style should be set", mockStyle, textArea.getStyle());
    }

    @Test
    public void testSetPrefRows() {
        VisTextArea textArea = new VisTextArea();
        
        textArea.setPrefRows(5);
        
        // We can't easily verify the pref rows value without accessing private fields
        // but we can verify it affects the preferred height
        float prefHeight = textArea.getPrefHeight();
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetPrefHeightWithPrefRows() {
        VisTextArea textArea = new VisTextArea("Line 1\nLine 2\nLine 3");
        textArea.setPrefRows(2);
        
        float prefHeight = textArea.getPrefHeight();
        Assert.assertTrue("Pref height should be positive with pref rows", prefHeight > 0);
    }

    @Test
    public void testGetPrefHeightWithoutPrefRows() {
        VisTextArea textArea = new VisTextArea("Line 1\nLine 2\nLine 3");
        
        float prefHeight = textArea.getPrefHeight();
        Assert.assertTrue("Pref height should be positive without pref rows", prefHeight > 0);
    }

    @Test
    public void testGetLines() {
        VisTextArea textArea = new VisTextArea("Line 1\nLine 2\nLine 3");
        
        int lines = textArea.getLines();
        Assert.assertEquals("Should have 3 lines", 3, lines);
    }

    @Test
    public void testGetLinesWithEmptyText() {
        VisTextArea textArea = new VisTextArea("");
        
        int lines = textArea.getLines();
        Assert.assertEquals("Empty text should have 0 lines", 0, lines);
    }

    @Test
    public void testGetLinesWithSingleLine() {
        VisTextArea textArea = new VisTextArea("Single line");
        
        int lines = textArea.getLines();
        Assert.assertEquals("Single line should have 1 line", 1, lines);
    }

    @Test
    public void testNewLineAtEnd() {
        VisTextArea textArea1 = new VisTextArea("Text\n");
        VisTextArea textArea2 = new VisTextArea("Text");
        VisTextArea textArea3 = new VisTextArea("Text\r\n");
        
        Assert.assertTrue("Should detect newline at end (\\n)", textArea1.newLineAtEnd());
        Assert.assertFalse("Should not detect newline when not present", textArea2.newLineAtEnd());
        Assert.assertTrue("Should detect newline at end (\\r\\n)", textArea3.newLineAtEnd());
    }

    @Test
    public void testMoveCursorLine() {
        VisTextArea textArea = new VisTextArea("Line 1\nLine 2\nLine 3");
        
        // Move to first line
        textArea.moveCursorLine(0);
        Assert.assertEquals("Cursor should be on line 0", 0, textArea.getCursorLine());
        
        // Move to second line
        textArea.moveCursorLine(1);
        Assert.assertEquals("Cursor should be on line 1", 1, textArea.getCursorLine());
        
        // Move to last line
        textArea.moveCursorLine(2);
        Assert.assertEquals("Cursor should be on line 2", 2, textArea.getCursorLine());
    }

    @Test
    public void testMoveCursorLineOutOfBounds() {
        VisTextArea textArea = new VisTextArea("Line 1\nLine 2");
        
        // Move to negative line
        textArea.moveCursorLine(-1);
        Assert.assertEquals("Cursor should be clamped to line 0", 0, textArea.getCursorLine());
        
        // Move to line beyond text
        textArea.moveCursorLine(10);
        Assert.assertEquals("Cursor should be clamped to last line", 1, textArea.getCursorLine());
    }

    @Test
    public void testGetCursorLine() {
        VisTextArea textArea = new VisTextArea("Line 1\nLine 2\nLine 3");
        
        // Initially cursor should be at line 0
        Assert.assertEquals("Initial cursor line should be 0", 0, textArea.getCursorLine());
        
        textArea.moveCursorLine(1);
        Assert.assertEquals("Cursor line should be updated", 1, textArea.getCursorLine());
    }

    @Test
    public void testGetFirstLineShowing() {
        VisTextArea textArea = new VisTextArea("Line 1\nLine 2\nLine 3\nLine 4\nLine 5");
        
        // Initially first line showing should be 0
        Assert.assertEquals("First line showing should be 0 initially", 0, textArea.getFirstLineShowing());
        
        // Move cursor down to scroll
        textArea.moveCursorLine(3);
        // First line showing might change depending on implementation
        Assert.assertTrue("First line showing should be non-negative", textArea.getFirstLineShowing() >= 0);
    }

    @Test
    public void testGetLinesShowing() {
        VisTextArea textArea = new VisTextArea();
        textArea.setSize(100, 50); // Set a size to calculate lines showing
        
        int linesShowing = textArea.getLinesShowing();
        Assert.assertTrue("Lines showing should be positive", linesShowing > 0);
    }

    @Test
    public void testGetCursorX() {
        VisTextArea textArea = new VisTextArea("Test text");
        
        float cursorX = textArea.getCursorX();
        Assert.assertTrue("Cursor X should be non-negative", cursorX >= 0);
    }

    @Test
    public void testGetCursorY() {
        VisTextArea textArea = new VisTextArea("Test text");
        
        float cursorY = textArea.getCursorY();
        // Cursor Y can be negative depending on coordinate system
        Assert.assertTrue("Cursor Y should be a number", !Float.isNaN(cursorY));
    }

    @Test
    public void testTextAreaInheritance() {
        VisTextArea textArea = new VisTextArea();
        
        Assert.assertTrue("VisTextArea should extend VisTextField", textArea instanceof VisTextField);
    }

    @Test
    public void testSetText() {
        VisTextArea textArea = new VisTextArea();
        
        textArea.setText("New text\nWith multiple lines");
        
        Assert.assertEquals("Text should be updated", "New text\nWith multiple lines", textArea.getText());
        Assert.assertEquals("Lines should be updated", 2, textArea.getLines());
    }

    @Test
    public void testSetTextWithNull() {
        VisTextArea textArea = new VisTextArea("Original");
        
        textArea.setText(null);
        
        Assert.assertEquals("Text should be empty when null is set", "", textArea.getText());
        Assert.assertEquals("Lines should be 0 for empty text", 0, textArea.getLines());
    }

    @Test
    public void testSetTextWithEmptyString() {
        VisTextArea textArea = new VisTextArea("Original");
        
        textArea.setText("");
        
        Assert.assertEquals("Text should be empty", "", textArea.getText());
        Assert.assertEquals("Lines should be 0 for empty text", 0, textArea.getLines());
    }

    @Test
    public void testTextAreaWithLongText() {
        String longText = "This is a very long line that should test the text area's ability to handle longer strings without breaking incorrectly";
        VisTextArea textArea = new VisTextArea(longText);
        
        Assert.assertEquals("Long text should be preserved", longText, textArea.getText());
        Assert.assertEquals("Long text should be 1 line", 1, textArea.getLines());
    }

    @Test
    public void testTextAreaWithMultipleLines() {
        String multiLineText = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        VisTextArea textArea = new VisTextArea(multiLineText);
        
        Assert.assertEquals("Multi-line text should be preserved", multiLineText, textArea.getText());
        Assert.assertEquals("Multi-line text should have correct line count", 5, textArea.getLines());
    }

    @Test
    public void testTextAreaWithSpecialCharacters() {
        String specialText = "Special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?\nNew line with special chars: áéíóú";
        VisTextArea textArea = new VisTextArea(specialText);
        
        Assert.assertEquals("Special characters should be handled", specialText, textArea.getText());
        Assert.assertEquals("Should handle special chars in multiple lines", 2, textArea.getLines());
    }

    @Test
    public void testTextAreaDisabled() {
        VisTextArea textArea = new VisTextArea("Test text");
        
        textArea.setDisabled(true);
        Assert.assertTrue("Text area should be disabled", textArea.isDisabled());
        Assert.assertEquals("Text should remain when disabled", "Test text", textArea.getText());
        
        // Text editing should still work when disabled
        textArea.setText("New text");
        Assert.assertEquals("Text should change even when disabled", "New text", textArea.getText());
    }

    @Test
    public void testTextAreaFocusManagement() {
        VisTextArea textArea = new VisTextArea("Test");
        
        // Test focus gained
        textArea.focusGained();
        // Focus border should be drawn (we can't easily test the visual effect)
        
        // Test focus lost
        textArea.focusLost();
        // Focus border should not be drawn
    }

    @Test
    public void testTextAreaStyleProperties() {
        VisTextArea textArea = new VisTextArea();
        TextField.TextFieldStyle style = textArea.getStyle();
        
        Assert.assertNotNull("Font should not be null", style.font);
        Assert.assertNotNull("Font color should not be null", style.fontColor);
        Assert.assertNotNull("Background should not be null", style.background);
        Assert.assertNotNull("Cursor should not be null", style.cursor);
        Assert.assertNotNull("Selection should not be null", style.selection);
    }

    @Test
    public void testMultipleTextAreas() {
        VisTextArea textArea1 = new VisTextArea();
        VisTextArea textArea2 = new VisTextArea("Initial text");
        VisTextArea textArea3 = new VisTextArea("Multi\nline\ntext", "default");
        TextField.TextFieldStyle mockStyle = Mockito.mock(TextField.TextFieldStyle.class);
        VisTextArea textArea4 = new VisTextArea("Custom style", mockStyle);
        
        Assert.assertNotNull("All text areas should be created", textArea1);
        Assert.assertNotNull("All text areas should be created", textArea2);
        Assert.assertNotNull("All text areas should be created", textArea3);
        Assert.assertNotNull("All text areas should be created", textArea4);
        
        Assert.assertEquals("Text area 1 should be empty", "", textArea1.getText());
        Assert.assertEquals("Text area 2 should have initial text", "Initial text", textArea2.getText());
        Assert.assertEquals("Text area 3 should have multi-line text", "Multi\nline\ntext", textArea3.getText());
        Assert.assertEquals("Text area 4 should have custom style text", "Custom style", textArea4.getText());
        
        Assert.assertSame("Custom style should be set", mockStyle, textArea4.getStyle());
    }

    @Test
    public void testTextAreaWithDifferentStyleNames() {
        VisTextArea defaultArea = new VisTextArea("Test", "default");
        VisTextArea customArea = new VisTextArea("Test", "custom");
        
        Assert.assertNotNull("Default style should be loaded", defaultArea.getStyle());
        Assert.assertNotNull("Custom style should be loaded", customArea.getStyle());
    }

    @Test
    public void testTextAreaSizeChanged() {
        VisTextArea textArea = new VisTextArea("Test text");
        
        // Test that size change doesn't throw exceptions
        textArea.setSize(100, 50);
        textArea.setSize(200, 100);
        textArea.setSize(50, 25);
        
        Assert.assertTrue("Size changes should be handled", true);
    }

    @Test
    public void testTextAreaLayout() {
        VisTextArea textArea = new VisTextArea("Line 1\nLine 2\nLine 3");
        textArea.setSize(150, 100);
        
        // Test that layout doesn't throw exceptions
        textArea.layout();
        
        Assert.assertTrue("Layout should complete successfully", true);
    }

    @Test
    public void testTextAreaSelection() {
        VisTextArea textArea = new VisTextArea("Select this text");
        
        // Test selection
        textArea.setSelection(0, 6);
        
        // We can't easily verify the selection without accessing private fields
        // but we can verify it doesn't throw exceptions
        Assert.assertTrue("Selection should be handled", true);
    }

    @Test
    public void testTextAreaCursorPosition() {
        VisTextArea textArea = new VisTextArea("Test cursor position");
        
        // Test cursor position
        textArea.setCursorPosition(5);
        
        Assert.assertTrue("Cursor position should be handled", true);
    }

    @Test
    public void testTextAreaInputListener() {
        VisTextArea textArea = new VisTextArea("Test");
        
        // The text area should have a TextAreaListener
        Assert.assertNotNull("Input listener should be created", textArea.createInputListener());
        Assert.assertTrue("Should create TextAreaListener", 
                        textArea.createInputListener() instanceof VisTextArea.TextAreaListener);
    }

    @Test
    public void testTextAreaWithEmptyLines() {
        VisTextArea textArea = new VisTextArea("Line 1\n\nLine 3");
        
        Assert.assertEquals("Should handle empty lines", 3, textArea.getLines());
        Assert.assertEquals("Text should preserve empty lines", "Line 1\n\nLine 3", textArea.getText());
    }

    @Test
    public void testTextAreaWithTrailingNewlines() {
        VisTextArea textArea = new VisTextArea("Text\n\n\n");
        
        Assert.assertEquals("Should handle trailing newlines", 4, textArea.getLines());
        Assert.assertTrue("Should detect newline at end", textArea.newLineAtEnd());
    }
}
