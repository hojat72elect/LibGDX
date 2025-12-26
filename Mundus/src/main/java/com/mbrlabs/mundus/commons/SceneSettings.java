package com.mbrlabs.mundus.commons;

import com.mbrlabs.mundus.commons.water.WaterResolution;

/**
 * Settings specific to a Scene.
 */
public class SceneSettings {

    public final float distortionEdgeCorrection = 1f;
    // Water
    public float waterHeight = 0f;
    public WaterResolution waterResolution = WaterResolution.DEFAULT_WATER_RESOLUTION;
    public boolean enableWaterReflections = true;
    public boolean enableWaterRefractions = true;

    // Performance
    public boolean useFrustumCulling = true;
}
