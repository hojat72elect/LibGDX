package com.talosvfx.talos.editor.addons.scene.events;

import com.talosvfx.talos.editor.notifications.TalosEvent;
import com.talosvfx.talos.runtime.maps.TalosLayer;

public class TalosLayerSelectEvent implements TalosEvent {

    public TalosLayer layer;

    public TalosLayerSelectEvent() {
    }

    public TalosLayerSelectEvent(TalosLayer layer) {
        this.layer = layer;
    }

    @Override
    public void reset() {
        layer = null;
    }
}
