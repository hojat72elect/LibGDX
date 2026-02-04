package com.kotcrab.vis.ui.layout;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link HorizontalFlowGroup}.
 */
public class HorizontalFlowGroupTest {

    @Test
    public void testDefaultConstructor() {
        HorizontalFlowGroup group = new HorizontalFlowGroup();
        Assert.assertEquals(0f, group.getSpacing(), 0.001f);
        Assert.assertEquals(0, group.getChildren().size);
    }

    @Test
    public void testConstructorWithSpacing() {
        HorizontalFlowGroup group = new HorizontalFlowGroup(10f);
        Assert.assertEquals(10f, group.getSpacing(), 0.001f);
    }

    @Test
    public void testSetSpacingGetSpacing() {
        HorizontalFlowGroup group = new HorizontalFlowGroup();
        group.setSpacing(6f);
        Assert.assertEquals(6f, group.getSpacing(), 0.001f);
    }

    @Test
    public void testLayoutWithNoChildren() {
        HorizontalFlowGroup group = new HorizontalFlowGroup();
        group.setBounds(0, 0, 100, 100);
        group.layout();
        Assert.assertEquals(0, group.getChildren().size);
    }

    @Test
    public void testLayoutWithOneChild() {
        HorizontalFlowGroup group = new HorizontalFlowGroup();
        Actor child = new Actor();
        child.setSize(30f, 20f);
        group.addActor(child);
        group.setBounds(0, 0, 100, 100);
        group.layout();
        Assert.assertEquals(30f, child.getWidth(), 0.001f);
        Assert.assertEquals(20f, child.getHeight(), 0.001f);
    }

    @Test
    public void testLayoutWithMultipleChildren() {
        HorizontalFlowGroup group = new HorizontalFlowGroup(5f);
        Actor a1 = new Actor();
        a1.setSize(40f, 25f);
        Actor a2 = new Actor();
        a2.setSize(40f, 25f);
        group.addActor(a1);
        group.addActor(a2);
        group.setBounds(0, 0, 100, 100);
        group.layout();
        Assert.assertEquals(2, group.getChildren().size);
        Assert.assertEquals(40f, a1.getWidth(), 0.001f);
        Assert.assertEquals(40f, a2.getWidth(), 0.001f);
    }

    @Test
    public void testGetPrefWidthWithNoChildren() {
        HorizontalFlowGroup group = new HorizontalFlowGroup();
        group.setBounds(0, 0, 100, 100);
        float pref = group.getPrefWidth();
        Assert.assertTrue(pref >= 0f);
    }

    @Test
    public void testGetPrefHeightWithChildren() {
        HorizontalFlowGroup group = new HorizontalFlowGroup();
        Actor a = new Actor();
        a.setSize(20f, 30f);
        group.addActor(a);
        group.setBounds(0, 0, 100, 100);
        float pref = group.getPrefHeight();
        Assert.assertTrue(pref >= 0f);
    }

    @Test
    public void testInvalidate() {
        HorizontalFlowGroup group = new HorizontalFlowGroup();
        group.addActor(new Actor());
        group.setBounds(0, 0, 100, 100);
        group.getPrefHeight();
        group.invalidate();
        group.getPrefHeight();
        Assert.assertTrue(group.getPrefHeight() >= 0f);
    }

    @Test
    public void testAddActor() {
        HorizontalFlowGroup group = new HorizontalFlowGroup();
        Actor a = new Actor();
        group.addActor(a);
        Assert.assertEquals(1, group.getChildren().size);
        Assert.assertSame(group, a.getParent());
    }
}
