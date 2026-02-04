package com.crashinvaders.vfx.effects.util;

import com.crashinvaders.vfx.VfxRenderContext;
import com.crashinvaders.vfx.effects.VfxEffectTestBase;
import com.crashinvaders.vfx.framebuffer.VfxFrameBuffer;
import com.crashinvaders.vfx.framebuffer.VfxPingPongWrapper;
import com.crashinvaders.vfx.utils.ViewportQuadMesh;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CopyEffectTest extends VfxEffectTestBase {

    private CopyEffect effect;

    @Before
    public void setUp() {
        effect = new CopyEffect();
    }

    @Test
    public void testRebind() {
        effect.rebind();
        verify(shaderProgram, atLeastOnce()).setUniformi("u_texture0", 0);
    }

    @Test
    public void testRender() {
        VfxRenderContext context = mock(VfxRenderContext.class);
        ViewportQuadMesh mesh = mock(ViewportQuadMesh.class);
        when(context.getViewportMesh()).thenReturn(mesh);

        VfxFrameBuffer src = mock(VfxFrameBuffer.class, "src");
        VfxFrameBuffer dst = mock(VfxFrameBuffer.class, "dst");
        com.badlogic.gdx.graphics.Texture tex = mock(com.badlogic.gdx.graphics.Texture.class, "tex");
        when(src.getTexture()).thenReturn(tex);

        effect.render(context, src, dst);

        verify(tex).bind(0);
        verify(mesh).render(shaderProgram);
    }

    @Test
    public void testRenderPingPong() {
        VfxRenderContext context = mock(VfxRenderContext.class);
        ViewportQuadMesh mesh = mock(ViewportQuadMesh.class);
        when(context.getViewportMesh()).thenReturn(mesh);

        VfxPingPongWrapper buffers = mock(VfxPingPongWrapper.class);
        VfxFrameBuffer src = mock(VfxFrameBuffer.class, "src");
        VfxFrameBuffer dst = mock(VfxFrameBuffer.class, "dst");
        com.badlogic.gdx.graphics.Texture tex = mock(com.badlogic.gdx.graphics.Texture.class, "tex");

        when(buffers.getSrcBuffer()).thenReturn(src);
        when(buffers.getDstBuffer()).thenReturn(dst);
        when(src.getTexture()).thenReturn(tex);

        effect.render(context, buffers);

        verify(tex).bind(0);
        verify(mesh).render(shaderProgram);
    }
}
