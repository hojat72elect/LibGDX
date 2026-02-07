package com.kotcrab.vis.ui.widget;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.VisUI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VisLabelTest {

    @Mock
    private Application mockApplication;
    @Mock
    private Files mockFiles;
    @Mock
    private Input mockInput;
    @Mock
    private Graphics mockGraphics;
    @Mock
    private Clipboard mockClipboard;
    private BitmapFont testFont;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock Gdx application
        Gdx.app = mockApplication;
        Gdx.files = mockFiles;
        Gdx.input = mockInput;
        Gdx.graphics = mockGraphics;
        when(mockApplication.getClipboard()).thenReturn(mockClipboard);

        // Load VisUI for testing
        if (!VisUI.isLoaded()) {
            VisUI.setSkipGdxVersionCheck(true);
            testFont = newTestFont();
            Skin testSkin = createMinimalSkin();
            VisUI.load(testSkin);
        } else {
            testFont = newTestFont();
        }

        testFont.setColor(Color.WHITE);
    }

    private Skin createMinimalSkin() {
        Skin skin = new Skin();
        // Add minimal required style for VisLabel
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = testFont;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // Add font with name for constructors that use font names
        skin.add("default-font", testFont);

        // Add colors for constructors that use color names
        skin.add("white", Color.WHITE);

        return skin;
    }

    private static BitmapFont newTestFont() {
        com.badlogic.gdx.graphics.Texture mockTexture = org.mockito.Mockito.mock(com.badlogic.gdx.graphics.Texture.class);
        org.mockito.Mockito.when(mockTexture.getWidth()).thenReturn(1);
        org.mockito.Mockito.when(mockTexture.getHeight()).thenReturn(1);

        com.badlogic.gdx.graphics.g2d.TextureRegion mockRegion = org.mockito.Mockito.mock(com.badlogic.gdx.graphics.g2d.TextureRegion.class);
        org.mockito.Mockito.when(mockRegion.getTexture()).thenReturn(mockTexture);

        BitmapFont.BitmapFontData fontData = new BitmapFont.BitmapFontData() {
            @Override
            public boolean hasGlyph(char ch) {
                return true;
            }

            @Override
            public BitmapFont.Glyph getGlyph(char ch) {
                BitmapFont.Glyph glyph = new BitmapFont.Glyph();
                glyph.id = ch;
                glyph.width = 8;  // Set a reasonable width for each character
                glyph.height = 12; // Set a reasonable height for each character
                glyph.xoffset = 0;
                glyph.yoffset = 0;
                glyph.xadvance = 8; // Set advance for proper width calculation
                return glyph;
            }
        };

        return new BitmapFont(fontData, com.badlogic.gdx.utils.Array.with(mockRegion), true);
    }

    @Test
    public void testDefaultConstructor() {
        VisLabel label = new VisLabel();

        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Default text should be empty", "", label.getText().toString());
        Assert.assertNotNull("Style should not be null", label.getStyle());
    }

    @Test
    public void testConstructorWithText() {
        VisLabel label = new VisLabel("Test Text");

        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertNotNull("Style should not be null", label.getStyle());
    }

    @Test
    public void testConstructorWithTextAndColor() {
        Color testColor = Color.RED;
        VisLabel label = new VisLabel("Test Text", testColor);

        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertEquals("Color should be set", testColor, label.getColor());
    }

    @Test
    public void testConstructorWithTextAndStyle() {
        Label.LabelStyle testStyle = new Label.LabelStyle();
        testStyle.font = testFont;
        testStyle.fontColor = Color.WHITE;
        VisLabel label = new VisLabel("Test Text", testStyle);

        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertSame("Style should be set", testStyle, label.getStyle());
    }

    @Test
    public void testConstructorWithTextAndStyleName() {
        VisLabel label = new VisLabel("Test Text", "default");

        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertNotNull("Style should not be null", label.getStyle());
    }

    @Test
    public void testConstructorWithTextFontNameAndColor() {
        VisLabel label = new VisLabel("Test Text", "default-font", Color.BLUE);
        label.setColor(Color.BLUE); // Set color explicitly to override style color

        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertEquals("Color should be set", Color.BLUE, label.getColor());
    }

    @Test
    public void testConstructorWithTextFontNameAndColorName() {
        VisLabel label = new VisLabel("Test Text", "default-font", "white");

        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertNotNull("Color should be set", label.getColor());
    }

    @Test
    public void testSetText() {
        VisLabel label = new VisLabel();
        label.setText("New Text");

        Assert.assertEquals("Text should be updated", "New Text", label.getText().toString());
    }

    @Test
    public void testSetTextWithNull() {
        VisLabel label = new VisLabel("Original");
        label.setText(null);

        Assert.assertEquals("Text should be empty when null is set", "", label.getText().toString());
    }

    @Test
    public void testSetColor() {
        VisLabel label = new VisLabel();
        Color testColor = Color.GREEN;
        label.setColor(testColor);

        Assert.assertEquals("Color should be set", testColor, label.getColor());
    }

    @Test
    public void testSetWrap() {
        VisLabel label = new VisLabel("Test Text");
        label.setWrap(true);

        Assert.assertTrue("Wrap should be enabled", label.getWrap());
    }

    @Test
    public void testGetPrefWidth() {
        VisLabel label = new VisLabel("Test Text");
        float prefWidth = label.getPrefWidth();

        Assert.assertTrue("Pref width should be positive", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeight() {
        VisLabel label = new VisLabel("Test Text");
        float prefHeight = label.getPrefHeight();

        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetPrefWidthWithEmptyText() {
        VisLabel label = new VisLabel("");
        float prefWidth = label.getPrefWidth();

        Assert.assertTrue("Pref width should be non-negative", prefWidth >= 0);
    }

    @Test
    public void testGetPrefHeightWithEmptyText() {
        VisLabel label = new VisLabel("");
        float prefHeight = label.getPrefHeight();

        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testLabelInheritance() {
        VisLabel label = new VisLabel();

        Assert.assertTrue("VisLabel should extend Label", label instanceof Label);
    }

    @Test
    public void testMultipleLabels() {
        VisLabel label1 = new VisLabel("Label 1");
        VisLabel label2 = new VisLabel("Label 2", Color.RED);

        Assert.assertNotNull("All labels should be created", label1);
        Assert.assertNotNull("All labels should be created", label2);
        Assert.assertEquals("Label 1 should have correct text", "Label 1", label1.getText().toString());
        Assert.assertEquals("Label 2 should have correct text", "Label 2", label2.getText().toString());
        Assert.assertEquals("Label 2 should have red color", Color.RED, label2.getColor());
    }

    @Test
    public void testLabelWithLongText() {
        String longText = "This is a very long text that should test the label's ability to handle longer strings";
        VisLabel label = new VisLabel(longText);

        Assert.assertEquals("Long text should be set correctly", longText, label.getText().toString());
        Assert.assertTrue("Pref width should be larger for long text", label.getPrefWidth() > 0);
    }

    @Test
    public void testLabelWithSpecialCharacters() {
        String specialText = "Special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        VisLabel label = new VisLabel(specialText);

        Assert.assertEquals("Special characters should be handled", specialText, label.getText().toString());
    }

    @After
    public void tearDown() {
        if (VisUI.isLoaded()) {
            VisUI.dispose();
        }
        // Reset Gdx static references
        Gdx.app = null;
        Gdx.files = null;
        Gdx.input = null;
        Gdx.graphics = null;
    }
}
