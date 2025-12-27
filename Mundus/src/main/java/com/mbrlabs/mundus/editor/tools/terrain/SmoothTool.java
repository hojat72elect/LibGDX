
package com.mbrlabs.mundus.editor.tools.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.mbrlabs.mundus.commons.terrain.Terrain;
import com.mbrlabs.mundus.editor.tools.brushes.TerrainBrush;

/**
 * Get average height of all vertices in radius, interpolate heights to average height
 * will a falloff effect based on distance from radius.
 */
public class SmoothTool extends RadiusTerrainTool {
    private float averageHeight = 0;

    // Interpolate heights to average height with falloff
    private final TerrainBrush.TerrainModifyAction modifier = (brush, terrainComponent, x, z, tVec2, vertexPos) -> {
        Terrain terrain = terrainComponent.getTerrainAsset().getTerrain();
        final int index = z * terrain.vertexResolution + x;
        float heightAtIndex = terrain.heightData[index];
        // Determine how much to interpolate based on distance from radius
        float elevation = brush.getValueOfBrushPixmap(tVec2.x, tVec2.z, vertexPos.x, vertexPos.z, brush.getScaledRadius(terrainComponent));
        float smoothedHeight = Interpolation.smooth2.apply(heightAtIndex, averageHeight, elevation * TerrainBrush.getStrength());
        terrain.heightData[index] = smoothedHeight;
    };

    @Override
    public void act(TerrainBrush brush) {
        final int[] weights = {0};
        final float[] totalHeights = {0};

        // Get average height of all vertices in radius
        TerrainBrush.TerrainModifyAction heightAverage = (terrainBrush, terrain, x, z, tVec2, vertexPos) -> {
            totalHeights[0] += vertexPos.y;
            weights[0]++;
        };
        brush.modifyTerrain(heightAverage, radiusDistanceComparison, true);

        averageHeight = totalHeights[0] / weights[0];
        brush.modifyTerrain(modifier, radiusDistanceComparison, true);
    }
}
