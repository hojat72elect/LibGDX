package com.mbrlabs.mundus.pluginapi;

import com.badlogic.gdx.graphics.Camera;
import org.pf4j.ExtensionPoint;

public interface CustomShaderRenderExtension extends ExtensionPoint {

    /**
     * Can render model with custom shader.
     *
     * @param camera The camera.
     */
    void render(Camera camera);
}
