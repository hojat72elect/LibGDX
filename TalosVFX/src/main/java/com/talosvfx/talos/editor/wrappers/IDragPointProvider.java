
package com.talosvfx.talos.editor.wrappers;

import com.talosvfx.talos.editor.widgets.ui.DragPoint;

public interface IDragPointProvider {

    DragPoint[] fetchDragPoints();

    void dragPointChanged(DragPoint point);
}
