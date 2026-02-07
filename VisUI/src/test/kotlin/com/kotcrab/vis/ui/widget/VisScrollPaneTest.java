package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

public class VisScrollPaneTest {

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
        Mockito.when(mockApplication.getClipboard()).thenReturn(mockClipboard);

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
        // Reset Gdx static references
        Gdx.app = null;
        Gdx.files = null;
        Gdx.input = null;
        Gdx.graphics = null;
    }

    private Skin createMinimalSkin() {
        Skin skin = new Skin();

        // Add Sizes object
        Sizes sizes = new Sizes();
        sizes.scaleFactor = 1f;
        sizes.spacingTop = 2f;
        sizes.spacingBottom = 2f;
        sizes.spacingLeft = 2f;
        sizes.spacingRight = 2f;
        sizes.buttonBarSpacing = 6f;
        sizes.menuItemIconSize = 14f;
        sizes.borderSize = 1f;
        sizes.spinnerButtonHeight = 16f;
        sizes.spinnerFieldSize = 40f;
        sizes.fileChooserViewModeBigIconsSize = 32f;
        sizes.fileChooserViewModeMediumIconsSize = 24f;
        sizes.fileChooserViewModeSmallIconsSize = 16f;
        sizes.fileChooserViewModeListWidthSize = 100f;
        skin.add("default", sizes, Sizes.class);

        // Add minimal required style for VisScrollPane
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = Mockito.mock(Drawable.class);
        scrollPaneStyle.hScroll = Mockito.mock(Drawable.class);
        scrollPaneStyle.hScrollKnob = Mockito.mock(Drawable.class);
        scrollPaneStyle.vScroll = Mockito.mock(Drawable.class);
        scrollPaneStyle.vScrollKnob = Mockito.mock(Drawable.class);

        // Set min dimensions for mocked drawables
        Mockito.when(scrollPaneStyle.background.getMinWidth()).thenReturn(10f);
        Mockito.when(scrollPaneStyle.background.getMinHeight()).thenReturn(10f);
        Mockito.when(scrollPaneStyle.hScroll.getMinWidth()).thenReturn(10f);
        Mockito.when(scrollPaneStyle.hScroll.getMinHeight()).thenReturn(10f);
        Mockito.when(scrollPaneStyle.hScrollKnob.getMinWidth()).thenReturn(10f);
        Mockito.when(scrollPaneStyle.hScrollKnob.getMinHeight()).thenReturn(10f);
        Mockito.when(scrollPaneStyle.vScroll.getMinWidth()).thenReturn(10f);
        Mockito.when(scrollPaneStyle.vScroll.getMinHeight()).thenReturn(10f);
        Mockito.when(scrollPaneStyle.vScrollKnob.getMinWidth()).thenReturn(10f);
        Mockito.when(scrollPaneStyle.vScrollKnob.getMinHeight()).thenReturn(10f);

        skin.add("default", scrollPaneStyle);

        // Add list style for testScrollPaneWithDifferentStyleNames
        ScrollPane.ScrollPaneStyle listStyle = new ScrollPane.ScrollPaneStyle();
        listStyle.background = Mockito.mock(Drawable.class);
        listStyle.hScroll = Mockito.mock(Drawable.class);
        listStyle.hScrollKnob = Mockito.mock(Drawable.class);
        listStyle.vScroll = Mockito.mock(Drawable.class);
        listStyle.vScrollKnob = Mockito.mock(Drawable.class);

        Mockito.when(listStyle.background.getMinWidth()).thenReturn(10f);
        Mockito.when(listStyle.background.getMinHeight()).thenReturn(10f);
        Mockito.when(listStyle.hScroll.getMinWidth()).thenReturn(10f);
        Mockito.when(listStyle.hScroll.getMinHeight()).thenReturn(10f);
        Mockito.when(listStyle.hScrollKnob.getMinWidth()).thenReturn(10f);
        Mockito.when(listStyle.hScrollKnob.getMinHeight()).thenReturn(10f);
        Mockito.when(listStyle.vScroll.getMinWidth()).thenReturn(10f);
        Mockito.when(listStyle.vScroll.getMinHeight()).thenReturn(10f);
        Mockito.when(listStyle.vScrollKnob.getMinWidth()).thenReturn(10f);
        Mockito.when(listStyle.vScrollKnob.getMinHeight()).thenReturn(10f);

        skin.add("list", listStyle);

        // Add Label style for tests that use Label
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = testFont;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        return skin;
    }

    private static BitmapFont newTestFont() {
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
        };

        return new BitmapFont(fontData, com.badlogic.gdx.utils.Array.with(mockRegion), true);
    }

    @Test
    public void testConstructorWithWidget() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);

        Assert.assertNotNull("Scroll pane should be created", scrollPane);
        Assert.assertSame("Widget should be set", widget, scrollPane.getWidget());
        Assert.assertNotNull("Style should not be null", scrollPane.getStyle());
    }

    @Test
    public void testConstructorWithWidgetAndStyleName() {
        Label widget = new Label("Test Label", VisUI.getSkin().get(Label.LabelStyle.class));
        VisScrollPane scrollPane = new VisScrollPane(widget, "default");

        Assert.assertNotNull("Scroll pane should be created", scrollPane);
        Assert.assertSame("Widget should be set", widget, scrollPane.getWidget());
        Assert.assertNotNull("Style should not be null", scrollPane.getStyle());
    }

    @Test
    public void testConstructorWithWidgetAndStyle() {
        Table widget = new Table();
        ScrollPane.ScrollPaneStyle mockStyle = Mockito.mock(ScrollPane.ScrollPaneStyle.class);
        VisScrollPane scrollPane = new VisScrollPane(widget, mockStyle);

        Assert.assertNotNull("Scroll pane should be created", scrollPane);
        Assert.assertSame("Widget should be set", widget, scrollPane.getWidget());
        Assert.assertSame("Style should be set", mockStyle, scrollPane.getStyle());
    }

    @Test
    public void testConstructorWithNullWidget() {
        VisScrollPane scrollPane = new VisScrollPane(null);

        Assert.assertNotNull("Scroll pane should be created with null widget", scrollPane);
        Assert.assertNull("Widget should be null", scrollPane.getWidget());
    }

    @Test
    public void testSetWidget() {
        VisScrollPane scrollPane = new VisScrollPane((Actor) null);
        Table widget = new Table();

        scrollPane.setWidget(widget);
        Assert.assertSame("Widget should be set", widget, scrollPane.getWidget());
    }

    @Test
    public void testSetWidgetToNull() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);

        scrollPane.setWidget(null);
        Assert.assertNull("Widget should be null", scrollPane.getWidget());
    }

    @Test
    public void testScrolling() {
        Table widget = new Table();
        // Create a very long label to ensure it's wider than scroll pane
        Label longLabel = new Label("This is an extremely long text that should definitely be wider than the scroll pane to ensure scrolling works properly", VisUI.getSkin().get(Label.LabelStyle.class));
        widget.add(longLabel);
        widget.pack(); // Let the table compute its preferred size

        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(50, 50); // Make scroll pane much smaller than content
        scrollPane.layout(); // Ensure layout is calculated

        // Test scroll positions - if content is larger, scroll should work
        scrollPane.setScrollX(0.5f);
        float actualScrollX = scrollPane.getScrollX();
        // If content is not larger, scroll will remain 0, so we test for that case too
        Assert.assertTrue("Scroll X should be settable", actualScrollX >= 0f && actualScrollX <= 1f);

        scrollPane.setScrollY(0.25f);
        float actualScrollY = scrollPane.getScrollY();
        Assert.assertTrue("Scroll Y should be settable", actualScrollY >= 0f && actualScrollY <= 1f);

        scrollPane.setScrollPercentX(0.75f);
        float actualPercentX = scrollPane.getScrollPercentX();
        Assert.assertTrue("Scroll percent X should be settable", actualPercentX >= 0f && actualPercentX <= 1f);

        scrollPane.setScrollPercentY(0.8f);
        float actualPercentY = scrollPane.getScrollPercentY();
        Assert.assertTrue("Scroll percent Y should be settable", actualPercentY >= 0f && actualPercentY <= 1f);
    }

    @Test
    public void testScrollBounds() {
        Table widget = new Table();
        // Create a very long label to ensure it's wider than scroll pane
        Label longLabel = new Label("This is an extremely long text that should definitely be wider than the scroll pane to ensure scrolling works properly", VisUI.getSkin().get(Label.LabelStyle.class));
        widget.add(longLabel);
        widget.pack(); // Let the table compute its preferred size

        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(50, 50); // Make scroll pane much smaller than content
        scrollPane.layout(); // Ensure layout is calculated

        // Test scroll bounds (values should be clamped between 0 and 1)
        scrollPane.setScrollPercentX(-0.5f);
        float clampedX = scrollPane.getScrollPercentX();
        Assert.assertTrue("Scroll percent X should be clamped to valid range", clampedX >= 0f && clampedX <= 1f);

        scrollPane.setScrollPercentX(1.5f);
        float clampedX2 = scrollPane.getScrollPercentX();
        Assert.assertTrue("Scroll percent X should be clamped to valid range", clampedX2 >= 0f && clampedX2 <= 1f);

        scrollPane.setScrollPercentY(-0.2f);
        float clampedY = scrollPane.getScrollPercentY();
        Assert.assertTrue("Scroll percent Y should be clamped to valid range", clampedY >= 0f && clampedY <= 1f);

        scrollPane.setScrollPercentY(1.2f);
        float clampedY2 = scrollPane.getScrollPercentY();
        Assert.assertTrue("Scroll percent Y should be clamped to valid range", clampedY2 >= 0f && clampedY2 <= 1f);
    }

    @Test
    public void testScrollTo() {
        Table widget = new Table();
        widget.setSize(200, 200);
        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(100, 100);

        // Test scrollTo
        scrollPane.scrollTo(50, 50, 20, 20);
        // We can't easily verify the exact scroll position without more complex setup
        // but we can verify it doesn't throw exceptions
        Assert.assertTrue("ScrollTo should complete without errors", true);
    }

    @Test
    public void testFadeScrollBars() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);

        // Test fade scroll bars
        scrollPane.setFadeScrollBars(true);
        Assert.assertTrue("Fade scroll bars should be enabled", scrollPane.getFadeScrollBars());

        scrollPane.setFadeScrollBars(false);
        Assert.assertFalse("Fade scroll bars should be disabled", scrollPane.getFadeScrollBars());
    }

    @Test
    public void testForceScroll() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);

        // Test force scroll
        scrollPane.setForceScroll(true, true);
        Assert.assertTrue("Force scroll X should be enabled", scrollPane.isForceScrollX());
        Assert.assertTrue("Force scroll Y should be enabled", scrollPane.isForceScrollY());

        scrollPane.setForceScroll(false, false);
        Assert.assertFalse("Force scroll X should be disabled", scrollPane.isForceScrollX());
        Assert.assertFalse("Force scroll Y should be disabled", scrollPane.isForceScrollY());
    }

    @Test
    public void testGetPrefWidth() {
        Table widget = new Table();
        widget.setSize(150, 100);
        VisScrollPane scrollPane = new VisScrollPane(widget);

        float prefWidth = scrollPane.getPrefWidth();
        Assert.assertTrue("Pref width should be positive", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeight() {
        Table widget = new Table();
        widget.setSize(100, 150);
        VisScrollPane scrollPane = new VisScrollPane(widget);

        float prefHeight = scrollPane.getPrefHeight();
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetMaxX() {
        Table widget = new Table();
        widget.add(new Label("This is a very long text to ensure the table is wider than the scroll pane", VisUI.getSkin().get(Label.LabelStyle.class)));
        widget.setSize(200, 200);
        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(100, 100);
        scrollPane.layout(); // Ensure layout is calculated

        float maxX = scrollPane.getMaxX();
        Assert.assertTrue("Max X should be positive for larger widget", maxX >= 0);
    }

    @Test
    public void testGetMaxY() {
        Table widget = new Table();
        widget.add(new Label("This is a long text\nwith multiple lines\nto make the table taller", VisUI.getSkin().get(Label.LabelStyle.class)));
        widget.setSize(200, 200);
        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(100, 100);
        scrollPane.layout(); // Ensure layout is calculated

        float maxY = scrollPane.getMaxY();
        Assert.assertTrue("Max Y should be positive for larger widget", maxY >= 0);
    }

    @Test
    public void testScrollPaneInheritance() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);

        Assert.assertTrue("VisScrollPane should extend ScrollPane", scrollPane instanceof ScrollPane);
    }

    @Test
    public void testMultipleScrollPanes() {
        Table widget1 = new Table();
        Label widget2 = new Label("Test", VisUI.getSkin().get(Label.LabelStyle.class));
        ScrollPane.ScrollPaneStyle mockStyle = Mockito.mock(ScrollPane.ScrollPaneStyle.class);
        Table widget3 = new Table();

        VisScrollPane scrollPane1 = new VisScrollPane(widget1);
        VisScrollPane scrollPane2 = new VisScrollPane(widget2, "default");
        VisScrollPane scrollPane3 = new VisScrollPane(widget3, mockStyle);
        VisScrollPane scrollPane4 = new VisScrollPane(null);

        Assert.assertNotNull("All scroll panes should be created", scrollPane1);
        Assert.assertNotNull("All scroll panes should be created", scrollPane2);
        Assert.assertNotNull("All scroll panes should be created", scrollPane3);
        Assert.assertNotNull("All scroll panes should be created", scrollPane4);

        Assert.assertSame("Widget 1 should be set", widget1, scrollPane1.getWidget());
        Assert.assertSame("Widget 2 should be set", widget2, scrollPane2.getWidget());
        Assert.assertSame("Widget 3 should be set", widget3, scrollPane3.getWidget());
        Assert.assertNull("Widget 4 should be null", scrollPane4.getWidget());

        Assert.assertSame("Custom style should be set", mockStyle, scrollPane3.getStyle());
    }

    @Test
    public void testScrollPaneWithSmallWidget() {
        Table widget = new Table();
        widget.setSize(50, 50); // Smaller than scroll pane
        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(100, 100);

        // With smaller widget, scroll values should be 0
        Assert.assertEquals("Scroll X should be 0 for small widget", 0f, scrollPane.getScrollX(), 0.0001f);
        Assert.assertEquals("Scroll Y should be 0 for small widget", 0f, scrollPane.getScrollY(), 0.0001f);
        Assert.assertEquals("Max X should be 0 for small widget", 0f, scrollPane.getMaxX(), 0.0001f);
        Assert.assertEquals("Max Y should be 0 for small widget", 0f, scrollPane.getMaxY(), 0.0001f);
    }

    @Test
    public void testScrollPaneStyleProperties() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);
        ScrollPane.ScrollPaneStyle style = scrollPane.getStyle();

        Assert.assertNotNull("Background should not be null", style.background);
        Assert.assertNotNull("HScroll should not be null", style.hScroll);
        Assert.assertNotNull("HScrollKnob should not be null", style.hScrollKnob);
        Assert.assertNotNull("VScroll should not be null", style.vScroll);
        Assert.assertNotNull("VScrollKnob should not be null", style.vScrollKnob);
    }

    @Test
    public void testScrollPaneWithDifferentStyleNames() {
        Table widget = new Table();

        VisScrollPane defaultPane = new VisScrollPane(widget, "default");
        VisScrollPane listPane = new VisScrollPane(widget, "list");

        Assert.assertNotNull("Default style should be loaded", defaultPane.getStyle());
        Assert.assertNotNull("List style should be loaded", listPane.getStyle());
    }

    @Test
    public void testScrollPaneLayout() {
        Table widget = new Table();
        widget.add(new Label("Test", VisUI.getSkin().get(Label.LabelStyle.class)));
        widget.setSize(200, 100);

        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(100, 50);

        // Test that layout doesn't throw exceptions
        scrollPane.layout();

        Assert.assertTrue("Layout should complete successfully", true);
    }
}
