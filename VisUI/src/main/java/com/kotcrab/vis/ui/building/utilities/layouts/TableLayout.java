package com.kotcrab.vis.ui.building.utilities.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.building.OneColumnTableBuilder;
import com.kotcrab.vis.ui.building.OneRowTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.CellWidget;

/**
 * Default ActorLayout implementations, using table builders that don't require row() calls to convert
 * multiple actors into one cell.
 * <p>
 * Beside VERTICAL and HORIZONTAL, there's also grid layout available. Since it's customizable, an instance of
 * grid table layout must be manually initiated using grid() method.
 */
public enum TableLayout implements ActorLayout {
    /**
     * Converts passed widgets into a single column.
     */
    VERTICAL {
        @Override
        public Actor convertToActor(final CellWidget<?>... widgets) {
            return convertToTable(new OneColumnTableBuilder(), widgets);
        }
    },
    /**
     * Converts passed widgets into a single row.
     */
    HORIZONTAL {
        @Override
        public Actor convertToActor(final CellWidget<?>... widgets) {
            return convertToTable(new OneRowTableBuilder(), widgets);
        }
    };

    /**
     * Utility method. Appends all widgets into the passed builder and creates a table with no additional
     * settings.
     */
    public static Actor convertToTable(final TableBuilder usingBuilder, final CellWidget<?>... widgets) {
        for (final CellWidget<?> widget : widgets) {
            usingBuilder.append(widget);
        }
        return usingBuilder.build();
    }

    /**
     * @return a new instance of GridTableLayout that creates tables as grids with the specified row size.
     */
    public static GridTableLayout grid(final int rowSize) {
        return GridTableLayout.withRowSize(rowSize);
    }

    @Override
    public Actor convertToActor(final Actor... widgets) {
        return convertToActor(CellWidget.wrap(widgets));
    }
}
