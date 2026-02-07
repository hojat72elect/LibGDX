package com.kotcrab.vis.ui.widget;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
 * Unit tests for {@link PopupMenu}.
 */
public class PopupMenuTest {

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

        // Add PopupMenuStyle required by PopupMenu
        PopupMenu.PopupMenuStyle popupMenuStyle = new PopupMenu.PopupMenuStyle();
        popupMenuStyle.background = Mockito.mock(Drawable.class);
        skin.add("default", popupMenuStyle);

        // Add Label styles required by MenuItem
        com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle shortcutLabelStyle = new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle();
        shortcutLabelStyle.font = testFont;
        shortcutLabelStyle.fontColor = Color.WHITE;
        skin.add("menuitem-shortcut", shortcutLabelStyle);

        // Add minimal required style for MenuItem
        MenuItem.MenuItemStyle menuItemStyle = new MenuItem.MenuItemStyle();
        menuItemStyle.font = testFont;
        menuItemStyle.fontColor = Color.WHITE;
        menuItemStyle.up = Mockito.mock(Drawable.class);
        menuItemStyle.down = Mockito.mock(Drawable.class);
        menuItemStyle.over = Mockito.mock(Drawable.class);
        menuItemStyle.subMenu = Mockito.mock(Drawable.class);
        skin.add("default", menuItemStyle);

        // Add SeparatorStyle required by addSeparator() method
        Separator.SeparatorStyle separatorStyle = new Separator.SeparatorStyle();
        separatorStyle.background = Mockito.mock(Drawable.class);
        separatorStyle.thickness = 1;
        skin.add("menu", separatorStyle);

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
        };

        return new BitmapFont(fontData, com.badlogic.gdx.utils.Array.with(mockRegion), true);
    }

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
        Assert.assertFalse("Should not contain point on menu edge (uses < > not <= >=)", menu.contains(10, 20));
        Assert.assertFalse("Should not contain point outside menu", menu.contains(5, 5));
        Assert.assertFalse("Should not contain point outside menu", menu.contains(150, 100));
    }

    @Test
    public void testShowMenuWithCoordinates() {
        PopupMenu menu = new PopupMenu();
        Stage mockStage = mock(Stage.class);
        when(mockStage.getHeight()).thenReturn(480f); // Set stage height

        menu.showMenu(mockStage, 100, 200);

        verify(mockStage).addActor(menu);
        // After showMenu, position is set by ActorUtils.keepWithinStage, so we just verify it was called
        Assert.assertTrue("Menu should be positioned", menu.getX() >= 0 || menu.getY() >= 0);
    }

    @Test
    public void testShowMenuWithActor() {
        PopupMenu menu = new PopupMenu();
        Stage mockStage = mock(Stage.class);
        when(mockStage.getHeight()).thenReturn(480f); // Set stage height
        Actor actor = new Actor();
        actor.setPosition(50, 100);
        actor.setSize(80, 30);

        menu.showMenu(mockStage, actor);

        verify(mockStage).addActor(menu);
        // After showMenu, position is set by ActorUtils.keepWithinStage, so we just verify it was called
        Assert.assertTrue("Menu should be positioned", menu.getX() >= 0 || menu.getY() >= 0);
    }

    @Test
    public void testRemove() {
        PopupMenu menu = new PopupMenu();
        Stage mockStage = mock(Stage.class);

        // Directly set the stage field to simulate the menu being added to a stage
        setPrivateField(menu, "stage", mockStage);

        // Verify that getStage() now returns our mock
        Assert.assertNotNull("Menu should have a stage", menu.getStage());
        Assert.assertSame("Menu's stage should be our mock", mockStage, menu.getStage());

        boolean removed = menu.remove();

        // Now getStage() should return mockStage, so removeListener should be called
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

        // removeEveryMenu calls getActors() once to iterate through actors
        verify(mockStage, times(1)).getActors();
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
            Class<?> clazz = obj.getClass();
            while (clazz != null) {
                try {
                    java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field.get(obj);
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private void setPrivateField(Object obj, String fieldName, Object value) {
        try {
            Class<?> clazz = obj.getClass();
            while (clazz != null) {
                try {
                    java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(obj, value);
                    return;
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
        } catch (Exception e) {
            // Ignore
        }
    }
}
