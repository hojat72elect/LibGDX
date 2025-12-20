package com.kotcrab.vis.ui.building.utilities.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.building.utilities.CellWidget;

/**
 * An interface that allows to convert multiple widgets into one, providing utilities for complex tables
 * building. For sample implementations, see TableLayout class.
 */
public interface ActorLayout {
    /**
     * @return passed actors merged into one widget.
     */
    Actor convertToActor(Actor... widgets);

    /**
     * @return passed wrapped actors merged into one widget.
     */
    Actor convertToActor(CellWidget<?>... widgets);
}
