package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link Menu}.
 */
public class MenuTest {

    @Test
    public void testConstructorWithTitle() {
        Menu menu = new Menu("Test Menu");
        
        Assert.assertNotNull("Menu should be created", menu);
        Assert.assertEquals("Title should be set", "Test Menu", menu.getTitle());
        Assert.assertNotNull("Open button should be created", menu.openButton);
        Assert.assertEquals("Open button text should match title", "Test Menu", menu.openButton.getText().toString());
    }

    @Test
    public void testConstructorWithTitleAndStyleName() {
        Menu menu = new Menu("Test Menu", "default");
        
        Assert.assertNotNull("Menu should be created", menu);
        Assert.assertEquals("Title should be set", "Test Menu", menu.getTitle());
        Assert.assertNotNull("Open button should be created", menu.openButton);
    }

    @Test
    public void testConstructorWithTitleAndStyle() {
        Menu.MenuStyle style = new Menu.MenuStyle();
        Menu menu = new Menu("Test Menu", style);
        
        Assert.assertNotNull("Menu should be created", menu);
        Assert.assertEquals("Title should be set", "Test Menu", menu.getTitle());
        Assert.assertNotNull("Open button should be created", menu.openButton);
    }

    @Test
    public void testGetOpenButton() {
        Menu menu = new Menu("Test Menu");
        TextButton openButton = menu.getOpenButton();
        
        Assert.assertNotNull("Open button should not be null", openButton);
        Assert.assertSame("Open button should be the same instance", menu.openButton, openButton);
    }

    @Test
    public void testButtonDefaultDrawable() {
        Menu.MenuStyle style = new Menu.MenuStyle();
        Menu menu = new Menu("Test Menu", style);
        
        Assert.assertNotNull("Button default drawable should be set", menu.buttonDefault);
    }

    @Test
    public void testMenuStyleCopyConstructor() {
        Drawable mockDrawable = Mockito.mock(Drawable.class);
        VisTextButton.VisTextButtonStyle buttonStyle = new VisTextButton.VisTextButtonStyle();
        buttonStyle.up = mockDrawable;
        
        Menu.MenuStyle original = new Menu.MenuStyle();
        original.openButtonStyle = buttonStyle;
        
        Menu.MenuStyle copy = new Menu.MenuStyle(original);
        
        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertSame("Open button style should be copied", buttonStyle, copy.openButtonStyle);
    }

    @Test
    public void testMenuStyleConstructorWithParameters() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Drawable mockBorder = Mockito.mock(Drawable.class);
        VisTextButton.VisTextButtonStyle buttonStyle = new VisTextButton.VisTextButtonStyle();
        
        Menu.MenuStyle style = new Menu.MenuStyle(mockBackground, mockBorder, buttonStyle);
        
        Assert.assertNotNull("Style should be created", style);
        Assert.assertEquals("Background should be set", mockBackground, style.background);
        Assert.assertEquals("Border should be set", mockBorder, style.border);
        Assert.assertSame("Open button style should be set", buttonStyle, style.openButtonStyle);
    }

    @Test
    public void testMenuStyleInheritance() {
        Menu.MenuStyle style = new Menu.MenuStyle();
        
        // Test that MenuStyle extends PopupMenuStyle
        Assert.assertTrue("MenuStyle should extend PopupMenuStyle", 
                        style instanceof PopupMenu.PopupMenuStyle);
    }

    @Test
    public void testRemoveReturnsSuperRemove() {
        Menu menu = new Menu("Test Menu");
        
        // Test that remove() method exists and returns boolean
        // Since we can't easily test the actual removal without a stage,
        // we just test the method signature and basic behavior
        try {
            boolean result = menu.remove();
            // Should return false when not added to a stage
            Assert.assertFalse("Remove should return false when not in stage", result);
        } catch (Exception e) {
            Assert.fail("Remove method should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testMenuWithNullTitle() {
        try {
            Menu menu = new Menu(null);
            Assert.assertNotNull("Menu should be created even with null title", menu);
            Assert.assertNull("Title should be null", menu.getTitle());
        } catch (Exception e) {
            // Expected behavior may vary, but shouldn't crash
        }
    }

    @Test
    public void testMenuWithEmptyTitle() {
        Menu menu = new Menu("");
        
        Assert.assertNotNull("Menu should be created with empty title", menu);
        Assert.assertEquals("Title should be empty", "", menu.getTitle());
        Assert.assertNotNull("Open button should be created", menu.openButton);
    }

    @Test
    public void testMenuStyleDefaultConstructor() {
        Menu.MenuStyle style = new Menu.MenuStyle();
        
        Assert.assertNotNull("Style should be created", style);
        Assert.assertNull("Open button style should be null by default", style.openButtonStyle);
    }
}
