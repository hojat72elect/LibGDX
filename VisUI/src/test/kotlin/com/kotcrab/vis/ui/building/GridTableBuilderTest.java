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
 * Unit tests for {@link GridTableBuilder}. Row calls are ignored; widgets are placed in a grid with fixed row size.
 */
public class GridTableBuilderTest {

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
        GridTableBuilder builder = new GridTableBuilder(2);
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(0, table.getChildren().size);
    }

    @Test
    public void testRowSizeOne() {
        GridTableBuilder builder = new GridTableBuilder(1);
        builder.append(new Actor()).append(new Actor()).append(new Actor());
        Table table = builder.build();
        Assert.assertEquals(3, table.getChildren().size);
        Assert.assertEquals(3, table.getCells().size);
    }

    @Test
    public void testRowSizeTwoFullRows() {
        GridTableBuilder builder = new GridTableBuilder(2);
        builder.append(new Actor()).append(new Actor())
                .append(new Actor()).append(new Actor());
        Table table = builder.build();
        Assert.assertEquals(4, table.getChildren().size);
        Assert.assertEquals(4, table.getCells().size);
    }

    @Test
    public void testRowSizeTwoWithIncompleteLastRow() {
        GridTableBuilder builder = new GridTableBuilder(2);
        builder.append(new Actor()).append(new Actor())
                .append(new Actor());
        Table table = builder.build();
        // 3 widgets: 2 in first row, 1 in second row (no "repair" of last row)
        Assert.assertEquals(3, table.getChildren().size);
        Assert.assertEquals(3, table.getCells().size);
    }

    @Test
    public void testRowSizeThree() {
        GridTableBuilder builder = new GridTableBuilder(3);
        builder.append(new Actor()).append(new Actor()).append(new Actor())
                .append(new Actor()).append(new Actor()).append(new Actor());
        Table table = builder.build();
        Assert.assertEquals(6, table.getChildren().size);
        Assert.assertEquals(6, table.getCells().size);
    }

    @Test
    public void testRowCallsIgnored() {
        GridTableBuilder builder = new GridTableBuilder(2);
        builder.append(new Actor()).row().append(new Actor())
                .append(new Actor()).append(new Actor());
        Table table = builder.build();
        // row() ignored; 4 widgets in 2x2 grid
        Assert.assertEquals(4, table.getChildren().size);
    }

    @Test
    public void testBuildWithCustomTable() {
        GridTableBuilder builder = new GridTableBuilder(2);
        builder.append(new Actor()).append(new Actor());
        Table customTable = new Table();
        Table result = builder.build(customTable);
        Assert.assertSame(customTable, result);
        Assert.assertEquals(2, result.getChildren().size);
    }

    @Test
    public void testConstructorWithPadding() {
        GridTableBuilder builder = new GridTableBuilder(Padding.PAD_4, 2);
        builder.append(new Actor()).append(new Actor());
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(2, table.getChildren().size);
    }

    @Test
    public void testConstructorWithRowSizeAndEstimatedAmounts() {
        GridTableBuilder builder = new GridTableBuilder(2, 10, 5);
        builder.append(new Actor()).append(new Actor()).append(new Actor()).append(new Actor());
        Table table = builder.build();
        Assert.assertEquals(4, table.getChildren().size);
    }

    @Test
    public void testConstructorWithAllParams() {
        GridTableBuilder builder = new GridTableBuilder(2, 10, 5, Padding.PAD_2);
        builder.append(new Actor()).append(new Actor());
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(2, table.getChildren().size);
    }

    @Test
    public void testSingleWidgetInGrid() {
        GridTableBuilder builder = new GridTableBuilder(2);
        builder.append(new Actor());
        Table table = builder.build();
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testAppendEmptyCell() {
        GridTableBuilder builder = new GridTableBuilder(2);
        builder.append().append(new Actor()).append(new Actor());
        Table table = builder.build();
        Assert.assertEquals(3, table.getCells().size);
    }
}
