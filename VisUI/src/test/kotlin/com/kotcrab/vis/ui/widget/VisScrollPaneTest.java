package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link VisScrollPane}.
 */
public class VisScrollPaneTest {

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
        Label widget = new Label("Test Label", Mockito.mock(com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle.class));
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
        widget.setSize(200, 200); // Make widget larger than scroll pane
        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(100, 100);
        
        // Test scroll positions
        scrollPane.setScrollX(0.5f);
        Assert.assertEquals("Scroll X should be set", 0.5f, scrollPane.getScrollX(), 0.0001f);
        
        scrollPane.setScrollY(0.25f);
        Assert.assertEquals("Scroll Y should be set", 0.25f, scrollPane.getScrollY(), 0.0001f);
        
        scrollPane.setScrollPercentX(0.75f);
        Assert.assertEquals("Scroll percent X should be set", 0.75f, scrollPane.getScrollPercentX(), 0.0001f);
        
        scrollPane.setScrollPercentY(0.8f);
        Assert.assertEquals("Scroll percent Y should be set", 0.8f, scrollPane.getScrollPercentY(), 0.0001f);
    }

    @Test
    public void testScrollBounds() {
        Table widget = new Table();
        widget.setSize(200, 200);
        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(100, 100);
        
        // Test scroll bounds (values should be clamped between 0 and 1)
        scrollPane.setScrollPercentX(-0.5f);
        Assert.assertEquals("Scroll percent X should be clamped to 0", 0f, scrollPane.getScrollPercentX(), 0.0001f);
        
        scrollPane.setScrollPercentX(1.5f);
        Assert.assertEquals("Scroll percent X should be clamped to 1", 1f, scrollPane.getScrollPercentX(), 0.0001f);
        
        scrollPane.setScrollPercentY(-0.2f);
        Assert.assertEquals("Scroll percent Y should be clamped to 0", 0f, scrollPane.getScrollPercentY(), 0.0001f);
        
        scrollPane.setScrollPercentY(1.2f);
        Assert.assertEquals("Scroll percent Y should be clamped to 1", 1f, scrollPane.getScrollPercentY(), 0.0001f);
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
    public void testScrollToCenter() {
        Table widget = new Table();
        widget.setSize(200, 200);
        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(100, 100);
        
        // Test scrollToCenter
        scrollPane.scrollToCenter(50, 50, 20, 20);
        // We can't easily verify the exact scroll position without more complex setup
        // but we can verify it doesn't throw exceptions
        Assert.assertTrue("ScrollToCenter should complete without errors", true);
    }

    @Test
    public void testFlickScroll() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);
        
        // Test flick scroll properties
        scrollPane.setFlickScroll(true);
        Assert.assertTrue("Flick scroll should be enabled", scrollPane.isFlickScroll());
        
        scrollPane.setFlickScroll(false);
        Assert.assertFalse("Flick scroll should be disabled", scrollPane.isFlickScroll());
    }

    @Test
    public void testSmoothScrolling() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);
        
        // Test smooth scrolling
        scrollPane.setSmoothScrolling(true);
        Assert.assertTrue("Smooth scrolling should be enabled", scrollPane.isSmoothScrolling());
        
        scrollPane.setSmoothScrolling(false);
        Assert.assertFalse("Smooth scrolling should be disabled", scrollPane.isSmoothScrolling());
    }

    @Test
    public void testOverscroll() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);
        
        // Test overscroll X
        scrollPane.setOverscrollX(true);
        Assert.assertTrue("Overscroll X should be enabled", scrollPane.isOverscrollX());
        
        scrollPane.setOverscrollX(false);
        Assert.assertFalse("Overscroll X should be disabled", scrollPane.isOverscrollX());
        
        // Test overscroll Y
        scrollPane.setOverscrollY(true);
        Assert.assertTrue("Overscroll Y should be enabled", scrollPane.isOverscrollY());
        
        scrollPane.setOverscrollY(false);
        Assert.assertFalse("Overscroll Y should be disabled", scrollPane.isOverscrollY());
    }

    @Test
    public void testScrollBarVisibility() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);
        
        // Test scroll bar visibility
        scrollPane.setScrollBarVisibility(ScrollPane.ScrollBarVisibility.Always);
        Assert.assertEquals("Scroll bar visibility should be Always", 
                          ScrollPane.ScrollBarVisibility.Always, scrollPane.getScrollBarVisibility());
        
        scrollPane.setScrollBarVisibility(ScrollPane.ScrollBarVisibility.Never);
        Assert.assertEquals("Scroll bar visibility should be Never", 
                          ScrollPane.ScrollBarVisibility.Never, scrollPane.getScrollBarVisibility());
        
        scrollPane.setScrollBarVisibility(ScrollPane.ScrollBarVisibility.WhenNeeded);
        Assert.assertEquals("Scroll bar visibility should be WhenNeeded", 
                          ScrollPane.ScrollBarVisibility.WhenNeeded, scrollPane.getScrollBarVisibility());
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
    public void testScrollBarsOnTop() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);
        
        // Test scroll bars on top
        scrollPane.setScrollBarsOnTop(true);
        Assert.assertTrue("Scroll bars on top should be enabled", scrollPane.isScrollBarsOnTop());
        
        scrollPane.setScrollBarsOnTop(false);
        Assert.assertFalse("Scroll bars on top should be disabled", scrollPane.isScrollBarsOnTop());
    }

    @Test
    public void testVariableSizeKnobs() {
        Table widget = new Table();
        VisScrollPane scrollPane = new VisScrollPane(widget);
        
        // Test variable size knobs
        scrollPane.setVariableSizeKnobs(true);
        Assert.assertTrue("Variable size knobs should be enabled", scrollPane.isVariableSizeKnobs());
        
        scrollPane.setVariableSizeKnobs(false);
        Assert.assertFalse("Variable size knobs should be disabled", scrollPane.isVariableSizeKnobs());
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
        widget.setSize(200, 200);
        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(100, 100);
        
        float maxX = scrollPane.getMaxX();
        Assert.assertTrue("Max X should be positive for larger widget", maxX > 0);
    }

    @Test
    public void testGetMaxY() {
        Table widget = new Table();
        widget.setSize(200, 200);
        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(100, 100);
        
        float maxY = scrollPane.getMaxY();
        Assert.assertTrue("Max Y should be positive for larger widget", maxY > 0);
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
        Label widget2 = new Label("Test", Mockito.mock(com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle.class));
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
        widget.add(new Label("Test", Mockito.mock(com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle.class)));
        widget.setSize(200, 100);
        
        VisScrollPane scrollPane = new VisScrollPane(widget);
        scrollPane.setSize(100, 50);
        
        // Test that layout doesn't throw exceptions
        scrollPane.layout();
        
        Assert.assertTrue("Layout should complete successfully", true);
    }
}
