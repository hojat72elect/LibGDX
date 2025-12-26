package com.mbrlabs.mundus.commons.rendering;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.mbrlabs.mundus.commons.Scene;
import com.mbrlabs.mundus.commons.scene3d.GameObject;
import com.mbrlabs.mundus.commons.water.WaterResolution;

/**
 * Responsible for rendering a scene.
 */
public interface SceneRenderer {

    /**
     * Renders the given scene. Primary render method. If using this method
     * you should not need to call any other render methods.
     *
     * @param scene the scene to render
     * @param delta time since last frame
     */
    void render(Scene scene, float delta);

    void renderSkybox(Scene scene);

    /**
     * Renders all renderable components (except Water) of the given parent game objects children
     * recursively using default shaders.
     *
     * @param batch  the model batch to use
     * @param parent the parent game object
     */
    void renderComponents(Scene scene, ModelBatch batch, GameObject parent);

    /**
     * Renders all renderable components (except Water) of the given parent game objects children
     * recursively.
     *
     * @param batch       the model batch to use
     * @param parent      the parent game object
     * @param shader      the shader to use
     * @param isDepthPass whether this is a depth render pass
     */
    void renderComponents(Scene scene, ModelBatch batch, GameObject parent, Shader shader, boolean isDepthPass);

    Shader getDepthShader();

    void setDepthShader(Shader depthShader);

    void updateWaterResolution(WaterResolution waterResolution);
}
