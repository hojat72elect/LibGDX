package com.mbrlabs.mundus.pluginapi;

import com.badlogic.gdx.utils.Array;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import org.pf4j.ExtensionPoint;

public interface TerrainSceneExtension extends ExtensionPoint {

    /**
     * Calls this method ad scene loaded.
     *
     * @param terrains The loaded terrain's component.
     */
    void sceneLoaded(Array<TerrainComponent> terrains);
}
