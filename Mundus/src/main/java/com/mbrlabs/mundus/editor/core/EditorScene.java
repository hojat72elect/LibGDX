package com.mbrlabs.mundus.editor.core;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mbrlabs.mundus.commons.Scene;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.Component;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.editor.Mundus;
import com.mbrlabs.mundus.editor.events.ComponentAddedEvent;

public class EditorScene extends Scene {

    public Viewport viewport;
    public Array<TerrainComponent> terrains;
    public GameObject currentSelection;

    public EditorScene() {
        super();
        currentSelection = null;
        terrains = new Array<>();
        isRuntime = false;
    }

    /**
     * Call this when the scene is loaded and is now the current active scene.
     */
    public void onLoaded() {
        // Post component added events for all components in the scene now that it is loaded
        for (GameObject gameObject : sceneGraph.getGameObjects()) {
            for (Component component : gameObject.getComponents()) {
                Mundus.INSTANCE.postEvent(new ComponentAddedEvent(component));
            }
        }
    }
}
