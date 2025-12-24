package com.talosvfx.talos.editor.addons.scene.events;

import com.talosvfx.talos.editor.notifications.TalosEvent;
import com.talosvfx.talos.runtime.scene.GameObject;

public class GameObjectActiveChanged implements TalosEvent {

    public GameObject target;

    @Override
    public void reset() {
        target = null;
    }
}
