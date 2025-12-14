package com.badlogic.gdx.tests.g3d.shadows.utils

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g3d.environment.BaseLight

/**
 * Select only casting shadow lights. Allows to optimize shadow system.
 */
interface LightFilter {

    /**
     * Return true if light should be used for shadow computation.
     * @param light Current light
     * @param camera Light's camera
     * @param mainCamera Main scene camera
     * @return boolean
     */
    fun filter(light: BaseLight<*>, camera: Camera, mainCamera: Camera): Boolean
}
