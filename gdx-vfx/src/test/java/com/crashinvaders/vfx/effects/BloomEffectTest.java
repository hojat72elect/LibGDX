package com.crashinvaders.vfx.effects;

import com.badlogic.gdx.graphics.GL20;
import com.crashinvaders.vfx.VfxRenderContext;
import com.crashinvaders.vfx.framebuffer.VfxFrameBuffer;
import com.crashinvaders.vfx.framebuffer.VfxFrameBufferPool;
import com.crashinvaders.vfx.framebuffer.VfxPingPongWrapper;
import com.crashinvaders.vfx.gl.VfxGLUtils;
import com.crashinvaders.vfx.utils.ViewportQuadMesh;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BloomEffectTest extends VfxEffectTestBase {

    private BloomEffect effect;

    @Before
    public void setUp() {
        effect = new BloomEffect();
    }

    @Test
    public void testSettings() {
        BloomEffect.Settings settings = new BloomEffect.Settings(5, 0.5f, 1.2f, 0.9f, 0.7f, 1.1f);
        effect.applySettings(settings);

        assertEquals(0.5f, effect.getThreshold(), 0.001f);
        assertEquals(1.2f, effect.getBaseIntensity(), 0.001f);
        assertEquals(0.9f, effect.getBaseSaturation(), 0.001f);
        assertEquals(0.7f, effect.getBloomIntensity(), 0.001f);
        assertEquals(1.1f, effect.getBloomSaturation(), 0.001f);
        assertEquals(5, effect.getBlurPasses());
    }

    @Test
    public void testRender() {
        VfxRenderContext context = mock(VfxRenderContext.class);
        ViewportQuadMesh mesh = mock(ViewportQuadMesh.class);
        when(context.getViewportMesh()).thenReturn(mesh);

        VfxFrameBufferPool pool = mock(VfxFrameBufferPool.class);
        when(context.getBufferPool()).thenReturn(pool);

        VfxFrameBuffer origSrc = mock(VfxFrameBuffer.class, "origSrc");
        when(pool.obtain()).thenReturn(origSrc);

        VfxPingPongWrapper buffers = mock(VfxPingPongWrapper.class);
        VfxFrameBuffer src = mock(VfxFrameBuffer.class, "src");
        VfxFrameBuffer dst = mock(VfxFrameBuffer.class, "dst");
        when(buffers.getSrcBuffer()).thenReturn(src);
        when(buffers.getDstBuffer()).thenReturn(dst);

        com.badlogic.gdx.graphics.Texture tex = mock(com.badlogic.gdx.graphics.Texture.class, "tex");
        when(src.getTexture()).thenReturn(tex);
        when(origSrc.getTexture()).thenReturn(tex);

        vfxGlUtilsMock.when(() -> VfxGLUtils.isGLEnabled(GL20.GL_BLEND)).thenReturn(false);

        effect.render(context, buffers);

        // Verify some key steps in BloomEffect.render
        verify(pool).obtain();
        verify(pool).free(origSrc);
        verify(buffers, atLeastOnce()).swap();
    }
}
