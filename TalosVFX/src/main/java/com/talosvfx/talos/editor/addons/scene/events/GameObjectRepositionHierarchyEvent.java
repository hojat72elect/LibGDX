package com.talosvfx.talos.editor.addons.scene.events;

import com.talosvfx.talos.editor.notifications.TalosEvent;
import com.talosvfx.talos.runtime.scene.GameObject;

import lombok.Data;

@Data
public class GameObjectRepositionHierarchyEvent implements TalosEvent {

    private GameObject parent;
    private GameObject child;

    @Override
    public void reset() {

    }
}
