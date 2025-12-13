package com.badlogic.gdx.tests.g3d.shadows.system

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Cubemap.CubemapSide
import com.badlogic.gdx.graphics.g3d.RenderableProvider
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.environment.PointLight
import com.badlogic.gdx.graphics.g3d.environment.SpotLight
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider

/**
 * Shadow system provides functionalities to render shadows.
 *
 * Typical use: <br></br>
 *
 * ```
 * // Init system:
 * Array&lt;ModelBatch&gt; passBatches = new Array&lt;ModelBatch&gt;();
 * ModelBatch mainBatch;
 * ShadowSystem system = new XXXShadowSystem();
 * system.init();
 * for (int i = 0; i &lt; system.getPassQuantity(); i++) {
 * passBatches.add(new ModelBatch(system.getPassShaderProvider(i)));
 * }
 * mainBatch = new ModelBatch(system.getShaderProvider());
 *
 * // Render scene with shadows:
 * system.begin(camera, instances);
 * system.update();
 * for (int i = 0; i &lt; system.getPassQuantity(); i++) {
 * system.begin(i);
 * Camera camera;
 * while ((camera = system.next()) != null) {
 * passBatches.get(i).begin(camera);
 * passBatches.get(i).render(instances, environment);
 * passBatches.get(i).end();
 * }
 * camera = null;
 * system.end(i);
 * }
 * system.end();
 *
 * HdpiUtils.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
 * Gdx.gl.glClearColor(0, 0, 0, 1);
 * Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
 *
 * mainBatch.begin(cam);
 * mainBatch.render(instances, environment);
 * mainBatch.end();
 * ```
 *
 * Current environnment should be alway be synchonized with shadow system lights. It means that if you add or remove light from
 * environment, you should do it in shadow system too. <br></br>
 * If you have two different environments, when you switch, you should add and remove all lights in shadow system.
 */
interface ShadowSystem {
    /**
     * Initialize system.
     */
    fun init()

    /**
     * Return number of pass.
     */
    fun getPassQuantity(): Int

    /**
     * Return shaderProvider of the pass n.
     */
    fun getPassShaderProvider(n: Int): ShaderProvider

    /**
     * Return shaderProvider used for main rendering.
     */
    fun getShaderProvider(): ShaderProvider

    /**
     * Add spot light in shadow system.
     * @param spot SpotLight to add in the ShadowSystem.
     */
    fun addLight(spot: SpotLight)

    /**
     * Add directional light in shadow system.
     * @param dir DirectionalLight to add in the ShadowSystem.
     */
    fun addLight(dir: DirectionalLight)

    /**
     * Add point light in shadow system.
     * @param point PointLight to add in the ShadowSystem.
     */
    fun addLight(point: PointLight)

    /**
     * Add point light in shadow system
     * @param point PointLight to add in the ShadowSystem
     * @param sides Set of side
     */
    fun addLight(point: PointLight, sides: MutableSet<CubemapSide>)

    /**
     * Remove light from the shadowSystem
     * @param spot SpotLight to remove in the ShadowSystem
     */
    fun removeLight(spot: SpotLight)

    /**
     * Remove light from the shadowSystem
     * @param dir DirectionalLight to remove in the ShadowSystem
     */
    fun removeLight(dir: DirectionalLight)

    /**
     * Remove light from the shadowSystem
     * @param point PointLight to remove in the ShadowSystem
     */
    fun removeLight(point: PointLight)

    /**
     * @param spot SpotLight to check
     * @return true if light analyzed
     */
    fun hasLight(spot: SpotLight): Boolean

    /**
     * @param dir Directional Light to check
     * @return true if light analyzed
     */
    fun hasLight(dir: DirectionalLight): Boolean

    /**
     * @param point PointLight to check
     * @return true if light analyzed
     */
    fun hasLight(point: PointLight): Boolean

    /**
     * Update shadowSystem
     */
    fun update()

    /**
     * Begin shadow system with main camera and renderable providers.
     */
    fun <T : RenderableProvider> begin(camera: Camera, renderableProviders: Iterable<T>)

    /**
     * Begin pass n rendering.
     * @param n Pass number
     */
    fun begin(n: Int)

    /**
     * Switch light
     * @return Current camera or null if there's no camera or an error happens.
     */
    fun next(): Camera?

    /**
     * End shadow system
     */
    fun end()

    /**
     * End pass n rendering
     */
    fun end(n: Int)
}
