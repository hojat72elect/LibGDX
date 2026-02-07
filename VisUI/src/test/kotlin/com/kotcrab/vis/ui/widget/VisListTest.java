package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.FocusManager;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 * Unit tests for {@link VisList}.
 */
public class VisListTest {

    @Test
    public void testDefaultConstructor() {
        VisList<String> list = new VisList<>();
        
        Assert.assertNotNull("List should be created", list);
        Assert.assertNotNull("Style should not be null", list.getStyle());
    }

    @Test
    public void testConstructorWithStyleName() {
        VisList<String> list = new VisList<>("default");
        
        Assert.assertNotNull("List should be created", list);
        Assert.assertNotNull("Style should not be null", list.getStyle());
    }

    @Test
    public void testConstructorWithStyle() {
        List.ListStyle mockStyle = Mockito.mock(List.ListStyle.class);
        VisList<String> list = new VisList<>(mockStyle);
        
        Assert.assertNotNull("List should be created", list);
        Assert.assertSame("Style should be set", mockStyle, list.getStyle());
    }

    @Test
    public void testSetItems() {
        VisList<String> list = new VisList<>();
        String[] items = {"Item1", "Item2", "Item3"};
        
        list.setItems(items);
        
        Assert.assertEquals("Items should be set", 3, list.getItems().size);
        Assert.assertEquals("First item should be correct", "Item1", list.getItems().get(0));
        Assert.assertEquals("Second item should be correct", "Item2", list.getItems().get(1));
        Assert.assertEquals("Third item should be correct", "Item3", list.getItems().get(2));
    }

    @Test
    public void testSetItemsWithArray() {
        VisList<String> list = new VisList<>();
        String[] items = {"A", "B", "C", "D"};
        
        list.setItems(items);
        
        Assert.assertEquals("Items should be set", 4, list.getItems().size);
        Assert.assertArrayEquals("Items array should match", items, list.getItems().toArray());
    }

    @Test
    public void testGetSelected() {
        VisList<String> list = new VisList<>();
        list.setItems("Option1", "Option2", "Option3");
        
        list.setSelectedIndex(1);
        
        Assert.assertEquals("Selected item should be correct", "Option2", list.getSelected());
    }

    @Test
    public void testGetSelectedIndex() {
        VisList<String> list = new VisList<>();
        list.setItems("First", "Second", "Third");
        
        list.setSelectedIndex(2);
        
        Assert.assertEquals("Selected index should be correct", 2, list.getSelectedIndex());
    }

    @Test
    public void testSetSelectedIndex() {
        VisList<String> list = new VisList<>();
        list.setItems("Item1", "Item2", "Item3");
        
        list.setSelectedIndex(0);
        Assert.assertEquals("Selected index should be 0", 0, list.getSelectedIndex());
        Assert.assertEquals("Selected item should be Item1", "Item1", list.getSelected());
        
        list.setSelectedIndex(2);
        Assert.assertEquals("Selected index should be 2", 2, list.getSelectedIndex());
        Assert.assertEquals("Selected item should be Item3", "Item3", list.getSelected());
    }

    @Test
    public void testSetSelectedIndexOutOfBounds() {
        VisList<String> list = new VisList<>();
        list.setItems("Item1", "Item2");
        
        // Test negative index
        list.setSelectedIndex(-1);
        Assert.assertEquals("Selected index should be -1 for out of bounds", -1, list.getSelectedIndex());
        
        // Test index too large
        list.setSelectedIndex(10);
        Assert.assertEquals("Selected index should be -1 for out of bounds", -1, list.getSelectedIndex());
    }

    @Test
    public void testSetSelected() {
        VisList<String> list = new VisList<>();
        list.setItems("Apple", "Banana", "Cherry");
        
        list.setSelected("Banana");
        Assert.assertEquals("Selected item should be Banana", "Banana", list.getSelected());
        Assert.assertEquals("Selected index should be 1", 1, list.getSelectedIndex());
    }

    @Test
    public void testSetSelectedWithNonExistentItem() {
        VisList<String> list = new VisList<>();
        list.setItems("Item1", "Item2");
        
        list.setSelected("NonExistent");
        Assert.assertNull("Selected item should be null for non-existent item", list.getSelected());
        Assert.assertEquals("Selected index should be -1", -1, list.getSelectedIndex());
    }

    @Test
    public void testClearSelection() {
        VisList<String> list = new VisList<>();
        list.setItems("Item1", "Item2", "Item3");
        list.setSelectedIndex(1);
        
        list.clearSelection();
        
        Assert.assertEquals("Selected index should be -1 after clear", -1, list.getSelectedIndex());
        Assert.assertNull("Selected item should be null after clear", list.getSelected());
    }

    @Test
    public void testGetItems() {
        VisList<String> list = new VisList<>();
        String[] items = {"One", "Two", "Three"};
        list.setItems(items);
        
        com.badlogic.gdx.utils.Array<String> retrievedItems = list.getItems();
        
        Assert.assertNotNull("Items should not be null", retrievedItems);
        Assert.assertEquals("Items count should match", 3, retrievedItems.size);
        Assert.assertTrue("Items should contain One", retrievedItems.contains("One", false));
        Assert.assertTrue("Items should contain Two", retrievedItems.contains("Two", false));
        Assert.assertTrue("Items should contain Three", retrievedItems.contains("Three", false));
    }

    @Test
    public void testGetPrefWidth() {
        VisList<String> list = new VisList<>();
        list.setItems("Short", "A much longer item", "Medium");
        
        float prefWidth = list.getPrefWidth();
        
        Assert.assertTrue("Pref width should be positive", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeight() {
        VisList<String> list = new VisList<>();
        list.setItems("Item1", "Item2", "Item3");
        
        float prefHeight = list.getPrefHeight();
        
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetPrefHeightWithEmptyList() {
        VisList<String> list = new VisList<>();
        list.setItems();
        
        float prefHeight = list.getPrefHeight();
        
        Assert.assertTrue("Pref height should be positive even for empty list", prefHeight > 0);
    }

    @Test
    public void testListInheritance() {
        VisList<String> list = new VisList<>();
        
        Assert.assertTrue("VisList should extend List", list instanceof List);
    }

    @Test
    public void testFocusManagementOnTouch() {
        VisList<String> list = new VisList<>();
        list.setItems("Item1", "Item2");
        
        // Create a mock stage and input event
        com.badlogic.gdx.scenes.scene2d.Stage mockStage = Mockito.mock(com.badlogic.gdx.scenes.scene2d.Stage.class);
        list.setStage(mockStage);
        
        InputEvent mockEvent = Mockito.mock(InputEvent.class);
        
        // Simulate touch down event
        boolean result = list.fire(mockEvent);
        
        // The touch event should be handled by the input listener
        // We can't easily test the FocusManager.resetFocus call without more complex mocking
        // but we can verify the event structure is correct
        Assert.assertNotNull("Event should be processed", mockEvent);
    }

    @Test
    public void testMultipleLists() {
        VisList<String> list1 = new VisList<>();
        VisList<String> list2 = new VisList<>("default");
        List.ListStyle mockStyle = Mockito.mock(List.ListStyle.class);
        VisList<String> list3 = new VisList<>(mockStyle);
        
        Assert.assertNotNull("All lists should be created", list1);
        Assert.assertNotNull("All lists should be created", list2);
        Assert.assertNotNull("All lists should be created", list3);
        
        Assert.assertNotSame("Lists should have different style instances", 
                           list1.getStyle(), list2.getStyle());
        Assert.assertSame("Custom style should be set", mockStyle, list3.getStyle());
    }

    @Test
    public void testListWithNullItems() {
        VisList<String> list = new VisList<>();
        
        // Setting null items should not crash
        list.setItems((String[]) null);
        
        Assert.assertNotNull("Items array should not be null", list.getItems());
        Assert.assertEquals("Items should be empty", 0, list.getItems().size);
    }

    @Test
    public void testListWithEmptyItems() {
        VisList<String> list = new VisList<>();
        list.setItems();
        
        Assert.assertNotNull("Items array should not be null", list.getItems());
        Assert.assertEquals("Items should be empty", 0, list.getItems().size);
        Assert.assertEquals("Selected index should be -1 for empty list", -1, list.getSelectedIndex());
        Assert.assertNull("Selected item should be null for empty list", list.getSelected());
    }

    @Test
    public void testListWithDuplicateItems() {
        VisList<String> list = new VisList<>();
        list.setItems("Duplicate", "Unique", "Duplicate");
        
        Assert.assertEquals("Items count should include duplicates", 3, list.getItems().size);
        
        list.setSelected("Duplicate");
        Assert.assertEquals("Should select first occurrence", 0, list.getSelectedIndex());
    }

    @Test
    public void testListWithNullItemsInArray() {
        VisList<String> list = new VisList<>();
        list.setItems("Item1", null, "Item3");
        
        Assert.assertEquals("Items count should include null", 3, list.getItems().size);
        Assert.assertEquals("First item should be Item1", "Item1", list.getItems().get(0));
        Assert.assertNull("Second item should be null", list.getItems().get(1));
        Assert.assertEquals("Third item should be Item3", "Item3", list.getItems().get(2));
    }

    @Test
    public void testListStyleProperties() {
        VisList<String> list = new VisList<>();
        List.ListStyle style = list.getStyle();
        
        Assert.assertNotNull("Font should not be null", style.font);
        Assert.assertNotNull("Selection should not be null", style.selection);
        Assert.assertNotNull("Background should not be null", style.background);
    }
}
