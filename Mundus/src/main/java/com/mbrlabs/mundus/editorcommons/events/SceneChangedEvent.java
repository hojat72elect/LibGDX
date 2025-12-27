package com.mbrlabs.mundus.editorcommons.events;

import com.mbrlabs.mundus.commons.scene3d.SceneGraph;
import com.mbrlabs.mundus.editorcommons.EventListener;
import com.mbrlabs.mundus.editorcommons.Subscribe;

public class SceneChangedEvent {

    private final SceneGraph sceneGraph;

    public SceneChangedEvent(final SceneGraph sceneGraph) {
        this.sceneGraph = sceneGraph;
    }

    public SceneGraph getSceneGraph() {
        return sceneGraph;
    }

    public interface SceneChangedListener extends EventListener {

        @Subscribe
        void onSceneChanged(SceneChangedEvent event);
    }
}
