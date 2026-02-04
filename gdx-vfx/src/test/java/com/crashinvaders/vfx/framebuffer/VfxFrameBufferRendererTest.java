package com.crashinvaders.vfx.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.crashinvaders.vfx.utils.ViewportQuadMesh;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VfxFrameBufferRendererTest {

    @Mock
    GL20 gl20;
    @Mock
    ViewportQuadMesh mesh;
    @Mock
    ShaderProgram shader;
    @Mock
    Texture texture;
    @Mock
    VfxFrameBuffer vfxFrameBuffer;

    private AutoCloseable mocks;
    private VfxFrameBufferRenderer renderer;

    @Before
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        Gdx.gl = gl20;
        Gdx.gl20 = gl20;
        Gdx.graphics = mock(com.badlogic.gdx.Graphics.class);
        when(Gdx.graphics.getGL20()).thenReturn(gl20);

        renderer = new VfxFrameBufferRenderer(mesh, shader);
        reset(shader);
    }

    @After
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void testRebind() {
        renderer.rebind();
        verify(shader).begin();
        verify(shader).setUniformi("u_texture0", 0);
        verify(shader).end();
    }

    @Test
    public void testRenderToScreen() {
        when(Gdx.graphics.getBackBufferWidth()).thenReturn(800);
        when(Gdx.graphics.getBackBufferHeight()).thenReturn(600);

        renderer.renderToScreen(texture);

        verify(texture).bind(0);
        verify(gl20).glViewport(0, 0, 800, 600);
        verify(shader).begin();
        verify(mesh).render(shader);
        verify(shader).end();
    }

    @Test
    public void testRenderToScreenWithParams() {
        renderer.renderToScreen(texture, 10, 20, 100, 200);

        verify(texture).bind(0);
        verify(gl20).glViewport(10, 20, 100, 200);
        verify(shader).begin();
        verify(mesh).render(shader);
        verify(shader).end();
    }

    @Test
    public void testRenderToFbo() {
        VfxFrameBuffer dstBuf = mock(VfxFrameBuffer.class);

        renderer.renderToFbo(texture, dstBuf);

        verify(texture).bind(0);
        verify(dstBuf).begin();
        verify(shader).begin();
        verify(mesh).render(shader);
        verify(shader).end();
        verify(dstBuf).end();
    }

    @Test
    public void testDispose() {
        renderer.dispose();
        verify(shader, never()).dispose(); // Because ownsShader is false
        verify(mesh).dispose();
    }
}
