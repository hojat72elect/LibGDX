package com.talosvfx.talos.editor.addons.scene.events.scene;

import com.talosvfx.talos.editor.notifications.TalosEvent;
import com.talosvfx.talos.runtime.scene.GameObject;

import lombok.Data;

@Data
public class DeSelectGameObjectExternallyEvent implements TalosEvent {

    private GameObject gameObject;

    @Override
    public void reset() {
        gameObject = null;
    }
}
