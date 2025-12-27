package com.mbrlabs.mundus.editorcommons.events;

import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.editorcommons.EventListener;
import com.mbrlabs.mundus.editorcommons.Subscribe;

public class TerrainVerticesChangedEvent {

    private final TerrainComponent terrainComponent;

    public TerrainVerticesChangedEvent(TerrainComponent terrainComponent) {
        this.terrainComponent = terrainComponent;
    }

    public TerrainComponent getTerrainComponent() {
        return terrainComponent;
    }

    public interface TerrainVerticesChangedEventListener extends EventListener {

        @Subscribe
        void onTerrainVerticesChanged(TerrainVerticesChangedEvent event);
    }
}
