package com.kotcrab.vis.ui.layout;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link VerticalFlowGroup}.
 */
public class VerticalFlowGroupTest {

    @Test
    public void testDefaultConstructor() {
        VerticalFlowGroup group = new VerticalFlowGroup();
        Assert.assertEquals(0f, group.getSpacing(), 0.001f);
        Assert.assertEquals(0, group.getChildren().size);
    }

    @Test
    public void testConstructorWithSpacing() {
        VerticalFlowGroup group = new VerticalFlowGroup(8f);
        Assert.assertEquals(8f, group.getSpacing(), 0.001f);
    }

    @Test
    public void testSetSpacingGetSpacing() {
        VerticalFlowGroup group = new VerticalFlowGroup();
        group.setSpacing(4f);
        Assert.assertEquals(4f, group.getSpacing(), 0.001f);
    }

    @Test
    public void testLayoutWithNoChildren() {
        VerticalFlowGroup group = new VerticalFlowGroup();
        group.setBounds(0, 0, 100, 100);
        group.layout();
        Assert.assertEquals(0, group.getChildren().size);
    }

    @Test
    public void testLayoutWithOneChild() {
        VerticalFlowGroup group = new VerticalFlowGroup();
        Actor child = new Actor();
        child.setSize(25f, 35f);
        group.addActor(child);
        group.setBounds(0, 0, 100, 100);
        group.layout();
        Assert.assertEquals(25f, child.getWidth(), 0.001f);
        Assert.assertEquals(35f, child.getHeight(), 0.001f);
    }

    @Test
    public void testLayoutWithMultipleChildren() {
        VerticalFlowGroup group = new VerticalFlowGroup(6f);
        Actor a1 = new Actor();
        a1.setSize(30f, 20f);
        Actor a2 = new Actor();
        a2.setSize(30f, 20f);
        group.addActor(a1);
        group.addActor(a2);
        group.setBounds(0, 0, 100, 100);
        group.layout();
        Assert.assertEquals(2, group.getChildren().size);
        Assert.assertEquals(30f, a1.getWidth(), 0.001f);
        Assert.assertEquals(20f, a1.getHeight(), 0.001f);
    }

    @Test
    public void testGetPrefWidthWithChildren() {
        VerticalFlowGroup group = new VerticalFlowGroup();
        Actor a = new Actor();
        a.setSize(40f, 30f);
        group.addActor(a);
        group.setBounds(0, 0, 100, 100);
        float pref = group.getPrefWidth();
        Assert.assertTrue(pref >= 0f);
    }

    @Test
    public void testGetPrefHeightWithNoChildren() {
        VerticalFlowGroup group = new VerticalFlowGroup();
        group.setBounds(0, 0, 100, 100);
        float pref = group.getPrefHeight();
        Assert.assertTrue(pref >= 0f);
    }

    @Test
    public void testInvalidate() {
        VerticalFlowGroup group = new VerticalFlowGroup();
        group.addActor(new Actor());
        group.setBounds(0, 0, 100, 100);
        group.getPrefWidth();
        group.invalidate();
        group.getPrefWidth();
        Assert.assertTrue(group.getPrefWidth() >= 0f);
    }

    @Test
    public void testAddActor() {
        VerticalFlowGroup group = new VerticalFlowGroup();
        Actor a = new Actor();
        group.addActor(a);
        Assert.assertEquals(1, group.getChildren().size);
        Assert.assertSame(group, a.getParent());
    }
}
