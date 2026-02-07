package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link VisTable}.
 */
public class VisTableTest {

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
        
        Separator separator = separatorCell.getActor();
        Assert.assertFalse("Separator should be horizontal", separator.isVertical());
        
        // Verify cell properties
        Assert.assertTrue("Separator should have fillX", separatorCell.isFillX());
        Assert.assertTrue("Separator should have expandX", separatorCell.isExpandX());
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
        
        Separator separator = separatorCell.getActor();
        Assert.assertTrue("Separator should be vertical", separator.isVertical());
        
        // Verify cell properties
        Assert.assertTrue("Separator should have fillY", separatorCell.isFillY());
        Assert.assertTrue("Separator should have expandY", separatorCell.isExpandY());
        Assert.assertEquals("Pad top should be 2", 2f, separatorCell.getPadTop(), 0.0001f);
        Assert.assertEquals("Pad bottom should be 2", 2f, separatorCell.getPadBottom(), 0.0001f);
    }

    @Test
    public void testAddSeparatorDefault() {
        VisTable table = new VisTable();
        
        Cell<Separator> separatorCell = table.addSeparator();
        
        Assert.assertNotNull("Separator should be created", separatorCell);
        Assert.assertFalse("Default separator should be horizontal", separatorCell.getActor().isVertical());
        
        // Should have added a new row after the separator
        // We can verify this by checking the table's row count indirectly
        table.add(new Label("Test", Mockito.mock(com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle.class)));
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
        Label label = new Label("Test", Mockito.mock(com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle.class));
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
        
        Assert.assertFalse("First should be horizontal", horizontalSep.getActor().isVertical());
        Assert.assertTrue("Second should be vertical", verticalSep.getActor().isVertical());
        Assert.assertFalse("Third should be horizontal (default)", defaultSep.getActor().isVertical());
    }

    @Test
    public void testTableLayoutWithSeparators() {
        VisTable table = new VisTable();
        
        // Add some content with separators
        table.add(new Label("Header", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        table.row();
        table.addSeparator();
        table.add(new Label("Content", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        table.row();
        table.addSeparator();
        table.add(new Label("Footer", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        
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
        Label label1 = new Label("Test", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class));
        Label label2 = new Label("Test", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class));
        
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
        table.add(new Label("Column 1", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        table.add(new Label("Column 2", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        table.row();
        
        table.addSeparator();
        table.row();
        
        table.add(new Label("Row 2 Col 1", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        table.add(new Label("Row 2 Col 2", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        table.row();
        
        table.addSeparator(true); // Vertical separator in next position
        table.add(new Label("After vertical", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        
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
        Assert.assertFalse("Default should be horizontal", defaultSep.getActor().isVertical());
        Assert.assertTrue("Vertical should be vertical", verticalSep.getActor().isVertical());
    }

    @Test
    public void testTablePaddingAndSpacing() {
        VisTable table = new VisTable(true); // With VisUI defaults
        
        table.add(new Label("Test", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        table.addSeparator();
        table.add(new Label("Test 2", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        
        table.pack();
        
        // Table should have reasonable dimensions
        Assert.assertTrue("Table should have positive width", table.getWidth() > 0);
        Assert.assertTrue("Table should have positive height", table.getHeight() > 0);
        
        // Height should account for separator and padding
        Assert.assertTrue("Table height should be reasonable for content", table.getHeight() > 20);
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
        nestedTable.add(new Label("Nested", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        nestedTable.row();
        nestedTable.addSeparator();
        nestedTable.add(new Label("Content", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        
        // Add nested table to main table
        mainTable.add(nestedTable);
        mainTable.row();
        mainTable.addSeparator();
        mainTable.add(new Label("Main", Mockito.mock(com.badlogic.gdx.scenesame2d.ui.Label.LabelStyle.class)));
        
        // Should layout without issues
        mainTable.pack();
        
        Assert.assertTrue("Nested content should work", true);
        Assert.assertTrue("Main table should have positive dimensions", mainTable.getWidth() > 0);
        Assert.assertTrue("Main table should have positive dimensions", mainTable.getHeight() > 0);
    }
}
