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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

import java.util.Arrays;

public class MultiSplitPaneTest {

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

        // Add minimal required style for MultiSplitPane
        MultiSplitPane.MultiSplitPaneStyle splitPaneStyle = new MultiSplitPane.MultiSplitPaneStyle();
        splitPaneStyle.handle = Mockito.mock(Drawable.class);
        skin.add("default", splitPaneStyle);
        skin.add("default-vertical", splitPaneStyle);
        skin.add("default-horizontal", splitPaneStyle);

        // Add default LabelStyle for Table widgets
        com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle labelStyle = new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle();
        labelStyle.font = testFont;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

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
    public void testConstructorWithVertical() {
        MultiSplitPane splitPane = new MultiSplitPane(true);

        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertTrue("Should be vertical", (Boolean) getPrivateField(splitPane, "vertical"));
    }

    @Test
    public void testConstructorWithHorizontal() {
        MultiSplitPane splitPane = new MultiSplitPane(false);

        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertFalse("Should be horizontal", (Boolean) getPrivateField(splitPane, "vertical"));
    }

    @Test
    public void testConstructorWithVerticalAndStyleName() {
        MultiSplitPane splitPane = new MultiSplitPane(true, "default-vertical");

        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertTrue("Should be vertical", (Boolean) getPrivateField(splitPane, "vertical"));
    }

    @Test
    public void testConstructorWithHorizontalAndStyleName() {
        MultiSplitPane splitPane = new MultiSplitPane(false, "default-horizontal");

        Assert.assertNotNull("Split pane should be created", splitPane);
        Assert.assertFalse("Should be horizontal", (Boolean) getPrivateField(splitPane, "vertical"));
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
        Assert.assertFalse("Should be horizontal", (Boolean) getPrivateField(splitPane, "vertical"));

        splitPane.setVertical(true);
        Assert.assertTrue("Should be vertical", (Boolean) getPrivateField(splitPane, "vertical"));
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
    public void testGetPrefHeight() {
        MultiSplitPane splitPane = new MultiSplitPane(true); // Vertical
        Table table1 = new Table();
        Table table2 = new Table();
        table1.setSkin(VisUI.getSkin()); // Set skin on table
        table2.setSkin(VisUI.getSkin()); // Set skin on table
        table1.add("Label1"); // Give table some content
        table2.add("Label2"); // Give table some content
        splitPane.setWidgets(table1, table2);
        splitPane.setSize(100, 200); // Set size to help with preferred size calculation

        float prefHeight = splitPane.getPrefHeight();
        System.out.println("DEBUG: prefHeight = " + prefHeight); // Debug output
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
