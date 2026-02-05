package com.kotcrab.vis.ui.util;

import com.badlogic.gdx.graphics.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ColorUtils}.
 */
public class ColorUtilsTest {

    private static final float EPSILON = 0.001f;

    @Test
    public void testHSVtoRGBRed() {
        Color color = ColorUtils.HSVtoRGB(0, 100, 100);
        assertEquals("Red component should be 1", 1.0f, color.r, EPSILON);
        assertEquals("Green component should be 0", 0.0f, color.g, EPSILON);
        assertEquals("Blue component should be 0", 0.0f, color.b, EPSILON);
        assertEquals("Alpha component should be 1", 1.0f, color.a, EPSILON);
    }

    @Test
    public void testHSVtoRGBGreen() {
        Color color = ColorUtils.HSVtoRGB(120, 100, 100);
        assertEquals("Red component should be 0", 0.0f, color.r, EPSILON);
        assertEquals("Green component should be 1", 1.0f, color.g, EPSILON);
        assertEquals("Blue component should be 0", 0.0f, color.b, EPSILON);
        assertEquals("Alpha component should be 1", 1.0f, color.a, EPSILON);
    }

    @Test
    public void testHSVtoRGBBlue() {
        Color color = ColorUtils.HSVtoRGB(240, 100, 100);
        assertEquals("Red component should be 0", 0.0f, color.r, EPSILON);
        assertEquals("Green component should be 0", 0.0f, color.g, EPSILON);
        assertEquals("Blue component should be 1", 1.0f, color.b, EPSILON);
        assertEquals("Alpha component should be 1", 1.0f, color.a, EPSILON);
    }

    @Test
    public void testHSVtoRGBWhite() {
        Color color = ColorUtils.HSVtoRGB(0, 0, 100);
        assertEquals("Red component should be 1", 1.0f, color.r, EPSILON);
        assertEquals("Green component should be 1", 1.0f, color.g, EPSILON);
        assertEquals("Blue component should be 1", 1.0f, color.b, EPSILON);
        assertEquals("Alpha component should be 1", 1.0f, color.a, EPSILON);
    }

    @Test
    public void testHSVtoRGBBlack() {
        Color color = ColorUtils.HSVtoRGB(0, 0, 0);
        assertEquals("Red component should be 0", 0.0f, color.r, EPSILON);
        assertEquals("Green component should be 0", 0.0f, color.g, EPSILON);
        assertEquals("Blue component should be 0", 0.0f, color.b, EPSILON);
        assertEquals("Alpha component should be 1", 1.0f, color.a, EPSILON);
    }

    @Test
    public void testHSVtoRGBGray() {
        Color color = ColorUtils.HSVtoRGB(0, 0, 50);
        assertEquals("Red component should be approximately 0.5", 0.5f, color.r, 0.01f);
        assertEquals("Green component should be approximately 0.5", 0.5f, color.g, 0.01f);
        assertEquals("Blue component should be approximately 0.5", 0.5f, color.b, 0.01f);
        assertEquals("Alpha component should be 1", 1.0f, color.a, EPSILON);
    }

    @Test
    public void testHSVtoRGBWithAlpha() {
        Color color = ColorUtils.HSVtoRGB(0, 100, 100, 0.5f);
        assertEquals("Red component should be 1", 1.0f, color.r, EPSILON);
        assertEquals("Green component should be 0", 0.0f, color.g, EPSILON);
        assertEquals("Blue component should be 0", 0.0f, color.b, EPSILON);
        assertEquals("Alpha component should be 0.5", 0.5f, color.a, EPSILON);
    }

    @Test
    public void testHSVtoRGBWithTargetColor() {
        Color targetColor = new Color(0.1f, 0.2f, 0.3f, 0.4f);
        Color result = ColorUtils.HSVtoRGB(120, 100, 100, targetColor);

        assertSame("Should return the same target color instance", targetColor, result);
        assertEquals("Red component should be 0", 0.0f, result.r, EPSILON);
        assertEquals("Green component should be 1", 1.0f, result.g, EPSILON);
        assertEquals("Blue component should be 0", 0.0f, result.b, EPSILON);
        assertEquals("Alpha component should be preserved", 0.4f, result.a, EPSILON);
    }

    @Test
    public void testHSVtoRGBBoundaryValues() {
        // Test hue = 360 (should be treated as 359)
        Color color = ColorUtils.HSVtoRGB(360, 100, 100);
        // Hue 359 should be very close to red
        assertTrue("Red component should be high", color.r > 0.9f);
        assertTrue("Green component should be low", color.g < 0.1f);
        assertTrue("Blue component should be low", color.b < 0.1f);

        // Test out of bounds values (should be clamped)
        Color color2 = ColorUtils.HSVtoRGB(-10, 150, 150);
        assertNotNull("Should handle out of bounds values gracefully", color2);

        Color color3 = ColorUtils.HSVtoRGB(400, 150, 150);
        assertNotNull("Should handle out of bounds values gracefully", color3);
    }

    @Test
    public void testRGBtoHSVRed() {
        int[] hsv = ColorUtils.RGBtoHSV(Color.RED);
        assertEquals("Hue should be 0", 0, hsv[0]);
        assertEquals("Saturation should be 100", 100, hsv[1]);
        assertEquals("Value should be 100", 100, hsv[2]);
    }

    @Test
    public void testRGBtoHSVGreen() {
        int[] hsv = ColorUtils.RGBtoHSV(Color.GREEN);
        assertEquals("Hue should be 120", 120, hsv[0]);
        assertEquals("Saturation should be 100", 100, hsv[1]);
        assertEquals("Value should be 100", 100, hsv[2]);
    }

    @Test
    public void testRGBtoHSVBlue() {
        int[] hsv = ColorUtils.RGBtoHSV(Color.BLUE);
        assertEquals("Hue should be 240", 240, hsv[0]);
        assertEquals("Saturation should be 100", 100, hsv[1]);
        assertEquals("Value should be 100", 100, hsv[2]);
    }

    @Test
    public void testRGBtoHSVWhite() {
        int[] hsv = ColorUtils.RGBtoHSV(Color.WHITE);
        assertEquals("Hue should be 0", 0, hsv[0]);
        assertEquals("Saturation should be 0", 0, hsv[1]);
        assertEquals("Value should be 100", 100, hsv[2]);
    }

    @Test
    public void testRGBtoHSVBlack() {
        int[] hsv = ColorUtils.RGBtoHSV(Color.BLACK);
        assertEquals("Hue should be 0", 0, hsv[0]);
        assertEquals("Saturation should be 0", 0, hsv[1]);
        assertTrue("Value should be 0 or very close", hsv[2] <= 1);
    }

    @Test
    public void testRGBtoHSVGray() {
        Color gray = new Color(0.5f, 0.5f, 0.5f, 1.0f);
        int[] hsv = ColorUtils.RGBtoHSV(gray);
        assertEquals("Hue should be 0", 0, hsv[0]);
        assertEquals("Saturation should be 0", 0, hsv[1]);
        assertEquals("Value should be 50", 50, hsv[2]);
    }

    @Test
    public void testRGBtoHSVWithRGBValues() {
        int[] hsv = ColorUtils.RGBtoHSV(1.0f, 0.0f, 0.0f);
        assertEquals("Hue should be 0", 0, hsv[0]);
        assertEquals("Saturation should be 100", 100, hsv[1]);
        assertEquals("Value should be 100", 100, hsv[2]);
    }

    @Test
    public void testRGBtoHSVArrayLength() {
        int[] hsv = ColorUtils.RGBtoHSV(Color.RED);
        assertEquals("HSV array should have 3 elements", 3, hsv.length);
    }

    @Test
    public void testHSVtoRGBRoundTrip() {
        // Test that converting HSV to RGB and back gives approximately the same values
        int[] originalHSV = {180, 75, 60};
        Color rgbColor = ColorUtils.HSVtoRGB(originalHSV[0], originalHSV[1], originalHSV[2]);
        int[] convertedHSV = ColorUtils.RGBtoHSV(rgbColor);

        assertEquals("Hue should be preserved within rounding error", originalHSV[0], convertedHSV[0]);
        assertEquals("Saturation should be preserved within rounding error", originalHSV[1], convertedHSV[1]);
        assertEquals("Value should be preserved within rounding error", originalHSV[2], convertedHSV[2]);
    }

    @Test
    public void testRGBtoHSVRoundTrip() {
        // Test that converting RGB to HSV and back gives approximately the same values
        Color originalRGB = new Color(0.3f, 0.7f, 0.9f, 1.0f);
        int[] hsv = ColorUtils.RGBtoHSV(originalRGB);
        Color convertedRGB = ColorUtils.HSVtoRGB(hsv[0], hsv[1], hsv[2]);

        assertEquals("Red component should be preserved within rounding error", originalRGB.r, convertedRGB.r, 0.01f);
        assertEquals("Green component should be preserved within rounding error", originalRGB.g, convertedRGB.g, 0.01f);
        assertEquals("Blue component should be preserved within rounding error", originalRGB.b, convertedRGB.b, 0.01f);
    }

    @Test
    public void testHSVtoRGBVariousHues() {
        // Test various hues around the color wheel
        for (int hue = 0; hue < 360; hue += 30) {
            Color color = ColorUtils.HSVtoRGB(hue, 100, 100);
            assertNotNull("Color should not be null for hue " + hue, color);
            assertTrue("Red component should be valid for hue " + hue, color.r >= 0 && color.r <= 1);
            assertTrue("Green component should be valid for hue " + hue, color.g >= 0 && color.g <= 1);
            assertTrue("Blue component should be valid for hue " + hue, color.b >= 0 && color.b <= 1);
        }
    }
}
