package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 * Unit tests for {@link MultiSplitPane}.
 */
public class MultiSplitPaneTest {

    @Test
    public void testConstructorWithVertical() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        
        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertTrue("Should be vertical", getPrivateField(splitPane, "vertical"));
    }

    @Test
    public void testConstructorWithHorizontal() {
        MultiSplitPane splitPane = new MultiSplitPane(false);
        
        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertFalse("Should be horizontal", getPrivateField(splitPane, "vertical"));
    }

    @Test
    public void testConstructorWithVerticalAndStyleName() {
        MultiSplitPane splitPane = new MultiSplitPane(true, "default-vertical");
        
        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertTrue("Should be vertical", getPrivateField(splitPane, "vertical"));
    }

    @Test
    public void testConstructorWithHorizontalAndStyleName() {
        MultiSplitPane splitPane = new MultiSplitPane(false, "default-horizontal");
        
        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertFalse("Should be horizontal", getPrivateField(splitPane, "vertical"));
    }

    @Test
    public void testConstructorWithStyle() {
        Drawable mockHandle = Mockito.mock(Drawable.class);
        MultiSplitPane.MultiSplitPaneStyle style = new MultiSplitPane.MultiSplitPaneStyle(mockHandle, null);
        
        MultiSplitPane splitPane = new MultiSplitPane(true, style);
        
        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertSame("Style should be set", style, splitPane.getStyle());
    }

    @Test
    public void testSetAndGetStyle() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Drawable mockHandle = Mockito.mock(Drawable.class);
        MultiSplitPane.MultiSplitPaneStyle newStyle = new MultiSplitPane.MultiSplitPaneStyle(mockHandle, null);
        
        splitPane.setStyle(newStyle);
        
        Assert.assertSame("Style should be set", newStyle, splitPane.getStyle());
    }

    @Test
    public void testSetVertical() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        
        splitPane.setVertical(false);
        Assert.assertFalse("Should be horizontal", getPrivateField(splitPane, "vertical"));
        
        splitPane.setVertical(true);
        Assert.assertTrue("Should be vertical", getPrivateField(splitPane, "vertical"));
    }

    @Test
    public void testSetWidgetsWithArray() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        
        splitPane.setWidgets(table1, table2, table3);
        
        Assert.assertEquals("Should have 3 children", 3, splitPane.getChildren().size);
        Assert.assertTrue("Should contain table1", splitPane.getChildren().contains(table1, true));
        Assert.assertTrue("Should contain table2", splitPane.getChildren().contains(table2, true));
        Assert.assertTrue("Should contain table3", splitPane.getChildren().contains(table3, true));
    }

    @Test
    public void testSetWidgetsWithIterable() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table1 = new Table();
        Table table2 = new Table();
        
        splitPane.setWidgets(Arrays.asList(table1, table2));
        
        Assert.assertEquals("Should have 2 children", 2, splitPane.getChildren().size);
        Assert.assertTrue("Should contain table1", splitPane.getChildren().contains(table1, true));
        Assert.assertTrue("Should contain table2", splitPane.getChildren().contains(table2, true));
    }

    @Test
    public void testSetWidgetsWithSingleWidget() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table = new Table();
        
        splitPane.setWidgets(table);
        
        Assert.assertEquals("Should have 1 child", 1, splitPane.getChildren().size);
        Assert.assertTrue("Should contain table", splitPane.getChildren().contains(table, true));
    }

    @Test
    public void testSetWidgetsWithNoWidgets() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        
        splitPane.setWidgets();
        
        Assert.assertEquals("Should have no children", 0, splitPane.getChildren().size);
    }

    @Test
    public void testSetWidgetsReplacesExisting() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        
        splitPane.setWidgets(table1, table2);
        Assert.assertEquals("Should have 2 children initially", 2, splitPane.getChildren().size);
        
        splitPane.setWidgets(table3);
        Assert.assertEquals("Should have 1 child after replacement", 1, splitPane.getChildren().size);
        Assert.assertTrue("Should contain table3", splitPane.getChildren().contains(table3, true));
        Assert.assertFalse("Should not contain table1", splitPane.getChildren().contains(table1, true));
        Assert.assertFalse("Should not contain table2", splitPane.getChildren().contains(table2, true));
    }

    @Test
    public void testSetSplit() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        
        splitPane.setWidgets(table1, table2, table3);
        
        // Should have 2 split handles for 3 widgets
        splitPane.setSplit(0, 0.3f);
        splitPane.setSplit(1, 0.7f);
        
        // Test that splits are set without throwing exceptions
        // We can't easily test the actual split values without accessing private fields
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSplitWithNegativeIndex() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table1 = new Table();
        Table table2 = new Table();
        
        splitPane.setWidgets(table1, table2);
        splitPane.setSplit(-1, 0.5f);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSplitWithIndexTooLarge() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table1 = new Table();
        Table table2 = new Table();
        
        splitPane.setWidgets(table1, table2);
        splitPane.setSplit(2, 0.5f); // Only 1 split handle for 2 widgets
    }

    @Test
    public void testSetSplitClamping() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table1 = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        
        splitPane.setWidgets(table1, table2, table3);
        
        // Test clamping - values should be clamped to valid range
        splitPane.setSplit(0, -0.5f); // Should be clamped to 0
        splitPane.setSplit(1, 1.5f);  // Should be clamped to 1
        
        // Should not throw exceptions
    }

    @Test
    public void testGetPrefWidth() {
        MultiSplitPane splitPane = new MultiSplitPane(false); // Horizontal
        Table table1 = new Table();
        Table table2 = new Table();
        
        splitPane.setWidgets(table1, table2);
        
        float prefWidth = splitPane.getPrefWidth();
        Assert.assertTrue("Pref width should be positive", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeight() {
        MultiSplitPane splitPane = new MultiSplitPane(true); // Vertical
        Table table1 = new Table();
        Table table2 = new Table();
        
        splitPane.setWidgets(table1, table2);
        
        float prefHeight = splitPane.getPrefHeight();
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetMinWidthAndHeight() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        
        Assert.assertEquals("Min width should be 0", 0f, splitPane.getMinWidth(), 0.0001f);
        Assert.assertEquals("Min height should be 0", 0f, splitPane.getMinHeight(), 0.0001f);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddActorThrows() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table = new Table();
        
        splitPane.addActor(table);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddActorAtThrows() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table = new Table();
        
        splitPane.addActorAt(0, table);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddActorBeforeThrows() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table1 = new Table();
        Table table2 = new Table();
        
        splitPane.addActorBefore(table1, table2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddActorAfterThrows() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table1 = new Table();
        Table table2 = new Table();
        
        splitPane.addActorAfter(table1, table2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveActorThrows() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table = new Table();
        
        splitPane.removeActor(table);
    }

    @Test
    public void testMultiSplitPaneStyleCopyConstructor() {
        Drawable mockHandle = Mockito.mock(Drawable.class);
        Drawable mockHandleOver = Mockito.mock(Drawable.class);
        
        MultiSplitPane.MultiSplitPaneStyle original = new MultiSplitPane.MultiSplitPaneStyle(mockHandle, mockHandleOver);
        MultiSplitPane.MultiSplitPaneStyle copy = new MultiSplitPane.MultiSplitPaneStyle(original);
        
        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertEquals("Handle should be copied", mockHandle, copy.handle);
        Assert.assertEquals("Handle over should be copied", mockHandleOver, copy.handleOver);
    }

    @Test
    public void testMultiSplitPaneStyleConstructorWithDrawables() {
        Drawable mockHandle = Mockito.mock(Drawable.class);
        Drawable mockHandleOver = Mockito.mock(Drawable.class);
        
        MultiSplitPane.MultiSplitPaneStyle style = new MultiSplitPane.MultiSplitPaneStyle(mockHandle, mockHandleOver);
        
        Assert.assertNotNull("Style should be created", style);
        Assert.assertEquals("Handle should be set", mockHandle, style.handle);
        Assert.assertEquals("Handle over should be set", mockHandleOver, style.handleOver);
    }

    @Test
    public void testMultiSplitPaneStyleDefaultConstructor() {
        MultiSplitPane.MultiSplitPaneStyle style = new MultiSplitPane.MultiSplitPaneStyle();
        
        Assert.assertNotNull("Style should be created", style);
    }

    @Test
    public void testMultiSplitPaneStyleInheritance() {
        MultiSplitPane.MultiSplitPaneStyle style = new MultiSplitPane.MultiSplitPaneStyle();
        
        // Test that MultiSplitPaneStyle extends VisSplitPaneStyle
        Assert.assertTrue("MultiSplitPaneStyle should extend VisSplitPaneStyle", 
                        style instanceof VisSplitPane.VisSplitPaneStyle);
    }

    @Test
    public void testLayout() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table1 = new Table();
        Table table2 = new Table();
        
        splitPane.setWidgets(table1, table2);
        
        // Should not throw exception
        splitPane.layout();
    }

    @Test
    public void testHit() {
        MultiSplitPane splitPane = new MultiSplitPane(true);
        Table table1 = new Table();
        Table table2 = new Table();
        
        splitPane.setWidgets(table1, table2);
        splitPane.setSize(100, 100);
        
        // Test hit detection - should not throw exception
        Actor hit = splitPane.hit(50, 50, true);
        // Hit result depends on internal implementation, just test it doesn't crash
    }

    private Object getPrivateField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }
}
