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
}
