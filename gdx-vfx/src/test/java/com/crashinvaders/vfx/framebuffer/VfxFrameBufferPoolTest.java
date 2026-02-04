package com.crashinvaders.vfx.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VfxFrameBufferPoolTest {

    @Mock
    FrameBuffer mockFbo;
    @Mock
    Texture mockTexture;
    
    private VfxFrameBufferPool pool;

    @Before
    public void setUp() {
        // Mock Gdx.app to avoid NPE on logging
        Gdx.app = mock(com.badlogic.gdx.Application.class);

        // Setup default mock behavior
        when(mockFbo.getWidth()).thenReturn(100);
        when(mockFbo.getHeight()).thenReturn(100);

        pool = new VfxFrameBufferPool(Pixmap.Format.RGBA8888, 100, 100, 10) {
            @Override
            protected VfxFrameBuffer createBuffer() {
                VfxFrameBuffer buffer = mock(VfxFrameBuffer.class);
                when(buffer.getFbo()).thenReturn(mockFbo);
                when(buffer.getTexture()).thenReturn(mockTexture);
                when(buffer.getPixelFormat()).thenReturn(Pixmap.Format.RGBA8888);
                when(buffer.isInitialized()).thenReturn(true);
                
                this.managedBuffers.add(buffer);
                return buffer;
            }
        };
    }

    @Test
    public void testObtain() {
        VfxFrameBuffer buffer = pool.obtain();
        assertNotNull(buffer);
        assertTrue(pool.managedBuffers.contains(buffer, true));
    }

    @Test
    public void testFree() {
        VfxFrameBuffer buffer = pool.obtain();
        pool.free(buffer);
        
        assertEquals(1, pool.getFreeCount());

        verify(buffer).clearRenderers();
        verify(mockTexture).setWrap(any(), any());
        verify(mockTexture).setFilter(any(), any());
    }

    @Test
    public void testResize() {
        VfxFrameBuffer buffer = pool.obtain();

        // Resize pool to 200x200
        pool.resize(200, 200);

        // buffer should be re-initialized because 200 != 100
        verify(buffer).initialize(200, 200);
    }

    @Test
    public void testDispose() {
        VfxFrameBuffer buffer = pool.obtain();
        pool.dispose();

        verify(buffer).dispose();

        try {
            pool.obtain();
            fail("Should have thrown an exception");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}
