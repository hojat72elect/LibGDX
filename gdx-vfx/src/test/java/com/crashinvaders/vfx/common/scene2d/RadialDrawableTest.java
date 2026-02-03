package com.crashinvaders.vfx.common.scene2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RadialDrawableTest {

    private Texture texture;
    private TextureRegion textureRegion;
    private RadialDrawable radialDrawable;

    @Before
    public void setUp() {
        texture = mock(Texture.class);
        textureRegion = mock(TextureRegion.class);
        when(textureRegion.getTexture()).thenReturn(texture);
        when(textureRegion.getU()).thenReturn(0f);
        when(textureRegion.getV()).thenReturn(0f);
        when(textureRegion.getU2()).thenReturn(1f);
        when(textureRegion.getV2()).thenReturn(1f);
        when(textureRegion.getRegionWidth()).thenReturn(100);
        when(textureRegion.getRegionHeight()).thenReturn(100);

        radialDrawable = new RadialDrawable(textureRegion);
    }

    @Test
    public void testInitialization() {
        assertEquals(100f, radialDrawable.getMinWidth(), 0.01f);
        assertEquals(100f, radialDrawable.getMinHeight(), 0.01f);
    }

    @Test
    public void testSetAngle() {
        radialDrawable.setAngle(90f);
        assertEquals(90f, radialDrawable.getAngle(), 0.01f);

        // Setting same angle should not trigger anything (though hard to verify without
        // internal state access)
        radialDrawable.setAngle(90f);
        assertEquals(90f, radialDrawable.getAngle(), 0.01f);
    }

    @Test
    public void testSetProperties() {
        radialDrawable.setLeftWidth(10f);
        assertEquals(10f, radialDrawable.getLeftWidth(), 0.01f);

        radialDrawable.setRightWidth(20f);
        assertEquals(20f, radialDrawable.getRightWidth(), 0.01f);

        radialDrawable.setTopHeight(30f);
        assertEquals(30f, radialDrawable.getTopHeight(), 0.01f);

        radialDrawable.setBottomHeight(40f);
        assertEquals(40f, radialDrawable.getBottomHeight(), 0.01f);

        radialDrawable.setMinWidth(50f);
        assertEquals(50f, radialDrawable.getMinWidth(), 0.01f);

        radialDrawable.setMinHeight(60f);
        assertEquals(60f, radialDrawable.getMinHeight(), 0.01f);
    }

    @Test
    public void testDrawDelegation() {
        Batch batch = mock(Batch.class);
        when(batch.getPackedColor()).thenReturn(1f);

        radialDrawable.setAngle(180f);
        radialDrawable.draw(batch, 0, 0, 100, 100);

        // Verify that batch.draw was called with the texture and some vertices
        // The number of vertices depends on the angle (draw * 20)
        verify(batch).draw(eq(texture), any(float[].class), eq(0), anyInt());
    }

    @Test
    public void testStartAngleAndDirection() {
        // These are mostly setter tests as the calculation logic is private
        radialDrawable.setStartAngle(RadialDrawable.StartAngle.TOP);
        radialDrawable.setClockwiseDirection(true);

        // No crash is a good sign
        Batch batch = mock(Batch.class);
        when(batch.getPackedColor()).thenReturn(1f);
        radialDrawable.draw(batch, 0, 0, 100, 100);
        verify(batch).draw(eq(texture), any(float[].class), eq(0), anyInt());
    }

    @Test
    public void testSetOriginAndScale() {
        radialDrawable.setOrigin(10, 20);
        radialDrawable.setScale(2, 3);

        // No crash on draw
        Batch batch = mock(Batch.class);
        when(batch.getPackedColor()).thenReturn(1f);
        radialDrawable.draw(batch, 0, 0, 100, 100);
        verify(batch).draw(eq(texture), any(float[].class), eq(0), anyInt());
    }

    @Test
    public void testNegativeDimensions() {
        Batch batch = mock(Batch.class);
        when(batch.getPackedColor()).thenReturn(1f);

        // This should trigger scaleX = -1f and scaleY = -1f internally
        radialDrawable.draw(batch, 0, 0, -100, -100, 0f);

        verify(batch).draw(eq(texture), any(float[].class), eq(0), anyInt());
    }

    @Test
    public void testVariousAngles() {
        Batch batch = mock(Batch.class);
        when(batch.getPackedColor()).thenReturn(1f);

        float[] testAngles = {0f, 45f, 90f, 135f, 180f, 225f, 270f, 315f, 360f};
        for (float ang : testAngles) {
            radialDrawable.draw(batch, 0, 0, 100, 100, ang);
        }

        verify(batch, times(testAngles.length)).draw(eq(texture), any(float[].class), eq(0), anyInt());
    }
}
