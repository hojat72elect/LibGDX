package com.kotcrab.vis.ui.util;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;

/**
 * Utilities for VisTable/Table.
 */
public class TableUtils {
    /**
     * Sets default table spacing for VisUI skin. Uses values from current skin {@link Sizes} class obtained from {@link VisUI#getSizes()}.
     */
    public static void setSpacingDefaults(Table table) {
        Sizes sizes = VisUI.getSizes();
        if (sizes.spacingTop != 0) table.defaults().spaceTop(sizes.spacingTop);
        if (sizes.spacingBottom != 0) table.defaults().spaceBottom(sizes.spacingBottom);
        if (sizes.spacingRight != 0) table.defaults().spaceRight(sizes.spacingRight);
        if (sizes.spacingLeft != 0) table.defaults().spaceLeft(sizes.spacingLeft);
    }
}
