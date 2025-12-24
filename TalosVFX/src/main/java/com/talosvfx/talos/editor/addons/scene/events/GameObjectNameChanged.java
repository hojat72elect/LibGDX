package com.talosvfx.talos.editor.addons.scene.events;

import com.talosvfx.talos.editor.notifications.TalosEvent;
import com.talosvfx.talos.runtime.scene.GameObject;

public class GameObjectNameChanged implements TalosEvent {

    public GameObject target;
    public String oldName;
    public String newName;

    @Override
    public void reset() {
        target = null;
    }
}
