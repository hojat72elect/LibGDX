package com.mbrlabs.mundus.editorcommons.events;

import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.editorcommons.EventListener;
import com.mbrlabs.mundus.editorcommons.Subscribe;

public class TerrainRemovedEvent {

    private final TerrainComponent terrainComponent;

    public TerrainRemovedEvent(TerrainComponent terrainComponent) {
        this.terrainComponent = terrainComponent;
    }

    public TerrainComponent getTerrainComponent() {
        return terrainComponent;
    }

    public interface TerrainRemovedEventListener extends EventListener {

        @Subscribe
        void onTerrainRemoved(TerrainRemovedEvent event);
    }

}
