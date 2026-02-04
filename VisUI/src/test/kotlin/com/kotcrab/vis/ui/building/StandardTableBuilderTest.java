package com.kotcrab.vis.ui.building;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Unit tests for {@link StandardTableBuilder}.
 */
public class StandardTableBuilderTest {

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
        StandardTableBuilder builder = new StandardTableBuilder();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(0, table.getChildren().size);
    }

    @Test
    public void testSingleRowSingleWidget() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(1, table.getChildren().size);
        Assert.assertEquals(1, table.getCells().size);
    }

    @Test
    public void testSingleRowMultipleWidgets() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(new Actor()).append(new Actor()).append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(3, table.getChildren().size);
        Assert.assertEquals(3, table.getCells().size);
    }

    @Test
    public void testMultipleRowsSameSize() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(new Actor()).append(new Actor()).row()
                .append(new Actor()).append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(4, table.getChildren().size);
        Assert.assertEquals(4, table.getCells().size);
    }

    @Test
    public void testMultipleRowsDifferentSizesUsesLcm() {
        // Row 1: 2 widgets, Row 2: 3 widgets -> LCM = 6, so colspan 3 and 2
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(new Actor()).append(new Actor()).row()
                .append(new Actor()).append(new Actor()).append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(5, table.getChildren().size);
        Assert.assertEquals(5, table.getCells().size);
    }

    @Test
    public void testBuildWithCustomTable() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(new Actor()).row();
        Table customTable = new Table();
        Table result = builder.build(customTable);
        Assert.assertSame(customTable, result);
        Assert.assertEquals(1, result.getChildren().size);
    }

    @Test
    public void testConstructorWithPadding() {
        Padding padding = new Padding(4f);
        StandardTableBuilder builder = new StandardTableBuilder(padding);
        builder.append(new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testConstructorWithEstimatedAmounts() {
        StandardTableBuilder builder = new StandardTableBuilder(10, 2);
        builder.append(new Actor()).append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(2, table.getChildren().size);
    }

    @Test
    public void testConstructorWithEstimatedAmountsAndPadding() {
        StandardTableBuilder builder = new StandardTableBuilder(10, 2, Padding.PAD_2);
        builder.append(new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testAppendCellWidgetWithSettings() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(CellWidget.of(new Actor()).padding(Padding.PAD_4).wrap()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testEmptyCellInRow() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(new Actor()).append().append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(3, table.getCells().size);
    }
}
