package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link CollapsibleWidget}.
 */
public class CollapsibleWidgetTest {

    @Test
    public void testCollapsedAffectsPrefHeightAndTouchableWithoutAnimation() {
        Table table = new Table();
        table.setSize(100, 40);

        CollapsibleWidget widget = new CollapsibleWidget(table, false);
        widget.layout();

        Assert.assertFalse(widget.isCollapsed());
        Assert.assertEquals(Touchable.enabled, widget.getTouchable());
        Assert.assertEquals(table.getPrefHeight(), widget.getPrefHeight(), 0.0001f);

        widget.setCollapsed(true, false);

        Assert.assertTrue(widget.isCollapsed());
        Assert.assertEquals(Touchable.disabled, widget.getTouchable());
        Assert.assertEquals(0f, widget.getPrefHeight(), 0.0001f);

        widget.setCollapsed(false, false);

        Assert.assertFalse(widget.isCollapsed());
        Assert.assertEquals(Touchable.enabled, widget.getTouchable());
        Assert.assertEquals(table.getPrefHeight(), widget.getPrefHeight(), 0.0001f);
    }

    @Test
    public void testGetPrefWidthDelegatesToTable() {
        Table table = new Table();
        table.setSize(123, 30);

        CollapsibleWidget widget = new CollapsibleWidget(table, false);
        Assert.assertEquals(table.getPrefWidth(), widget.getPrefWidth(), 0.0001f);
    }

    @Test
    public void testSetTableReplacesChild() {
        Table first = new Table();
        first.setSize(50, 10);

        Table second = new Table();
        second.setSize(60, 20);

        CollapsibleWidget widget = new CollapsibleWidget(first, false);
        Assert.assertEquals(1, widget.getChildren().size);
        Assert.assertSame(first, widget.getChildren().first());

        widget.setTable(second);
        Assert.assertEquals(1, widget.getChildren().size);
        Assert.assertSame(second, widget.getChildren().first());
    }

    @Test(expected = GdxRuntimeException.class)
    public void testAddingMoreThanOneChildThrows() {
        CollapsibleWidget widget = new CollapsibleWidget(new Table(), false);
        widget.addActor(new Table());
    }

    @Test
    public void testDefaultConstructor() {
        CollapsibleWidget widget = new CollapsibleWidget();
        Assert.assertNull("Table should be null for default constructor", getPrivateField(widget, "table"));
        Assert.assertFalse("Widget should not be collapsed by default", widget.isCollapsed());
    }

    @Test
    public void testConstructorWithTable() {
        Table table = new Table();
        CollapsibleWidget widget = new CollapsibleWidget(table);
        Assert.assertFalse("Widget should not be collapsed by default", widget.isCollapsed());
        Assert.assertEquals("Widget should have one child", 1, widget.getChildren().size);
        Assert.assertSame("Child should be the provided table", table, widget.getChildren().first());
    }

    @Test
    public void testConstructorWithTableAndCollapsedState() {
        Table table = new Table();
        CollapsibleWidget widget = new CollapsibleWidget(table, true);
        Assert.assertTrue("Widget should be collapsed", widget.isCollapsed());
        Assert.assertEquals("Widget should have one child", 1, widget.getChildren().size);
        Assert.assertSame("Child should be the provided table", table, widget.getChildren().first());
    }

    @Test
    public void testSetCollapsedWithAnimation() {
        Table table = new Table();
        table.setSize(100, 40);

        CollapsibleWidget widget = new CollapsibleWidget(table, false);
        widget.setCollapsed(true, true); // with animation

        Assert.assertTrue("Widget should be collapsed", widget.isCollapsed());
        Assert.assertEquals("Widget should be disabled when collapsed", Touchable.disabled, widget.getTouchable());
    }

    @Test
    public void testSetCollapsedWithoutAnimation() {
        Table table = new Table();
        table.setSize(100, 40);

        CollapsibleWidget widget = new CollapsibleWidget(table, false);
        widget.setCollapsed(true, false); // without animation

        Assert.assertTrue("Widget should be collapsed", widget.isCollapsed());
        Assert.assertEquals("Widget should be disabled when collapsed", Touchable.disabled, widget.getTouchable());
    }

    @Test
    public void testSetCollapsedDefaultsToAnimation() {
        Table table = new Table();
        table.setSize(100, 40);

        CollapsibleWidget widget = new CollapsibleWidget(table, false);
        widget.setCollapsed(true); // defaults to with animation

        Assert.assertTrue("Widget should be collapsed", widget.isCollapsed());
        Assert.assertEquals("Widget should be disabled when collapsed", Touchable.disabled, widget.getTouchable());
    }

    @Test
    public void testSetCollapseInterpolation() {
        CollapsibleWidget widget = new CollapsibleWidget();
        com.badlogic.gdx.math.Interpolation interpolation = com.badlogic.gdx.math.Interpolation.linear;
        widget.setCollapseInterpolation(interpolation);
        Assert.assertEquals("Collapse interpolation should be set", interpolation, getPrivateField(widget, "collapseInterpolation"));
    }

    @Test
    public void testGetPrefWidthWithNullTable() {
        CollapsibleWidget widget = new CollapsibleWidget();
        Assert.assertEquals("Pref width should be 0 when table is null", 0f, widget.getPrefWidth(), 0.0001f);
    }

    @Test
    public void testGetPrefHeightWithNullTable() {
        CollapsibleWidget widget = new CollapsibleWidget();
        Assert.assertEquals("Pref height should be 0 when table is null", 0f, widget.getPrefHeight(), 0.0001f);
    }

    @Test
    public void testSetCollapsedWithNullTable() {
        CollapsibleWidget widget = new CollapsibleWidget();
        // Should not throw exception
        widget.setCollapsed(true, false);
        Assert.assertTrue("Widget should be collapsed even with null table", widget.isCollapsed());
    }

    private Object getPrivateField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }
}
