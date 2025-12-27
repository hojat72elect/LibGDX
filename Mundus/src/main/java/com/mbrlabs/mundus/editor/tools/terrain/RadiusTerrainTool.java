
package com.mbrlabs.mundus.editor.tools.terrain;

import com.mbrlabs.mundus.editor.tools.brushes.TerrainBrush;

public abstract class RadiusTerrainTool implements TerrainTool {
    protected static final TerrainBrush.TerrainModifyComparison radiusDistanceComparison = (terrainBrush, vertexPos, localBrushPos) -> {
        float distance = vertexPos.dst(localBrushPos);
        return distance <= terrainBrush.getRadius();
    };
}
