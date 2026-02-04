package com.crashinvaders.vfx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Disposable;
import com.crashinvaders.vfx.framebuffer.VfxFrameBufferPool;
import com.crashinvaders.vfx.framebuffer.VfxFrameBufferRenderer;
import com.crashinvaders.vfx.utils.ViewportQuadMesh;

public class VfxRenderContext implements Disposable {

    private final VfxFrameBufferPool bufferPool;
    private final VfxFrameBufferRenderer bufferRenderer;
    private final Pixmap.Format pixelFormat;

    private int bufferWidth;
    private int bufferHeight;

    public VfxRenderContext(Pixmap.Format pixelFormat, int bufferWidth, int bufferHeight) {
        this(new VfxFrameBufferPool(pixelFormat, bufferWidth, bufferHeight, 8),
                new VfxFrameBufferRenderer(),
                pixelFormat, bufferWidth, bufferHeight);
    }

    /** For testing purposes. */
    VfxRenderContext(VfxFrameBufferPool bufferPool, VfxFrameBufferRenderer bufferRenderer,
            Pixmap.Format pixelFormat, int bufferWidth, int bufferHeight) {
        this.bufferPool = bufferPool;
        this.bufferRenderer = bufferRenderer;
        this.pixelFormat = pixelFormat;
        this.bufferWidth = bufferWidth;
        this.bufferHeight = bufferHeight;
    }

    @Override
    public void dispose() {
        bufferPool.dispose();
        bufferRenderer.dispose();
    }

    public void resize(int bufferWidth, int bufferHeight) {
        this.bufferWidth = bufferWidth;
        this.bufferHeight = bufferHeight;
        this.bufferPool.resize(bufferWidth, bufferHeight);
    }

    public VfxFrameBufferPool getBufferPool() {
        return bufferPool;
    }

    public void rebind() {
        bufferRenderer.rebind();
    }

    public Pixmap.Format getPixelFormat() {
        return pixelFormat;
    }

    public VfxFrameBufferRenderer getBufferRenderer() {
        return bufferRenderer;
    }

    public ViewportQuadMesh getViewportMesh() {
        return bufferRenderer.getMesh();
    }

    public int getBufferWidth() {
        return bufferWidth;
    }

    public int getBufferHeight() {
        return bufferHeight;
    }
}