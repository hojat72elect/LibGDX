package com.kotcrab.vis.ui.layout;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link FloatingGroup}.
 */
public class FloatingGroupTest {

    @Test
    public void testDefaultConstructor() {
        FloatingGroup group = new FloatingGroup();
        Assert.assertFalse(group.isUseChildrenPreferredSize());
        Assert.assertEquals(0, group.getChildren().size);
    }

    @Test
    public void testConstructorWithPrefSize() {
        FloatingGroup group = new FloatingGroup(100f, 200f);
        Assert.assertEquals(100f, group.getPrefWidth(), 0.001f);
        Assert.assertEquals(200f, group.getPrefHeight(), 0.001f);
    }

    @Test
    public void testSetPrefWidthGetPrefWidth() {
        FloatingGroup group = new FloatingGroup();
        group.setPrefWidth(150f);
        Assert.assertEquals(150f, group.getPrefWidth(), 0.001f);
    }

    @Test
    public void testSetPrefHeightGetPrefHeight() {
        FloatingGroup group = new FloatingGroup();
        group.setPrefHeight(75f);
        Assert.assertEquals(75f, group.getPrefHeight(), 0.001f);
    }

    @Test
    public void testGetPrefWidthUsesGetWidthWhenNegative() {
        FloatingGroup group = new FloatingGroup();
        group.setWidth(300f);
        group.setPrefWidth(-1f);
        Assert.assertEquals(300f, group.getPrefWidth(), 0.001f);
    }

    @Test
    public void testGetPrefHeightUsesGetHeightWhenNegative() {
        FloatingGroup group = new FloatingGroup();
        group.setHeight(120f);
        group.setPrefHeight(-1f);
        Assert.assertEquals(120f, group.getPrefHeight(), 0.001f);
    }

    @Test
    public void testSetUseChildrenPreferredSize() {
        FloatingGroup group = new FloatingGroup();
        Assert.assertFalse(group.isUseChildrenPreferredSize());
        group.setUseChildrenPreferredSize(true);
        Assert.assertTrue(group.isUseChildrenPreferredSize());
    }

    @Test
    public void testLayoutDoesNothingWhenNotUseChildrenPreferredSize() {
        FloatingGroup group = new FloatingGroup();
        Actor child = new Actor();
        child.setSize(50f, 50f);
        group.addActor(child);
        group.setBounds(0, 0, 200, 200);
        group.layout();
        Assert.assertEquals(50f, child.getWidth(), 0.001f);
        Assert.assertEquals(50f, child.getHeight(), 0.001f);
    }

    @Test
    public void testLayoutWithUseChildrenPreferredSizeUsesChildBounds() {
        FloatingGroup group = new FloatingGroup();
        group.setUseChildrenPreferredSize(true);
        Actor child = new Actor();
        child.setPosition(10f, 20f);
        child.setSize(30f, 40f);
        group.addActor(child);
        group.setBounds(0, 0, 200, 200);
        group.layout();
        Assert.assertEquals(10f, child.getX(), 0.001f);
        Assert.assertEquals(20f, child.getY(), 0.001f);
        Assert.assertEquals(30f, child.getWidth(), 0.001f);
        Assert.assertEquals(40f, child.getHeight(), 0.001f);
    }

    @Test
    public void testLayoutWithLayoutChildUsesPreferredSize() {
        FloatingGroup group = new FloatingGroup();
        group.setUseChildrenPreferredSize(true);
        MockLayoutChild child = new MockLayoutChild(25f, 35f);
        child.setPosition(5f, 5f);
        group.addActor(child);
        group.setBounds(0, 0, 200, 200);
        group.layout();
        Assert.assertEquals(25f, child.getWidth(), 0.001f);
        Assert.assertEquals(35f, child.getHeight(), 0.001f);
    }

    @Test
    public void testAddActor() {
        FloatingGroup group = new FloatingGroup();
        Actor a = new Actor();
        group.addActor(a);
        Assert.assertEquals(1, group.getChildren().size);
        Assert.assertSame(group, a.getParent());
    }

    private static class MockLayoutChild extends Widget {
        private final float prefWidth;
        private final float prefHeight;

        MockLayoutChild(float prefWidth, float prefHeight) {
            this.prefWidth = prefWidth;
            this.prefHeight = prefHeight;
        }

        @Override
        public float getPrefWidth() {
            return prefWidth;
        }

        @Override
        public float getPrefHeight() {
            return prefHeight;
        }
    }
}
