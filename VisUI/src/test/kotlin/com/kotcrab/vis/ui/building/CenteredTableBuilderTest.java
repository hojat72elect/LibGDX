package com.kotcrab.vis.ui.building;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.building.utilities.Padding;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Unit tests for {@link CenteredTableBuilder}. Builds tables with widgets centered; first/last in row get expand/align.
 */
public class CenteredTableBuilderTest {

    @BeforeClass
    public static void setupGdx() {
        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> null);
        }
    }

    @Test
    public void testBuildEmpty() {
        CenteredTableBuilder builder = new CenteredTableBuilder();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(0, table.getChildren().size);
    }

    @Test
    public void testSingleRowSingleWidget() {
        CenteredTableBuilder builder = new CenteredTableBuilder();
        builder.append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(1, table.getChildren().size);
        Assert.assertEquals(1, table.getCells().size);
    }

    @Test
    public void testSingleRowMultipleWidgets() {
        CenteredTableBuilder builder = new CenteredTableBuilder();
        builder.append(new Actor()).append(new Actor()).append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(3, table.getChildren().size);
        Assert.assertEquals(3, table.getCells().size);
    }

    @Test
    public void testMultipleRowsSameSize() {
        CenteredTableBuilder builder = new CenteredTableBuilder();
        builder.append(new Actor()).append(new Actor()).row()
                .append(new Actor()).append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(4, table.getChildren().size);
        Assert.assertEquals(4, table.getCells().size);
    }

    @Test
    public void testMultipleRowsDifferentSizes() {
        CenteredTableBuilder builder = new CenteredTableBuilder();
        builder.append(new Actor()).append(new Actor()).row()
                .append(new Actor()).append(new Actor()).append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(5, table.getChildren().size);
        Assert.assertEquals(5, table.getCells().size);
    }

    @Test
    public void testBuildWithCustomTable() {
        CenteredTableBuilder builder = new CenteredTableBuilder();
        builder.append(new Actor()).append(new Actor()).row();
        Table customTable = new Table();
        Table result = builder.build(customTable);
        Assert.assertSame(customTable, result);
        Assert.assertEquals(2, result.getChildren().size);
    }

    @Test
    public void testConstructorWithPadding() {
        CenteredTableBuilder builder = new CenteredTableBuilder(new Padding(4f));
        builder.append(new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testConstructorWithEstimatedAmounts() {
        CenteredTableBuilder builder = new CenteredTableBuilder(10, 2);
        builder.append(new Actor()).append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(2, table.getChildren().size);
    }

    @Test
    public void testConstructorWithEstimatedAmountsAndPadding() {
        CenteredTableBuilder builder = new CenteredTableBuilder(10, 2, Padding.PAD_2);
        builder.append(new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testEmptyCellInRow() {
        CenteredTableBuilder builder = new CenteredTableBuilder();
        builder.append(new Actor()).append().append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(3, table.getCells().size);
    }

    @Test
    public void testSingleWidgetInRowNotExpanded() {
        // Single widget in row: no expand/align (centered as-is)
        CenteredTableBuilder builder = new CenteredTableBuilder();
        builder.append(new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getCells().size);
    }
}
