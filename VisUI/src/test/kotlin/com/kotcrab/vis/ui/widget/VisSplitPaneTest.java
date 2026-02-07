package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link VisSplitPane}.
 */
public class VisSplitPaneTest {

    @Test
    public void testConstructorHorizontal() {
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, false);
        
        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertFalse("Should be horizontal", splitPane.vertical);
        Assert.assertSame("First widget should be set", firstWidget, splitPane.getFirstWidget());
        Assert.assertSame("Second widget should be set", secondWidget, splitPane.getSecondWidget());
        Assert.assertNotNull("Style should not be null", splitPane.getStyle());
        Assert.assertEquals("Default split amount should be 0.5", 0.5f, splitPane.getSplit(), 0.0001f);
    }

    @Test
    public void testConstructorVertical() {
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, true);
        
        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertTrue("Should be vertical", splitPane.vertical);
        Assert.assertSame("First widget should be set", firstWidget, splitPane.getFirstWidget());
        Assert.assertSame("Second widget should be set", secondWidget, splitPane.getSecondWidget());
        Assert.assertNotNull("Style should not be null", splitPane.getStyle());
        Assert.assertEquals("Default split amount should be 0.5", 0.5f, splitPane.getSplit(), 0.0001f);
    }

    @Test
    public void testConstructorWithNullWidgets() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        
        Assert.assertNotNull("Split pane should be created with null widgets", splitPane);
        Assert.assertNull("First widget should be null", splitPane.getFirstWidget());
        Assert.assertNull("Second widget should be null", splitPane.getSecondWidget());
    }

    @Test
    public void testConstructorWithStyleName() {
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, false, "default-horizontal");
        
        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertSame("First widget should be set", firstWidget, splitPane.getFirstWidget());
        Assert.assertSame("Second widget should be set", secondWidget, splitPane.getSecondWidget());
        Assert.assertNotNull("Style should not be null", splitPane.getStyle());
    }

    @Test
    public void testConstructorWithStyle() {
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        VisSplitPane.VisSplitPaneStyle mockStyle = Mockito.mock(VisSplitPane.VisSplitPaneStyle.class);
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, false, mockStyle);
        
        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertSame("First widget should be set", firstWidget, splitPane.getFirstWidget());
        Assert.assertSame("Second widget should be set", secondWidget, splitPane.getSecondWidget());
        Assert.assertSame("Style should be set", mockStyle, splitPane.getStyle());
    }

    @Test
    public void testSetSplitAmount() {
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, false);
        
        splitPane.setSplitAmount(0.25f);
        Assert.assertEquals("Split amount should be 0.25", 0.25f, splitPane.getSplit(), 0.0001f);
        
        splitPane.setSplitAmount(0.75f);
        Assert.assertEquals("Split amount should be 0.75", 0.75f, splitPane.getSplit(), 0.0001f);
    }

    @Test
    public void testSetSplitAmountOutOfBounds() {
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, false);
        
        // Test values below 0
        splitPane.setSplitAmount(-0.5f);
        Assert.assertEquals("Split amount should be clamped to 0", 0f, splitPane.getSplit(), 0.0001f);
        
        // Test values above 1
        splitPane.setSplitAmount(1.5f);
        Assert.assertEquals("Split amount should be clamped to 1", 1f, splitPane.getSplit(), 0.0001f);
    }

    @Test
    public void testSetMinSplitAmount() {
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, false);
        
        splitPane.setMinSplitAmount(0.2f);
        splitPane.setSplitAmount(0.1f); // Try to set below min
        Assert.assertEquals("Split amount should be clamped to min", 0.2f, splitPane.getSplit(), 0.0001f);
    }

    @Test
    public void testSetMaxSplitAmount() {
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, false);
        
        splitPane.setMaxSplitAmount(0.8f);
        splitPane.setSplitAmount(0.9f); // Try to set above max
        Assert.assertEquals("Split amount should be clamped to max", 0.8f, splitPane.getSplit(), 0.0001f);
    }

    @Test(expected = GdxRuntimeException.class)
    public void testSetMinSplitAmountNegative() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        splitPane.setMinSplitAmount(-0.1f);
    }

    @Test(expected = GdxRuntimeException.class)
    public void testSetMaxSplitAmountGreaterThanOne() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        splitPane.setMaxSplitAmount(1.1f);
    }

    @Test(expected = GdxRuntimeException.class)
    public void testSetMinSplitAmountGreaterThanMax() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        splitPane.setMaxSplitAmount(0.8f);
        splitPane.setMinSplitAmount(0.9f);
    }

    @Test
    public void testSetWidgets() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        
        splitPane.setWidgets(firstWidget, secondWidget);
        
        Assert.assertSame("First widget should be set", firstWidget, splitPane.getFirstWidget());
        Assert.assertSame("Second widget should be set", secondWidget, splitPane.getSecondWidget());
    }

    @Test
    public void testSetFirstWidget() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        Table widget = new Table();
        
        splitPane.setFirstWidget(widget);
        
        Assert.assertSame("First widget should be set", widget, splitPane.getFirstWidget());
    }

    @Test
    public void testSetSecondWidget() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        Table widget = new Table();
        
        splitPane.setSecondWidget(widget);
        
        Assert.assertSame("Second widget should be set", widget, splitPane.getSecondWidget());
    }

    @Test
    public void testGetFirstWidgetBounds() {
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, false);
        splitPane.setSize(200, 100);
        splitPane.layout();
        
        Rectangle bounds = splitPane.getFirstWidgetBounds();
        
        Assert.assertNotNull("Bounds should not be null", bounds);
        Assert.assertTrue("Bounds should have positive width", bounds.width > 0);
        Assert.assertTrue("Bounds should have positive height", bounds.height > 0);
    }

    @Test
    public void testGetSecondWidgetBounds() {
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, false);
        splitPane.setSize(200, 100);
        splitPane.layout();
        
        Rectangle bounds = splitPane.getSecondWidgetBounds();
        
        Assert.assertNotNull("Bounds should not be null", bounds);
        Assert.assertTrue("Bounds should have positive width", bounds.width > 0);
        Assert.assertTrue("Bounds should have positive height", bounds.height > 0);
    }

    @Test
    public void testSetVertical() {
        Table firstWidget = new Table();
        Table secondWidget = new Table();
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, false);
        
        splitPane.setVertical(true);
        Assert.assertTrue("Should be vertical", splitPane.vertical);
        
        splitPane.setVertical(false);
        Assert.assertFalse("Should be horizontal", splitPane.vertical);
    }

    @Test
    public void testGetPrefWidth() {
        Table firstWidget = new Table();
        firstWidget.setSize(100, 50);
        Table secondWidget = new Table();
        secondWidget.setSize(80, 60);
        
        VisSplitPane horizontalSplitPane = new VisSplitPane(firstWidget, secondWidget, false);
        VisSplitPane verticalSplitPane = new VisSplitPane(firstWidget, secondWidget, true);
        
        float horizontalPrefWidth = horizontalSplitPane.getPrefWidth();
        float verticalPrefWidth = verticalSplitPane.getPrefWidth();
        
        Assert.assertTrue("Horizontal pref width should be positive", horizontalPrefWidth > 0);
        Assert.assertTrue("Vertical pref width should be positive", verticalPrefWidth > 0);
        Assert.assertTrue("Horizontal pref width should be larger (includes handle)", 
                         horizontalPrefWidth > verticalPrefWidth);
    }

    @Test
    public void testGetPrefHeight() {
        Table firstWidget = new Table();
        firstWidget.setSize(50, 100);
        Table secondWidget = new Table();
        secondWidget.setSize(60, 80);
        
        VisSplitPane horizontalSplitPane = new VisSplitPane(firstWidget, secondWidget, false);
        VisSplitPane verticalSplitPane = new VisSplitPane(firstWidget, secondWidget, true);
        
        float horizontalPrefHeight = horizontalSplitPane.getPrefHeight();
        float verticalPrefHeight = verticalSplitPane.getPrefHeight();
        
        Assert.assertTrue("Horizontal pref height should be positive", horizontalPrefHeight > 0);
        Assert.assertTrue("Vertical pref height should be positive", verticalPrefHeight > 0);
        Assert.assertTrue("Vertical pref height should be larger (includes handle)", 
                         verticalPrefHeight > horizontalPrefHeight);
    }

    @Test
    public void testGetMinWidthAndHeight() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        
        Assert.assertEquals("Min width should be 0", 0f, splitPane.getMinWidth(), 0.0001f);
        Assert.assertEquals("Min height should be 0", 0f, splitPane.getMinHeight(), 0.0001f);
    }

    @Test
    public void testSplitPaneInheritance() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        
        Assert.assertTrue("VisSplitPane should extend WidgetGroup", 
                        splitPane instanceof com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup);
    }

    @Test
    public void testMultipleSplitPanes() {
        Table widget1 = new Table();
        Table widget2 = new Table();
        Table widget3 = new Table();
        Table widget4 = new Table();
        VisSplitPane.VisSplitPaneStyle mockStyle = Mockito.mock(VisSplitPane.VisSplitPaneStyle.class);
        
        VisSplitPane horizontal1 = new VisSplitPane(widget1, widget2, false);
        VisSplitPane horizontal2 = new VisSplitPane(widget1, widget2, false, "default-horizontal");
        VisSplitPane vertical1 = new VisSplitPane(widget3, widget4, true);
        VisSplitPane customStyle = new VisSplitPane(widget1, widget2, false, mockStyle);
        
        Assert.assertNotNull("All split panes should be created", horizontal1);
        Assert.assertNotNull("All split panes should be created", horizontal2);
        Assert.assertNotNull("All split panes should be created", vertical1);
        Assert.assertNotNull("All split panes should be created", customStyle);
        
        Assert.assertFalse("First should be horizontal", horizontal1.vertical);
        Assert.assertFalse("Second should be horizontal", horizontal2.vertical);
        Assert.assertTrue("Third should be vertical", vertical1.vertical);
        Assert.assertFalse("Fourth should be horizontal", customStyle.vertical);
        
        Assert.assertSame("Custom style should be set", mockStyle, customStyle.getStyle());
    }

    @Test
    public void testSplitPaneWithNullWidget() {
        Table widget = new Table();
        VisSplitPane splitPane = new VisSplitPane(widget, null, false);
        
        Assert.assertSame("First widget should be set", widget, splitPane.getFirstWidget());
        Assert.assertNull("Second widget should be null", splitPane.getSecondWidget());
        
        splitPane = new VisSplitPane(null, widget, false);
        Assert.assertNull("First widget should be null", splitPane.getFirstWidget());
        Assert.assertSame("Second widget should be set", widget, splitPane.getSecondWidget());
    }

    @Test
    public void testSplitPaneStyleProperties() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        VisSplitPane.VisSplitPaneStyle style = splitPane.getStyle();
        
        Assert.assertNotNull("Handle should not be null", style.handle);
        Assert.assertNull("Handle over should be null by default", style.handleOver);
    }

    @Test
    public void testSplitPaneStyleCopyConstructor() {
        Drawable mockHandle = Mockito.mock(Drawable.class);
        Drawable mockHandleOver = Mockito.mock(Drawable.class);
        
        VisSplitPane.VisSplitPaneStyle original = new VisSplitPane.VisSplitPaneStyle(mockHandle, mockHandleOver);
        VisSplitPane.VisSplitPaneStyle copy = new VisSplitPane.VisSplitPaneStyle(original);
        
        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertEquals("Handle should be copied", mockHandle, copy.handle);
        Assert.assertEquals("Handle over should be copied", mockHandleOver, copy.handleOver);
    }

    @Test
    public void testSplitPaneStyleDefaultConstructor() {
        VisSplitPane.VisSplitPaneStyle style = new VisSplitPane.VisSplitPaneStyle();
        
        Assert.assertNotNull("Style should be created", style);
        Assert.assertNull("Handle should be null by default", style.handle);
        Assert.assertNull("Handle over should be null by default", style.handleOver);
    }

    @Test
    public void testSplitPaneStyleConstructorWithDrawables() {
        Drawable mockHandle = Mockito.mock(Drawable.class);
        Drawable mockHandleOver = Mockito.mock(Drawable.class);
        
        VisSplitPane.VisSplitPaneStyle style = new VisSplitPane.VisSplitPaneStyle(mockHandle, mockHandleOver);
        
        Assert.assertNotNull("Style should be created", style);
        Assert.assertEquals("Handle should be set", mockHandle, style.handle);
        Assert.assertEquals("Handle over should be set", mockHandleOver, style.handleOver);
    }

    @Test
    public void testSplitPaneLayout() {
        Table firstWidget = new Table();
        firstWidget.add(new Label("First", Mockito.mock(com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle.class)));
        firstWidget.setSize(100, 50);
        
        Table secondWidget = new Table();
        secondWidget.add(new Label("Second", Mockito.mock(com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle.class)));
        secondWidget.setSize(100, 50);
        
        VisSplitPane splitPane = new VisSplitPane(firstWidget, secondWidget, false);
        splitPane.setSize(200, 100);
        
        // Test that layout doesn't throw exceptions
        splitPane.layout();
        
        Assert.assertTrue("Layout should complete successfully", true);
    }

    @Test
    public void testSplitPaneWithDifferentStyleNames() {
        Table widget1 = new Table();
        Table widget2 = new Table();
        
        VisSplitPane horizontalPane = new VisSplitPane(widget1, widget2, false, "default-horizontal");
        VisSplitPane verticalPane = new VisSplitPane(widget1, widget2, true, "default-vertical");
        
        Assert.assertNotNull("Horizontal style should be loaded", horizontalPane.getStyle());
        Assert.assertNotNull("Vertical style should be loaded", verticalPane.getStyle());
    }

    @Test
    public void testSplitPaneRemoveActor() {
        Table widget1 = new Table();
        Table widget2 = new Table();
        VisSplitPane splitPane = new VisSplitPane(widget1, widget2, false);
        
        boolean removed1 = splitPane.removeActor(widget1);
        boolean removed2 = splitPane.removeActor(widget2);
        
        Assert.assertTrue("First widget should be removable", removed1);
        Assert.assertTrue("Second widget should be removable", removed2);
        Assert.assertNull("First widget should be null after removal", splitPane.getFirstWidget());
        Assert.assertNull("Second widget should be null after removal", splitPane.getSecondWidget());
    }

    @Test
    public void testSplitPaneRemoveActorWithUnfocus() {
        Table widget = new Table();
        VisSplitPane splitPane = new VisSplitPane(widget, null, false);
        
        boolean removed = splitPane.removeActor(widget, true);
        
        Assert.assertTrue("Widget should be removable", removed);
        Assert.assertNull("Widget should be null after removal", splitPane.getFirstWidget());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitPaneRemoveNullActor() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        splitPane.removeActor(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSplitPaneAddActor() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        Table widget = new Table();
        
        splitPane.addActor(widget);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSplitPaneAddActorAt() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        Table widget = new Table();
        
        splitPane.addActorAt(0, widget);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSplitPaneAddActorBefore() {
        VisSplitPane splitPane = new VisSplitPane(null, null, false);
        Table widget1 = new Table();
        Table widget2 = new Table();
        
        splitPane.addActorBefore(widget1, widget2);
    }
}
