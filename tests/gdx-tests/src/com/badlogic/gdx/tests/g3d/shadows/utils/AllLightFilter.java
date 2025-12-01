
package com.badlogic.gdx.tests.g3d.shadows.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;

/**
 * This Filter does not block lights. All lights are allowed.
 *
 * @author realitix
 */
public class AllLightFilter implements LightFilter {

    @Override
    public boolean filter(BaseLight light, Camera camera, Camera mainCamera) {
        return true;
    }
}
