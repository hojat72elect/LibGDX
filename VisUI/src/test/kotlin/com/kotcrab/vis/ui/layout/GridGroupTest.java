package com.kotcrab.vis.ui.layout;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link GridGroup}.
 */
public class GridGroupTest {

    @Test
    public void testDefaultConstructor() {
        GridGroup group = new GridGroup();
        Assert.assertEquals(256f, group.getItemWidth(), 0.001f);
        Assert.assertEquals(256f, group.getItemHeight(), 0.001f);
        Assert.assertEquals(8f, group.getSpacing(), 0.001f);
    }

    @Test
    public void testConstructorWithItemSize() {
        GridGroup group = new GridGroup(64f);
        Assert.assertEquals(64f, group.getItemWidth(), 0.001f);
        Assert.assertEquals(64f, group.getItemHeight(), 0.001f);
        Assert.assertEquals(8f, group.getSpacing(), 0.001f);
    }

    @Test
    public void testConstructorWithItemSizeAndSpacing() {
        GridGroup group = new GridGroup(32f, 4f);
        Assert.assertEquals(32f, group.getItemWidth(), 0.001f);
        Assert.assertEquals(32f, group.getItemHeight(), 0.001f);
        Assert.assertEquals(4f, group.getSpacing(), 0.001f);
    }

    @Test
    public void testSetSpacingGetSpacing() {
        GridGroup group = new GridGroup();
        group.setSpacing(12f);
        Assert.assertEquals(12f, group.getSpacing(), 0.001f);
    }

    @Test
    public void testSetItemSizeSingleValue() {
        GridGroup group = new GridGroup();
        group.setItemSize(100f);
        Assert.assertEquals(100f, group.getItemWidth(), 0.001f);
        Assert.assertEquals(100f, group.getItemHeight(), 0.001f);
    }

    @Test
    public void testSetItemSizeWidthHeight() {
        GridGroup group = new GridGroup();
        group.setItemSize(80f, 60f);
        Assert.assertEquals(80f, group.getItemWidth(), 0.001f);
        Assert.assertEquals(60f, group.getItemHeight(), 0.001f);
    }

    @Test
    public void testSetItemWidthSetItemHeight() {
        GridGroup group = new GridGroup();
        group.setItemWidth(70f);
        group.setItemHeight(90f);
        Assert.assertEquals(70f, group.getItemWidth(), 0.001f);
        Assert.assertEquals(90f, group.getItemHeight(), 0.001f);
    }

    @Test
    public void testLayoutWithNoChildren() {
        GridGroup group = new GridGroup(32f, 4f);
        group.setBounds(0, 0, 200, 200);
        group.layout();
        Assert.assertEquals(0, group.getChildren().size);
    }

    @Test
    public void testLayoutWithOneChild() {
        GridGroup group = new GridGroup(40f, 8f);
        Actor child = new Actor();
        group.addActor(child);
        group.setBounds(0, 0, 200, 200);
        group.layout();
        Assert.assertEquals(40f, child.getWidth(), 0.001f);
        Assert.assertEquals(40f, child.getHeight(), 0.001f);
    }

    @Test
    public void testLayoutWithMultipleChildren() {
        GridGroup group = new GridGroup(30f, 5f);
        for (int i = 0; i < 5; i++) {
            group.addActor(new Actor());
        }
        group.setBounds(0, 0, 100, 300);
        group.layout();
        Assert.assertEquals(5, group.getChildren().size);
        Actor first = group.getChildren().get(0);
        Assert.assertEquals(30f, first.getWidth(), 0.001f);
        Assert.assertEquals(30f, first.getHeight(), 0.001f);
    }

    @Test
    public void testGetPrefWidthGetPrefHeightWithNoChildren() {
        GridGroup group = new GridGroup(32f, 4f);
        group.setBounds(0, 0, 200, 200);
        Assert.assertEquals(0f, group.getPrefWidth(), 0.001f);
        Assert.assertEquals(0f, group.getPrefHeight(), 0.001f);
    }

    @Test
    public void testGetPrefWidthWithChildren() {
        GridGroup group = new GridGroup(50f, 8f);
        group.addActor(new Actor());
        group.setBounds(0, 0, 200, 200);
        float prefW = group.getPrefWidth();
        Assert.assertTrue(prefW >= 0f);
    }

    @Test
    public void testGetPrefHeightWithChildren() {
        GridGroup group = new GridGroup(50f, 8f);
        group.addActor(new Actor());
        group.setBounds(0, 0, 200, 200);
        float prefH = group.getPrefHeight();
        Assert.assertTrue(prefH >= 0f);
    }

    @Test
    public void testInvalidate() {
        GridGroup group = new GridGroup();
        group.addActor(new Actor());
        group.setBounds(0, 0, 200, 200);
        group.getPrefHeight();
        group.invalidate();
        group.getPrefHeight();
        Assert.assertTrue(group.getPrefHeight() >= 0f);
    }

    @Test
    public void testAddActor() {
        GridGroup group = new GridGroup();
        Actor a = new Actor();
        group.addActor(a);
        Assert.assertEquals(1, group.getChildren().size);
        Assert.assertSame(group, a.getParent());
    }
}
