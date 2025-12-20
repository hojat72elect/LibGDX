package com.kotcrab.vis.ui.building.utilities.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.building.GridTableBuilder;
import com.kotcrab.vis.ui.building.utilities.CellWidget;

/**
 * Additional TableLayout with customizable variables. Converts passed widgets into a table using
 * GridTableBuilder.
 */
public class GridTableLayout implements ActorLayout {
    private final int rowSize;

    public GridTableLayout(final int rowSize) {
        this.rowSize = rowSize;
    }

    /**
     * Default factory method.
     *
     * @return new GridTableLayout, building grid with the passed row size.
     */
    public static GridTableLayout withRowSize(final int rowSize) {
        return new GridTableLayout(rowSize);
    }

    @Override
    public Actor convertToActor(final Actor... widgets) {
        return convertToActor(CellWidget.wrap(widgets));
    }

    @Override
    public Actor convertToActor(final CellWidget<?>... widgets) {
        return TableLayout.convertToTable(new GridTableBuilder(rowSize), widgets);
    }
}
