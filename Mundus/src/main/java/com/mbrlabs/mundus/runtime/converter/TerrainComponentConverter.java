package com.mbrlabs.mundus.runtime.converter;

import com.mbrlabs.mundus.commons.assets.AssetManager;
import com.mbrlabs.mundus.commons.assets.TerrainAsset;
import com.mbrlabs.mundus.commons.dto.TerrainComponentDTO;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;

/**
 * The converter for terrain.
 */
public class TerrainComponentConverter {

    /**
     * Converts {@link TerrainComponentDTO} to {@link TerrainComponent}.
     */
    public static TerrainComponent convert(TerrainComponentDTO terrainComponentDTO, GameObject gameObject, AssetManager assetManager) {
        TerrainComponent tc = new TerrainComponent(gameObject);
        tc.setTerrainAsset((TerrainAsset) assetManager.findAssetByID(terrainComponentDTO.getTerrainID()));

        return tc;
    }
}
