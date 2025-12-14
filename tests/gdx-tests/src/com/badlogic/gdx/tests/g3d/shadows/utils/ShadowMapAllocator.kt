package com.badlogic.gdx.tests.g3d.shadows.utils

import com.badlogic.gdx.graphics.g3d.environment.BaseLight

/**
 * Shadow map allocator return texture region for each light
 *
 */
interface ShadowMapAllocator {
    /**
     * Begin the texture allocation
     */
    fun begin()

    /**
     * End the texture allocation
     */
    fun end()

    /**
     * Find the next texture region for the current light
     *
     * @param light Current light
     * @return ShadowMapRegion or null if there's no more space on the texture.
     */
    fun nextResult(light: BaseLight<*>): ShadowMapRegion?

    /**
     * Return shadow map width.
     * @return int
     */
    fun getWidth(): Int

    /**
     * Return shadow map height.
     * @return int
     */
    fun getHeight(): Int

    /**
     * Result of the allocator analyze
     */
    class ShadowMapRegion {
        @JvmField
        var x: Int = 0

        @JvmField
        var y: Int = 0

        @JvmField
        var width: Int = 0

        @JvmField
        var height: Int = 0
    }
}
