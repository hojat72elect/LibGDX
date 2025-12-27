
package com.mbrlabs.mundus.editor.tools.terrain;

import com.mbrlabs.mundus.commons.terrain.Terrain;
import com.mbrlabs.mundus.editor.tools.brushes.TerrainBrush;

public class RaiseLowerTool extends RadiusTerrainTool {

    // Raise or lower the terrain
    private static float dir = 1;

    private static final TerrainBrush.TerrainModifyAction modifier = (brush, terrainComponent, x, z, localBrushPos, vertexPos) -> {
        Terrain terrain = terrainComponent.getTerrainAsset().getTerrain();
        float elevation = brush.getValueOfBrushPixmap(localBrushPos.x, localBrushPos.z, vertexPos.x, vertexPos.z, brush.getScaledRadius(terrainComponent));
        terrain.heightData[z * terrain.vertexResolution + x] += dir * elevation * TerrainBrush.getStrength();
    };

    public void act(TerrainBrush brush) {
        dir = (brush.getBrushAction() == TerrainBrush.BrushAction.PRIMARY) ? 1 : -1;
        brush.modifyTerrain(modifier, radiusDistanceComparison, true);
    }
}
