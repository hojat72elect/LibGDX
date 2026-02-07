package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PopupMenu}.
 */
public class PopupMenuTest {

    @Test
    public void testDefaultConstructor() {
        PopupMenu menu = new PopupMenu();
        
        Assert.assertNotNull("Menu should be created", menu);
        Assert.assertNotNull("Style should not be null", getPrivateField(menu, "style"));
        Assert.assertNull("Active sub menu should be null", menu.getActiveSubMenu());
        Assert.assertNull("Active item should be null", menu.getActiveItem());
    }

    @Test
    public void testConstructorWithStyleName() {
        PopupMenu menu = new PopupMenu("default");
        
        Assert.assertNotNull("Menu should be created", menu);
        Assert.assertNotNull("Style should not be null", getPrivateField(menu, "style"));
    }

    @Test
    public void testConstructorWithStyle() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        PopupMenu.PopupMenuStyle style = new PopupMenu.PopupMenuStyle(mockBackground, null);
        
        PopupMenu menu = new PopupMenu(style);
        
        Assert.assertNotNull("Menu should be created", menu);
        Assert.assertSame("Style should be set", style, getPrivateField(menu, "style"));
    }

    @Test
    public void testConstructorWithSizesAndStyle() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        PopupMenu.PopupMenuStyle style = new PopupMenu.PopupMenuStyle(mockBackground, null);
        
        PopupMenu menu = new PopupMenu(com.kotcrab.vis.ui.Sizes.DEFAULT, style);
        
        Assert.assertNotNull("Menu should be created", menu);
        Assert.assertSame("Style should be set", style, getPrivateField(menu, "style"));
    }

    @Test
    public void testAddItem() {
        PopupMenu menu = new PopupMenu();
        MenuItem item = new MenuItem("Test Item");
        
        menu.addItem(item);
        
        Assert.assertEquals("Menu should have one item", 1, menu.getChildren().size);
        Assert.assertTrue("Menu should contain the item", menu.getChildren().contains(item, true));
    }

    @Test
    public void testAddMultipleItems() {
        PopupMenu menu = new PopupMenu();
        MenuItem item1 = new MenuItem("Item 1");
        MenuItem item2 = new MenuItem("Item 2");
        MenuItem item3 = new MenuItem("Item 3");
        
        menu.addItem(item1);
        menu.addItem(item2);
        menu.addItem(item3);
        
        Assert.assertEquals("Menu should have three items", 3, menu.getChildren().size);
        Assert.assertTrue("Menu should contain item1", menu.getChildren().contains(item1, true));
        Assert.assertTrue("Menu should contain item2", menu.getChildren().contains(item2, true));
        Assert.assertTrue("Menu should contain item3", menu.getChildren().contains(item3, true));
    }

    @Test
    public void testAddSeparator() {
        PopupMenu menu = new PopupMenu();
        
        menu.addSeparator();
        
        Assert.assertEquals("Menu should have one item (separator)", 1, menu.getChildren().size);
        Actor child = menu.getChildren().first();
        Assert.assertTrue("Child should be a separator", child instanceof Separator);
    }

    @Test
    public void testAddItemAndSeparator() {
        PopupMenu menu = new PopupMenu();
        MenuItem item = new MenuItem("Test Item");
        
        menu.addItem(item);
        menu.addSeparator();
        
        Assert.assertEquals("Menu should have two items", 2, menu.getChildren().size);
        Assert.assertTrue("Menu should contain the item", menu.getChildren().contains(item, true));
        Assert.assertTrue("Second item should be a separator", 
                        menu.getChildren().get(1) instanceof Separator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMenuItemDirectlyThrows() {
        PopupMenu menu = new PopupMenu();
        MenuItem item = new MenuItem("Test Item");
        
        menu.add(item); // Should throw exception
    }

    @Test
    public void testAddNonMenuItemActor() {
        PopupMenu menu = new PopupMenu();
        Actor actor = new Actor();
        
        menu.add(actor); // Should not throw exception for non-MenuItem
        
        Assert.assertEquals("Menu should have one item", 1, menu.getChildren().size);
        Assert.assertSame("Menu should contain the actor", actor, menu.getChildren().first());
    }

    @Test
    public void testGetActiveSubMenu() {
        PopupMenu menu = new PopupMenu();
        
        Assert.assertNull("Active sub menu should be null initially", menu.getActiveSubMenu());
    }

    @Test
    public void testGetActiveItem() {
        PopupMenu menu = new PopupMenu();
        
        Assert.assertNull("Active item should be null initially", menu.getActiveItem());
    }

    @Test
    public void testGetAndSetListener() {
        PopupMenu menu = new PopupMenu();
        PopupMenu.PopupMenuListener listener = new PopupMenu.PopupMenuListener() {
            @Override
            public void activeItemChanged(MenuItem newActiveItem, boolean changedByKeyboard) {
                // Implementation
            }
        };
        
        menu.setListener(listener);
        Assert.assertSame("Listener should be set", listener, menu.getListener());
    }

    @Test
    public void testContains() {
        PopupMenu menu = new PopupMenu();
        menu.setPosition(10, 20);
        menu.setSize(100, 50);
        
        Assert.assertTrue("Should contain point inside menu", menu.contains(50, 40));
        Assert.assertTrue("Should contain point on menu edge", menu.contains(10, 20));
        Assert.assertFalse("Should not contain point outside menu", menu.contains(5, 5));
        Assert.assertFalse("Should not contain point outside menu", menu.contains(150, 100));
    }

    @Test
    public void testShowMenuWithCoordinates() {
        PopupMenu menu = new PopupMenu();
        Stage mockStage = mock(Stage.class);
        
        menu.showMenu(mockStage, 100, 200);
        
        verify(mockStage).addActor(menu);
        Assert.assertEquals("Y position should be adjusted for menu height", 200 - menu.getHeight(), menu.getY(), 0.0001f);
        Assert.assertEquals("X position should be set", 100, menu.getX(), 0.0001f);
    }

    @Test
    public void testShowMenuWithActor() {
        PopupMenu menu = new PopupMenu();
        Stage mockStage = mock(Stage.class);
        Actor actor = new Actor();
        actor.setPosition(50, 100);
        actor.setSize(80, 30);
        
        menu.showMenu(mockStage, actor);
        
        verify(mockStage).addActor(menu);
        // Position should be calculated based on actor position and size
        Assert.assertTrue("X position should be based on actor", menu.getX() >= 50);
    }

    @Test
    public void testRemove() {
        PopupMenu menu = new PopupMenu();
        Stage mockStage = mock(Stage.class);
        setPrivateField(menu, "stage", mockStage);
        
        boolean removed = menu.remove();
        
        Assert.assertTrue("Remove should return true when stage is set", removed);
        verify(mockStage).removeListener(any(com.badlogic.gdx.scenes.scene2d.InputListener.class));
    }

    @Test
    public void testRemoveWithoutStage() {
        PopupMenu menu = new PopupMenu();
        
        boolean removed = menu.remove();
        
        Assert.assertFalse("Remove should return false when stage is null", removed);
    }

    @Test
    public void testPopupMenuStyleCopyConstructor() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Drawable mockBorder = Mockito.mock(Drawable.class);
        
        PopupMenu.PopupMenuStyle original = new PopupMenu.PopupMenuStyle(mockBackground, mockBorder);
        PopupMenu.PopupMenuStyle copy = new PopupMenu.PopupMenuStyle(original);
        
        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertEquals("Background should be copied", mockBackground, copy.background);
        Assert.assertEquals("Border should be copied", mockBorder, copy.border);
    }

    @Test
    public void testPopupMenuStyleConstructorWithDrawables() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Drawable mockBorder = Mockito.mock(Drawable.class);
        
        PopupMenu.PopupMenuStyle style = new PopupMenu.PopupMenuStyle(mockBackground, mockBorder);
        
        Assert.assertNotNull("Style should be created", style);
        Assert.assertEquals("Background should be set", mockBackground, style.background);
        Assert.assertEquals("Border should be set", mockBorder, style.border);
    }

    @Test
    public void testPopupMenuStyleDefaultConstructor() {
        PopupMenu.PopupMenuStyle style = new PopupMenu.PopupMenuStyle();
        
        Assert.assertNotNull("Style should be created", style);
        Assert.assertNull("Background should be null by default", style.background);
        Assert.assertNull("Border should be null by default", style.border);
    }

    @Test
    public void testGetDefaultInputListener() {
        PopupMenu menu = new PopupMenu();
        
        com.badlogic.gdx.scenes.scene2d.InputListener listener = menu.getDefaultInputListener();
        
        Assert.assertNotNull("Default input listener should be created", listener);
        
        // Test that the same listener is returned on subsequent calls
        com.badlogic.gdx.scenes.scene2d.InputListener listener2 = menu.getDefaultInputListener();
        Assert.assertSame("Same listener instance should be returned", listener, listener2);
    }

    @Test
    public void testGetDefaultInputListenerWithButton() {
        PopupMenu menu = new PopupMenu();
        
        com.badlogic.gdx.scenes.scene2d.InputListener listener = menu.getDefaultInputListener(Input.Buttons.LEFT);
        
        Assert.assertNotNull("Input listener should be created", listener);
    }

    @Test
    public void testRemoveEveryMenu() {
        PopupMenu menu1 = new PopupMenu();
        PopupMenu menu2 = new PopupMenu();
        Stage mockStage = mock(Stage.class);
        
        when(mockStage.getActors()).thenReturn(new com.badlogic.gdx.utils.Array<>(new Actor[]{menu1, menu2}));
        
        PopupMenu.removeEveryMenu(mockStage);
        
        verify(mockStage, times(2)).getActors();
    }

    @Test
    public void testPopupMenuListenerInterface() {
        // Test that the interface can be implemented
        PopupMenu.PopupMenuListener listener = new PopupMenu.PopupMenuListener() {
            @Override
            public void activeItemChanged(MenuItem newActiveItem, boolean changedByKeyboard) {
                // Implementation
            }
        };
        
        Assert.assertNotNull("Listener should be created", listener);
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

    private void setPrivateField(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            // Ignore
        }
    }
}
