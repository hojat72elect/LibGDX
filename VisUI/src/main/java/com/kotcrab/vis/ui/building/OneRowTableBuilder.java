package com.kotcrab.vis.ui.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;

/**
 * Ignores row() calls and builds table with all widgets put into one row. Works like a StandardTableBuilder
 * if row() is never used, but keeps the code clearer, as the name pretty much tells what you are trying to
 * do.
 */
public class OneRowTableBuilder extends TableBuilder {
    public OneRowTableBuilder() {
        super();
    }

    /**
     * @param defaultWidgetPadding will be applied to all added widgets if no specific padding is given.
     */
    public OneRowTableBuilder(final Padding defaultWidgetPadding) {
        super(defaultWidgetPadding);
    }

    public OneRowTableBuilder(final int estimatedWidgetsAmount, final int estimatedRowsAmount) {
        super(estimatedWidgetsAmount, estimatedRowsAmount);
    }

    /**
     * @param defaultWidgetPadding will be applied to all added widgets if no specific padding is given.
     */
    public OneRowTableBuilder(final int estimatedWidgetsAmount, final int estimatedRowsAmount,
                              final Padding defaultWidgetPadding) {
        super(estimatedWidgetsAmount, estimatedRowsAmount, defaultWidgetPadding);
    }

    @Override
    protected void fillTable(final Table table) {
        for (final CellWidget<? extends Actor> widget : getWidgets()) {
            widget.buildCell(table, getDefaultWidgetPadding());
        }
    }
}
