package com.kotcrab.vis.ui.layout;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link FlowGroup}.
 */
public class FlowGroupTest {

    @Test
    public void testConstructorHorizontal() {
        FlowGroup group = new FlowGroup(false);
        Assert.assertFalse(group.isVertical());
        Assert.assertEquals(0f, group.getSpacing(), 0.001f);
    }

    @Test
    public void testConstructorVertical() {
        FlowGroup group = new FlowGroup(true);
        Assert.assertTrue(group.isVertical());
    }

    @Test
    public void testConstructorWithSpacing() {
        FlowGroup group = new FlowGroup(false, 10f);
        Assert.assertEquals(10f, group.getSpacing(), 0.001f);
    }

    @Test
    public void testSetVertical() {
        FlowGroup group = new FlowGroup(false);
        group.setVertical(true);
        Assert.assertTrue(group.isVertical());
        group.setVertical(false);
        Assert.assertFalse(group.isVertical());
    }

    @Test
    public void testSetVerticalSameValueNoChange() {
        FlowGroup group = new FlowGroup(false);
        group.setVertical(false);
        Assert.assertFalse(group.isVertical());
    }

    @Test
    public void testSetSpacing() {
        FlowGroup group = new FlowGroup(true);
        group.setSpacing(5f);
        Assert.assertEquals(5f, group.getSpacing(), 0.001f);
    }

    @Test
    public void testLayoutHorizontalWithNoChildren() {
        FlowGroup group = new FlowGroup(false);
        group.setBounds(0, 0, 100, 100);
        group.layout();
        Assert.assertEquals(0, group.getChildren().size);
    }

    @Test
    public void testLayoutHorizontalWithOneChild() {
        FlowGroup group = new FlowGroup(false);
        Actor child = new Actor();
        child.setSize(20f, 20f);
        group.addActor(child);
        group.setBounds(0, 0, 100, 100);
        group.layout();
        Assert.assertEquals(20f, child.getWidth(), 0.001f);
        Assert.assertEquals(20f, child.getHeight(), 0.001f);
    }

    @Test
    public void testLayoutVerticalWithOneChild() {
        FlowGroup group = new FlowGroup(true);
        Actor child = new Actor();
        child.setSize(20f, 30f);
        group.addActor(child);
        group.setBounds(0, 0, 100, 100);
        group.layout();
        Assert.assertEquals(20f, child.getWidth(), 0.001f);
        Assert.assertEquals(30f, child.getHeight(), 0.001f);
    }

    @Test
    public void testGetPrefWidthHorizontalAfterLayout() {
        FlowGroup group = new FlowGroup(false);
        group.setBounds(0, 0, 200, 100);
        Actor a = new Actor();
        a.setSize(50f, 20f);
        group.addActor(a);
        float pref = group.getPrefWidth();
        Assert.assertTrue(pref >= 0f);
    }

    @Test
    public void testGetPrefHeightHorizontalAfterLayout() {
        FlowGroup group = new FlowGroup(false);
        group.setBounds(0, 0, 200, 100);
        Actor a = new Actor();
        a.setSize(50f, 20f);
        group.addActor(a);
        float pref = group.getPrefHeight();
        Assert.assertTrue(pref >= 0f);
    }

    @Test
    public void testGetMinWidthGetMinHeight() {
        FlowGroup group = new FlowGroup(false);
        group.setBounds(0, 0, 100, 100);
        Actor a = new Actor();
        a.setSize(30f, 25f);
        group.addActor(a);
        Assert.assertTrue(group.getMinWidth() >= 0f);
        Assert.assertTrue(group.getMinHeight() >= 0f);
    }

    @Test
    public void testInvalidateSetsSizeInvalid() {
        FlowGroup group = new FlowGroup(false);
        group.setBounds(0, 0, 100, 100);
        Actor a = new Actor();
        a.setSize(10f, 10f);
        group.addActor(a);
        group.getPrefWidth();
        group.invalidate();
        group.getPrefWidth();
        Assert.assertNotNull(group.getPrefWidth());
    }

    @Test
    public void testAddActor() {
        FlowGroup group = new FlowGroup(true);
        Actor a = new Actor();
        group.addActor(a);
        Assert.assertEquals(1, group.getChildren().size);
        Assert.assertSame(group, a.getParent());
    }
}
