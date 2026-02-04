package com.crashinvaders.vfx.effects.util;

import com.crashinvaders.vfx.VfxRenderContext;
import com.crashinvaders.vfx.effects.VfxEffectTestBase;
import com.crashinvaders.vfx.framebuffer.VfxFrameBuffer;
import com.crashinvaders.vfx.utils.ViewportQuadMesh;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GammaThresholdEffectTest extends VfxEffectTestBase {

    private GammaThresholdEffect effect;

    @Before
    public void setUp() {
        effect = new GammaThresholdEffect(GammaThresholdEffect.Type.RGB);
    }

    @Test
    public void testProperties() {
        effect.setGamma(0.5f);
        assertEquals(0.5f, effect.getGamma(), 0.001f);
        verify(shaderProgram).setUniformf("u_threshold", 0.5f);
        verify(shaderProgram).setUniformf("u_thresholdInv", 1f / (1f - 0.5f));
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
}
