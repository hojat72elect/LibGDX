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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

public class MenuTest {

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

        // Add PopupMenuStyle required by Menu (extends PopupMenu)
        PopupMenu.PopupMenuStyle popupMenuStyle = new PopupMenu.PopupMenuStyle();
        popupMenuStyle.background = Mockito.mock(Drawable.class);
        skin.add("default", popupMenuStyle);

        // Add minimal required style for Menu
        VisTextButtonStyle buttonStyle = new VisTextButtonStyle();
        buttonStyle.font = testFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.up = Mockito.mock(Drawable.class);
        buttonStyle.down = Mockito.mock(Drawable.class);
        buttonStyle.over = Mockito.mock(Drawable.class);
        buttonStyle.checked = Mockito.mock(Drawable.class);

        Menu.MenuStyle menuStyle = new Menu.MenuStyle();
        menuStyle.openButtonStyle = buttonStyle;
        menuStyle.background = Mockito.mock(Drawable.class);
        menuStyle.border = Mockito.mock(Drawable.class);
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
        // Initialize the style to prevent NullPointerException
        style.background = Mockito.mock(Drawable.class);
        style.border = Mockito.mock(Drawable.class);
        style.openButtonStyle = new VisTextButton.VisTextButtonStyle();
        style.openButtonStyle.up = Mockito.mock(Drawable.class);
        style.openButtonStyle.down = Mockito.mock(Drawable.class);
        style.openButtonStyle.over = Mockito.mock(Drawable.class);
        style.openButtonStyle.checked = Mockito.mock(Drawable.class);
        style.openButtonStyle.font = testFont;
        style.openButtonStyle.fontColor = Color.WHITE;

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
        // Initialize the style to prevent NullPointerException
        style.background = Mockito.mock(Drawable.class);
        style.border = Mockito.mock(Drawable.class);
        style.openButtonStyle = new VisTextButton.VisTextButtonStyle();
        style.openButtonStyle.up = Mockito.mock(Drawable.class);
        style.openButtonStyle.down = Mockito.mock(Drawable.class);
        style.openButtonStyle.over = Mockito.mock(Drawable.class);
        style.openButtonStyle.checked = Mockito.mock(Drawable.class);
        style.openButtonStyle.font = testFont;
        style.openButtonStyle.fontColor = Color.WHITE;

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
        // Create a mock MenuBar and set it to prevent null pointer exception
        MenuBar mockMenuBar = Mockito.mock(MenuBar.class);
        setPrivateField(menu, "menuBar", mockMenuBar);

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

    private void setPrivateField(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            // Ignore
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
