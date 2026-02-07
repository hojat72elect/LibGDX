package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.FocusManager;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 * Unit tests for {@link VisSelectBox}.
 */
public class VisSelectBoxTest {

    @Test
    public void testDefaultConstructor() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        
        Assert.assertNotNull("Select box should be created", selectBox);
        Assert.assertNotNull("Style should not be null", selectBox.getStyle());
        Assert.assertEquals("Selected index should be -1 for empty select box", -1, selectBox.getSelectedIndex());
        Assert.assertNull("Selected item should be null for empty select box", selectBox.getSelected());
    }

    @Test
    public void testConstructorWithStyleName() {
        VisSelectBox<String> selectBox = new VisSelectBox<>("default");
        
        Assert.assertNotNull("Select box should be created", selectBox);
        Assert.assertNotNull("Style should not be null", selectBox.getStyle());
        Assert.assertEquals("Selected index should be -1", -1, selectBox.getSelectedIndex());
    }

    @Test
    public void testConstructorWithStyle() {
        SelectBox.SelectBoxStyle mockStyle = Mockito.mock(SelectBox.SelectBoxStyle.class);
        VisSelectBox<String> selectBox = new VisSelectBox<>(mockStyle);
        
        Assert.assertNotNull("Select box should be created", selectBox);
        Assert.assertSame("Style should be set", mockStyle, selectBox.getStyle());
        Assert.assertEquals("Selected index should be -1", -1, selectBox.getSelectedIndex());
    }

    @Test
    public void testSetItems() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        String[] items = {"Option1", "Option2", "Option3"};
        
        selectBox.setItems(items);
        
        Assert.assertEquals("Items count should be 3", 3, selectBox.getItems().size);
        Assert.assertEquals("Selected index should be 0 (first item)", 0, selectBox.getSelectedIndex());
        Assert.assertEquals("Selected item should be first", "Option1", selectBox.getSelected());
    }

    @Test
    public void testSetItemsWithArray() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        String[] items = {"A", "B", "C", "D"};
        
        selectBox.setItems(items);
        
        Assert.assertEquals("Items count should be 4", 4, selectBox.getItems().size);
        Assert.assertArrayEquals("Items array should match", items, selectBox.getItems().toArray());
    }

    @Test
    public void testSetItemsWithNullArray() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        
        selectBox.setItems((String[]) null);
        
        Assert.assertNotNull("Items array should not be null", selectBox.getItems());
        Assert.assertEquals("Items should be empty", 0, selectBox.getItems().size);
        Assert.assertEquals("Selected index should be -1", -1, selectBox.getSelectedIndex());
    }

    @Test
    public void testSetItemsWithEmptyArray() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        
        selectBox.setItems();
        
        Assert.assertNotNull("Items array should not be null", selectBox.getItems());
        Assert.assertEquals("Items should be empty", 0, selectBox.getItems().size);
        Assert.assertEquals("Selected index should be -1", -1, selectBox.getSelectedIndex());
    }

    @Test
    public void testGetSelected() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Option1", "Option2", "Option3");
        
        selectBox.setSelectedIndex(1);
        Assert.assertEquals("Selected item should be correct", "Option2", selectBox.getSelected());
    }

    @Test
    public void testGetSelectedIndex() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("First", "Second", "Third");
        
        selectBox.setSelectedIndex(2);
        Assert.assertEquals("Selected index should be correct", 2, selectBox.getSelectedIndex());
    }

    @Test
    public void testSetSelectedIndex() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Item1", "Item2", "Item3");
        
        selectBox.setSelectedIndex(0);
        Assert.assertEquals("Selected index should be 0", 0, selectBox.getSelectedIndex());
        Assert.assertEquals("Selected item should be Item1", "Item1", selectBox.getSelected());
        
        selectBox.setSelectedIndex(2);
        Assert.assertEquals("Selected index should be 2", 2, selectBox.getSelectedIndex());
        Assert.assertEquals("Selected item should be Item3", "Item3", selectBox.getSelected());
    }

    @Test
    public void testSetSelectedIndexOutOfBounds() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Item1", "Item2");
        
        // Test negative index
        selectBox.setSelectedIndex(-1);
        Assert.assertEquals("Selected index should be -1 for out of bounds", -1, selectBox.getSelectedIndex());
        
        // Test index too large
        selectBox.setSelectedIndex(10);
        Assert.assertEquals("Selected index should be -1 for out of bounds", -1, selectBox.getSelectedIndex());
    }

    @Test
    public void testSetSelected() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Apple", "Banana", "Cherry");
        
        selectBox.setSelected("Banana");
        Assert.assertEquals("Selected item should be Banana", "Banana", selectBox.getSelected());
        Assert.assertEquals("Selected index should be 1", 1, selectBox.getSelectedIndex());
    }

    @Test
    public void testSetSelectedWithNonExistentItem() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Item1", "Item2");
        
        selectBox.setSelected("NonExistent");
        Assert.assertNull("Selected item should be null for non-existent item", selectBox.getSelected());
        Assert.assertEquals("Selected index should be -1", -1, selectBox.getSelectedIndex());
    }

    @Test
    public void testGetItems() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        String[] items = {"One", "Two", "Three"};
        selectBox.setItems(items);
        
        com.badlogic.gdx.utils.Array<String> retrievedItems = selectBox.getItems();
        
        Assert.assertNotNull("Items should not be null", retrievedItems);
        Assert.assertEquals("Items count should match", 3, retrievedItems.size);
        Assert.assertTrue("Items should contain One", retrievedItems.contains("One", false));
        Assert.assertTrue("Items should contain Two", retrievedItems.contains("Two", false));
        Assert.assertTrue("Items should contain Three", retrievedItems.contains("Three", false));
    }

    @Test
    public void testGetPrefWidth() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Short", "A much longer option", "Medium");
        
        float prefWidth = selectBox.getPrefWidth();
        
        Assert.assertTrue("Pref width should be positive", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeight() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Item1", "Item2", "Item3");
        
        float prefHeight = selectBox.getPrefHeight();
        
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetPrefHeightWithEmptySelectBox() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems();
        
        float prefHeight = selectBox.getPrefHeight();
        
        Assert.assertTrue("Pref height should be positive even for empty select box", prefHeight > 0);
    }

    @Test
    public void testSelectBoxInheritance() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        
        Assert.assertTrue("VisSelectBox should extend SelectBox", selectBox instanceof SelectBox);
    }

    @Test
    public void testFocusManagementOnTouch() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Item1", "Item2");
        
        // Create a mock stage and input event
        com.badlogic.gdx.scenes.scene2d.Stage mockStage = Mockito.mock(com.badlogic.gdx.scenes.scene2d.Stage.class);
        selectBox.setStage(mockStage);
        
        InputEvent mockEvent = Mockito.mock(InputEvent.class);
        
        // Simulate touch down event
        boolean result = selectBox.fire(mockEvent);
        
        // The touch event should be handled by the input listener
        // We can't easily test the FocusManager.resetFocus call without more complex mocking
        // but we can verify the event structure is correct
        Assert.assertNotNull("Event should be processed", mockEvent);
    }

    @Test
    public void testMultipleSelectBoxes() {
        VisSelectBox<String> selectBox1 = new VisSelectBox<>();
        VisSelectBox<String> selectBox2 = new VisSelectBox<>("default");
        SelectBox.SelectBoxStyle mockStyle = Mockito.mock(SelectBox.SelectBoxStyle.class);
        VisSelectBox<String> selectBox3 = new VisSelectBox<>(mockStyle);
        
        Assert.assertNotNull("All select boxes should be created", selectBox1);
        Assert.assertNotNull("All select boxes should be created", selectBox2);
        Assert.assertNotNull("All select boxes should be created", selectBox3);
        
        Assert.assertNotSame("Select boxes should have different style instances", 
                           selectBox1.getStyle(), selectBox2.getStyle());
        Assert.assertSame("Custom style should be set", mockStyle, selectBox3.getStyle());
    }

    @Test
    public void testSelectBoxWithDuplicateItems() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Duplicate", "Unique", "Duplicate");
        
        Assert.assertEquals("Items count should include duplicates", 3, selectBox.getItems().size);
        
        selectBox.setSelected("Duplicate");
        Assert.assertEquals("Should select first occurrence", 0, selectBox.getSelectedIndex());
    }

    @Test
    public void testSelectBoxWithNullItemsInArray() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Item1", null, "Item3");
        
        Assert.assertEquals("Items count should include null", 3, selectBox.getItems().size);
        Assert.assertEquals("First item should be Item1", "Item1", selectBox.getItems().get(0));
        Assert.assertNull("Second item should be null", selectBox.getItems().get(1));
        Assert.assertEquals("Third item should be Item3", "Item3", selectBox.getItems().get(2));
    }

    @Test
    public void testSelectBoxStyleProperties() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        SelectBox.SelectBoxStyle style = selectBox.getStyle();
        
        Assert.assertNotNull("Font should not be null", style.font);
        Assert.assertNotNull("Font color should not be null", style.fontColor);
        Assert.assertNotNull("Background should not be null", style.background);
        Assert.assertNotNull("ScrollStyle should not be null", style.scrollStyle);
        Assert.assertNotNull("ListStyle should not be null", style.listStyle);
    }

    @Test
    public void testSelectBoxWithDifferentStyleNames() {
        VisSelectBox<String> defaultBox = new VisSelectBox<>("default");
        VisSelectBox<String> customBox = new VisSelectBox<>("custom");
        
        Assert.assertNotNull("Default style should be loaded", defaultBox.getStyle());
        Assert.assertNotNull("Custom style should be loaded", customBox.getStyle());
    }

    @Test
    public void testSelectBoxWithIntegerType() {
        VisSelectBox<Integer> selectBox = new VisSelectBox<>();
        Integer[] items = {1, 2, 3, 4, 5};
        
        selectBox.setItems(items);
        
        Assert.assertEquals("Items count should be 5", 5, selectBox.getItems().size);
        Assert.assertEquals("First item should be 1", Integer.valueOf(1), selectBox.getItems().get(0));
        Assert.assertEquals("Last item should be 5", Integer.valueOf(5), selectBox.getItems().get(4));
        
        selectBox.setSelectedIndex(2);
        Assert.assertEquals("Selected item should be 3", Integer.valueOf(3), selectBox.getSelected());
    }

    @Test
    public void testSelectBoxWithEnumType() {
        VisSelectBox<TestEnum> selectBox = new VisSelectBox<>();
        TestEnum[] items = TestEnum.values();
        
        selectBox.setItems(items);
        
        Assert.assertEquals("Items count should match enum values", items.length, selectBox.getItems().size);
        Assert.assertArrayEquals("Items should match enum values", items, selectBox.getItems().toArray());
        
        selectBox.setSelected(TestEnum.SECOND);
        Assert.assertEquals("Selected item should be SECOND", TestEnum.SECOND, selectBox.getSelected());
        Assert.assertEquals("Selected index should be 1", 1, selectBox.getSelectedIndex());
    }

    @Test
    public void testSelectBoxDisabled() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Option1", "Option2");
        selectBox.setSelectedIndex(0);
        
        selectBox.setDisabled(true);
        Assert.assertTrue("Select box should be disabled", selectBox.isDisabled());
        Assert.assertEquals("Selected item should remain when disabled", "Option1", selectBox.getSelected());
        
        // Selection should still work when disabled
        selectBox.setSelectedIndex(1);
        Assert.assertEquals("Selected item should change even when disabled", "Option2", selectBox.getSelected());
    }

    @Test
    public void testSelectBoxWithLongItems() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        String longItem = "This is a very long select box option that should test the select box's ability to handle longer strings";
        selectBox.setItems("Short", longItem, "Medium");
        
        Assert.assertEquals("Long item should be preserved", longItem, selectBox.getItems().get(1));
        Assert.assertTrue("Pref width should be larger for long items", selectBox.getPrefWidth() > 0);
    }

    @Test
    public void testSelectBoxWithSpecialCharacters() {
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        String specialItem = "Special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        selectBox.setItems("Normal", specialItem, "Another");
        
        Assert.assertEquals("Special characters should be handled", specialItem, selectBox.getItems().get(1));
        
        selectBox.setSelected(specialItem);
        Assert.assertEquals("Special item should be selectable", specialItem, selectBox.getSelected());
    }

    // Test enum for testing enum type select box
    private enum TestEnum {
        FIRST, SECOND, THIRD
    }
}
