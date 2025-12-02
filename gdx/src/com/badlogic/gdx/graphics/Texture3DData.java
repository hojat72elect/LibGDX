
package com.badlogic.gdx.graphics;

/**
 * Used by a {@link Texture3D} to load the pixel data. The Texture3D will request the Texture3DData to prepare itself through
 * {@link #prepare()} and upload its data using {@link #consume3DData()}. These are the first methods to be called by Texture3D.
 * After that the Texture3D will invoke the other methods to find out about the size of the image data, the format, whether the
 * Texture3DData is able to manage the pixel data if the OpenGL ES context is lost.
 * </p>
 * <p>
 * Before a call to either {@link #consume3DData()}, Texture3D will bind the OpenGL ES texture.
 * </p>
 *
 * @author mgsx
 */
public interface Texture3DData {

    /**
     * @return whether the TextureData is prepared or not.
     */
    boolean isPrepared();

    /**
     * Prepares the TextureData for a call to {@link #consume3DData()}. This method can be called from a non OpenGL thread and
     * should thus not interact with OpenGL.
     */
    void prepare();

    /**
     * @return the width of this Texture3D
     */
    int getWidth();

    /**
     * @return the height of this Texture3D
     */
    int getHeight();

    /**
     * @return the depth of this Texture3D
     */
    int getDepth();

    /**
     * @return the internal format of this Texture3D
     */
    int getInternalFormat();

    /**
     * @return the GL type of this Texture3D
     */
    int getGLType();

    /**
     * @return whether to generate mipmaps or not.
     */
    boolean useMipMaps();

    /**
     * Uploads the pixel data to the OpenGL ES texture. The caller must bind an OpenGL ES texture. A call to {@link #prepare()}
     * must preceed a call to this method. Any internal data structures created in {@link #prepare()} should be disposed of
     * here.
     */
    void consume3DData();

    /**
     * @return whether this implementation can cope with a EGL context loss.
     */
    boolean isManaged();
}
