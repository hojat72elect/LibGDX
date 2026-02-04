package com.kotcrab.vis.ui.building;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntArray;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import com.kotcrab.vis.ui.building.utilities.layouts.TableLayout;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Unit tests for {@link TableBuilder}. Uses {@link StandardTableBuilder} as concrete implementation
 * for testing inherited behavior.
 */
public class TableBuilderTest {

    @BeforeClass
    public static void setupGdx() {
        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> null);
        }
    }

    // --- Static methods: getGreatestCommonDenominator ---

    @Test
    public void testGetGreatestCommonDenominatorNormal() {
        Assert.assertEquals(6, TableBuilder.getGreatestCommonDenominator(54, 24));
        Assert.assertEquals(6, TableBuilder.getGreatestCommonDenominator(24, 54));
    }

    @Test
    public void testGetGreatestCommonDenominatorOneZero() {
        Assert.assertEquals(12, TableBuilder.getGreatestCommonDenominator(12, 0));
        Assert.assertEquals(7, TableBuilder.getGreatestCommonDenominator(0, 7));
    }

    @Test
    public void testGetGreatestCommonDenominatorCoprime() {
        Assert.assertEquals(1, TableBuilder.getGreatestCommonDenominator(7, 13));
    }

    @Test
    public void testGetGreatestCommonDenominatorEqual() {
        Assert.assertEquals(5, TableBuilder.getGreatestCommonDenominator(5, 5));
    }

    // --- Static methods: getLowestCommonMultiple (two ints) ---

    @Test
    public void testGetLowestCommonMultipleTwoValues() {
        Assert.assertEquals(12, TableBuilder.getLowestCommonMultiple(4, 6));
        Assert.assertEquals(12, TableBuilder.getLowestCommonMultiple(6, 4));
    }

    @Test
    public void testGetLowestCommonMultipleOneAndValue() {
        Assert.assertEquals(7, TableBuilder.getLowestCommonMultiple(1, 7));
        Assert.assertEquals(7, TableBuilder.getLowestCommonMultiple(7, 1));
    }

    @Test
    public void testGetLowestCommonMultipleSameValue() {
        Assert.assertEquals(5, TableBuilder.getLowestCommonMultiple(5, 5));
    }

    // --- Static methods: getLowestCommonMultiple (IntArray) ---

    @Test
    public void testGetLowestCommonMultipleIntArray() {
        IntArray values = new IntArray(new int[]{2, 3, 4});
        Assert.assertEquals(12, TableBuilder.getLowestCommonMultiple(values));
    }

    @Test
    public void testGetLowestCommonMultipleIntArraySingle() {
        IntArray values = new IntArray(new int[]{8});
        Assert.assertEquals(8, TableBuilder.getLowestCommonMultiple(values));
    }

    @Test
    public void testGetLowestCommonMultipleIntArrayTwo() {
        IntArray values = new IntArray(new int[]{3, 5});
        Assert.assertEquals(15, TableBuilder.getLowestCommonMultiple(values));
    }

    // --- Constructors and append/build (via StandardTableBuilder) ---

    @Test
    public void testBuildEmptyTable() {
        StandardTableBuilder builder = new StandardTableBuilder();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(0, table.getChildren().size);
    }

    @Test
    public void testBuildEmptyTableWithCustomTable() {
        StandardTableBuilder builder = new StandardTableBuilder();
        Table customTable = new Table();
        Table result = builder.build(customTable);
        Assert.assertSame(customTable, result);
        Assert.assertEquals(0, result.getChildren().size);
    }

    @Test
    public void testAppendActorAndBuild() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testAppendCellWidgetAndBuild() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(CellWidget.wrap(new Actor())).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testAppendEmptyCell() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append().row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getCells().size);
    }

    @Test
    public void testAppendVarargsActorsAsOneCell() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(new Actor(), new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        // One merged cell containing a table with 2 actors
        Assert.assertEquals(1, table.getCells().size);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testRowFlushesWidgets() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(new Actor()).row().append(new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(2, table.getChildren().size);
    }

    @Test
    public void testRowWithNoWidgetsIgnored() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.row().append(new Actor()).row();
        Table table = builder.build();
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testSetTablePadding() {
        Padding padding = new Padding(5f);
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.setTablePadding(padding).append(new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(5f, table.getPadTop(), 0.001f);
        Assert.assertEquals(5f, table.getPadLeft(), 0.001f);
    }

    @Test
    public void testAppendWithLayoutAndActors() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(TableLayout.HORIZONTAL, new Actor(), new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getCells().size);
    }

    @Test
    public void testAppendWithLayoutAndCellWidgets() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(TableLayout.HORIZONTAL, CellWidget.wrap(new Actor()), CellWidget.wrap(new Actor())).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getCells().size);
    }

    @Test
    public void testAppendWithMergedCellSettingsAndActors() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(CellWidget.builder(), new Actor(), new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getCells().size);
    }

    @Test
    public void testAppendWithMergedCellSettingsAndCellWidgets() {
        StandardTableBuilder builder = new StandardTableBuilder();
        builder.append(CellWidget.builder(), CellWidget.wrap(new Actor()), CellWidget.wrap(new Actor())).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getCells().size);
    }

    @Test
    public void testBuilderWithDefaultPaddingConstructor() {
        Padding pad = new Padding(2f);
        StandardTableBuilder builder = new StandardTableBuilder(pad);
        builder.append(new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testBuilderWithEstimatedAmountsConstructor() {
        StandardTableBuilder builder = new StandardTableBuilder(20, 5);
        builder.append(new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testBuilderWithEstimatedAmountsAndPaddingConstructor() {
        StandardTableBuilder builder = new StandardTableBuilder(20, 5, Padding.PAD_4);
        builder.append(new Actor()).row();
        Table table = builder.build();
        Assert.assertNotNull(table);
        Assert.assertEquals(1, table.getChildren().size);
    }

    @Test
    public void testChainingAppendAndRow() {
        StandardTableBuilder builder = new StandardTableBuilder();
        Table table = builder
                .append(new Actor())
                .append(new Actor())
                .row()
                .append(new Actor())
                .row()
                .build();
        Assert.assertNotNull(table);
        Assert.assertEquals(3, table.getChildren().size);
    }
}
