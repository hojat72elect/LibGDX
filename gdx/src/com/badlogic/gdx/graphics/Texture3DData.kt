package com.badlogic.gdx.graphics

/**
 * Used by a [Texture3D] to load the pixel data. The Texture3D will request the Texture3DData to prepare itself through
 * [.prepare] and upload its data using [.consume3DData]. These are the first methods to be called by Texture3D.
 * After that the Texture3D will invoke the other methods to find out about the size of the image data, the format, whether the
 * Texture3DData is able to manage the pixel data if the OpenGL ES context is lost.
 * Before a call to either [.consume3DData], Texture3D will bind the OpenGL ES texture.
 */
interface Texture3DData {

    /**
     * @return whether the TextureData is prepared or not.
     */
    fun isPrepared(): Boolean

    /**
     * Prepares the TextureData for a call to [.consume3DData]. This method can be called from a non OpenGL thread and
     * should thus not interact with OpenGL.
     */
    fun prepare()

    /**
     * @return the width of this Texture3D
     */
    fun getWidth(): Int

    /**
     * @return the height of this Texture3D
     */
    fun getHeight(): Int

    /**
     * @return the depth of this Texture3D
     */
    fun getDepth(): Int

    /**
     * @return the internal format of this Texture3D
     */
    fun getInternalFormat(): Int

    /**
     * @return the GL type of this Texture3D
     */
    fun getGLType(): Int

    /**
     * @return whether to generate mipmaps or not.
     */
    fun useMipMaps(): Boolean

    /**
     * Uploads the pixel data to the OpenGL ES texture. The caller must bind an OpenGL ES texture. A call to [.prepare]
     * must precede a call to this method. Any internal data structures created in [.prepare] should be disposed of here.
     */
    fun consume3DData()

    /**
     * @return whether this implementation can cope with a EGL context loss.
     */
    fun isManaged(): Boolean
}
