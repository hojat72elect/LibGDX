package com.mbrlabs.mundus.editor.core.converter;

import com.mbrlabs.mundus.commons.assets.Asset;
import com.mbrlabs.mundus.commons.assets.TerrainAsset;
import com.mbrlabs.mundus.commons.dto.TerrainComponentDTO;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.editor.scene3d.components.PickableTerrainComponent;
import com.mbrlabs.mundus.editor.utils.Log;

import java.util.Map;

/**
 * The converter for terrain.
 */
public class TerrainComponentConverter {

    private final static String TAG = TerrainComponentConverter.class.getSimpleName();

    /**
     * Converts {@link TerrainComponentDTO} to {@link PickableTerrainComponent}.
     */
    public static PickableTerrainComponent convert(TerrainComponentDTO dto, GameObject go,
                                                   Map<String, Asset> assets) {
        // find terrainAsset
        TerrainAsset terrain = (TerrainAsset) assets.get(dto.getTerrainID());

        if (terrain == null) {
            Log.fatal(TAG, "Terrain for TerrainInstance not found");
            return null;
        }

        PickableTerrainComponent terrainComponent = new PickableTerrainComponent(go);
        terrainComponent.setTerrainAsset(terrain);

        return terrainComponent;
    }

    /**
     * Converts {@link PickableTerrainComponent} to {@link TerrainComponentDTO}.
     */
    public static TerrainComponentDTO convert(PickableTerrainComponent terrainComponent) {
        TerrainComponentDTO descriptor = new TerrainComponentDTO();
        descriptor.setTerrainID(terrainComponent.getTerrainAsset().getID());

        if (terrainComponent.getTopNeighbor() != null) {
            descriptor.setTopNeighborID(terrainComponent.getTopNeighbor().getGameObject().id);
        }

        if (terrainComponent.getRightNeighbor() != null) {
            descriptor.setRightNeighborID(terrainComponent.getRightNeighbor().getGameObject().id);
        }

        if (terrainComponent.getBottomNeighbor() != null) {
            descriptor.setBottomNeighborID(terrainComponent.getBottomNeighbor().getGameObject().id);
        }

        if (terrainComponent.getLeftNeighbor() != null) {
            descriptor.setLeftNeighborID(terrainComponent.getLeftNeighbor().getGameObject().id);
        }

        return descriptor;
    }
}
