package com.kotcrab.vis.ui.building;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntArray;
import com.kotcrab.vis.ui.building.utilities.Padding;

/**
 * Builds a standard table with the appended widgets. Each table's row will have the same colspan. Honors all
 * CellWidget settings and TableBuilder commands, making it the most flexible TableBuilder and the best one
 * for complex, custom tables.
 */
public class StandardTableBuilder extends TableBuilder {
    public StandardTableBuilder() {
        super();
    }

    /**
     * @param defaultWidgetPadding will be applied to all added widgets if no specific padding is given.
     */
    public StandardTableBuilder(final Padding defaultWidgetPadding) {
        super(defaultWidgetPadding);
    }

    public StandardTableBuilder(final int estimatedWidgetsAmount, final int estimatedRowsAmount) {
        super(estimatedWidgetsAmount, estimatedRowsAmount);
    }

    /**
     * @param defaultWidgetPadding will be applied to all added widgets if no specific padding is given.
     */
    public StandardTableBuilder(final int estimatedWidgetsAmount, final int estimatedRowsAmount,
                                final Padding defaultWidgetPadding) {
        super(estimatedWidgetsAmount, estimatedRowsAmount, defaultWidgetPadding);
    }

    @Override
    protected void fillTable(final Table table) {
        final IntArray rowSizes = getRowSizes();
        final int widgetsInRow = getLowestCommonMultiple(rowSizes);
        for (int rowIndex = 0, widgetIndex = 0; rowIndex < rowSizes.size; rowIndex++) {
            final int rowSize = rowSizes.get(rowIndex);
            final int currentWidgetColspan = widgetsInRow / rowSize;
            for (final int totalWidgets = widgetIndex + rowSize; widgetIndex < totalWidgets; widgetIndex++) {
                getWidget(widgetIndex).buildCell(table, getDefaultWidgetPadding()).colspan(
                        currentWidgetColspan);
            }
            table.row();
        }
    }
}
