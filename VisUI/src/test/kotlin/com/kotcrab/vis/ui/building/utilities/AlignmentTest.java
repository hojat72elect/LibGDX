package com.kotcrab.vis.ui.building.utilities;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.utils.Align;
import org.junit.Assert;
import org.junit.Test;

public class AlignmentTest {
    @Test
    public void testGetByIndex() {
        Assert.assertEquals(Alignment.CENTER, Alignment.getByIndex(0));
        Assert.assertEquals(Alignment.TOP, Alignment.getByIndex(1));
        Assert.assertEquals(Alignment.BOTTOM, Alignment.getByIndex(2));
        Assert.assertEquals(Alignment.LEFT, Alignment.getByIndex(3));
        Assert.assertEquals(Alignment.RIGHT, Alignment.getByIndex(4));
        Assert.assertEquals(Alignment.TOP_LEFT, Alignment.getByIndex(5));
        Assert.assertEquals(Alignment.TOP_RIGHT, Alignment.getByIndex(6));
        Assert.assertEquals(Alignment.BOTTOM_LEFT, Alignment.getByIndex(7));
        Assert.assertEquals(Alignment.BOTTOM_RIGHT, Alignment.getByIndex(8));

        Assert.assertNull(Alignment.getByIndex(-1));
        Assert.assertNull(Alignment.getByIndex(9));
    }

    @Test
    public void testGetByValidIndex() {
        Assert.assertEquals(Alignment.CENTER, Alignment.getByValidIndex(0));
        Assert.assertEquals(Alignment.BOTTOM_RIGHT, Alignment.getByValidIndex(8));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetByValidIndexInvalid() {
        Alignment.getByValidIndex(9);
    }

    @Test
    public void testIsIndexValid() {
        Assert.assertTrue(Alignment.isIndexValid(0));
        Assert.assertTrue(Alignment.isIndexValid(8));
        Assert.assertFalse(Alignment.isIndexValid(-1));
        Assert.assertFalse(Alignment.isIndexValid(9));
    }

    @Test
    public void testIsIndexLast() {
        Assert.assertFalse(Alignment.isIndexLast(0));
        Assert.assertTrue(Alignment.isIndexLast(8));
        Assert.assertFalse(Alignment.isIndexLast(9));
    }

    @Test
    public void testGetAlignment() {
        Assert.assertEquals(Align.center, Alignment.CENTER.getAlignment());
        Assert.assertEquals(Align.top, Alignment.TOP.getAlignment());
        Assert.assertEquals(Align.bottom, Alignment.BOTTOM.getAlignment());
        Assert.assertEquals(Align.left, Alignment.LEFT.getAlignment());
        Assert.assertEquals(Align.right, Alignment.RIGHT.getAlignment());
        Assert.assertEquals(Align.topLeft, Alignment.TOP_LEFT.getAlignment());
        Assert.assertEquals(Align.topRight, Alignment.TOP_RIGHT.getAlignment());
        Assert.assertEquals(Align.bottomLeft, Alignment.BOTTOM_LEFT.getAlignment());
        Assert.assertEquals(Align.bottomRight, Alignment.BOTTOM_RIGHT.getAlignment());
    }

    @Test
    public void testApply() {
        com.badlogic.gdx.Gdx.files = (com.badlogic.gdx.Files) java.lang.reflect.Proxy.newProxyInstance(
                com.badlogic.gdx.Files.class.getClassLoader(),
                new Class[]{com.badlogic.gdx.Files.class},
                new java.lang.reflect.InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args)
                            throws Throwable {
                        return null;
                    }
                });
        TestCell cell = new TestCell();
        Alignment.TOP_LEFT.apply(cell);
        Assert.assertEquals(Align.topLeft, cell.getAlignValue());
    }

    @Test
    public void testIsAlignedWithTop() {
        Assert.assertTrue(Alignment.TOP.isAlignedWithTop());
        Assert.assertTrue(Alignment.TOP_LEFT.isAlignedWithTop());
        Assert.assertTrue(Alignment.TOP_RIGHT.isAlignedWithTop());

        Assert.assertFalse(Alignment.CENTER.isAlignedWithTop());
        Assert.assertFalse(Alignment.BOTTOM.isAlignedWithTop());
        Assert.assertFalse(Alignment.LEFT.isAlignedWithTop());
        Assert.assertFalse(Alignment.RIGHT.isAlignedWithTop());
        Assert.assertFalse(Alignment.BOTTOM_LEFT.isAlignedWithTop());
        Assert.assertFalse(Alignment.BOTTOM_RIGHT.isAlignedWithTop());
    }

    @Test
    public void testIsAlignedWithBottom() {
        Assert.assertTrue(Alignment.BOTTOM.isAlignedWithBottom());
        Assert.assertTrue(Alignment.BOTTOM_LEFT.isAlignedWithBottom());
        Assert.assertTrue(Alignment.BOTTOM_RIGHT.isAlignedWithBottom());

        Assert.assertFalse(Alignment.CENTER.isAlignedWithBottom());
        Assert.assertFalse(Alignment.TOP.isAlignedWithBottom());
        Assert.assertFalse(Alignment.LEFT.isAlignedWithBottom());
        Assert.assertFalse(Alignment.RIGHT.isAlignedWithBottom());
        Assert.assertFalse(Alignment.TOP_LEFT.isAlignedWithBottom());
        Assert.assertFalse(Alignment.TOP_RIGHT.isAlignedWithBottom());
    }

    @Test
    public void testIsAlignedWithLeft() {
        Assert.assertTrue(Alignment.LEFT.isAlignedWithLeft());
        Assert.assertTrue(Alignment.TOP_LEFT.isAlignedWithLeft());
        Assert.assertTrue(Alignment.BOTTOM_LEFT.isAlignedWithLeft());

        Assert.assertFalse(Alignment.CENTER.isAlignedWithLeft());
        Assert.assertFalse(Alignment.RIGHT.isAlignedWithLeft());
        Assert.assertFalse(Alignment.TOP.isAlignedWithLeft());
        Assert.assertFalse(Alignment.BOTTOM.isAlignedWithLeft());
        Assert.assertFalse(Alignment.TOP_RIGHT.isAlignedWithLeft());
        Assert.assertFalse(Alignment.BOTTOM_RIGHT.isAlignedWithLeft());
    }

    @Test
    public void testIsAlignedWithRight() {
        Assert.assertTrue(Alignment.RIGHT.isAlignedWithRight());
        Assert.assertTrue(Alignment.TOP_RIGHT.isAlignedWithRight());
        Assert.assertTrue(Alignment.BOTTOM_RIGHT.isAlignedWithRight());

        Assert.assertFalse(Alignment.CENTER.isAlignedWithRight());
        Assert.assertFalse(Alignment.LEFT.isAlignedWithRight());
        Assert.assertFalse(Alignment.TOP.isAlignedWithRight());
        Assert.assertFalse(Alignment.BOTTOM.isAlignedWithRight());
        Assert.assertFalse(Alignment.TOP_LEFT.isAlignedWithRight());
        Assert.assertFalse(Alignment.BOTTOM_LEFT.isAlignedWithRight());
    }

    @Test
    public void testIsCentered() {
        Assert.assertTrue(Alignment.CENTER.isCentered());

        Assert.assertFalse(Alignment.TOP.isCentered());
        Assert.assertFalse(Alignment.BOTTOM.isCentered());
        Assert.assertFalse(Alignment.LEFT.isCentered());
        Assert.assertFalse(Alignment.RIGHT.isCentered());
        Assert.assertFalse(Alignment.TOP_LEFT.isCentered());
        Assert.assertFalse(Alignment.TOP_RIGHT.isCentered());
        Assert.assertFalse(Alignment.BOTTOM_LEFT.isCentered());
        Assert.assertFalse(Alignment.BOTTOM_RIGHT.isCentered());
    }

    private static class TestCell extends Cell<Actor> {
        private int alignValue;

        @Override
        public Cell<Actor> align(int align) {
            this.alignValue = align;
            return this;
        }

        public int getAlignValue() {
            return alignValue;
        }
    }
}
