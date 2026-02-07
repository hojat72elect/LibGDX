package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link VisProgressBar}.
 */
public class VisProgressBarTest {

    @Test
    public void testConstructorHorizontal() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        
        Assert.assertNotNull("Progress bar should be created", progressBar);
        Assert.assertEquals("Min value should be 0", 0f, progressBar.getMinValue(), 0.0001f);
        Assert.assertEquals("Max value should be 100", 100f, progressBar.getMaxValue(), 0.0001f);
        Assert.assertEquals("Step size should be 1", 1f, progressBar.getStepSize(), 0.0001f);
        Assert.assertFalse("Should be horizontal", progressBar.isVertical());
        Assert.assertNotNull("Style should not be null", progressBar.getStyle());
    }

    @Test
    public void testConstructorVertical() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 5f, true);
        
        Assert.assertNotNull("Progress bar should be created", progressBar);
        Assert.assertEquals("Min value should be 0", 0f, progressBar.getMinValue(), 0.0001f);
        Assert.assertEquals("Max value should be 100", 100f, progressBar.getMaxValue(), 0.0001f);
        Assert.assertEquals("Step size should be 5", 5f, progressBar.getStepSize(), 0.0001f);
        Assert.assertTrue("Should be vertical", progressBar.isVertical());
        Assert.assertNotNull("Style should not be null", progressBar.getStyle());
    }

    @Test
    public void testConstructorWithStyleName() {
        VisProgressBar progressBar = new VisProgressBar(0f, 50f, 2.5f, false, "default-horizontal");
        
        Assert.assertNotNull("Progress bar should be created", progressBar);
        Assert.assertEquals("Min value should be 0", 0f, progressBar.getMinValue(), 0.0001f);
        Assert.assertEquals("Max value should be 50", 50f, progressBar.getMaxValue(), 0.0001f);
        Assert.assertEquals("Step size should be 2.5", 2.5f, progressBar.getStepSize(), 0.0001f);
        Assert.assertFalse("Should be horizontal", progressBar.isVertical());
        Assert.assertNotNull("Style should not be null", progressBar.getStyle());
    }

    @Test
    public void testConstructorWithStyle() {
        ProgressBar.ProgressBarStyle mockStyle = Mockito.mock(ProgressBar.ProgressBarStyle.class);
        VisProgressBar progressBar = new VisProgressBar(10f, 90f, 0.1f, true, mockStyle);
        
        Assert.assertNotNull("Progress bar should be created", progressBar);
        Assert.assertEquals("Min value should be 10", 10f, progressBar.getMinValue(), 0.0001f);
        Assert.assertEquals("Max value should be 90", 90f, progressBar.getMaxValue(), 0.0001f);
        Assert.assertEquals("Step size should be 0.1", 0.1f, progressBar.getStepSize(), 0.0001f);
        Assert.assertTrue("Should be vertical", progressBar.isVertical());
        Assert.assertSame("Style should be set", mockStyle, progressBar.getStyle());
    }

    @Test
    public void testSetValue() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        
        progressBar.setValue(25f);
        Assert.assertEquals("Value should be 25", 25f, progressBar.getValue(), 0.0001f);
        
        progressBar.setValue(75f);
        Assert.assertEquals("Value should be 75", 75f, progressBar.getValue(), 0.0001f);
    }

    @Test
    public void testSetValueOutOfBounds() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        
        // Test value below minimum
        progressBar.setValue(-10f);
        Assert.assertEquals("Value should be clamped to min", 0f, progressBar.getValue(), 0.0001f);
        
        // Test value above maximum
        progressBar.setValue(150f);
        Assert.assertEquals("Value should be clamped to max", 100f, progressBar.getValue(), 0.0001f);
    }

    @Test
    public void testSetMinValue() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        progressBar.setValue(50f);
        
        progressBar.setMinValue(25f);
        Assert.assertEquals("Min value should be updated", 25f, progressBar.getMinValue(), 0.0001f);
        Assert.assertEquals("Value should be clamped to new min", 50f, progressBar.getValue(), 0.0001f);
    }

    @Test
    public void testSetMaxValue() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        progressBar.setValue(50f);
        
        progressBar.setMaxValue(75f);
        Assert.assertEquals("Max value should be updated", 75f, progressBar.getMaxValue(), 0.0001f);
        Assert.assertEquals("Value should be clamped to new max", 50f, progressBar.getValue(), 0.0001f);
    }

    @Test
    public void testSetRange() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        
        progressBar.setRange(20f, 80f);
        Assert.assertEquals("Min value should be updated", 20f, progressBar.getMinValue(), 0.0001f);
        Assert.assertEquals("Max value should be updated", 80f, progressBar.getMaxValue(), 0.0001f);
        Assert.assertEquals("Value should be clamped", 20f, progressBar.getValue(), 0.0001f);
    }

    @Test
    public void testSetStepSize() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        
        progressBar.setStepSize(5f);
        Assert.assertEquals("Step size should be updated", 5f, progressBar.getStepSize(), 0.0001f);
    }

    @Test
    public void testGetVisualValue() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        progressBar.setValue(37.5f);
        
        float visualValue = progressBar.getVisualValue();
        Assert.assertEquals("Visual value should match actual value", 37.5f, visualValue, 0.0001f);
    }

    @Test
    public void testGetPercent() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        
        progressBar.setValue(0f);
        Assert.assertEquals("0% should be 0", 0f, progressBar.getPercent(), 0.0001f);
        
        progressBar.setValue(50f);
        Assert.assertEquals("50% should be 0.5", 0.5f, progressBar.getPercent(), 0.0001f);
        
        progressBar.setValue(100f);
        Assert.assertEquals("100% should be 1", 1f, progressBar.getPercent(), 0.0001f);
    }

    @Test
    public void testGetPercentWithCustomRange() {
        VisProgressBar progressBar = new VisProgressBar(50f, 150f, 1f, false);
        
        progressBar.setValue(50f);
        Assert.assertEquals("Min value should be 0%", 0f, progressBar.getPercent(), 0.0001f);
        
        progressBar.setValue(100f);
        Assert.assertEquals("Middle value should be 50%", 0.5f, progressBar.getPercent(), 0.0001f);
        
        progressBar.setValue(150f);
        Assert.assertEquals("Max value should be 100%", 1f, progressBar.getPercent(), 0.0001f);
    }

    @Test
    public void testGetPrefWidth() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        
        float prefWidth = progressBar.getPrefWidth();
        Assert.assertTrue("Pref width should be positive for horizontal", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeight() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        
        float prefHeight = progressBar.getPrefHeight();
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetPrefWidthForVertical() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, true);
        
        float prefWidth = progressBar.getPrefWidth();
        Assert.assertTrue("Pref width should be positive for vertical", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeightForVertical() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, true);
        
        float prefHeight = progressBar.getPrefHeight();
        Assert.assertTrue("Pref height should be positive for vertical", prefHeight > 0);
    }

    @Test
    public void testProgressBarInheritance() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        
        Assert.assertTrue("VisProgressBar should extend ProgressBar", progressBar instanceof ProgressBar);
    }

    @Test
    public void testProgressBarWithZeroRange() {
        VisProgressBar progressBar = new VisProgressBar(50f, 50f, 1f, false);
        
        Assert.assertEquals("Min and max should be equal", 50f, progressBar.getMinValue(), 0.0001f);
        Assert.assertEquals("Min and max should be equal", 50f, progressBar.getMaxValue(), 0.0001f);
        Assert.assertEquals("Value should be equal to min/max", 50f, progressBar.getValue(), 0.0001f);
    }

    @Test
    public void testProgressBarWithNegativeRange() {
        VisProgressBar progressBar = new VisProgressBar(-100f, 0f, 1f, false);
        
        Assert.assertEquals("Min should be -100", -100f, progressBar.getMinValue(), 0.0001f);
        Assert.assertEquals("Max should be 0", 0f, progressBar.getMaxValue(), 0.0001f);
        
        progressBar.setValue(-50f);
        Assert.assertEquals("Value should be -50", -50f, progressBar.getValue(), 0.0001f);
        Assert.assertEquals("Middle value should be 50%", 0.5f, progressBar.getPercent(), 0.0001f);
    }

    @Test
    public void testProgressBarWithFractionalStepSize() {
        VisProgressBar progressBar = new VisProgressBar(0f, 1f, 0.25f, false);
        
        Assert.assertEquals("Step size should be 0.25", 0.25f, progressBar.getStepSize(), 0.0001f);
        
        progressBar.setValue(0.5f);
        Assert.assertEquals("Value should be 0.5", 0.5f, progressBar.getValue(), 0.0001f);
    }

    @Test
    public void testMultipleProgressBars() {
        VisProgressBar horizontal1 = new VisProgressBar(0f, 100f, 1f, false);
        VisProgressBar horizontal2 = new VisProgressBar(0f, 50f, 2f, false, "default-horizontal");
        VisProgressBar vertical = new VisProgressBar(0f, 100f, 1f, true);
        ProgressBar.ProgressBarStyle mockStyle = Mockito.mock(ProgressBar.ProgressBarStyle.class);
        VisProgressBar customStyle = new VisProgressBar(10f, 90f, 0.5f, false, mockStyle);
        
        Assert.assertNotNull("All progress bars should be created", horizontal1);
        Assert.assertNotNull("All progress bars should be created", horizontal2);
        Assert.assertNotNull("All progress bars should be created", vertical);
        Assert.assertNotNull("All progress bars should be created", customStyle);
        
        Assert.assertFalse("First should be horizontal", horizontal1.isVertical());
        Assert.assertFalse("Second should be horizontal", horizontal2.isVertical());
        Assert.assertTrue("Third should be vertical", vertical.isVertical());
        Assert.assertFalse("Fourth should be horizontal", customStyle.isVertical());
        
        Assert.assertSame("Custom style should be set", mockStyle, customStyle.getStyle());
    }

    @Test
    public void testProgressBarStyleProperties() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        ProgressBar.ProgressBarStyle style = progressBar.getStyle();
        
        Assert.assertNotNull("Background should not be null", style.background);
        Assert.assertNotNull("Knob should not be null", style.knob);
        Assert.assertNotNull("KnobBefore should not be null", style.knobBefore);
        Assert.assertNotNull("KnobAfter should not be null", style.knobAfter);
    }

    @Test
    public void testProgressBarAnimation() {
        VisProgressBar progressBar = new VisProgressBar(0f, 100f, 1f, false);
        
        // Test that setting value doesn't throw exceptions
        progressBar.setValue(0f);
        progressBar.setValue(25f);
        progressBar.setValue(50f);
        progressBar.setValue(75f);
        progressBar.setValue(100f);
        
        // All values should be set correctly
        Assert.assertEquals("Final value should be 100", 100f, progressBar.getValue(), 0.0001f);
    }

    @Test
    public void testProgressBarWithLargeValues() {
        VisProgressBar progressBar = new VisProgressBar(0f, 1000000f, 1000f, false);
        
        progressBar.setValue(500000f);
        Assert.assertEquals("Large value should be handled", 500000f, progressBar.getValue(), 0.0001f);
        Assert.assertEquals("Large value percent should be 0.5", 0.5f, progressBar.getPercent(), 0.0001f);
    }
}
