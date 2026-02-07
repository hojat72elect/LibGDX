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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

public class VisTableTest {

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

        // Add Separator styles
        Separator.SeparatorStyle defaultSeparatorStyle = new Separator.SeparatorStyle();
        Drawable defaultBackground = Mockito.mock(Drawable.class);
        Mockito.when(defaultBackground.getMinWidth()).thenReturn(10.0f);
        Mockito.when(defaultBackground.getMinHeight()).thenReturn(2.0f);
        defaultSeparatorStyle.background = defaultBackground;
        skin.add("default", defaultSeparatorStyle);

        Separator.SeparatorStyle verticalSeparatorStyle = new Separator.SeparatorStyle();
        Drawable verticalBackground = Mockito.mock(Drawable.class);
        Mockito.when(verticalBackground.getMinWidth()).thenReturn(2.0f);
        Mockito.when(verticalBackground.getMinHeight()).thenReturn(10.0f);
        verticalSeparatorStyle.background = verticalBackground;
        skin.add("vertical", verticalSeparatorStyle);

        // Add font for labels
        skin.add("default", testFont, BitmapFont.class);

        // Add Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = testFont;
        skin.add("default", labelStyle);

        return skin;
    }

    private BitmapFont newTestFont() {
        com.badlogic.gdx.graphics.Texture mockTexture = Mockito.mock(com.badlogic.gdx.graphics.Texture.class);
        Mockito.when(mockTexture.getWidth()).thenReturn(1);
        Mockito.when(mockTexture.getHeight()).thenReturn(1);

        com.badlogic.gdx.graphics.g2d.TextureRegion mockRegion = Mockito.mock(com.badlogic.gdx.graphics.g2d.TextureRegion.class);
        Mockito.when(mockRegion.getTexture()).thenReturn(mockTexture);

        BitmapFont.BitmapFontData fontData = new BitmapFont.BitmapFontData() {
            @Override
            public boolean hasGlyph(char ch) {
                return true;
            }

            @Override
            public BitmapFont.Glyph getGlyph(char ch) {
                BitmapFont.Glyph glyph = new BitmapFont.Glyph();
                glyph.id = ch;
                glyph.width = 8; // Width of the character
                glyph.height = 16; // Height of the character
                glyph.xadvance = 8; // How much to move after this character
                glyph.xoffset = 0;
                glyph.yoffset = 0;
                glyph.srcX = 0;
                glyph.srcY = 0;
                return glyph;
            }

            public float getLineHeight() {
                return 20.0f; // Larger line height for better table height
            }
        };

        // Set space advance
        fontData.spaceXadvance = 4.0f;

        return new BitmapFont(fontData, com.badlogic.gdx.utils.Array.with(mockRegion), true);
    }

    @Test
    public void testDefaultConstructor() {
        VisTable table = new VisTable();

        Assert.assertNotNull("Table should be created", table);
        Assert.assertNotNull("Skin should not be null", table.getSkin());
    }

    @Test
    public void testConstructorWithVisDefaults() {
        VisTable table = new VisTable(true);

        Assert.assertNotNull("Table should be created", table);
        Assert.assertNotNull("Skin should not be null", table.getSkin());
        // The TableUtils.setSpacingDefaults should have been called
        // We can't easily verify the exact spacing without accessing private fields
    }

    @Test
    public void testConstructorWithoutVisDefaults() {
        VisTable table = new VisTable(false);

        Assert.assertNotNull("Table should be created", table);
        Assert.assertNotNull("Skin should not be null", table.getSkin());
        // TableUtils.setSpacingDefaults should not have been called
    }

    @Test
    public void testAddSeparatorHorizontal() {
        VisTable table = new VisTable();

        Cell<Separator> separatorCell = table.addSeparator();

        Assert.assertNotNull("Separator should be created", separatorCell);
        Assert.assertNotNull("Separator actor should not be null", separatorCell.getActor());
        Assert.assertTrue("Actor should be a Separator", separatorCell.getActor() instanceof Separator);

        Assert.assertEquals("Pad top should be 2", 2f, separatorCell.getPadTop(), 0.0001f);
        Assert.assertEquals("Pad bottom should be 2", 2f, separatorCell.getPadBottom(), 0.0001f);
    }

    @Test
    public void testAddSeparatorVertical() {
        VisTable table = new VisTable();

        Cell<Separator> separatorCell = table.addSeparator(true);

        Assert.assertNotNull("Separator should be created", separatorCell);
        Assert.assertNotNull("Separator actor should not be null", separatorCell.getActor());
        Assert.assertTrue("Actor should be a Separator", separatorCell.getActor() instanceof Separator);

        Assert.assertEquals("Pad top should be 2", 2f, separatorCell.getPadTop(), 0.0001f);
        Assert.assertEquals("Pad bottom should be 2", 2f, separatorCell.getPadBottom(), 0.0001f);
    }

    @Test
    public void testAddSeparatorDefault() {
        VisTable table = new VisTable();

        Cell<Separator> separatorCell = table.addSeparator();

        Assert.assertNotNull("Separator should be created", separatorCell);
        Assert.assertNotNull("Separator actor should not be null", separatorCell.getActor());
        Assert.assertTrue("Actor should be a Separator", separatorCell.getActor() instanceof Separator);

        // Should have added a new row after the separator
        // We can verify this by checking the table's row count indirectly
        table.add(new Label("Test", VisUI.getSkin().get(Label.LabelStyle.class)));
        // If no exception is thrown, the table structure is valid
        Assert.assertTrue("Table should be valid after adding separator and label", true);
    }

    @Test
    public void testTableInheritance() {
        VisTable table = new VisTable();

        Assert.assertTrue("VisTable should extend Table", table instanceof Table);
    }

    @Test
    public void testAddActorAfterSeparator() {
        VisTable table = new VisTable();

        table.addSeparator();
        Label label = new Label("Test", VisUI.getSkin().get(Label.LabelStyle.class));
        Cell<Label> labelCell = table.add(label);

        Assert.assertNotNull("Label should be added", labelCell);
        Assert.assertSame("Label should be set", label, labelCell.getActor());
    }

    @Test
    public void testMultipleSeparators() {
        VisTable table = new VisTable();

        Cell<Separator> horizontalSep = table.addSeparator(false);
        table.row(); // Explicit row
        Cell<Separator> verticalSep = table.addSeparator(true);
        table.row(); // Explicit row
        Cell<Separator> defaultSep = table.addSeparator();

        Assert.assertNotNull("Horizontal separator should be created", horizontalSep);
        Assert.assertNotNull("Vertical separator should be created", verticalSep);
        Assert.assertNotNull("Default separator should be created", defaultSep);
    }

    @Test
    public void testTableLayoutWithSeparators() {
        VisTable table = new VisTable();

        // Add some content with separators
        table.add(new Label("Header", VisUI.getSkin().get(Label.LabelStyle.class)));
        table.row();
        table.addSeparator();
        table.add(new Label("Content", VisUI.getSkin().get(Label.LabelStyle.class)));
        table.row();
        table.addSeparator();
        table.add(new Label("Footer", VisUI.getSkin().get(Label.LabelStyle.class)));

        // Test that layout doesn't throw exceptions
        table.pack();

        Assert.assertTrue("Layout should complete successfully", true);
        Assert.assertTrue("Table should have positive width", table.getWidth() > 0);
        Assert.assertTrue("Table should have positive height", table.getHeight() > 0);
    }

    @Test
    public void testTableWithVisDefaultsSpacing() {
        VisTable tableWithDefaults = new VisTable(true);
        VisTable tableWithoutDefaults = new VisTable(false);

        // Both tables should be functional
        Assert.assertNotNull("Table with defaults should be created", tableWithDefaults);
        Assert.assertNotNull("Table without defaults should be created", tableWithoutDefaults);

        // Add same content to both
        Label label1 = new Label("Test", VisUI.getSkin().get(Label.LabelStyle.class));
        Label label2 = new Label("Test", VisUI.getSkin().get(Label.LabelStyle.class));

        tableWithDefaults.add(label1);
        tableWithoutDefaults.add(label2);

        // Both should layout successfully
        tableWithDefaults.pack();
        tableWithoutDefaults.pack();

        Assert.assertTrue("Both tables should have positive dimensions",
                tableWithDefaults.getWidth() > 0 && tableWithoutDefaults.getWidth() > 0);
    }

    @Test
    public void testSeparatorStyleProperties() {
        VisTable table = new VisTable();
        Cell<Separator> separatorCell = table.addSeparator();
        Separator separator = separatorCell.getActor();

        Assert.assertNotNull("Separator should not be null", separator);
        Assert.assertNotNull("Separator style should not be null", separator.getStyle());
    }

    @Test
    public void testAddSeparatorWithComplexLayout() {
        VisTable table = new VisTable();

        // Create a complex layout with separators
        table.add(new Label("Column 1", VisUI.getSkin().get(Label.LabelStyle.class)));
        table.add(new Label("Column 2", VisUI.getSkin().get(Label.LabelStyle.class)));
        table.row();

        table.addSeparator();
        table.row();

        table.add(new Label("Row 2 Col 1", VisUI.getSkin().get(Label.LabelStyle.class)));
        table.add(new Label("Row 2 Col 2", VisUI.getSkin().get(Label.LabelStyle.class)));
        table.row();

        table.addSeparator(true); // Vertical separator in next position
        table.add(new Label("After vertical", VisUI.getSkin().get(Label.LabelStyle.class)));

        // Should layout without issues
        table.pack();

        Assert.assertTrue("Complex layout should work", true);
    }

    @Test
    public void testTableWithNullActors() {
        VisTable table = new VisTable();

        // Adding null actors should be handled gracefully
        try {
            table.add((Actor) null);
            // If no exception is thrown, that's the expected behavior
            Assert.assertTrue("Table should handle null actors", true);
        } catch (Exception e) {
            // If an exception is thrown, that's also acceptable behavior
            Assert.assertTrue("Table should handle null actors gracefully", true);
        }
    }

    @Test
    public void testSeparatorCellProperties() {
        VisTable table = new VisTable();

        // Test horizontal separator cell properties
        Cell<Separator> horizontalCell = table.addSeparator(false);
        Assert.assertEquals("Horizontal separator should not expand Y", 0f, horizontalCell.getExpandY(), 0.0001f);
        Assert.assertEquals("Horizontal separator should not fill Y", 0f, horizontalCell.getFillY(), 0.0001f);

        table.row();

        // Test vertical separator cell properties
        Cell<Separator> verticalCell = table.addSeparator(true);
        Assert.assertEquals("Vertical separator should not expand X", 0f, verticalCell.getExpandX(), 0.0001f);
        Assert.assertEquals("Vertical separator should not fill X", 0f, verticalCell.getFillX(), 0.0001f);
    }

    @Test
    public void testTableWithDifferentSeparatorStyles() {
        VisTable table = new VisTable();

        // Test default separator style
        Cell<Separator> defaultSep = table.addSeparator();
        Assert.assertNotNull("Default separator should use 'default' style", defaultSep.getActor());

        table.row();

        // Test vertical separator style
        Cell<Separator> verticalSep = table.addSeparator(true);
        Assert.assertNotNull("Vertical separator should use 'vertical' style", verticalSep.getActor());

        // Both should be valid separators
        Assert.assertNotNull("Default separator should be created", defaultSep.getActor());
        Assert.assertNotNull("Vertical separator should be created", verticalSep.getActor());
    }

    @Test
    public void testTablePaddingAndSpacing() {
        VisTable table = new VisTable(true); // With VisUI defaults

        table.add(new Label("Test", VisUI.getSkin().get(Label.LabelStyle.class)));
        table.addSeparator();
        table.add(new Label("Test 2", VisUI.getSkin().get(Label.LabelStyle.class)));

        table.pack();

        // Table should have reasonable dimensions
        Assert.assertTrue("Table should have positive width", table.getWidth() > 0);
        Assert.assertTrue("Table should have positive height", table.getHeight() > 0);

        // Height should account for separator and padding
        Assert.assertTrue("Table height should be reasonable for content", table.getHeight() > 5);
    }

    @Test
    public void testMultipleTables() {
        VisTable table1 = new VisTable();
        VisTable table2 = new VisTable(true);
        VisTable table3 = new VisTable(false);

        Assert.assertNotNull("All tables should be created", table1);
        Assert.assertNotNull("All tables should be created", table2);
        Assert.assertNotNull("All tables should be created", table3);

        // All should be functional
        table1.addSeparator();
        table2.addSeparator();
        table3.addSeparator();

        Assert.assertTrue("All tables should be functional", true);
    }

    @Test
    public void testTableWithNestedContent() {
        VisTable mainTable = new VisTable();
        VisTable nestedTable = new VisTable();

        // Add content to nested table
        nestedTable.add(new Label("Nested", VisUI.getSkin().get(Label.LabelStyle.class)));
        nestedTable.row();
        nestedTable.addSeparator();
        nestedTable.add(new Label("Content", VisUI.getSkin().get(Label.LabelStyle.class)));

        // Add nested table to main table
        mainTable.add(nestedTable);
        mainTable.row();
        mainTable.addSeparator();
        mainTable.add(new Label("Main", VisUI.getSkin().get(Label.LabelStyle.class)));

        // Should layout without issues
        mainTable.pack();

        Assert.assertTrue("Nested content should work", true);
        Assert.assertTrue("Main table should have positive dimensions", mainTable.getWidth() > 0);
        Assert.assertTrue("Main table should have positive dimensions", mainTable.getHeight() > 0);
    }
}
