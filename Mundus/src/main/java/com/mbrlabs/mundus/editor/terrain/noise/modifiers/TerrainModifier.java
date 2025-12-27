package com.mbrlabs.mundus.editor.terrain.noise.modifiers;

import com.mbrlabs.mundus.editor.terrain.noise.TerrainNoiseData;

public interface TerrainModifier {
    String getName();

    void modify(TerrainNoiseData noiseData, float x, float y);
}
