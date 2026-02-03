package com.crashinvaders.vfx.common.scene2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RepeatTextureDrawableTest {

    private Texture texture;
    private RepeatTextureDrawable drawable;

    @Before
    public void setUp() {
        texture = mock(Texture.class);
        when(texture.getWidth()).thenReturn(100);
        when(texture.getHeight()).thenReturn(50);
        drawable = new RepeatTextureDrawable(texture);
    }

    @Test
    public void testInitialization() {
        assertEquals(texture, drawable.getTexture());
        assertEquals(100f, drawable.getMinWidth(), 0.01f);
        assertEquals(50f, drawable.getMinHeight(), 0.01f);
    }

    @Test
    public void testCopyConstructor() {
        drawable.setShift(0.5f, 0.2f);
        RepeatTextureDrawable copy = new RepeatTextureDrawable(drawable);
        assertEquals(drawable.getTexture(), copy.getTexture());
        assertEquals(drawable.getMinWidth(), copy.getMinWidth(), 0.01f);
        // Note: shiftX/Y are private, but we can verify behavior if they were public or
        // through draw calls.
        // For now just ensuring it doesn't crash.
    }

    @Test
    public void testSetShift() {
        drawable.setShift(0.5f, 1.2f); // 1.2 % 1.0 = 0.2
        // shiftX = 100 * 0.5 = 50
        // shiftY = 50 * 0.2 = 10

        Batch batch = mock(Batch.class);
        drawable.draw(batch, 0, 0, 100, 100);

        verify(batch).draw(eq(texture), eq(0f), eq(0f), eq(100f), eq(100f), eq(50), eq(10), eq(100), eq(100), eq(false),
                eq(false));
    }

    @Test
    public void testSetTextureNull() {
        drawable.setTexture(null);
        assertNull(drawable.getTexture());
    }

    @Test
    public void testTransformDraw() {
        Batch batch = mock(Batch.class);
        drawable.setShift(0.1f, 0.1f);
        drawable.draw(batch, 10, 20, 5, 5, 100, 100, 1f, 1f, 0f);

        verify(batch).draw(eq(texture), eq(10f), eq(20f), eq(5f), eq(5f), eq(100f), eq(100f), eq(1f), eq(1f), eq(0f),
                anyInt(), anyInt(), anyInt(), anyInt(), eq(false), eq(false));
    }
}
