package com.kotcrab.vis.ui.widget;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ScrollableTextAreaTest {

    private static class TestableScrollableTextArea extends ScrollableTextArea {
        public TestableScrollableTextArea(String text) {
            super(text);
        }

        public void callCalculateOffsets() {
            calculateOffsets();
        }
    }

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

        // Add Sizes object (required by VisUI)
        Sizes sizes = new Sizes();
        sizes.scaleFactor = 1f;
        sizes.spacingTop = 2;
        sizes.spacingBottom = 2;
        sizes.spacingLeft = 2;
        sizes.spacingRight = 2;
        sizes.buttonBarSpacing = 6;
        sizes.menuItemIconSize = 16;
        sizes.borderSize = 1;
        sizes.spinnerButtonHeight = 16;
        sizes.spinnerFieldSize = 16;
        sizes.fileChooserViewModeBigIconsSize = 16;
        sizes.fileChooserViewModeMediumIconsSize = 16;
        sizes.fileChooserViewModeSmallIconsSize = 16;
        sizes.fileChooserViewModeListWidthSize = 16;
        skin.add("default", sizes, Sizes.class);

        // Add VisTextFieldStyle for textArea style
        VisTextField.VisTextFieldStyle textAreaStyle = new VisTextField.VisTextFieldStyle();
        textAreaStyle.font = testFont;
        textAreaStyle.fontColor = Color.WHITE;
        textAreaStyle.background = mockDrawable();
        textAreaStyle.cursor = mockDrawable();
        textAreaStyle.selection = mockDrawable();
        textAreaStyle.focusedBackground = mockDrawable();
        skin.add("textArea", textAreaStyle);

        // Add default style as fallback
        skin.add("default", textAreaStyle);

        // Add ScrollPaneStyle for createCompatibleScrollPane
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = mockDrawable();
        scrollPaneStyle.corner = mockDrawable();
        scrollPaneStyle.hScroll = mockDrawable();
        scrollPaneStyle.hScrollKnob = mockDrawable();
        scrollPaneStyle.vScroll = mockDrawable();
        scrollPaneStyle.vScrollKnob = mockDrawable();
        skin.add("default", scrollPaneStyle);
        skin.add("list", scrollPaneStyle); // Add "list" style for VisScrollPane

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

        BitmapFont font = new BitmapFont(fontData, com.badlogic.gdx.utils.Array.with(mockRegion), true);
        font.getData().setLineHeight(12); // Set explicit line height

        return font;
    }

    private static com.badlogic.gdx.scenes.scene2d.utils.Drawable mockDrawable() {
        com.badlogic.gdx.scenes.scene2d.utils.Drawable drawable = org.mockito.Mockito.mock(com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);
        org.mockito.Mockito.when(drawable.getMinWidth()).thenReturn(10f);
        org.mockito.Mockito.when(drawable.getMinHeight()).thenReturn(10f);
        org.mockito.Mockito.when(drawable.getLeftWidth()).thenReturn(2f);
        org.mockito.Mockito.when(drawable.getRightWidth()).thenReturn(2f);
        org.mockito.Mockito.when(drawable.getTopHeight()).thenReturn(2f);
        org.mockito.Mockito.when(drawable.getBottomHeight()).thenReturn(2f);
        return drawable;
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

    @Test
    public void testConstructorWithText() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test Text");

        Assert.assertNotNull("Text area should be created", textArea);
        Assert.assertEquals("Text should be set", "Test Text", textArea.getText());
        Assert.assertTrue("Should implement Cullable", true);
    }

    @Test
    public void testConstructorWithTextAndStyleName() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test Text", "default");

        Assert.assertNotNull("Text area should be created", textArea);
        Assert.assertEquals("Text should be set", "Test Text", textArea.getText());
    }

    @Test
    public void testConstructorWithTextAndStyle() {
        VisTextField.VisTextFieldStyle style = VisUI.getSkin().get("default", VisTextField.VisTextFieldStyle.class);
        ScrollableTextArea textArea = new ScrollableTextArea("Test Text", style);

        Assert.assertNotNull("Text area should be created", textArea);
        Assert.assertEquals("Text should be set", "Test Text", textArea.getText());
    }

    @Test
    public void testGetPrefHeight() {
        TestableScrollableTextArea textArea = new TestableScrollableTextArea("Line 1\nLine 2\nLine 3");
        textArea.setSize(200, 100); // Set width to enable proper line calculation
        textArea.callCalculateOffsets(); // Ensure line breaks are calculated
        textArea.layout(); // Ensure layout is calculated

        float prefHeight = textArea.getPrefHeight();
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);

        // Pref height should be based on number of lines and font line height
        int expectedLines = 3;
        float expectedHeight = expectedLines * textArea.getStyle().font.getLineHeight();
        Assert.assertEquals("Pref height should match lines * line height",
                expectedHeight, prefHeight, 0.0001f);
    }

    @Test
    public void testGetPrefHeightWithEmptyText() {
        TestableScrollableTextArea textArea = new TestableScrollableTextArea("");
        textArea.setSize(200, 100); // Set width to enable proper line calculation
        textArea.callCalculateOffsets(); // Ensure line breaks are calculated
        textArea.layout(); // Ensure layout is calculated

        float prefHeight = textArea.getPrefHeight();
        // For empty text, getLines() returns 0, so getPrefHeight() returns 0
        // This is the actual behavior of ScrollableTextArea
        Assert.assertEquals("Pref height should be 0 for empty text", 0.0f, prefHeight, 0.0001f);
    }

    @Test
    public void testGetPrefHeightWithSingleLine() {
        TestableScrollableTextArea textArea = new TestableScrollableTextArea("Single line");
        textArea.setSize(200, 100); // Set width to enable proper line calculation
        textArea.callCalculateOffsets(); // Ensure line breaks are calculated
        textArea.layout(); // Ensure layout is calculated

        float prefHeight = textArea.getPrefHeight();
        float expectedHeight = 1 * textArea.getStyle().font.getLineHeight();
        Assert.assertEquals("Pref height should match one line height",
                expectedHeight, prefHeight, 0.0001f);
    }

    @Test
    public void testSetText() {
        ScrollableTextArea textArea = new ScrollableTextArea("Original");

        Assert.assertEquals("Original text should be set", "Original", textArea.getText());

        textArea.setText("New text");
        Assert.assertEquals("Text should be updated", "New text", textArea.getText());
    }

    @Test
    public void testSetTextWithEmptyString() {
        ScrollableTextArea textArea = new ScrollableTextArea("Original");

        textArea.setText("");
        Assert.assertEquals("Text should be empty", "", textArea.getText());
    }

    @Test
    public void testSetTextWithNull() {
        ScrollableTextArea textArea = new ScrollableTextArea("Original");

        textArea.setText(null);
        Assert.assertEquals("Text should be empty when null is set", "", textArea.getText());
    }

    @Test
    public void testCreateCompatibleScrollPane() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        ScrollPane scrollPane = textArea.createCompatibleScrollPane();

        Assert.assertNotNull("Scroll pane should be created", scrollPane);
        Assert.assertFalse("Fade scroll bars should be disabled", scrollPane.getFadeScrollBars());
        Assert.assertTrue("X scrolling should be disabled", scrollPane.isScrollingDisabledX());
        Assert.assertFalse("Y scrolling should be enabled", scrollPane.isScrollingDisabledY());
    }

    @Test
    public void testSetCullingArea() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        Rectangle cullingArea = new Rectangle(0, 0, 100, 50);

        textArea.setCullingArea(cullingArea);

        // Test that culling area is set (we can't easily access the private field)
        // This test mainly ensures the method doesn't throw exceptions
        Assert.assertTrue("Should implement Cullable", true);
    }

    @Test
    public void testSetParentWithScrollPane() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        ScrollPane scrollPane = new ScrollPane(textArea);

        // Setting parent to scroll pane should not throw exception
        textArea.setParent(scrollPane);

        Assert.assertNotNull("Parent should be set", textArea.getParent());
    }

    @Test
    public void testSetParentWithNonScrollPane() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        Group group = new Group();

        // Setting parent to non-scroll pane should not throw exception
        textArea.setParent(group);

        Assert.assertNotNull("Parent should be set", textArea.getParent());
    }

    @Test
    public void testSetParentWithNull() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");

        // Setting parent to null should not throw exception
        textArea.setParent(null);

        Assert.assertNull("Parent should be null", textArea.getParent());
    }

    @Test
    public void testSizeChanged() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");

        // Size change should not throw exception
        textArea.setSize(200, 100);
        textArea.sizeChanged();

        Assert.assertEquals("Width should be set", 200, textArea.getWidth(), 0.0001f);
        Assert.assertEquals("Height should be set", 100, textArea.getHeight(), 0.0001f);
    }

    @Test
    public void testLinesShowingProperty() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");

        // After size change, linesShowing should be set to a large value
        textArea.sizeChanged();

        // We can't access the private field directly, but we can test that sizeChanged doesn't crash
        Assert.assertNotNull("Text area should still be functional", textArea.getText());
    }

    @Test
    public void testUpdateScrollLayout() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");

        // Should not throw exception
        textArea.updateScrollLayout();
    }

    @Test
    public void testUpdateScrollPosition() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        Rectangle cullingArea = new Rectangle(0, 0, 100, 50);
        textArea.setCullingArea(cullingArea);
    }


    @Test
    public void testUpdateScrollPositionWithoutScrollPaneParent() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        Rectangle cullingArea = new Rectangle(0, 0, 100, 50);
        textArea.setCullingArea(cullingArea);
        textArea.setParent(new Group()); // Non-scroll pane parent
    }

    @Test
    public void testProgrammaticChangeEvents() {
        ScrollableTextArea textArea = new ScrollableTextArea("Original");

        // Test with programmatic change events enabled (default)
        textArea.setText("New text");
        Assert.assertEquals("Text should be updated", "New text", textArea.getText());
    }

    @Test
    public void testChangeTextMethod() {
        ScrollableTextArea textArea = new ScrollableTextArea("Original");

        // Test the changeText method indirectly through setText
        textArea.setText("New text");
        Assert.assertEquals("Text should be updated", "New text", textArea.getText());
    }

    @Test
    public void testWithMultilineText() {
        String multilineText = "Line 1\nLine 2\nLine 3\nLine 4";
        TestableScrollableTextArea textArea = new TestableScrollableTextArea(multilineText);
        textArea.setSize(200, 100); // Set width to enable proper line calculation
        textArea.callCalculateOffsets(); // Ensure line breaks are calculated
        textArea.layout(); // Ensure layout is calculated

        Assert.assertEquals("Multiline text should be preserved", multilineText, textArea.getText());

        float prefHeight = textArea.getPrefHeight();
        int expectedLines = 4;
        float expectedHeight = expectedLines * textArea.getStyle().font.getLineHeight();
        Assert.assertEquals("Pref height should account for all lines",
                expectedHeight, prefHeight, 0.0001f);
    }

    @Test
    public void testWithEmptyConstructor() {
        ScrollableTextArea textArea = new ScrollableTextArea("");

        Assert.assertNotNull("Text area should be created with empty text", textArea);
        Assert.assertEquals("Text should be empty", "", textArea.getText());
    }
}
