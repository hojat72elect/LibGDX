package com.kotcrab.vis.ui.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;

/**
 * Ignores row() calls and builds table with all widgets put into rows of given size. Note that this builder
 * will not center or in any way try to "repair" the last row if too few widgets are given to create a true
 * grid.
 */
public class GridTableBuilder extends TableBuilder {
    private final int rowSize;

    public GridTableBuilder(final int rowSize) {
        super();
        this.rowSize = rowSize;
    }

    /**
     * @param defaultWidgetPadding will be applied to all added widgets if no specific padding is given.
     */
    public GridTableBuilder(final Padding defaultWidgetPadding, final int rowSize) {
        super(defaultWidgetPadding);
        this.rowSize = rowSize;
    }

    public GridTableBuilder(final int rowSize, final int estimatedWidgetsAmount, final int estimatedRowsAmount) {
        super(estimatedWidgetsAmount, estimatedRowsAmount);
        this.rowSize = rowSize;
    }

    /**
     * @param defaultWidgetPadding will be applied to all added widgets if no specific padding is given.
     */
    public GridTableBuilder(final int rowSize, final int estimatedWidgetsAmount,
                            final int estimatedRowsAmount, final Padding defaultWidgetPadding) {
        super(estimatedWidgetsAmount, estimatedRowsAmount, defaultWidgetPadding);
        this.rowSize = rowSize;
    }

    @Override
    protected void fillTable(final Table table) {
        int widgetsCounter = 0;
        for (final CellWidget<? extends Actor> widget : getWidgets()) {
            widget.buildCell(table, getDefaultWidgetPadding());
            if (++widgetsCounter == rowSize) {
                widgetsCounter -= rowSize;
                table.row();
            }
        }
    }
}
