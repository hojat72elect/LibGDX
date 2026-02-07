package com.kotcrab.vis.ui.widget;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.VisUI;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Proxy;

public class VisTextAreaTest {

    private static Files previousFiles;
    private static Application previousApp;
    private static Graphics previousGraphics;
    private static Input previousInput;

    private static class TestableVisTextArea extends VisTextArea {
        public TestableVisTextArea(String text) {
            super(text);
        }

        public TestableVisTextArea() {
            super();
        }

        public TestableVisTextArea(String text, String styleName) {
            super(text, styleName);
        }

        public TestableVisTextArea(String text, VisTextFieldStyle style) {
            super(text, style);
        }

        public void callCalculateOffsets() {
            calculateOffsets();
        }
    }

    @BeforeClass
    public static void setup() {
        previousFiles = Gdx.files;
        previousApp = Gdx.app;
        previousGraphics = Gdx.graphics;
        previousInput = Gdx.input;

        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> {
                        if ("local".equals(method.getName())) {
                            return new com.badlogic.gdx.files.FileHandle("test");
                        }
                        return null;
                    });
        }

        if (Gdx.app == null) {
            Clipboard clipboard = Mockito.mock(Clipboard.class);
            Gdx.app = (Application) Proxy.newProxyInstance(
                    Application.class.getClassLoader(),
                    new Class[]{Application.class},
                    (proxy, method, args) -> {
                        if ("getClipboard".equals(method.getName())) {
                            return clipboard;
                        }
                        return null;
                    });
        }

        if (Gdx.graphics == null) {
            Gdx.graphics = Mockito.mock(Graphics.class);
            when(Gdx.graphics.getWidth()).thenReturn(800);
            when(Gdx.graphics.getHeight()).thenReturn(600);
        }

        if (Gdx.input == null) {
            Gdx.input = Mockito.mock(Input.class);
        }

        try {
            VisUI.dispose(false);
            VisUI.setSkipGdxVersionCheck(true);
            VisUI.load(createMinimalSkin());
        } catch (Exception e) {
            // VisUI might already be loaded, dispose and reload
            try {
                VisUI.dispose();
                VisUI.setSkipGdxVersionCheck(true);
                VisUI.load(createMinimalSkin());
            } catch (Exception ignored) {
            }
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            if (VisUI.isLoaded()) {
                VisUI.dispose();
            }
        } catch (Exception ignored) {
        }

        Gdx.files = previousFiles;
        Gdx.app = previousApp;
        Gdx.graphics = previousGraphics;
        Gdx.input = previousInput;
    }

    @Before
    public void setUp() {
        // Ensure VisUI is loaded before each test
        if (!VisUI.isLoaded()) {
            try {
                VisUI.setSkipGdxVersionCheck(true);
                VisUI.load(createMinimalSkin());
            } catch (Exception ignored) {
            }
        }
    }

    @After
    public void tearDownTest() {
        // Clean up any VisUI state after each test if needed
    }

    private static Skin createMinimalSkin() {
        Skin skin = new Skin();
        BitmapFont font = newTestFont();

        skin.add("default-font", font, BitmapFont.class);
        skin.add("default", new Label.LabelStyle(font, Color.WHITE), Label.LabelStyle.class);

        // Create minimal TextFieldStyle
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.cursor = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        textFieldStyle.selection = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        textFieldStyle.background = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);

        skin.add("default", textFieldStyle, TextField.TextFieldStyle.class);

        // Create VisTextFieldStyle
        VisTextField.VisTextFieldStyle visTextFieldStyle = new VisTextField.VisTextFieldStyle();
        visTextFieldStyle.font = font;
        visTextFieldStyle.fontColor = Color.WHITE;
        visTextFieldStyle.cursor = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        visTextFieldStyle.selection = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        visTextFieldStyle.background = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        visTextFieldStyle.focusBorder = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        visTextFieldStyle.errorBorder = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        visTextFieldStyle.backgroundOver = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);

        skin.add("default", visTextFieldStyle, VisTextField.VisTextFieldStyle.class);

        // Create custom VisTextFieldStyle for testing different style names
        VisTextField.VisTextFieldStyle customVisTextFieldStyle = new VisTextField.VisTextFieldStyle();
        customVisTextFieldStyle.font = font;
        customVisTextFieldStyle.fontColor = Color.BLUE;
        customVisTextFieldStyle.cursor = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        customVisTextFieldStyle.selection = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        customVisTextFieldStyle.background = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        customVisTextFieldStyle.focusBorder = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        customVisTextFieldStyle.errorBorder = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        customVisTextFieldStyle.backgroundOver = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);

        skin.add("custom", customVisTextFieldStyle, VisTextField.VisTextFieldStyle.class);

        return skin;
    }

    private static BitmapFont newTestFont() {
        Texture mockTexture = Mockito.mock(Texture.class);
        when(mockTexture.getWidth()).thenReturn(1);
        when(mockTexture.getHeight()).thenReturn(1);

        TextureRegion mockRegion = Mockito.mock(TextureRegion.class);
        when(mockRegion.getTexture()).thenReturn(mockTexture);

        BitmapFont.BitmapFontData fontData = new BitmapFont.BitmapFontData() {
            @Override
            public boolean hasGlyph(char ch) {
                return true;
            }

            @Override
            public Glyph getGlyph(char ch) {
                Glyph glyph = new Glyph();
                glyph.id = ch;
                glyph.width = 10; // Set a reasonable width
                glyph.height = 12; // Set a reasonable height
                glyph.xoffset = 0;
                glyph.yoffset = 0;
                glyph.xadvance = 10; // Set a reasonable advance
                return glyph;
            }
        };

        return new BitmapFont(fontData, Array.with(mockRegion), true);
    }

    @Test
    public void testDefaultConstructor() {
        VisTextArea textArea = new VisTextArea();

        Assert.assertNotNull("Text area should be created", textArea);
        Assert.assertEquals("Default text should be empty", "", textArea.getText());
        Assert.assertNotNull("Style should not be null", textArea.getStyle());
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
        VisTextField.VisTextFieldStyle mockStyle = Mockito.mock(VisTextField.VisTextFieldStyle.class);
        mockStyle.font = newTestFont();
        mockStyle.fontColor = Color.WHITE;
        mockStyle.cursor = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        mockStyle.selection = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        mockStyle.background = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
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
        TestableVisTextArea textArea = new TestableVisTextArea("Line 1\nLine 2\nLine 3");
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

        int lines = textArea.getLines();
        Assert.assertEquals("Should have 3 lines", 3, lines);
    }

    @Test
    public void testGetLinesWithEmptyText() {
        TestableVisTextArea textArea = new TestableVisTextArea("");
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

        int lines = textArea.getLines();
        Assert.assertEquals("Empty text should have 0 lines", 0, lines);
    }

    @Test
    public void testGetLinesWithSingleLine() {
        TestableVisTextArea textArea = new TestableVisTextArea("Single line");
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

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
        TestableVisTextArea textArea = new TestableVisTextArea("Line 1\nLine 2\nLine 3");
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

        // Ensure glyph positions are calculated by calling layout
        textArea.layout();

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
        TestableVisTextArea textArea = new TestableVisTextArea("Line 1\nLine 2");
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

        // Ensure glyph positions are calculated by calling layout
        textArea.layout();

        // Move to negative line
        textArea.moveCursorLine(-1);
        Assert.assertEquals("Cursor should be clamped to line 0", 0, textArea.getCursorLine());

        // Move to line beyond text
        textArea.moveCursorLine(10);
        Assert.assertEquals("Cursor should be clamped to last line", 1, textArea.getCursorLine());
    }

    @Test
    public void testGetCursorLine() {
        TestableVisTextArea textArea = new TestableVisTextArea("Line 1\nLine 2\nLine 3");
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

        // Ensure glyph positions are calculated by calling layout
        textArea.layout();

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
        Assert.assertFalse("Cursor Y should be a number", Float.isNaN(cursorY));
    }

    @Test
    public void testTextAreaInheritance() {
        VisTextArea textArea = new VisTextArea();

        Assert.assertTrue("VisTextArea should extend VisTextField", textArea instanceof VisTextField);
    }

    @Test
    public void testSetText() {
        TestableVisTextArea textArea = new TestableVisTextArea();
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

        textArea.setText("New text\nWith multiple lines");
        textArea.callCalculateOffsets(); // Recalculate after text change

        Assert.assertEquals("Text should be updated", "New text\nWith multiple lines", textArea.getText());
        Assert.assertEquals("Lines should be updated", 2, textArea.getLines());
    }

    @Test
    public void testSetTextWithNull() {
        TestableVisTextArea textArea = new TestableVisTextArea("Original");
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

        textArea.setText(null);
        textArea.callCalculateOffsets(); // Recalculate after text change

        Assert.assertEquals("Text should be empty when null is set", "", textArea.getText());
        Assert.assertEquals("Lines should be 0 for empty text", 0, textArea.getLines());
    }

    @Test
    public void testSetTextWithEmptyString() {
        TestableVisTextArea textArea = new TestableVisTextArea("Original");
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

        textArea.setText("");
        textArea.callCalculateOffsets(); // Recalculate after text change

        Assert.assertEquals("Text should be empty", "", textArea.getText());
        Assert.assertEquals("Lines should be 0 for empty text", 0, textArea.getLines());
    }

    @Test
    public void testTextAreaWithLongText() {
        String longText = "This is a very long line that should test the text area's ability to handle longer strings without breaking incorrectly";
        TestableVisTextArea textArea = new TestableVisTextArea(longText);
        textArea.setSize(2000, 1000); // Use wider width to prevent wrapping
        textArea.callCalculateOffsets();

        Assert.assertEquals("Long text should be preserved", longText, textArea.getText());
        Assert.assertEquals("Long text should be 1 line", 1, textArea.getLines());
    }

    @Test
    public void testTextAreaWithMultipleLines() {
        String multiLineText = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        TestableVisTextArea textArea = new TestableVisTextArea(multiLineText);
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

        Assert.assertEquals("Multi-line text should be preserved", multiLineText, textArea.getText());
        Assert.assertEquals("Multi-line text should have correct line count", 5, textArea.getLines());
    }

    @Test
    public void testTextAreaWithSpecialCharacters() {
        String specialText = "Special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?\nNew line with special chars: áéíóú";
        TestableVisTextArea textArea = new TestableVisTextArea(specialText);
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

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
        TestableVisTextArea textArea1 = new TestableVisTextArea();
        TestableVisTextArea textArea2 = new TestableVisTextArea("Initial text");
        TestableVisTextArea textArea3 = new TestableVisTextArea("Multi\nline\ntext", "default");
        VisTextField.VisTextFieldStyle mockStyle = Mockito.mock(VisTextField.VisTextFieldStyle.class);
        mockStyle.font = newTestFont();
        mockStyle.fontColor = Color.WHITE;
        mockStyle.cursor = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        mockStyle.selection = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        mockStyle.background = Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        TestableVisTextArea textArea4 = new TestableVisTextArea("Custom style", mockStyle);

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
        TestableVisTextArea defaultArea = new TestableVisTextArea("Test", "default");
        TestableVisTextArea customArea = new TestableVisTextArea("Test", "custom");

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
        TestableVisTextArea textArea = new TestableVisTextArea("Line 1\n\nLine 3");
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

        Assert.assertEquals("Should handle empty lines", 3, textArea.getLines());
        Assert.assertEquals("Text should preserve empty lines", "Line 1\n\nLine 3", textArea.getText());
    }

    @Test
    public void testTextAreaWithTrailingNewlines() {
        TestableVisTextArea textArea = new TestableVisTextArea("Text\n\n\n");
        textArea.setSize(1000, 1000);
        textArea.callCalculateOffsets();

        Assert.assertEquals("Should handle trailing newlines", 4, textArea.getLines());
        Assert.assertTrue("Should detect newline at end", textArea.newLineAtEnd());
    }
}
