package com.kotcrab.vis.ui.widget;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link Separator}.
 */
public class SeparatorTest {

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

    @After
    public void tearDown() {
        if (VisUI.isLoaded()) {
            VisUI.dispose();
        }
    }

    private Skin createMinimalSkin() {
        Skin skin = new Skin();

        // Add Sizes object - required by various VisUI components
        Sizes sizes = new Sizes();
        sizes.scaleFactor = 1.0f;
        sizes.spacingTop = 2.0f;
        sizes.spacingBottom = 2.0f;
        sizes.spacingRight = 2.0f;
        sizes.spacingLeft = 2.0f;
        sizes.buttonBarSpacing = 6.0f;
        sizes.menuItemIconSize = 16.0f;
        sizes.borderSize = 1.0f;
        sizes.spinnerButtonHeight = 20.0f;
        sizes.spinnerFieldSize = 40.0f;
        sizes.fileChooserViewModeBigIconsSize = 32.0f;
        sizes.fileChooserViewModeMediumIconsSize = 24.0f;
        sizes.fileChooserViewModeSmallIconsSize = 16.0f;
        sizes.fileChooserViewModeListWidthSize = 150.0f;
        skin.add("default", sizes, Sizes.class);

        // Add Separator styles
        Separator.SeparatorStyle defaultSeparatorStyle = new Separator.SeparatorStyle();
        Drawable defaultBackground = Mockito.mock(Drawable.class);
        Mockito.when(defaultBackground.getMinWidth()).thenReturn(10.0f);
        Mockito.when(defaultBackground.getMinHeight()).thenReturn(2.0f);
        defaultSeparatorStyle.background = defaultBackground;
        defaultSeparatorStyle.thickness = 2;
        skin.add("default", defaultSeparatorStyle);

        // Add font for labels
        skin.add("default", testFont, BitmapFont.class);

        // Add Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = testFont;
        skin.add("default", labelStyle);

        return skin;
    }

    private BitmapFont newTestFont() {
        com.badlogic.gdx.graphics.Texture mockTexture = Mockito.mock(com.badlogic.gdx.graphics.Texture.class);
        Mockito.when(mockTexture.getWidth()).thenReturn(1);
        Mockito.when(mockTexture.getHeight()).thenReturn(1);

        com.badlogic.gdx.graphics.g2d.TextureRegion mockRegion = Mockito.mock(com.badlogic.gdx.graphics.g2d.TextureRegion.class);
        Mockito.when(mockRegion.getTexture()).thenReturn(mockTexture);

        BitmapFont.BitmapFontData fontData = new BitmapFont.BitmapFontData() {
            @Override
            public boolean hasGlyph(char ch) {
                return true;
            }

            @Override
            public BitmapFont.Glyph getGlyph(char ch) {
                BitmapFont.Glyph glyph = new BitmapFont.Glyph();
                glyph.id = ch;
                glyph.width = 8;
                glyph.height = 16;
                glyph.xadvance = 8;
                glyph.xoffset = 0;
                glyph.yoffset = 0;
                glyph.srcX = 0;
                glyph.srcY = 0;
                return glyph;
            }

            public float getLineHeight() {
                return 16;
            }
        };

        return new BitmapFont(fontData, mockRegion, true);
    }

    @Test
    public void testDefaultConstructor() {
        Separator separator = new Separator();

        Assert.assertNotNull("Separator should be created", separator);
        Assert.assertNotNull("Style should not be null", separator.getStyle());
    }

    @Test
    public void testConstructorWithStyleName() {
        Separator separator = new Separator("default");

        Assert.assertNotNull("Separator should be created", separator);
        Assert.assertNotNull("Style should not be null", separator.getStyle());
    }

    @Test
    public void testConstructorWithStyle() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 2);

        Separator separator = new Separator(style);

        Assert.assertNotNull("Separator should be created", separator);
        Assert.assertSame("Style should be set", style, separator.getStyle());
    }

    @Test
    public void testGetPrefHeight() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 5);
        Separator separator = new Separator(style);

        float prefHeight = separator.getPrefHeight();

        Assert.assertEquals("Pref height should equal style thickness", 5f, prefHeight, 0.0001f);
    }

    @Test
    public void testGetPrefWidth() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 3);
        Separator separator = new Separator(style);

        float prefWidth = separator.getPrefWidth();

        Assert.assertEquals("Pref width should equal style thickness", 3f, prefWidth, 0.0001f);
    }

    @Test
    public void testGetPrefHeightWithDefaultStyle() {
        Separator separator = new Separator();

        float prefHeight = separator.getPrefHeight();

        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetPrefWidthWithDefaultStyle() {
        Separator separator = new Separator();

        float prefWidth = separator.getPrefWidth();

        Assert.assertTrue("Pref width should be positive", prefWidth > 0);
    }

    @Test
    public void testGetStyle() {
        Separator separator = new Separator();

        Separator.SeparatorStyle style = separator.getStyle();

        Assert.assertNotNull("Style should not be null", style);
    }

    @Test
    public void testSeparatorStyleCopyConstructor() {
        Drawable mockBackground = Mockito.mock(Drawable.class);

        Separator.SeparatorStyle original = new Separator.SeparatorStyle(mockBackground, 4);
        Separator.SeparatorStyle copy = new Separator.SeparatorStyle(original);

        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertEquals("Background should be copied", mockBackground, copy.background);
        Assert.assertEquals("Thickness should be copied", 4, copy.thickness);
    }

    @Test
    public void testSeparatorStyleConstructorWithDrawableAndThickness() {
        Drawable mockBackground = Mockito.mock(Drawable.class);

        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 7);

        Assert.assertNotNull("Style should be created", style);
        Assert.assertEquals("Background should be set", mockBackground, style.background);
        Assert.assertEquals("Thickness should be set", 7, style.thickness);
    }

    @Test
    public void testSeparatorStyleDefaultConstructor() {
        Separator.SeparatorStyle style = new Separator.SeparatorStyle();

        Assert.assertNotNull("Style should be created", style);
        Assert.assertNull("Background should be null by default", style.background);
        Assert.assertEquals("Thickness should be 0 by default", 0, style.thickness);
    }

    @Test
    public void testDraw() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 3);
        Separator separator = new Separator(style);
        separator.setSize(100, 3);

        Batch mockBatch = Mockito.mock(Batch.class);

        // Should not throw exception
        separator.draw(mockBatch, 1.0f);

        // Verify that background draw was called
        Mockito.verify(mockBackground).draw(mockBatch, 0f, 0f, 100f, 3f);
    }

    @Test
    public void testDrawWithParentAlpha() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 2);
        Separator separator = new Separator(style);
        separator.setSize(50, 2);

        Batch mockBatch = Mockito.mock(Batch.class);

        separator.draw(mockBatch, 0.5f);

        // Verify that background draw was called with correct dimensions
        Mockito.verify(mockBackground).draw(mockBatch, 0f, 0f, 50f, 2f);
    }

    @Test
    public void testDrawWithNullBackground() {
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(null, 2);
        Separator separator = new Separator(style);
        separator.setSize(50, 2);

        Batch mockBatch = Mockito.mock(Batch.class);

        // Should not throw exception even with null background
        separator.draw(mockBatch, 1.0f);
        // If we reach here, the test passes
    }

    @Test
    public void testSeparatorWithZeroThickness() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 0);
        Separator separator = new Separator(style);

        Assert.assertEquals("Pref height should be 0", 0f, separator.getPrefHeight(), 0.0001f);
        Assert.assertEquals("Pref width should be 0", 0f, separator.getPrefWidth(), 0.0001f);
    }

    @Test
    public void testSeparatorWithNegativeThickness() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, -1);
        Separator separator = new Separator(style);

        Assert.assertEquals("Pref height should be negative", -1f, separator.getPrefHeight(), 0.0001f);
        Assert.assertEquals("Pref width should be negative", -1f, separator.getPrefWidth(), 0.0001f);
    }

    @Test
    public void testSeparatorWithLargeThickness() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 100);
        Separator separator = new Separator(style);

        Assert.assertEquals("Pref height should be 100", 100f, separator.getPrefHeight(), 0.0001f);
        Assert.assertEquals("Pref width should be 100", 100f, separator.getPrefWidth(), 0.0001f);
    }

    @Test
    public void testStyleIndependence() {
        Drawable mockBackground1 = Mockito.mock(Drawable.class);
        Drawable mockBackground2 = Mockito.mock(Drawable.class);

        Separator.SeparatorStyle original = new Separator.SeparatorStyle(mockBackground1, 5);
        Separator.SeparatorStyle copy = new Separator.SeparatorStyle(original);
        copy.background = mockBackground2;
        copy.thickness = 10;

        Assert.assertEquals("Original background should remain unchanged", mockBackground1, original.background);
        Assert.assertEquals("Original thickness should remain unchanged", 5, original.thickness);
        Assert.assertEquals("Copy background should be updated", mockBackground2, copy.background);
        Assert.assertEquals("Copy thickness should be updated", 10, copy.thickness);
    }

    @Test
    public void testSeparatorInheritance() {
        Separator separator = new Separator();

        // Test that Separator extends Widget
        Assert.assertTrue("Separator should extend Widget",
                separator instanceof com.badlogic.gdx.scenes.scene2d.ui.Widget);
    }

    @Test
    public void testSeparatorStyleWithNullBackground() {
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(null, 5);

        Assert.assertNotNull("Style should be created with null background", style);
        Assert.assertNull("Background should be null", style.background);
        Assert.assertEquals("Thickness should be set", 5, style.thickness);
    }

    @Test
    public void testMultipleSeparators() {
        Separator separator1 = new Separator();
        Separator separator2 = new Separator("default");
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle customStyle = new Separator.SeparatorStyle(mockBackground, 8);
        Separator separator3 = new Separator(customStyle);

        Assert.assertNotNull("All separators should be created", separator1);
        Assert.assertNotNull("All separators should be created", separator2);
        Assert.assertNotNull("All separators should be created", separator3);

        Assert.assertSame("Separators using default style should have same style instance",
                separator1.getStyle(), separator2.getStyle());
        Assert.assertSame("Custom style should be set", customStyle, separator3.getStyle());
    }
}
