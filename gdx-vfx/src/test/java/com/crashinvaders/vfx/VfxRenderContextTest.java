package com.crashinvaders.vfx;

import com.badlogic.gdx.graphics.Pixmap;
import com.crashinvaders.vfx.framebuffer.VfxFrameBufferPool;
import com.crashinvaders.vfx.framebuffer.VfxFrameBufferRenderer;
import com.crashinvaders.vfx.utils.ViewportQuadMesh;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class VfxRenderContextTest {

    @Mock
    VfxFrameBufferPool bufferPool;
    @Mock
    VfxFrameBufferRenderer bufferRenderer;

    private VfxRenderContext context;
    private final Pixmap.Format format = Pixmap.Format.RGBA8888;
    private final int width = 800;
    private final int height = 600;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        context = new VfxRenderContext(bufferPool, bufferRenderer, format, width, height);
    }

    @Test
    public void testGetters() {
        assertEquals(bufferPool, context.getBufferPool());
        assertEquals(bufferRenderer, context.getBufferRenderer());
        assertEquals(format, context.getPixelFormat());
        assertEquals(width, context.getBufferWidth());
        assertEquals(height, context.getBufferHeight());
    }

    @Test
    public void testResize() {
        int newWidth = 1024;
        int newHeight = 768;

        context.resize(newWidth, newHeight);

        assertEquals(newWidth, context.getBufferWidth());
        assertEquals(newHeight, context.getBufferHeight());
        verify(bufferPool).resize(newWidth, newHeight);
    }

    @Test
    public void testRebind() {
        context.rebind();
        verify(bufferRenderer).rebind();
    }

    @Test
    public void testGetViewportMesh() {
        ViewportQuadMesh mesh = mock(ViewportQuadMesh.class);
        when(bufferRenderer.getMesh()).thenReturn(mesh);

        assertEquals(mesh, context.getViewportMesh());
        verify(bufferRenderer).getMesh();
    }

    @Test
    public void testDispose() {
        context.dispose();

        verify(bufferPool).dispose();
        verify(bufferRenderer).dispose();
    }
}
