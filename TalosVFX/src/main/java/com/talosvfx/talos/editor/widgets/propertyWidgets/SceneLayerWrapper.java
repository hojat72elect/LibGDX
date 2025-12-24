package com.talosvfx.talos.editor.widgets.propertyWidgets;

import com.talosvfx.talos.runtime.scene.SceneLayer;

import java.util.UUID;

import lombok.Getter;

public class SceneLayerWrapper {
    @Getter
    private final SceneLayer instance;

    public SceneLayerWrapper(SceneLayer sceneLayer) {
        instance = sceneLayer;
    }

    @Override
    public String toString() {
        return instance.getName();
    }

    public void updateName(String newText) {
        instance.setName(newText);
    }

    public boolean canDelete() {
        return !instance.getName().equals("Default");
    }

    public UUID getID() {
        return instance.getUniqueID();
    }

    public String getName() {
        return instance.getName();
    }
}
