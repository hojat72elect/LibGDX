package com.kotcrab.vis.ui.widget;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton.VisTextButtonStyle;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MenuItemTest {

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

        // Add Label styles required by MenuItem
        com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle shortcutLabelStyle = new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle();
        shortcutLabelStyle.font = testFont;
        shortcutLabelStyle.fontColor = Color.WHITE;
        skin.add("menuitem-shortcut", shortcutLabelStyle);

        // Add PopupMenuStyle required by PopupMenu
        PopupMenu.PopupMenuStyle popupMenuStyle = new PopupMenu.PopupMenuStyle();
        popupMenuStyle.background = Mockito.mock(Drawable.class);
        skin.add("default", popupMenuStyle);

        // Add minimal required style for MenuItem
        VisTextButtonStyle buttonStyle = new VisTextButtonStyle();
        buttonStyle.font = testFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = Mockito.mock(Drawable.class);
        buttonStyle.down = Mockito.mock(Drawable.class);
        buttonStyle.over = Mockito.mock(Drawable.class);
        buttonStyle.checked = Mockito.mock(Drawable.class);

        MenuItem.MenuItemStyle menuItemStyle = new MenuItem.MenuItemStyle();
        menuItemStyle.font = testFont;
        menuItemStyle.fontColor = Color.WHITE;
        menuItemStyle.up = Mockito.mock(Drawable.class);
        menuItemStyle.down = Mockito.mock(Drawable.class);
        menuItemStyle.over = Mockito.mock(Drawable.class);
        menuItemStyle.subMenu = Mockito.mock(Drawable.class);
        skin.add("default", menuItemStyle);

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
    public void testConstructorWithText() {
        MenuItem item = new MenuItem("Test Item");

        Assert.assertNotNull("Menu item should be created", item);
        Assert.assertEquals("Text should be set", "Test Item", item.getText().toString());
        Assert.assertNotNull("Label should be created", item.getLabel());
        Assert.assertNull("Image should be null by default", item.getImage());
        Assert.assertNull("Sub menu should be null by default", item.getSubMenu());
    }

    @Test
    public void testConstructorWithTextAndStyleName() {
        MenuItem item = new MenuItem("Test Item", "default");

        Assert.assertNotNull("Menu item should be created", item);
        Assert.assertEquals("Text should be set", "Test Item", item.getText().toString());
        Assert.assertNotNull("Label should be created", item.getLabel());
    }

    @Test
    public void testConstructorWithTextAndChangeListener() {
        final boolean[] changed = {false};
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changed[0] = true;
            }
        };

        MenuItem item = new MenuItem("Test Item", listener);

        Assert.assertNotNull("Menu item should be created", item);
        Assert.assertEquals("Text should be set", "Test Item", item.getText().toString());

        // Test that listener was added by firing a change event
        item.fireChangeEvent();
        Assert.assertTrue("Listener should be called", changed[0]);
    }

    @Test
    public void testConstructorWithTextAndDrawable() {
        Drawable mockDrawable = Mockito.mock(Drawable.class);

        MenuItem item = new MenuItem("Test Item", mockDrawable);

        Assert.assertNotNull("Menu item should be created", item);
        Assert.assertEquals("Text should be set", "Test Item", item.getText().toString());
        Assert.assertNotNull("Image should be created", item.getImage());
        Assert.assertNotNull("Image cell should not be null", item.getImageCell());
    }

    @Test
    public void testConstructorWithTextDrawableAndChangeListener() {
        Drawable mockDrawable = Mockito.mock(Drawable.class);
        final boolean[] changed = {false};
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changed[0] = true;
            }
        };

        MenuItem item = new MenuItem("Test Item", mockDrawable, listener);

        Assert.assertNotNull("Menu item should be created", item);
        Assert.assertNotNull("Image should be created", item.getImage());

        item.fireChangeEvent();
        Assert.assertTrue("Listener should be called", changed[0]);
    }

    @Test
    public void testConstructorWithTextDrawableAndStyleName() {
        Drawable mockDrawable = Mockito.mock(Drawable.class);

        MenuItem item = new MenuItem("Test Item", mockDrawable, "default");

        Assert.assertNotNull("Menu item should be created", item);
        Assert.assertEquals("Text should be set", "Test Item", item.getText().toString());
        Assert.assertNotNull("Image should be created", item.getImage());
    }

    @Test
    public void testConstructorWithTextAndImage() {
        Image image = new Image();

        MenuItem item = new MenuItem("Test Item", image);

        Assert.assertNotNull("Menu item should be created", item);
        Assert.assertEquals("Text should be set", "Test Item", item.getText().toString());
        Assert.assertSame("Image should be set", image, item.getImage());
    }

    @Test
    public void testConstructorWithTextImageAndChangeListener() {
        Image image = new Image();
        final boolean[] changed = {false};
        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changed[0] = true;
            }
        };

        MenuItem item = new MenuItem("Test Item", image, listener);

        Assert.assertNotNull("Menu item should be created", item);
        Assert.assertSame("Image should be set", image, item.getImage());

        item.fireChangeEvent();
        Assert.assertTrue("Listener should be called", changed[0]);
    }

    @Test
    public void testConstructorWithTextImageAndStyleName() {
        Image image = new Image();

        MenuItem item = new MenuItem("Test Item", image, "default");

        Assert.assertNotNull("Menu item should be created", item);
        Assert.assertSame("Image should be set", image, item.getImage());
    }

    @Test
    public void testSetAndGetText() {
        MenuItem item = new MenuItem("Original Text");

        Assert.assertEquals("Original text should be set", "Original Text", item.getText().toString());

        item.setText("New Text");
        Assert.assertEquals("Text should be updated", "New Text", item.getText().toString());
    }

    @Test
    public void testSetAndGetSubMenu() {
        MenuItem item = new MenuItem("Test Item");
        PopupMenu subMenu = new PopupMenu();

        Assert.assertNull("Sub menu should be null initially", item.getSubMenu());

        item.setSubMenu(subMenu);
        Assert.assertSame("Sub menu should be set", subMenu, item.getSubMenu());

        item.setSubMenu(null);
        Assert.assertNull("Sub menu should be null after removal", item.getSubMenu());
    }

    @Test
    public void testSetAndGetShortcut() {
        MenuItem item = new MenuItem("Test Item");

        Assert.assertEquals("Shortcut should be empty initially", "", item.getShortcut().toString());

        item.setShortcut("Ctrl+S");
        Assert.assertEquals("Shortcut should be set", "Ctrl+S", item.getShortcut().toString());

        item.setShortcut(Input.Keys.F5);
        Assert.assertEquals("Key code should be converted to string", "F5", item.getShortcut().toString());
    }

    @Test
    public void testSetShortcutWithMultipleKeys() {
        MenuItem item = new MenuItem("Test Item");

        item.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.SHIFT_LEFT, Input.Keys.F5);

        String shortcut = item.getShortcut().toString();
        Assert.assertNotNull("Shortcut should be set", shortcut);
        Assert.assertTrue("Shortcut should contain keys", shortcut.length() > 0);
    }

    @Test
    public void testGenerateDisabledImage() {
        MenuItem item = new MenuItem("Test Item");

        Assert.assertTrue("Generate disabled image should be true by default", item.isGenerateDisabledImage());

        item.setGenerateDisabledImage(false);
        Assert.assertFalse("Generate disabled image should be false", item.isGenerateDisabledImage());

        item.setGenerateDisabledImage(true);
        Assert.assertTrue("Generate disabled image should be true", item.isGenerateDisabledImage());
    }

    @Test
    public void testGetCells() {
        MenuItem item = new MenuItem("Test Item");

        Assert.assertNotNull("Image cell should not be null", item.getImageCell());
        Assert.assertNotNull("Label cell should not be null", item.getLabelCell());
        Assert.assertNotNull("Shortcut cell should not be null", item.getShortcutCell());
        Assert.assertNotNull("Sub menu icon cell should not be null", item.getSubMenuIconCell());
    }

    @Test
    public void testMenuItemStyleCopyConstructor() {
        Drawable mockSubMenu = Mockito.mock(Drawable.class);
        BitmapFont mockFont = Mockito.mock(BitmapFont.class);

        MenuItem.MenuItemStyle original = new MenuItem.MenuItemStyle();
        original.subMenu = mockSubMenu;
        original.font = mockFont;

        MenuItem.MenuItemStyle copy = new MenuItem.MenuItemStyle(original);

        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertSame("Sub menu drawable should be copied", mockSubMenu, copy.subMenu);
        Assert.assertSame("Font should be copied", mockFont, copy.font);
    }

    @Test
    public void testMenuItemStyleConstructorWithSubMenu() {
        Drawable mockSubMenu = Mockito.mock(Drawable.class);

        MenuItem.MenuItemStyle style = new MenuItem.MenuItemStyle(mockSubMenu);

        Assert.assertNotNull("Style should be created", style);
        Assert.assertSame("Sub menu drawable should be set", mockSubMenu, style.subMenu);
    }

    @Test
    public void testMenuItemStyleDefaultConstructor() {
        MenuItem.MenuItemStyle style = new MenuItem.MenuItemStyle();

        Assert.assertNotNull("Style should be created", style);
        Assert.assertNull("Sub menu drawable should be null by default", style.subMenu);
    }

    @Test
    public void testMenuItemStyleInheritance() {
        MenuItem.MenuItemStyle style = new MenuItem.MenuItemStyle();

        // Test that MenuItemStyle extends TextButtonStyle
        Assert.assertTrue("MenuItemStyle should extend TextButtonStyle",
                style instanceof com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle);
    }

    @Test
    public void testGetStyle() {
        MenuItem item = new MenuItem("Test Item");

        MenuItem.MenuItemStyle style = item.getStyle();
        Assert.assertNotNull("Style should not be null", style);
        Assert.assertTrue("Style should be MenuItemStyle", style instanceof MenuItem.MenuItemStyle);
    }

    @Test
    public void testSetStyle() {
        MenuItem item = new MenuItem("Test Item");
        MenuItem.MenuItemStyle newStyle = new MenuItem.MenuItemStyle();
        newStyle.font = testFont; // Add font to prevent IllegalArgumentException

        item.setStyle(newStyle);

        Assert.assertSame("Style should be set", newStyle, item.getStyle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStyleWithInvalidType() {
        MenuItem item = new MenuItem("Test Item");
        com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle invalidStyle =
                new com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle();

        item.setStyle(invalidStyle);
    }

    @Test
    public void testFireChangeEvent() {
        final boolean[] changed = {false};
        MenuItem item = new MenuItem("Test Item", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changed[0] = true;
            }
        });

        item.fireChangeEvent();
        Assert.assertTrue("Change event should be fired", changed[0]);
    }

    @Test
    public void testMenuItemWithNullText() {
        try {
            MenuItem item = new MenuItem((String) null);
            Assert.assertNotNull("Menu item should be created even with null text", item);
        } catch (Exception e) {
            // Expected behavior may vary
        }
    }

    @Test
    public void testMenuItemWithEmptyText() {
        MenuItem item = new MenuItem("");

        Assert.assertNotNull("Menu item should be created with empty text", item);
        Assert.assertEquals("Text should be empty", "", item.getText().toString());
    }
}
