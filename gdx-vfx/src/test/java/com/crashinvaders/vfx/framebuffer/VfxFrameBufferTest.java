package com.crashinvaders.vfx.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.crashinvaders.vfx.gl.VfxGlViewport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VfxFrameBufferTest {

    @Mock
    GL20 gl20;

    private int currentBoundFboHandle = 0;
    private VfxGlViewport mockViewport = new VfxGlViewport().set(0, 0, 800, 600);

    private AutoCloseable mocks;

    @Before
    public void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);
        Gdx.gl = gl20;
        Gdx.gl20 = gl20;

        currentBoundFboHandle = 0;

        // Mock glBindFramebuffer to update currentBoundFboHandle
        doAnswer((Answer<Void>) invocation -> {
            currentBoundFboHandle = invocation.getArgument(1);
            return null;
        }).when(gl20).glBindFramebuffer(eq(GL20.GL_FRAMEBUFFER), anyInt());

        // Reset static bufferNesting via reflection
        Field field = VfxFrameBuffer.class.getDeclaredField("bufferNesting");
        field.setAccessible(true);
        field.setInt(null, 0);
    }

    @After
    public void tearDown() throws Exception {
        mocks.close();
    }

    private class TestVfxFrameBuffer extends VfxFrameBuffer {
        public FrameBuffer mockFbo;

        public TestVfxFrameBuffer(Pixmap.Format pixelFormat) {
            super(pixelFormat);
        }

        @Override
        protected FrameBuffer createFrameBuffer(Pixmap.Format pixelFormat, int width, int height) {
            if (mockFbo == null) {
                mockFbo = mock(FrameBuffer.class);
                Texture texture = mock(Texture.class);
                when(mockFbo.getColorBufferTexture()).thenReturn(texture);
            }
            return mockFbo;
        }

        @Override
        protected void recomputeMatrices(int width, int height) {
            // Do nothing to avoid native code calls
        }

        @Override
        protected int getBoundFboHandle() {
            return currentBoundFboHandle;
        }

        @Override
        protected VfxGlViewport getViewport() {
            return mockViewport;
        }
    }

    private class TestBatchRendererAdapter extends VfxFrameBuffer.BatchRendererAdapter {
        public Matrix4 mockProgMatrix;
        public Matrix4 mockTransMatrix;
        private int createCount = 0;

        public TestBatchRendererAdapter(Batch batch) {
            super(batch);
        }

        @Override
        protected Matrix4 createMatrix() {
            Matrix4 m = mock(Matrix4.class);
            if (createCount == 0)
                mockProgMatrix = m;
            else if (createCount == 1)
                mockTransMatrix = m;
            createCount++;
            return m;
        }
    }

    private class TestShapeRendererAdapter extends VfxFrameBuffer.ShapeRendererAdapter {
        public Matrix4 mockProgMatrix;
        public Matrix4 mockTransMatrix;
        private int createCount = 0;

        public TestShapeRendererAdapter(ShapeRenderer shapeRenderer) {
            super(shapeRenderer);
        }

        @Override
        protected Matrix4 createMatrix() {
            Matrix4 m = mock(Matrix4.class);
            if (createCount == 0)
                mockProgMatrix = m;
            else if (createCount == 1)
                mockTransMatrix = m;
            createCount++;
            return m;
        }
    }

    @Test
    public void testInitialization() {
        TestVfxFrameBuffer vfxFrameBuffer = new TestVfxFrameBuffer(Pixmap.Format.RGBA8888);
        assertFalse(vfxFrameBuffer.isInitialized());

        vfxFrameBuffer.initialize(800, 600);

        assertTrue(vfxFrameBuffer.isInitialized());
        assertNotNull(vfxFrameBuffer.getFbo());
        assertEquals(vfxFrameBuffer.mockFbo, vfxFrameBuffer.getFbo());
        assertEquals(Pixmap.Format.RGBA8888, vfxFrameBuffer.getPixelFormat());
    }

    @Test
    public void testReset() {
        TestVfxFrameBuffer vfxFrameBuffer = new TestVfxFrameBuffer(Pixmap.Format.RGBA8888);
        vfxFrameBuffer.initialize(800, 600);
        FrameBuffer fbo = vfxFrameBuffer.getFbo();

        vfxFrameBuffer.reset();

        assertFalse(vfxFrameBuffer.isInitialized());
        assertNull(vfxFrameBuffer.getFbo());
        verify(fbo).dispose();
    }

    @Test
    public void testBeginEnd() {
        TestVfxFrameBuffer vfxFrameBuffer = new TestVfxFrameBuffer(Pixmap.Format.RGBA8888);
        vfxFrameBuffer.initialize(800, 600);

        when(vfxFrameBuffer.mockFbo.getFramebufferHandle()).thenReturn(456);
        when(vfxFrameBuffer.mockFbo.getWidth()).thenReturn(800);
        when(vfxFrameBuffer.mockFbo.getHeight()).thenReturn(600);

        currentBoundFboHandle = 123;
        mockViewport.set(10, 20, 100, 200);

        vfxFrameBuffer.begin();

        assertTrue(vfxFrameBuffer.isDrawing());
        assertEquals(1, VfxFrameBuffer.getBufferNesting());
        assertEquals(456, currentBoundFboHandle);

        // Verify GL calls for begin
        verify(gl20).glBindFramebuffer(GL20.GL_FRAMEBUFFER, 456);
        verify(gl20).glViewport(0, 0, 800, 600);

        vfxFrameBuffer.end();

        assertFalse(vfxFrameBuffer.isDrawing());
        assertEquals(0, VfxFrameBuffer.getBufferNesting());
        assertEquals(123, currentBoundFboHandle);

        // Verify GL calls for end (restoring state)
        verify(gl20).glBindFramebuffer(GL20.GL_FRAMEBUFFER, 123);
        verify(gl20).glViewport(10, 20, 100, 200);
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginWithoutInitialize() {
        TestVfxFrameBuffer vfxFrameBuffer = new TestVfxFrameBuffer(Pixmap.Format.RGBA8888);
        vfxFrameBuffer.begin();
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginTwice() {
        TestVfxFrameBuffer vfxFrameBuffer = new TestVfxFrameBuffer(Pixmap.Format.RGBA8888);
        vfxFrameBuffer.initialize(800, 600);
        vfxFrameBuffer.begin();
        vfxFrameBuffer.begin();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndWithoutBegin() {
        TestVfxFrameBuffer vfxFrameBuffer = new TestVfxFrameBuffer(Pixmap.Format.RGBA8888);
        vfxFrameBuffer.initialize(800, 600);
        vfxFrameBuffer.end();
    }

    @Test
    public void testRendererCallbacks() {
        TestVfxFrameBuffer vfxFrameBuffer = new TestVfxFrameBuffer(Pixmap.Format.RGBA8888);
        VfxFrameBuffer.Renderer renderer = mock(VfxFrameBuffer.Renderer.class);
        vfxFrameBuffer.addRenderer(renderer);

        vfxFrameBuffer.initialize(800, 600);
        when(vfxFrameBuffer.mockFbo.getFramebufferHandle()).thenReturn(456);
        when(vfxFrameBuffer.mockFbo.getWidth()).thenReturn(800);
        when(vfxFrameBuffer.mockFbo.getHeight()).thenReturn(600);

        vfxFrameBuffer.begin();
        verify(renderer).flush();
        verify(renderer).assignLocalMatrices(any(Matrix4.class), any(Matrix4.class));

        vfxFrameBuffer.end();
        verify(renderer, times(2)).flush(); // One on begin, one on end
        verify(renderer).restoreOwnMatrices();
    }

    @Test
    public void testBatchRendererAdapter() {
        Batch batch = mock(Batch.class);
        Matrix4 originalProj = mock(Matrix4.class);
        Matrix4 originalTrans = mock(Matrix4.class);
        when(batch.getProjectionMatrix()).thenReturn(originalProj);
        when(batch.getTransformMatrix()).thenReturn(originalTrans);

        TestBatchRendererAdapter adapter = new TestBatchRendererAdapter(batch);

        Matrix4 newProj = mock(Matrix4.class);
        Matrix4 trans = mock(Matrix4.class);

        adapter.assignLocalMatrices(newProj, trans);
        verify(batch).getProjectionMatrix();
        verify(batch).setProjectionMatrix(newProj);
        verify(adapter.mockProgMatrix).set(originalProj);

        adapter.flush();
        verify(batch).flush();

        adapter.restoreOwnMatrices();
        verify(batch).setProjectionMatrix(adapter.mockProgMatrix);
    }

    @Test
    public void testShapeRendererAdapter() {
        ShapeRenderer shapeRenderer = mock(ShapeRenderer.class);
        when(shapeRenderer.isDrawing()).thenReturn(true);
        Matrix4 originalProj = mock(Matrix4.class);
        when(shapeRenderer.getProjectionMatrix()).thenReturn(originalProj);
        when(shapeRenderer.getTransformMatrix()).thenReturn(mock(Matrix4.class));

        TestShapeRendererAdapter adapter = new TestShapeRendererAdapter(shapeRenderer);

        adapter.flush();
        verify(shapeRenderer).flush();

        Matrix4 newProj = mock(Matrix4.class);
        adapter.assignLocalMatrices(newProj, mock(Matrix4.class));
        verify(shapeRenderer).setProjectionMatrix(newProj);
        verify(adapter.mockProgMatrix).set(originalProj);

        adapter.restoreOwnMatrices();
        verify(shapeRenderer).setProjectionMatrix(adapter.mockProgMatrix);
    }
}
