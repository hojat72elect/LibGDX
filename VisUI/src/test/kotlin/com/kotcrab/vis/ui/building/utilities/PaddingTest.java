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

public class PaddingTest {
    private static final float DELTA = 0.001f;

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
    public void testSingleValueConstructor() {
        Padding padding = new Padding(10f);
        Assert.assertEquals(10f, padding.top(), DELTA);
        Assert.assertEquals(10f, padding.left(), DELTA);
        Assert.assertEquals(10f, padding.bottom(), DELTA);
        Assert.assertEquals(10f, padding.right(), DELTA);
    }

    @Test
    public void testTwoValueConstructor() {
        Padding padding = new Padding(5f, 10f);
        Assert.assertEquals(10f, padding.top(), DELTA);
        Assert.assertEquals(5f, padding.left(), DELTA);
        Assert.assertEquals(10f, padding.bottom(), DELTA);
        Assert.assertEquals(5f, padding.right(), DELTA);
    }

    @Test
    public void testFourValueConstructor() {
        Padding padding = new Padding(1f, 2f, 3f, 4f);
        Assert.assertEquals(1f, padding.top(), DELTA);
        Assert.assertEquals(2f, padding.left(), DELTA);
        Assert.assertEquals(3f, padding.bottom(), DELTA);
        Assert.assertEquals(4f, padding.right(), DELTA);
    }

    @Test
    public void testOfSingleValue() {
        Padding padding = Padding.of(15f);
        Assert.assertEquals(15f, padding.top(), DELTA);
        Assert.assertEquals(15f, padding.left(), DELTA);
        Assert.assertEquals(15f, padding.bottom(), DELTA);
        Assert.assertEquals(15f, padding.right(), DELTA);
    }

    @Test
    public void testOfTwoValues() {
        Padding padding = Padding.of(8f, 12f);
        Assert.assertEquals(12f, padding.top(), DELTA);
        Assert.assertEquals(8f, padding.left(), DELTA);
        Assert.assertEquals(12f, padding.bottom(), DELTA);
        Assert.assertEquals(8f, padding.right(), DELTA);
    }

    @Test
    public void testOfFourValues() {
        Padding padding = Padding.of(1f, 2f, 3f, 4f);
        Assert.assertEquals(1f, padding.top(), DELTA);
        Assert.assertEquals(2f, padding.left(), DELTA);
        Assert.assertEquals(3f, padding.bottom(), DELTA);
        Assert.assertEquals(4f, padding.right(), DELTA);
    }

    @Test
    public void testAdd() {
        Padding p1 = new Padding(1f, 2f, 3f, 4f);
        Padding p2 = new Padding(10f, 20f, 30f, 40f);
        Padding result = p1.add(p2);
        Assert.assertEquals(11f, result.top(), DELTA);
        Assert.assertEquals(22f, result.left(), DELTA);
        Assert.assertEquals(33f, result.bottom(), DELTA);
        Assert.assertEquals(44f, result.right(), DELTA);
    }

    @Test
    public void testSubtract() {
        Padding p1 = new Padding(10f, 20f, 30f, 40f);
        Padding p2 = new Padding(1f, 2f, 3f, 4f);
        Padding result = p1.subtract(p2);
        Assert.assertEquals(9f, result.top(), DELTA);
        Assert.assertEquals(18f, result.left(), DELTA);
        Assert.assertEquals(27f, result.bottom(), DELTA);
        Assert.assertEquals(36f, result.right(), DELTA);
    }

    @Test
    public void testReverse() {
        Padding padding = new Padding(1f, 2f, 3f, 4f);
        Padding reversed = padding.reverse();
        Assert.assertEquals(-1f, reversed.top(), DELTA);
        Assert.assertEquals(-2f, reversed.left(), DELTA);
        Assert.assertEquals(-3f, reversed.bottom(), DELTA);
        Assert.assertEquals(-4f, reversed.right(), DELTA);
    }

    @Test
    public void testPredefinedConstants() {
        Assert.assertEquals(0f, Padding.PAD_0.top(), DELTA);
        Assert.assertEquals(2f, Padding.PAD_2.top(), DELTA);
        Assert.assertEquals(4f, Padding.PAD_4.top(), DELTA);
        Assert.assertEquals(8f, Padding.PAD_8.top(), DELTA);
    }

    @Test
    public void testEquals() {
        Padding p1 = new Padding(1f, 2f, 3f, 4f);
        Padding p2 = new Padding(1f, 2f, 3f, 4f);
        Padding p3 = new Padding(5f, 6f, 7f, 8f);
        Assert.assertEquals(p1, p2);
        Assert.assertNotEquals(p1, p3);
    }

    @Test
    public void testApplyPaddingToTable() {
        Padding padding = new Padding(10f, 20f, 30f, 40f);
        TestTable table = new TestTable();
        Table result = padding.applyPadding(table);
        Assert.assertEquals(table, result);
        Assert.assertEquals(10f, table.getTopPad(), DELTA);
        Assert.assertEquals(20f, table.getLeftPad(), DELTA);
        Assert.assertEquals(30f, table.getBottomPad(), DELTA);
        Assert.assertEquals(40f, table.getRightPad(), DELTA);
    }

    @Test
    public void testApplyPaddingToCell() {
        Padding padding = new Padding(5f, 15f, 25f, 35f);
        TestCell cell = new TestCell();
        Cell<?> result = padding.applyPadding(cell);
        Assert.assertEquals(cell, result);
        Assert.assertEquals(5f, cell.getTopPad(), DELTA);
        Assert.assertEquals(15f, cell.getLeftPad(), DELTA);
        Assert.assertEquals(25f, cell.getBottomPad(), DELTA);
        Assert.assertEquals(35f, cell.getRightPad(), DELTA);
    }

    @Test
    public void testApplySpacingToCell() {
        Padding spacing = new Padding(2f, 4f, 6f, 8f);
        TestCell cell = new TestCell();
        Cell<?> result = spacing.applySpacing(cell);
        Assert.assertEquals(cell, result);
        Assert.assertEquals(2f, cell.getTopSpace(), DELTA);
        Assert.assertEquals(4f, cell.getLeftSpace(), DELTA);
        Assert.assertEquals(6f, cell.getBottomSpace(), DELTA);
        Assert.assertEquals(8f, cell.getRightSpace(), DELTA);
    }

    @Test
    public void testSetPaddingToTable() {
        Padding padding = new Padding(12f, 22f, 32f, 42f);
        TestTable table = new TestTable();
        Table result = Padding.setPadding(padding, table);
        Assert.assertEquals(table, result);
        Assert.assertEquals(12f, table.getTopPad(), DELTA);
        Assert.assertEquals(22f, table.getLeftPad(), DELTA);
        Assert.assertEquals(32f, table.getBottomPad(), DELTA);
        Assert.assertEquals(42f, table.getRightPad(), DELTA);
    }

    @Test
    public void testSetPaddingToCell() {
        Padding padding = new Padding(7f, 17f, 27f, 37f);
        TestCell cell = new TestCell();
        Cell<?> result = Padding.setPadding(padding, cell);
        Assert.assertEquals(cell, result);
        Assert.assertEquals(7f, cell.getTopPad(), DELTA);
        Assert.assertEquals(17f, cell.getLeftPad(), DELTA);
        Assert.assertEquals(27f, cell.getBottomPad(), DELTA);
        Assert.assertEquals(37f, cell.getRightPad(), DELTA);
    }

    @Test
    public void testSetSpacingToCell() {
        Padding spacing = new Padding(3f, 5f, 7f, 9f);
        TestCell cell = new TestCell();
        Cell<?> result = Padding.setSpacing(spacing, cell);
        Assert.assertEquals(cell, result);
        Assert.assertEquals(3f, cell.getTopSpace(), DELTA);
        Assert.assertEquals(5f, cell.getLeftSpace(), DELTA);
        Assert.assertEquals(7f, cell.getBottomSpace(), DELTA);
        Assert.assertEquals(9f, cell.getRightSpace(), DELTA);
    }

    // Test helper classes
    private static class TestTable extends Table {
        private float topPad, leftPad, bottomPad, rightPad;

        @Override
        public Table pad(float top, float left, float bottom, float right) {
            this.topPad = top;
            this.leftPad = left;
            this.bottomPad = bottom;
            this.rightPad = right;
            return this;
        }

        public float getTopPad() {
            return topPad;
        }

        public float getLeftPad() {
            return leftPad;
        }

        public float getBottomPad() {
            return bottomPad;
        }

        public float getRightPad() {
            return rightPad;
        }
    }

    private static class TestCell extends Cell<Actor> {
        private float topPad, leftPad, bottomPad, rightPad;
        private float topSpace, leftSpace, bottomSpace, rightSpace;

        @Override
        public Cell<Actor> pad(float top, float left, float bottom, float right) {
            this.topPad = top;
            this.leftPad = left;
            this.bottomPad = bottom;
            this.rightPad = right;
            return this;
        }

        @Override
        public Cell<Actor> space(float top, float left, float bottom, float right) {
            this.topSpace = top;
            this.leftSpace = left;
            this.bottomSpace = bottom;
            this.rightSpace = right;
            return this;
        }

        public float getTopPad() {
            return topPad;
        }

        public float getLeftPad() {
            return leftPad;
        }

        public float getBottomPad() {
            return bottomPad;
        }

        public float getRightPad() {
            return rightPad;
        }

        public float getTopSpace() {
            return topSpace;
        }

        public float getLeftSpace() {
            return leftSpace;
        }

        public float getBottomSpace() {
            return bottomSpace;
        }

        public float getRightSpace() {
            return rightSpace;
        }
    }
}
