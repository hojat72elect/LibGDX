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
 * Unit tests for {@link OneRowTableBuilder}. Row calls are ignored; all widgets are placed in one row.
 */
public class OneRowTableBuilderTest {

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
        OneRowTableBuilder builder = new OneRowTableBuilder();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(0, table.getChildren().size);
    }

    @Test
    public void testSingleWidget() {
        OneRowTableBuilder builder = new OneRowTableBuilder();
        builder.append(new Actor());
        Table table = builder.build();
        Assert.assertEquals(1, table.getChildren().size);
        Assert.assertEquals(1, table.getCells().size);
    }

    @Test
    public void testMultipleWidgetsOneRow() {
        OneRowTableBuilder builder = new OneRowTableBuilder();
        builder.append(new Actor()).append(new Actor()).append(new Actor());
        Table table = builder.build();
        Assert.assertEquals(3, table.getChildren().size);
        Assert.assertEquals(3, table.getCells().size);
    }

    @Test
    public void testRowCallsIgnored() {
        OneRowTableBuilder builder = new OneRowTableBuilder();
        builder.append(new Actor()).row().append(new Actor()).row().append(new Actor());
        Table table = builder.build();
        // All 3 widgets in one row; row() is ignored
        Assert.assertEquals(3, table.getChildren().size);
        Assert.assertEquals(3, table.getCells().size);
    }

    @Test
    public void testBuildWithCustomTable() {
        OneRowTableBuilder builder = new OneRowTableBuilder();
        builder.append(new Actor()).append(new Actor());
        Table customTable = new Table();
        Table result = builder.build(customTable);
        Assert.assertSame(customTable, result);
        Assert.assertEquals(2, result.getChildren().size);
    }

    @Test
    public void testConstructorWithPadding() {
        OneRowTableBuilder builder = new OneRowTableBuilder(new Padding(4f));
        builder.append(new Actor());
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testConstructorWithEstimatedAmounts() {
        OneRowTableBuilder builder = new OneRowTableBuilder(10, 1);
        builder.append(new Actor()).append(new Actor());
        Table table = builder.build();
        Assert.assertEquals(2, table.getChildren().size);
    }

    @Test
    public void testConstructorWithEstimatedAmountsAndPadding() {
        OneRowTableBuilder builder = new OneRowTableBuilder(10, 1, Padding.PAD_2);
        builder.append(new Actor());
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testAppendEmptyCell() {
        OneRowTableBuilder builder = new OneRowTableBuilder();
        builder.append().append(new Actor());
        Table table = builder.build();
        Assert.assertEquals(2, table.getCells().size);
    }
}
