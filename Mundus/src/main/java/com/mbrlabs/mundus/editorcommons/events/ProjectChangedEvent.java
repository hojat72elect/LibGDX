package com.mbrlabs.mundus.editorcommons.events;

import com.mbrlabs.mundus.commons.scene3d.SceneGraph;
import com.mbrlabs.mundus.editorcommons.EventListener;
import com.mbrlabs.mundus.editorcommons.Subscribe;

public class ProjectChangedEvent {

    private final SceneGraph sceneGraph;

    public ProjectChangedEvent(final SceneGraph sceneGraph) {
        this.sceneGraph = sceneGraph;
    }

    public SceneGraph getSceneGraph() {
        return sceneGraph;
    }

    public interface ProjectChangedListener extends EventListener {

        @Subscribe
        void onProjectChanged(ProjectChangedEvent event);
    }
}
