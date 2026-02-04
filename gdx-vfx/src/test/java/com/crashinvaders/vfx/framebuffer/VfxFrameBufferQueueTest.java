package com.crashinvaders.vfx.framebuffer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VfxFrameBufferQueueTest {

    @Mock
    VfxFrameBuffer buffer1;
    @Mock
    VfxFrameBuffer buffer2;
    @Mock
    VfxFrameBuffer buffer3;
    @Mock
    FrameBuffer fbo;
    @Mock
    Texture texture;
    private VfxFrameBufferQueue queue;

    @Before
    public void setUp() {
        queue = new VfxFrameBufferQueue(buffer1, buffer2, buffer3);

        when(buffer1.getFbo()).thenReturn(fbo);
        when(buffer2.getFbo()).thenReturn(fbo);
        when(buffer3.getFbo()).thenReturn(fbo);
        when(fbo.getColorBufferTexture()).thenReturn(texture);
    }

    @Test
    public void testGetCurrent() {
        VfxFrameBuffer buffer = queue.getCurrent();
        assertNotNull(buffer);
        assertEquals(buffer1, buffer);
    }

    @Test
    public void testChangeToNext() {
        VfxFrameBuffer first = queue.getCurrent();
        VfxFrameBuffer second = queue.changeToNext();
        VfxFrameBuffer third = queue.changeToNext();
        VfxFrameBuffer fourth = queue.changeToNext();

        assertEquals(buffer1, first);
        assertEquals(buffer2, second);
        assertEquals(buffer3, third);
        assertEquals(buffer1, fourth);
    }

    @Test
    public void testResize() {
        queue.resize(100, 100);
        verify(buffer1).initialize(100, 100);
        verify(buffer2).initialize(100, 100);
        verify(buffer3).initialize(100, 100);
    }

    @Test
    public void testDispose() {
        queue.dispose();
        verify(buffer1, never()).dispose(); // Because ownsBuffers is false
        verify(buffer2, never()).dispose();
        verify(buffer3, never()).dispose();
    }

    @Test
    public void testRebind() {
        queue.rebind();
        verify(texture, times(3)).setWrap(any(), any());
        verify(texture, times(3)).setFilter(any(), any());
    }
}
