package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Widget containing table that can be vertically collapsed.
 *
 * @see HorizontalCollapsibleWidget
 * @since 0.3.1
 */
public class CollapsibleWidget extends WidgetGroup {
    private Table table;

    private final CollapseAction collapseAction = new CollapseAction();
    private float collapseDuration = 0.3f;
    private Interpolation collapseInterpolation = Interpolation.pow3Out;

    private boolean collapsed;
    private boolean actionRunning;

    private float currentHeight;

    public CollapsibleWidget() {
    }

    public CollapsibleWidget(Table table) {
        this(table, false);
    }

    public CollapsibleWidget(Table table, boolean collapsed) {
        this.collapsed = collapsed;
        this.table = table;

        updateTouchable();

        if (table != null) addActor(table);
    }

    public void setCollapsed(boolean collapse, boolean withAnimation) {
        this.collapsed = collapse;
        updateTouchable();

        if (table == null) return;

        actionRunning = true;

        if (withAnimation) {
            collapseAction.reset();
            collapseAction.setStart(currentHeight);
            collapseAction.setEnd(collapse ? 0f : table.getPrefHeight());
            collapseAction.setDuration(collapseDuration);
            collapseAction.setInterpolation(collapseInterpolation);
            addAction(collapseAction);
        } else {
            if (collapse) {
                currentHeight = 0;
                collapsed = true;
            } else {
                currentHeight = table.getPrefHeight();
                collapsed = false;
            }

            actionRunning = false;
            invalidateHierarchy();
        }
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapse) {
        setCollapsed(collapse, true);
    }

    private void updateTouchable() {
        if (collapsed)
            setTouchable(Touchable.disabled);
        else
            setTouchable(Touchable.enabled);
    }

    public void setCollapseDuration(float collapseDuration) {
        this.collapseDuration = collapseDuration;
    }

    public void setCollapseInterpolation(Interpolation collapseInterpolation) {
        this.collapseInterpolation = collapseInterpolation;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (currentHeight > 1 && getY() + currentHeight > 1) {
            if (actionRunning) {
                batch.flush();
                boolean clipEnabled = clipBegin(getX(), getY(), getWidth(), currentHeight);

                super.draw(batch, parentAlpha);

                batch.flush();
                if (clipEnabled) clipEnd();
            } else {
                super.draw(batch, parentAlpha);
            }
        }
    }

    @Override
    public void layout() {
        if (table == null) return;

        table.setBounds(0, 0, table.getPrefWidth(), table.getPrefHeight());

        if (!actionRunning) {
            if (collapsed)
                currentHeight = 0;
            else
                currentHeight = table.getPrefHeight();
        }
    }

    @Override
    public float getPrefWidth() {
        return table == null ? 0 : table.getPrefWidth();
    }

    @Override
    public float getPrefHeight() {
        if (table == null) return 0;

        if (!actionRunning) {
            if (collapsed)
                return 0;
            else
                return table.getPrefHeight();
        }

        return currentHeight;
    }

    public void setTable(Table table) {
        this.table = table;
        clearChildren();
        addActor(table);
    }

    @Override
    protected void childrenChanged() {
        super.childrenChanged();
        if (getChildren().size > 1) throw new GdxRuntimeException("Only one actor can be added to CollapsibleWidget");
    }

    private class CollapseAction extends FloatAction {

        @Override
        protected void update(float percent) {
            super.update(percent);
            currentHeight = getValue();

            if (percent == 1) {
                actionRunning = false;
                collapsed = currentHeight == 0;
            }

            invalidateHierarchy();
        }
    }
}
