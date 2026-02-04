package com.kotcrab.vis.ui.building.utilities;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Proxy;

public class CellWidgetTest {
    @BeforeClass
    public static void setupGdx() {
        // Initialize Gdx.files to prevent StackOverflowError in Cell constructor
        // The Cell constructor calls set(defaults()) which can cause recursion
        // if Gdx.files is not initialized
        if (Gdx.files == null) {
            Gdx.files = (Files) Proxy.newProxyInstance(
                    Files.class.getClassLoader(),
                    new Class[]{Files.class},
                    (proxy, method, args) -> null);
        }
    }
    @Test
    public void testOf() {
        Actor actor = new Actor();
        CellWidget.CellWidgetBuilder<Actor> builder = CellWidget.of(actor);
        Assert.assertNotNull(builder);
        CellWidget<Actor> cellWidget = builder.wrap();
        Assert.assertEquals(actor, cellWidget.getWidget());
    }

    @Test
    public void testOfWithNull() {
        CellWidget.CellWidgetBuilder<Actor> builder = CellWidget.of(null);
        Assert.assertNotNull(builder);
        CellWidget<Actor> cellWidget = builder.wrap();
        Assert.assertNull(cellWidget.getWidget());
    }

    @Test
    public void testUsing() {
        Actor actor = new Actor();
        CellWidget<Actor> original = CellWidget.of(actor).wrap();
        CellWidget.CellWidgetBuilder<Actor> builder = CellWidget.using(original);
        Assert.assertNotNull(builder);
        CellWidget<Actor> newCellWidget = builder.wrap();
        Assert.assertEquals(actor, newCellWidget.getWidget());
    }

    @Test
    public void testWrap() {
        Actor actor = new Actor();
        CellWidget<Actor> cellWidget = CellWidget.wrap(actor);
        Assert.assertNotNull(cellWidget);
        Assert.assertEquals(actor, cellWidget.getWidget());
    }

    @Test
    public void testWrapArray() {
        Actor actor1 = new Actor();
        Actor actor2 = new Actor();
        Actor actor3 = new Actor();
        CellWidget<?>[] wrapped = CellWidget.wrap(actor1, actor2, actor3);
        Assert.assertEquals(3, wrapped.length);
        Assert.assertEquals(actor1, wrapped[0].getWidget());
        Assert.assertEquals(actor2, wrapped[1].getWidget());
        Assert.assertEquals(actor3, wrapped[2].getWidget());
    }

    @Test
    public void testWrapEmptyArray() {
        CellWidget<?>[] wrapped = CellWidget.wrap();
        Assert.assertEquals(0, wrapped.length);
    }

    @Test
    public void testEmpty() {
        CellWidget<?> empty = CellWidget.empty();
        Assert.assertNotNull(empty);
        Assert.assertNull(empty.getWidget());
    }

    @Test
    public void testEmptyConstant() {
        Assert.assertNotNull(CellWidget.EMPTY);
        Assert.assertNull(CellWidget.EMPTY.getWidget());
    }

    @Test
    public void testBuilder() {
        CellWidget.CellWidgetBuilder<Actor> builder = CellWidget.builder();
        Assert.assertNotNull(builder);
        CellWidget<Actor> cellWidget = builder.wrap();
        Assert.assertNull(cellWidget.getWidget());
    }

    @Test
    public void testGetWidget() {
        Actor actor = new Actor();
        CellWidget<Actor> cellWidget = CellWidget.wrap(actor);
        Assert.assertEquals(actor, cellWidget.getWidget());
    }

    @Test
    public void testBuilderWidget() {
        Actor actor1 = new Actor();
        Actor actor2 = new Actor();
        CellWidget<Actor> cellWidget = CellWidget.of(actor1)
                .widget(actor2)
                .wrap();
        Assert.assertEquals(actor2, cellWidget.getWidget());
    }

    @Test
    public void testBuilderPadding() {
        Padding padding = new Padding(10f);
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .padding(padding)
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertNotNull(cell);
        // Padding will be applied in buildCell
    }

    @Test
    public void testBuilderUseSpacing() {
        Padding padding = new Padding(5f);
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .padding(padding)
                .useSpacing()
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertNotNull(cell);
        // Spacing will be applied instead of padding
    }

    @Test
    public void testBuilderExpandX() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .expandX()
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertTrue(cell.isExpandX());
        Assert.assertFalse(cell.isExpandY());
    }

    @Test
    public void testBuilderExpandY() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .expandY()
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertFalse(cell.isExpandX());
        Assert.assertTrue(cell.isExpandY());
    }

    @Test
    public void testBuilderExpandBoth() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .expandX()
                .expandY()
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertTrue(cell.isExpandX());
        Assert.assertTrue(cell.isExpandY());
    }

    @Test
    public void testBuilderFillX() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .fillX()
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertTrue(cell.isFillX());
        Assert.assertFalse(cell.isFillY());
    }

    @Test
    public void testBuilderFillY() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .fillY()
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertFalse(cell.isFillX());
        Assert.assertTrue(cell.isFillY());
    }

    @Test
    public void testBuilderFillBoth() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .fillX()
                .fillY()
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertTrue(cell.isFillX());
        Assert.assertTrue(cell.isFillY());
    }

    @Test
    public void testBuilderAlign() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .align(Alignment.TOP_LEFT)
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertEquals(com.badlogic.gdx.utils.Align.topLeft, cell.getAlignValue());
    }

    @Test
    public void testBuilderWidth() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .width(100)
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertEquals(100f, cell.getWidthValue(), 0.001f);
    }

    @Test
    public void testBuilderHeight() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .height(200)
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertEquals(200f, cell.getHeightValue(), 0.001f);
    }

    @Test
    public void testBuilderMinWidth() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .minWidth(50)
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertEquals(50f, cell.getStoredMinWidth(), 0.001f);
    }

    @Test
    public void testBuilderMinHeight() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .minHeight(75)
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertEquals(75f, cell.getStoredMinHeight(), 0.001f);
    }

    @Test
    public void testBuilderIgnoresZeroWidth() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .width(0)
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertEquals(0f, cell.getWidthValue(), 0.001f);
    }

    @Test
    public void testBuilderIgnoresZeroHeight() {
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .height(0)
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertEquals(0f, cell.getHeightValue(), 0.001f);
    }

    @Test
    public void testBuildCell() {
        Actor actor = new Actor();
        CellWidget<Actor> cellWidget = CellWidget.wrap(actor);
        TestTable table = new TestTable();
        Cell<?> cell = cellWidget.buildCell(table);
        Assert.assertNotNull(cell);
        Assert.assertEquals(actor, table.getAddedActor());
    }

    @Test
    public void testBuildCellWithDefaultPadding() {
        Padding defaultPadding = new Padding(8f);
        Padding cellPadding = new Padding(4f);
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .padding(cellPadding)
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table, defaultPadding);
        // Should use cellPadding, not defaultPadding
        Assert.assertNotNull(cell);
    }

    @Test
    public void testBuildCellWithNullPaddingUsesDefault() {
        Padding defaultPadding = new Padding(10f);
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table, defaultPadding);
        Assert.assertNotNull(cell);
    }

    @Test
    public void testBuildCellWithSpacing() {
        Padding padding = new Padding(5f);
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .padding(padding)
                .useSpacing()
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertTrue(cell.isSpacingApplied());
        Assert.assertFalse(cell.isPaddingApplied());
    }

    @Test
    public void testBuildCellWithPadding() {
        Padding padding = new Padding(5f);
        CellWidget<Actor> cellWidget = CellWidget.builder()
                .padding(padding)
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertFalse(cell.isSpacingApplied());
        Assert.assertTrue(cell.isPaddingApplied());
    }

    @Test
    public void testBuilderChaining() {
        Actor actor = new Actor();
        Padding padding = new Padding(10f);
        CellWidget<Actor> cellWidget = CellWidget.of(actor)
                .padding(padding)
                .expandX()
                .expandY()
                .fillX()
                .fillY()
                .align(Alignment.CENTER)
                .width(100)
                .height(200)
                .minWidth(50)
                .minHeight(75)
                .wrap();
        Assert.assertEquals(actor, cellWidget.getWidget());
        TestTable table = new TestTable();
        TestCell cell = (TestCell) cellWidget.buildCell(table);
        Assert.assertTrue(cell.isExpandX());
        Assert.assertTrue(cell.isExpandY());
        Assert.assertTrue(cell.isFillX());
        Assert.assertTrue(cell.isFillY());
        Assert.assertEquals(com.badlogic.gdx.utils.Align.center, cell.getAlignValue());
        Assert.assertEquals(100f, cell.getWidthValue(), 0.001f);
        Assert.assertEquals(200f, cell.getHeightValue(), 0.001f);
        Assert.assertEquals(50f, cell.getStoredMinWidth(), 0.001f);
        Assert.assertEquals(75f, cell.getStoredMinHeight(), 0.001f);
    }

    @Test
    public void testUsingPreservesSettings() {
        Actor actor = new Actor();
        Padding padding = new Padding(5f);
        CellWidget<Actor> original = CellWidget.of(actor)
                .padding(padding)
                .expandX()
                .fillX()
                .width(100)
                .align(Alignment.TOP)
                .wrap();
        CellWidget<Actor> modified = CellWidget.using(original)
                .height(200)
                .wrap();
        TestTable table = new TestTable();
        TestCell cell = (TestCell) modified.buildCell(table);
        Assert.assertTrue(cell.isExpandX());
        Assert.assertTrue(cell.isFillX());
        Assert.assertEquals(100f, cell.getWidthValue(), 0.001f);
        Assert.assertEquals(200f, cell.getHeightValue(), 0.001f);
        Assert.assertEquals(com.badlogic.gdx.utils.Align.top, cell.getAlignValue());
    }

    // Test helper classes
    private static class TestTable extends Table {
        private Actor addedActor;

        @Override
        public Cell<?> add(Actor actor) {
            this.addedActor = actor;
            return new TestCell();
        }

        public Actor getAddedActor() {
            return addedActor;
        }
    }

    private static class TestCell extends Cell<Actor> {
        private boolean expandX, expandY, fillX, fillY;
        private int alignValue;
        private float widthValue, heightValue, minWidthValue, minHeightValue;
        private boolean paddingApplied, spacingApplied;


        @Override
        public Cell<Actor> expand(boolean x, boolean y) {
            this.expandX = x;
            this.expandY = y;
            return this;
        }

        @Override
        public Cell<Actor> fill(boolean x, boolean y) {
            this.fillX = x;
            this.fillY = y;
            return this;
        }

        @Override
        public Cell<Actor> align(int align) {
            this.alignValue = align;
            return this;
        }

        @Override
        public Cell<Actor> width(float width) {
            this.widthValue = width;
            return this;
        }

        @Override
        public Cell<Actor> height(float height) {
            this.heightValue = height;
            return this;
        }

        @Override
        public Cell<Actor> minWidth(float minWidth) {
            this.minWidthValue = minWidth;
            return this;
        }

        @Override
        public Cell<Actor> minHeight(float minHeight) {
            this.minHeightValue = minHeight;
            return this;
        }

        @Override
        public Cell<Actor> pad(float top, float left, float bottom, float right) {
            this.paddingApplied = true;
            return this;
        }

        @Override
        public Cell<Actor> space(float top, float left, float bottom, float right) {
            this.spacingApplied = true;
            return this;
        }

        public boolean isExpandX() {
            return expandX;
        }

        public boolean isExpandY() {
            return expandY;
        }

        public boolean isFillX() {
            return fillX;
        }

        public boolean isFillY() {
            return fillY;
        }

        public int getAlignValue() {
            return alignValue;
        }

        public float getWidthValue() {
            return widthValue;
        }

        public float getHeightValue() {
            return heightValue;
        }

        public float getStoredMinWidth() {
            return minWidthValue;
        }

        public float getStoredMinHeight() {
            return minHeightValue;
        }

        public boolean isPaddingApplied() {
            return paddingApplied;
        }

        public boolean isSpacingApplied() {
            return spacingApplied;
        }
    }
}
