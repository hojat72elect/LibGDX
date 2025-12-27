package com.mbrlabs.mundus.editorcommons.events;

import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.editorcommons.EventListener;
import com.mbrlabs.mundus.editorcommons.Subscribe;

public class TerrainAddedEvent {

    private final TerrainComponent terrainComponent;

    public TerrainAddedEvent(TerrainComponent terrainComponent) {
        this.terrainComponent = terrainComponent;
    }

    public TerrainComponent getTerrainComponent() {
        return terrainComponent;
    }

    public interface TerrainAddedEventListener extends EventListener {

        @Subscribe
        void onTerrainAdded(TerrainAddedEvent event);
    }
}
