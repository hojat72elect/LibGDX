package com.kotcrab.vis.ui.widget;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton.VisTextButtonStyle;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link MenuBar}.
 */
public class MenuBarTest {

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

        // Add Sizes object - required by Menu and PopupMenu
        com.kotcrab.vis.ui.Sizes sizes = new com.kotcrab.vis.ui.Sizes();
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
        skin.add("default", sizes, com.kotcrab.vis.ui.Sizes.class);

        // Add minimal required style for MenuBar
        MenuBar.MenuBarStyle menuBarStyle = new MenuBar.MenuBarStyle();
        menuBarStyle.background = Mockito.mock(Drawable.class);
        skin.add("default", menuBarStyle);

        // Add minimal required style for Menu
        VisTextButtonStyle buttonStyle = new VisTextButtonStyle();
        buttonStyle.font = testFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = Mockito.mock(Drawable.class);
        buttonStyle.down = Mockito.mock(Drawable.class);
        buttonStyle.over = Mockito.mock(Drawable.class);

        Menu.MenuStyle menuStyle = new Menu.MenuStyle();
        menuStyle.openButtonStyle = buttonStyle;
        skin.add("default", menuStyle);

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
        MenuBar menuBar = new MenuBar();

        Assert.assertNotNull("Menu bar should be created", menuBar);
        Assert.assertNotNull("Main table should not be null", menuBar.getTable());
        Assert.assertNotNull("Menu items table should not be null", getPrivateField(menuBar, "menuItems"));
        Assert.assertTrue("Menus array should be empty", ((Array<Menu>) getPrivateField(menuBar, "menus")).size == 0);
    }

    @Test
    public void testConstructorWithStyleName() {
        MenuBar menuBar = new MenuBar("default");

        Assert.assertNotNull("Menu bar should be created", menuBar);
        Assert.assertNotNull("Main table should not be null", menuBar.getTable());
    }

    @Test
    public void testConstructorWithStyle() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        MenuBar.MenuBarStyle style = new MenuBar.MenuBarStyle(mockBackground);

        MenuBar menuBar = new MenuBar(style);

        Assert.assertNotNull("Menu bar should be created", menuBar);
        Assert.assertNotNull("Main table should not be null", menuBar.getTable());
    }

    @Test
    public void testAddMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Test Menu");

        menuBar.addMenu(menu);

        Array<Menu> menus = (Array<Menu>) getPrivateField(menuBar, "menus");
        Assert.assertEquals("Menu should be added", 1, menus.size);
        Assert.assertSame("Added menu should be in array", menu, menus.first());
        Assert.assertNotNull("Menu should have menu bar set", getPrivateField(menu, "menuBar"));
    }

    @Test
    public void testAddMultipleMenus() {
        MenuBar menuBar = new MenuBar();
        Menu menu1 = new Menu("Menu 1");
        Menu menu2 = new Menu("Menu 2");
        Menu menu3 = new Menu("Menu 3");

        menuBar.addMenu(menu1);
        menuBar.addMenu(menu2);
        menuBar.addMenu(menu3);

        Array<Menu> menus = (Array<Menu>) getPrivateField(menuBar, "menus");
        Assert.assertEquals("All menus should be added", 3, menus.size);
        Assert.assertTrue("Menu1 should be in array", menus.contains(menu1, true));
        Assert.assertTrue("Menu2 should be in array", menus.contains(menu2, true));
        Assert.assertTrue("Menu3 should be in array", menus.contains(menu3, true));
    }

    @Test
    public void testRemoveMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menu1 = new Menu("Menu 1");
        Menu menu2 = new Menu("Menu 2");

        menuBar.addMenu(menu1);
        menuBar.addMenu(menu2);

        boolean removed = menuBar.removeMenu(menu1);

        Assert.assertTrue("Menu should be removed", removed);
        Array<Menu> menus = (Array<Menu>) getPrivateField(menuBar, "menus");
        Assert.assertEquals("One menu should remain", 1, menus.size);
        Assert.assertFalse("Removed menu should not be in array", menus.contains(menu1, true));
        Assert.assertTrue("Remaining menu should still be in array", menus.contains(menu2, true));
        Assert.assertNull("Removed menu should have menu bar set to null", getPrivateField(menu1, "menuBar"));
    }

    @Test
    public void testRemoveNonExistentMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menu1 = new Menu("Menu 1");
        Menu menu2 = new Menu("Menu 2");

        menuBar.addMenu(menu1);

        boolean removed = menuBar.removeMenu(menu2);

        Assert.assertFalse("Non-existent menu should not be removed", removed);
        Array<Menu> menus = (Array<Menu>) getPrivateField(menuBar, "menus");
        Assert.assertEquals("Menu count should not change", 1, menus.size);
    }

    @Test
    public void testInsertMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menu1 = new Menu("Menu 1");
        Menu menu2 = new Menu("Menu 2");
        Menu menu3 = new Menu("Menu 3");

        menuBar.addMenu(menu1);
        menuBar.addMenu(menu3);
        menuBar.insertMenu(1, menu2);

        Array<Menu> menus = (Array<Menu>) getPrivateField(menuBar, "menus");
        Assert.assertEquals("All menus should be present", 3, menus.size);
        Assert.assertSame("Menu at index 0 should be menu1", menu1, menus.get(0));
        Assert.assertSame("Menu at index 1 should be menu2", menu2, menus.get(1));
        Assert.assertSame("Menu at index 2 should be menu3", menu3, menus.get(2));
    }

    @Test
    public void testInsertMenuAtBeginning() {
        MenuBar menuBar = new MenuBar();
        Menu menu1 = new Menu("Menu 1");
        Menu menu2 = new Menu("Menu 2");

        menuBar.addMenu(menu1);
        menuBar.insertMenu(0, menu2);

        Array<Menu> menus = (Array<Menu>) getPrivateField(menuBar, "menus");
        Assert.assertEquals("All menus should be present", 2, menus.size);
        Assert.assertSame("Menu at index 0 should be menu2", menu2, menus.get(0));
        Assert.assertSame("Menu at index 1 should be menu1", menu1, menus.get(1));
    }

    @Test
    public void testCloseMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Test Menu");

        menuBar.addMenu(menu);

        // Set current menu manually for testing
        setPrivateField(menuBar, "currentMenu", menu);

        menuBar.closeMenu();

        Assert.assertNull("Current menu should be null after close", getPrivateField(menuBar, "currentMenu"));
    }

    @Test
    public void testCloseMenuWithNullCurrentMenu() {
        MenuBar menuBar = new MenuBar();

        // Should not throw exception
        menuBar.closeMenu();

        Assert.assertNull("Current menu should remain null", getPrivateField(menuBar, "currentMenu"));
    }

    @Test
    public void testMenuListener() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Test Menu");

        final boolean[] menuOpened = {false};
        final boolean[] menuClosed = {false};

        MenuBar.MenuBarListener listener = new MenuBar.MenuBarListener() {
            @Override
            public void menuOpened(Menu menu) {
                menuOpened[0] = true;
            }

            @Override
            public void menuClosed(Menu menu) {
                menuClosed[0] = true;
            }
        };

        menuBar.setMenuListener(listener);

        // Test that listener is set (we can't easily test the actual calls without a stage)
        Assert.assertNotNull("Listener should be set", getPrivateField(menuBar, "menuListener"));
    }

    @Test
    public void testMenuBarStyleCopyConstructor() {
        Drawable mockBackground = Mockito.mock(Drawable.class);

        MenuBar.MenuBarStyle original = new MenuBar.MenuBarStyle(mockBackground);
        MenuBar.MenuBarStyle copy = new MenuBar.MenuBarStyle(original);

        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertEquals("Background should be copied", mockBackground, copy.background);
    }

    @Test
    public void testMenuBarStyleConstructorWithDrawable() {
        Drawable mockBackground = Mockito.mock(Drawable.class);

        MenuBar.MenuBarStyle style = new MenuBar.MenuBarStyle(mockBackground);

        Assert.assertNotNull("Style should be created", style);
        Assert.assertEquals("Background should be set", mockBackground, style.background);
    }

    @Test
    public void testMenuBarStyleDefaultConstructor() {
        MenuBar.MenuBarStyle style = new MenuBar.MenuBarStyle();

        Assert.assertNotNull("Style should be created", style);
        Assert.assertNull("Background should be null by default", style.background);
    }

    @Test
    public void testAddMenuWithNullMenu() {
        MenuBar menuBar = new MenuBar();

        try {
            menuBar.addMenu(null);
            Assert.fail("Should throw exception for null menu");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testInsertMenuWithNullMenu() {
        MenuBar menuBar = new MenuBar();

        try {
            menuBar.insertMenu(0, null);
            Assert.fail("Should throw exception for null menu");
        } catch (Exception e) {
            // Expected
        }
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
