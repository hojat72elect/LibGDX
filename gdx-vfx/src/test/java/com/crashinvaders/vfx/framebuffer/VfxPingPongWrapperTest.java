package com.crashinvaders.vfx.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VfxPingPongWrapperTest {

    @Mock
    GL20 gl20;
    @Mock
    VfxFrameBuffer buffer1;
    @Mock
    VfxFrameBuffer buffer2;
    @Mock
    FrameBuffer fbo1;
    @Mock
    FrameBuffer fbo2;
    @Mock
    Texture texture1;
    @Mock
    Texture texture2;
    @Mock
    VfxFrameBufferPool bufferPool;

    private AutoCloseable mocks;

    @Before
    public void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);
        Gdx.gl = gl20;
        Gdx.gl20 = gl20;

        when(buffer1.getFbo()).thenReturn(fbo1);
        when(buffer2.getFbo()).thenReturn(fbo2);
        when(fbo1.getColorBufferTexture()).thenReturn(texture1);
        when(fbo2.getColorBufferTexture()).thenReturn(texture2);
    }

    @After
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void testConstructorWithBuffers() {
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(buffer2, buffer1);
        assertTrue(wrapper.isInitialized());
        assertEquals(buffer1, wrapper.getSrcBuffer());
        assertEquals(buffer2, wrapper.getDstBuffer());
    }

    @Test
    public void testInitializeWithPool() {
        // In initialize(VfxFrameBufferPool), it obtains two buffers.
        // The first one becomes bufSrc, the second one becomes bufDst.
        // Wait, let's re-verify line 59-61 of VfxPingPongWrapper.java
        // 59: VfxFrameBuffer bufDst = bufferPool.obtain();
        // 60: VfxFrameBuffer bufSrc = bufferPool.obtain();
        // 61: return initialize(bufDst, bufSrc);
        // And initialize(VfxFrameBuffer bufSrc, VfxFrameBuffer bufDst)
        // 71: this.bufSrc = bufSrc;
        // 72: this.bufDst = bufDst;
        // So this.bufSrc = local bufDst (1st obtain)
        // this.bufDst = local bufSrc (2nd obtain)

        when(bufferPool.obtain()).thenReturn(buffer1, buffer2);

        VfxPingPongWrapper wrapper = new VfxPingPongWrapper();
        wrapper.initialize(bufferPool);

        assertTrue(wrapper.isInitialized());
        assertEquals(buffer1, wrapper.getSrcBuffer());
        assertEquals(buffer2, wrapper.getDstBuffer());
        verify(bufferPool, times(2)).obtain();
    }

    @Test
    public void testReset() {
        when(bufferPool.obtain()).thenReturn(buffer1, buffer2);
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(bufferPool);

        wrapper.reset();

        assertFalse(wrapper.isInitialized());
        assertNull(wrapper.getSrcBuffer());
        assertNull(wrapper.getDstBuffer());
        verify(bufferPool).free(buffer1);
        verify(bufferPool).free(buffer2);
    }

    @Test
    public void testBeginEnd() {
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(buffer2, buffer1);

        wrapper.begin();
        assertTrue(wrapper.isCapturing());
        verify(buffer2).begin();

        wrapper.end();
        assertFalse(wrapper.isCapturing());
        verify(buffer2).end();
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginTwice() {
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(buffer2, buffer1);
        wrapper.begin();
        wrapper.begin();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndWithoutBegin() {
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(buffer2, buffer1);
        wrapper.end();
    }

    @Test
    public void testSwap() {
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(buffer2, buffer1);

        wrapper.swap();

        assertEquals(buffer2, wrapper.getSrcBuffer());
        assertEquals(buffer1, wrapper.getDstBuffer());
    }

    @Test
    public void testSwapWhileCapturing() {
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(buffer2, buffer1);

        wrapper.begin();
        verify(buffer2).begin();

        wrapper.swap();

        verify(buffer2).end();
        verify(buffer1).begin();

        assertEquals(buffer2, wrapper.getSrcBuffer());
        assertEquals(buffer1, wrapper.getDstBuffer());
        assertTrue(wrapper.isCapturing());
    }

    @Test
    public void testGetters() {
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(buffer2, buffer1);

        assertEquals(buffer1, wrapper.getSrcBuffer());
        assertEquals(buffer2, wrapper.getDstBuffer());
        assertEquals(texture1, wrapper.getSrcTexture());
        assertEquals(texture2, wrapper.getDstTexture());
    }

    @Test
    public void testCleanUpBuffers() {
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(buffer2, buffer1);

        wrapper.cleanUpBuffers(Color.RED);

        verify(gl20).glClearColor(1f, 0f, 0f, 1f);
        verify(gl20, times(2)).glClear(GL20.GL_COLOR_BUFFER_BIT);

        // cleanUpBuffers(Color) calls begin(), swap(), end() internally if not already
        // capturing
        // 1. begin() -> buffer2.begin()
        // 2. swap() -> buffer2.end(), buffer1.begin()
        // 3. end() -> buffer1.end()
        verify(buffer2).begin();
        verify(buffer2).end();
        verify(buffer1).begin();
        verify(buffer1).end();

        // After cleanup, buffers should be swapped
        assertEquals(buffer2, wrapper.getSrcBuffer());
        assertEquals(buffer1, wrapper.getDstBuffer());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeDuringCapturing() {
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(buffer2, buffer1);
        wrapper.begin();
        wrapper.initialize(buffer1, buffer2);
    }

    @Test(expected = IllegalStateException.class)
    public void testResetDuringCapturing() {
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(buffer2, buffer1);
        wrapper.begin();
        wrapper.reset();
    }

    @Test
    public void testInitializeWhileAlreadyInitialized() {
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(buffer2, buffer1);
        assertTrue(wrapper.isInitialized());

        VfxFrameBuffer buffer3 = mock(VfxFrameBuffer.class);
        VfxFrameBuffer buffer4 = mock(VfxFrameBuffer.class);

        wrapper.initialize(buffer3, buffer4);

        assertEquals(buffer3, wrapper.getSrcBuffer());
        assertEquals(buffer4, wrapper.getDstBuffer());
    }

    @Test
    public void testInitializeWithPoolWhileAlreadyInitialized() {
        when(bufferPool.obtain()).thenReturn(buffer1, buffer2, buffer1, buffer2);
        VfxPingPongWrapper wrapper = new VfxPingPongWrapper(bufferPool);

        // This should trigger reset() and thus free() on the pool
        wrapper.initialize(bufferPool);

        verify(bufferPool, times(2)).free(any(VfxFrameBuffer.class));
        verify(bufferPool, times(4)).obtain();
    }
}
