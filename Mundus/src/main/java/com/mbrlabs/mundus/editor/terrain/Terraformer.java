package com.mbrlabs.mundus.editor.terrain;

import com.mbrlabs.mundus.commons.scene3d.components.TerrainComponent;
import com.mbrlabs.mundus.commons.terrain.Terrain;

/**
 * Factory class for terraform generators.
 */
public class Terraformer {

    public static PerlinNoiseGenerator perlin(final TerrainComponent terrainComponent) {
        return new PerlinNoiseGenerator(terrainComponent);
    }

    public static HeightMapGenerator heightMap(final TerrainComponent terrainComponent) {
        return new HeightMapGenerator(terrainComponent);
    }

    public static FastNoiseGenerator fastNoise(Terrain terrain) {
        return new FastNoiseGenerator(terrain);
    }
}
