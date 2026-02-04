package com.crashinvaders.vfx.effects.util;

import com.crashinvaders.vfx.VfxRenderContext;
import com.crashinvaders.vfx.effects.VfxEffectTestBase;
import com.crashinvaders.vfx.framebuffer.VfxFrameBuffer;
import com.crashinvaders.vfx.utils.ViewportQuadMesh;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CombineEffectTest extends VfxEffectTestBase {

    private CombineEffect effect;

    @Before
    public void setUp() {
        effect = new CombineEffect();
    }

    @Test
    public void testProperties() {
        effect.setSource1Intensity(0.5f);
        assertEquals(0.5f, effect.getSource1Intensity(), 0.001f);
        verify(shaderProgram).setUniformf("u_src0Intensity", 0.5f);

        effect.setSource2Intensity(0.8f);
        assertEquals(0.8f, effect.getSource2Intensity(), 0.001f);
        verify(shaderProgram).setUniformf("u_src1Intensity", 0.8f);

        effect.setSource1Saturation(1.2f);
        assertEquals(1.2f, effect.getSource1Saturation(), 0.001f);
        verify(shaderProgram).setUniformf("u_src0Saturation", 1.2f);

        effect.setSource2Saturation(0.3f);
        assertEquals(0.3f, effect.getSource2Saturation(), 0.001f);
        verify(shaderProgram).setUniformf("u_src1Saturation", 0.3f);
    }

    @Test
    public void testRebind() {
        effect.rebind();
        verify(shaderProgram, atLeastOnce()).setUniformi("u_texture0", 0);
        verify(shaderProgram, atLeastOnce()).setUniformi("u_texture1", 1);
    }

    @Test
    public void testRender() {
        VfxRenderContext context = mock(VfxRenderContext.class);
        ViewportQuadMesh mesh = mock(ViewportQuadMesh.class);
        when(context.getViewportMesh()).thenReturn(mesh);

        VfxFrameBuffer src0 = mock(VfxFrameBuffer.class, "src0");
        VfxFrameBuffer src1 = mock(VfxFrameBuffer.class, "src1");
        VfxFrameBuffer dst = mock(VfxFrameBuffer.class, "dst");

        com.badlogic.gdx.graphics.Texture tex0 = mock(com.badlogic.gdx.graphics.Texture.class, "tex0");
        com.badlogic.gdx.graphics.Texture tex1 = mock(com.badlogic.gdx.graphics.Texture.class, "tex1");

        when(src0.getTexture()).thenReturn(tex0);
        when(src1.getTexture()).thenReturn(tex1);

        effect.render(context, src0, src1, dst);

        verify(tex0).bind(0);
        verify(tex1).bind(1);
        verify(mesh).render(shaderProgram);
    }
}
