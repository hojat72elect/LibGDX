package com.mbrlabs.mundus.pluginapi;

import com.badlogic.gdx.math.Vector3;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import org.pf4j.ExtensionPoint;

public interface TerrainHoverExtension extends ExtensionPoint {

    /**
     * The {@code terrainComponent} and {@code intersection} can be null if the mouse is not on terrain.
     */
    void hover(TerrainComponent terrainComponent, Vector3 intersection);

}
