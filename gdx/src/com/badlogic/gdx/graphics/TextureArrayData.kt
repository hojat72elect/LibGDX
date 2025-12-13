package com.badlogic.gdx.graphics

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.glutils.FileTextureArrayData

/**
 * Used by a [TextureArray] to load the pixel data. The TextureArray will request the TextureArrayData to prepare itself
 * through [prepare] and upload its data using [consumeTextureArrayData]. These are the first methods to be
 * called by TextureArray. After that the TextureArray will invoke the other methods to find out about the size of the image data,
 * the format, whether the TextureArrayData is able to manage the pixel data if the OpenGL ES context is lost.
 * Before a call to either [consumeTextureArrayData], TextureArray will bind the OpenGL ES texture.
 * Look at [FileTextureArrayData] for example implementation of this interface.
 */
interface TextureArrayData {

    /**
     * @return whether the TextureArrayData is prepared or not.
     */
    fun isPrepared(): Boolean

    /**
     * Prepares the TextureArrayData for a call to [consumeTextureArrayData]. This method can be called from a non OpenGL
     * thread and should thus not interact with OpenGL.
     */
    fun prepare()

    /**
     * Uploads the pixel data of the TextureArray layers of the TextureArray to the OpenGL ES texture. The caller must bind an
     * OpenGL ES texture. A call to [prepare] must preceed a call to this method. Any internal data structures created in
     * [prepare] should be disposed of here.
     */
    fun consumeTextureArrayData()

    /**
     * @return the width of this TextureArray
     */
    fun getWidth(): Int

    /**
     * @return the height of this TextureArray
     */
    fun getHeight(): Int

    /**
     * @return the layer count of this TextureArray
     */
    fun getDepth(): Int

    /**
     * @return whether this implementation can cope with a EGL context loss.
     */
    fun isManaged(): Boolean

    /**
     * @return the internal format of this TextureArray
     */
    fun getInternalFormat(): Int

    /**
     * @return the GL type of this TextureArray
     */
    fun getGLType(): Int

    /**
     * Provides static method to instantiate the right implementation.
     */
    object Factory {
        @JvmStatic
        fun loadFromFiles(format: Pixmap.Format, useMipMaps: Boolean, vararg files: FileHandle): TextureArrayData = FileTextureArrayData(format, useMipMaps, files)
    }
}
