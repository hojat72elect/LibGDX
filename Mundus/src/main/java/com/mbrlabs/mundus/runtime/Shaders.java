package com.mbrlabs.mundus.runtime;

import com.mbrlabs.mundus.commons.shaders.DepthShader;
import com.mbrlabs.mundus.commons.shaders.SkyboxShader;

public class Shaders {

    private final SkyboxShader skyboxShader;
    private final DepthShader depthShader;

    public Shaders() {
        skyboxShader = new SkyboxShader();
        skyboxShader.init();
        depthShader = new DepthShader();
        depthShader.init();
    }

    public SkyboxShader getSkyboxShader() {
        return skyboxShader;
    }

    public DepthShader getDepthShader() {
        return depthShader;
    }
}
