package com.kotcrab.vis.ui.widget;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class VisSliderTest {

    @Mock
    private Application mockApplication;
    @Mock
    private Files mockFiles;
    @Mock
    private Input mockInput;
    @Mock
    private Graphics mockGraphics;
    @Mock
    private Clipboard mockClipboard;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock Gdx application
        Gdx.app = mockApplication;
        Gdx.files = mockFiles;
        Gdx.input = mockInput;
        Gdx.graphics = mockGraphics;
        when(mockApplication.getClipboard()).thenReturn(mockClipboard);

        // Load VisUI for testing - always ensure we have our own skin
        if (VisUI.isLoaded()) {
            VisUI.dispose();
        }
        VisUI.setSkipGdxVersionCheck(true);
        Skin testSkin = createMinimalSkin();
        VisUI.load(testSkin);
    }

    @After
    public void tearDown() {
        if (VisUI.isLoaded()) {
            VisUI.dispose();
        }
    }

    private static BitmapFont newTestFont() {
        com.badlogic.gdx.graphics.Texture mockTexture = Mockito.mock(com.badlogic.gdx.graphics.Texture.class);
        Mockito.when(mockTexture.getWidth()).thenReturn(1);
        Mockito.when(mockTexture.getHeight()).thenReturn(1);

        com.badlogic.gdx.graphics.g2d.TextureRegion mockRegion = Mockito.mock(com.badlogic.gdx.graphics.g2d.TextureRegion.class);
        Mockito.when(mockRegion.getTexture()).thenReturn(mockTexture);

        BitmapFont.BitmapFontData fontData = new BitmapFont.BitmapFontData() {
            @Override
            public boolean hasGlyph(char ch) {
                return true;
            }
        };

        return new BitmapFont(fontData, com.badlogic.gdx.utils.Array.with(mockRegion), true);
    }

    private Skin createMinimalSkin() {
        Skin skin = new Skin();

        // Add Sizes object
        Sizes sizes = new Sizes();
        sizes.scaleFactor = 1f;
        sizes.spacingTop = 2f;
        sizes.spacingBottom = 2f;
        sizes.spacingLeft = 2f;
        sizes.spacingRight = 2f;
        sizes.buttonBarSpacing = 6f;
        sizes.menuItemIconSize = 16f;
        sizes.borderSize = 1f;
        sizes.spinnerButtonHeight = 16f;
        sizes.spinnerFieldSize = 16f;
        sizes.fileChooserViewModeBigIconsSize = 32f;
        sizes.fileChooserViewModeMediumIconsSize = 24f;
        sizes.fileChooserViewModeSmallIconsSize = 16f;
        sizes.fileChooserViewModeListWidthSize = 100f;
        skin.add("default", sizes, Sizes.class);

        // Add required Slider styles
        Drawable mockDrawable = Mockito.mock(Drawable.class);
        Mockito.when(mockDrawable.getMinWidth()).thenReturn(10f);
        Mockito.when(mockDrawable.getMinHeight()).thenReturn(10f);

        Slider.SliderStyle horizontalStyle = new Slider.SliderStyle();
        horizontalStyle.background = mockDrawable;
        horizontalStyle.knob = mockDrawable;
        horizontalStyle.knobBefore = mockDrawable;
        horizontalStyle.knobAfter = mockDrawable;
        skin.add("default-horizontal", horizontalStyle, Slider.SliderStyle.class);

        Slider.SliderStyle verticalStyle = new Slider.SliderStyle();
        verticalStyle.background = mockDrawable;
        verticalStyle.knob = mockDrawable;
        verticalStyle.knobBefore = mockDrawable;
        verticalStyle.knobAfter = mockDrawable;
        skin.add("default-vertical", verticalStyle, Slider.SliderStyle.class);

        return skin;
    }

    @Test
    public void testConstructorHorizontal() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        Assert.assertNotNull("Slider should be created", slider);
        Assert.assertEquals("Min value should be 0", 0f, slider.getMinValue(), 0.0001f);
        Assert.assertEquals("Max value should be 100", 100f, slider.getMaxValue(), 0.0001f);
        Assert.assertEquals("Step size should be 1", 1f, slider.getStepSize(), 0.0001f);
        Assert.assertFalse("Should be horizontal", slider.isVertical());
        Assert.assertNotNull("Style should not be null", slider.getStyle());
        Assert.assertEquals("Value should be min by default", 0f, slider.getValue(), 0.0001f);
    }

    @Test
    public void testConstructorVertical() {
        VisSlider slider = new VisSlider(0f, 100f, 5f, true);

        Assert.assertNotNull("Slider should be created", slider);
        Assert.assertEquals("Min value should be 0", 0f, slider.getMinValue(), 0.0001f);
        Assert.assertEquals("Max value should be 100", 100f, slider.getMaxValue(), 0.0001f);
        Assert.assertEquals("Step size should be 5", 5f, slider.getStepSize(), 0.0001f);
        Assert.assertTrue("Should be vertical", slider.isVertical());
        Assert.assertNotNull("Style should not be null", slider.getStyle());
        Assert.assertEquals("Value should be min by default", 0f, slider.getValue(), 0.0001f);
    }

    @Test
    public void testConstructorWithStyleName() {
        VisSlider slider = new VisSlider(0f, 50f, 2.5f, false, "default-horizontal");

        Assert.assertNotNull("Slider should be created", slider);
        Assert.assertEquals("Min value should be 0", 0f, slider.getMinValue(), 0.0001f);
        Assert.assertEquals("Max value should be 50", 50f, slider.getMaxValue(), 0.0001f);
        Assert.assertEquals("Step size should be 2.5", 2.5f, slider.getStepSize(), 0.0001f);
        Assert.assertFalse("Should be horizontal", slider.isVertical());
        Assert.assertNotNull("Style should not be null", slider.getStyle());
    }

    @Test
    public void testConstructorWithStyle() {
        Slider.SliderStyle mockStyle = Mockito.mock(Slider.SliderStyle.class);
        VisSlider slider = new VisSlider(10f, 90f, 0.1f, true, mockStyle);

        Assert.assertNotNull("Slider should be created", slider);
        Assert.assertEquals("Min value should be 10", 10f, slider.getMinValue(), 0.0001f);
        Assert.assertEquals("Max value should be 90", 90f, slider.getMaxValue(), 0.0001f);
        Assert.assertEquals("Step size should be 0.1", 0.1f, slider.getStepSize(), 0.0001f);
        Assert.assertTrue("Should be vertical", slider.isVertical());
        Assert.assertSame("Style should be set", mockStyle, slider.getStyle());
    }

    @Test
    public void testSetValue() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        slider.setValue(25f);
        Assert.assertEquals("Value should be 25", 25f, slider.getValue(), 0.0001f);

        slider.setValue(75f);
        Assert.assertEquals("Value should be 75", 75f, slider.getValue(), 0.0001f);
    }

    @Test
    public void testSetValueOutOfBounds() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        // Test value below minimum
        slider.setValue(-10f);
        Assert.assertEquals("Value should be clamped to min", 0f, slider.getValue(), 0.0001f);

        // Test value above maximum
        slider.setValue(150f);
        Assert.assertEquals("Value should be clamped to max", 100f, slider.getValue(), 0.0001f);
    }

    @Test
    public void testSetMinValue() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);
        slider.setValue(50f);

        slider.setRange(25f, 100f);
        Assert.assertEquals("Min value should be updated", 25f, slider.getMinValue(), 0.0001f);
        Assert.assertEquals("Value should be clamped to new min", 50f, slider.getValue(), 0.0001f);
    }

    @Test
    public void testSetMaxValue() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);
        slider.setValue(50f);

        slider.setRange(0f, 75f);
        Assert.assertEquals("Max value should be updated", 75f, slider.getMaxValue(), 0.0001f);
        Assert.assertEquals("Value should be clamped to new max", 50f, slider.getValue(), 0.0001f);
    }

    @Test
    public void testSetRange() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        slider.setRange(20f, 80f);
        Assert.assertEquals("Min value should be updated", 20f, slider.getMinValue(), 0.0001f);
        Assert.assertEquals("Max value should be updated", 80f, slider.getMaxValue(), 0.0001f);
        Assert.assertEquals("Value should be clamped", 20f, slider.getValue(), 0.0001f);
    }

    @Test
    public void testSetStepSize() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        slider.setStepSize(5f);
        Assert.assertEquals("Step size should be updated", 5f, slider.getStepSize(), 0.0001f);
    }

    @Test
    public void testGetVisualValue() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);
        slider.setValue(37.5f);

        float visualValue = slider.getVisualValue();
        // Slider should snap to nearest step (38.0) due to step size of 1.0
        Assert.assertEquals("Visual value should match actual value", 38.0f, visualValue, 0.0001f);
    }

    @Test
    public void testGetPercent() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        slider.setValue(0f);
        Assert.assertEquals("0% should be 0", 0f, slider.getPercent(), 0.0001f);

        slider.setValue(50f);
        Assert.assertEquals("50% should be 0.5", 0.5f, slider.getPercent(), 0.0001f);

        slider.setValue(100f);
        Assert.assertEquals("100% should be 1", 1f, slider.getPercent(), 0.0001f);
    }

    @Test
    public void testGetPercentWithCustomRange() {
        VisSlider slider = new VisSlider(50f, 150f, 1f, false);

        slider.setValue(50f);
        Assert.assertEquals("Min value should be 0%", 0f, slider.getPercent(), 0.0001f);

        slider.setValue(100f);
        Assert.assertEquals("Middle value should be 50%", 0.5f, slider.getPercent(), 0.0001f);

        slider.setValue(150f);
        Assert.assertEquals("Max value should be 100%", 1f, slider.getPercent(), 0.0001f);
    }

    @Test
    public void testGetPrefWidth() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        float prefWidth = slider.getPrefWidth();
        Assert.assertTrue("Pref width should be positive for horizontal", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeight() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        float prefHeight = slider.getPrefHeight();
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetPrefWidthForVertical() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, true);

        float prefWidth = slider.getPrefWidth();
        Assert.assertTrue("Pref width should be positive for vertical", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeightForVertical() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, true);

        float prefHeight = slider.getPrefHeight();
        Assert.assertTrue("Pref height should be positive for vertical", prefHeight > 0);
    }

    @Test
    public void testSliderInheritance() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        Assert.assertTrue("VisSlider should extend Slider", slider instanceof Slider);
    }

    @Test
    public void testSliderWithZeroRange() {
        VisSlider slider = new VisSlider(50f, 50f, 1f, false);

        Assert.assertEquals("Min and max should be equal", 50f, slider.getMinValue(), 0.0001f);
        Assert.assertEquals("Min and max should be equal", 50f, slider.getMaxValue(), 0.0001f);
        Assert.assertEquals("Value should be equal to min/max", 50f, slider.getValue(), 0.0001f);
    }

    @Test
    public void testSliderWithNegativeRange() {
        VisSlider slider = new VisSlider(-100f, 0f, 1f, false);

        Assert.assertEquals("Min should be -100", -100f, slider.getMinValue(), 0.0001f);
        Assert.assertEquals("Max should be 0", 0f, slider.getMaxValue(), 0.0001f);

        slider.setValue(-50f);
        Assert.assertEquals("Value should be -50", -50f, slider.getValue(), 0.0001f);
        Assert.assertEquals("Middle value should be 50%", 0.5f, slider.getPercent(), 0.0001f);
    }

    @Test
    public void testSliderWithFractionalStepSize() {
        VisSlider slider = new VisSlider(0f, 1f, 0.25f, false);

        Assert.assertEquals("Step size should be 0.25", 0.25f, slider.getStepSize(), 0.0001f);

        slider.setValue(0.5f);
        Assert.assertEquals("Value should be 0.5", 0.5f, slider.getValue(), 0.0001f);
    }

    @Test
    public void testMultipleSliders() {
        VisSlider horizontal1 = new VisSlider(0f, 100f, 1f, false);
        VisSlider horizontal2 = new VisSlider(0f, 50f, 2f, false, "default-horizontal");
        VisSlider vertical = new VisSlider(0f, 100f, 1f, true);
        Slider.SliderStyle mockStyle = Mockito.mock(Slider.SliderStyle.class);
        VisSlider customStyle = new VisSlider(10f, 90f, 0.5f, false, mockStyle);

        Assert.assertNotNull("All sliders should be created", horizontal1);
        Assert.assertNotNull("All sliders should be created", horizontal2);
        Assert.assertNotNull("All sliders should be created", vertical);
        Assert.assertNotNull("All sliders should be created", customStyle);

        Assert.assertFalse("First should be horizontal", horizontal1.isVertical());
        Assert.assertFalse("Second should be horizontal", horizontal2.isVertical());
        Assert.assertTrue("Third should be vertical", vertical.isVertical());
        Assert.assertFalse("Fourth should be horizontal", customStyle.isVertical());

        Assert.assertSame("Custom style should be set", mockStyle, customStyle.getStyle());
    }

    @Test
    public void testSliderStyleProperties() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);
        Slider.SliderStyle style = slider.getStyle();

        Assert.assertNotNull("Background should not be null", style.background);
        Assert.assertNotNull("Knob should not be null", style.knob);
        Assert.assertNotNull("KnobBefore should not be null", style.knobBefore);
        Assert.assertNotNull("KnobAfter should not be null", style.knobAfter);
    }

    @Test
    public void testSliderAnimation() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        // Test that setting value doesn't throw exceptions
        slider.setValue(0f);
        slider.setValue(25f);
        slider.setValue(50f);
        slider.setValue(75f);
        slider.setValue(100f);

        // All values should be set correctly
        Assert.assertEquals("Final value should be 100", 100f, slider.getValue(), 0.0001f);
    }

    @Test
    public void testSliderWithLargeValues() {
        VisSlider slider = new VisSlider(0f, 1000000f, 1000f, false);

        slider.setValue(500000f);
        Assert.assertEquals("Large value should be handled", 500000f, slider.getValue(), 0.0001f);
        Assert.assertEquals("Large value percent should be 0.5", 0.5f, slider.getPercent(), 0.0001f);
    }

    @Test
    public void testSliderChangeListener() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        final boolean[] changed = {false};
        final float[] newValue = {0f};

        slider.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent event, Actor actor) {
                changed[0] = true;
                newValue[0] = slider.getValue();
            }
        });

        // Trigger change
        slider.setValue(50f);
        Assert.assertTrue("Change listener should be called", changed[0]);
        Assert.assertEquals("New value should be passed to listener", 50f, newValue[0], 0.0001f);
    }

    @Test
    public void testSliderDisabled() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);
        slider.setValue(50f);

        slider.setDisabled(true);
        Assert.assertTrue("Slider should be disabled", slider.isDisabled());
        Assert.assertEquals("Value should remain when disabled", 50f, slider.getValue(), 0.0001f);

        // Changing value while disabled should still work
        slider.setValue(75f);
        Assert.assertEquals("Value should change even when disabled", 75f, slider.getValue(), 0.0001f);
    }

    @Test
    public void testSliderWithDifferentStyleNames() {
        VisSlider defaultHorizontal = new VisSlider(0f, 100f, 1f, false, "default-horizontal");
        VisSlider defaultVertical = new VisSlider(0f, 100f, 1f, true, "default-vertical");

        Assert.assertNotNull("Default horizontal style should be loaded", defaultHorizontal.getStyle());
        Assert.assertNotNull("Default vertical style should be loaded", defaultVertical.getStyle());
    }

    @Test
    public void testSliderSetValueWithStepSize() {
        VisSlider slider = new VisSlider(0f, 10f, 2f, false);

        // Test values that align with step size
        slider.setValue(2f);
        Assert.assertEquals("Value should be exactly 2", 2f, slider.getValue(), 0.0001f);

        slider.setValue(6f);
        Assert.assertEquals("Value should be exactly 6", 6f, slider.getValue(), 0.0001f);

        // Test value that doesn't align with step size
        slider.setValue(3.7f);
        // The slider should snap to the nearest step
        Assert.assertTrue("Value should snap to step", slider.getValue() == 2f || slider.getValue() == 4f);
    }

    @Test
    public void testSliderGetVisualPosition() {
        VisSlider slider = new VisSlider(0f, 100f, 1f, false);

        slider.setValue(25f);
        float position = slider.getVisualValue();
        Assert.assertEquals("Visual position should match value", 25f, position, 0.0001f);

        slider.setValue(75f);
        position = slider.getVisualValue();
        Assert.assertEquals("Visual position should match value", 75f, position, 0.0001f);
    }
}
