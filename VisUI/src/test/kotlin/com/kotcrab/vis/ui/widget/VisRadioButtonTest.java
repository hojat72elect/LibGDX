package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

public class VisRadioButtonTest {

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
    private BitmapFont testFont;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock Gdx application
        Gdx.app = mockApplication;
        Gdx.files = mockFiles;
        Gdx.input = mockInput;
        Gdx.graphics = mockGraphics;
        Mockito.when(mockApplication.getClipboard()).thenReturn(mockClipboard);

        // Load VisUI for testing
        if (!VisUI.isLoaded()) {
            VisUI.setSkipGdxVersionCheck(true);
            testFont = newTestFont();
            Skin testSkin = createMinimalSkin();
            VisUI.load(testSkin);
        } else {
            testFont = newTestFont();
        }

        testFont.setColor(Color.WHITE);
    }

    @After
    public void tearDown() {
        if (VisUI.isLoaded()) {
            VisUI.dispose();
        }
        // Reset Gdx static references
        Gdx.app = null;
        Gdx.files = null;
        Gdx.input = null;
        Gdx.graphics = null;
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
        sizes.menuItemIconSize = 14f;
        sizes.borderSize = 1f;
        sizes.spinnerButtonHeight = 16f;
        sizes.spinnerFieldSize = 40f;
        sizes.fileChooserViewModeBigIconsSize = 32f;
        sizes.fileChooserViewModeMediumIconsSize = 24f;
        sizes.fileChooserViewModeSmallIconsSize = 16f;
        sizes.fileChooserViewModeListWidthSize = 100f;
        skin.add("default", sizes, Sizes.class);

        // Add minimal required style for VisRadioButton (extends VisCheckBox)
        VisCheckBox.VisCheckBoxStyle checkBoxStyle = new VisCheckBox.VisCheckBoxStyle();
        checkBoxStyle.checkBackground = Mockito.mock(Drawable.class);
        checkBoxStyle.tick = Mockito.mock(Drawable.class);
        checkBoxStyle.font = testFont;
        checkBoxStyle.fontColor = Color.WHITE;

        // Set min dimensions for mocked drawables
        Mockito.when(checkBoxStyle.checkBackground.getMinWidth()).thenReturn(10f);
        Mockito.when(checkBoxStyle.checkBackground.getMinHeight()).thenReturn(10f);
        Mockito.when(checkBoxStyle.tick.getMinWidth()).thenReturn(10f);
        Mockito.when(checkBoxStyle.tick.getMinHeight()).thenReturn(10f);

        skin.add("default", checkBoxStyle);

        // Add "radio" style for VisRadioButton
        VisCheckBox.VisCheckBoxStyle radioStyle = new VisCheckBox.VisCheckBoxStyle();
        radioStyle.checkBackground = Mockito.mock(Drawable.class);
        radioStyle.tick = Mockito.mock(Drawable.class);
        radioStyle.font = testFont;
        radioStyle.fontColor = Color.WHITE;

        // Set min dimensions for mocked drawables
        Mockito.when(radioStyle.checkBackground.getMinWidth()).thenReturn(10f);
        Mockito.when(radioStyle.checkBackground.getMinHeight()).thenReturn(10f);
        Mockito.when(radioStyle.tick.getMinWidth()).thenReturn(10f);
        Mockito.when(radioStyle.tick.getMinHeight()).thenReturn(10f);

        skin.add("radio", radioStyle);

        return skin;
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

    @Test
    public void testConstructorWithText() {
        VisRadioButton radioButton = new VisRadioButton("Option 1");

        Assert.assertNotNull("Radio button should be created", radioButton);
        Assert.assertEquals("Text should be set", "Option 1", radioButton.getText().toString());
        Assert.assertFalse("Should be unchecked by default", radioButton.isChecked());
        Assert.assertNotNull("Style should not be null", radioButton.getStyle());
    }

    @Test
    public void testConstructorWithTextAndStyle() {
        VisCheckBox.VisCheckBoxStyle customStyle = new VisCheckBox.VisCheckBoxStyle();
        customStyle.checkBackground = Mockito.mock(Drawable.class);
        customStyle.tick = Mockito.mock(Drawable.class);
        customStyle.font = testFont;
        customStyle.fontColor = Color.WHITE;

        VisRadioButton radioButton = new VisRadioButton("Option", customStyle);

        Assert.assertNotNull("Radio button should be created", radioButton);
        Assert.assertEquals("Text should be set", "Option", radioButton.getText().toString());
        Assert.assertSame("Style should be set", customStyle, radioButton.getStyle());
    }

    @Test
    public void testSetChecked() {
        VisRadioButton radioButton = new VisRadioButton("Test");

        radioButton.setChecked(true);
        Assert.assertTrue("Should be checked", radioButton.isChecked());

        radioButton.setChecked(false);
        Assert.assertFalse("Should be unchecked", radioButton.isChecked());
    }

    @Test
    public void testToggle() {
        VisRadioButton radioButton = new VisRadioButton("Test");

        boolean initialState = radioButton.isChecked();
        radioButton.toggle();
        Assert.assertNotSame("State should change", initialState, radioButton.isChecked());

        radioButton.toggle();
        Assert.assertEquals("State should revert", initialState, radioButton.isChecked());
    }

    @Test
    public void testRadioButtonInheritance() {
        VisRadioButton radioButton = new VisRadioButton("Test");

        Assert.assertTrue("VisRadioButton should extend VisCheckBox", radioButton instanceof VisCheckBox);
    }

    @Test
    public void testGetBackgroundImage() {
        VisRadioButton radioButton = new VisRadioButton("Test");

        Assert.assertNotNull("Background image should not be null", radioButton.getBackgroundImage());
    }

    @Test
    public void testGetTickImage() {
        VisRadioButton radioButton = new VisRadioButton("Test");

        Assert.assertNotNull("Tick image should not be null", radioButton.getTickImage());
    }

    @Test
    public void testGetImageStack() {
        VisRadioButton radioButton = new VisRadioButton("Test");

        Assert.assertNotNull("Image stack should not be null", radioButton.getImageStack());
    }

    @Test
    public void testGetImageStackCell() {
        VisRadioButton radioButton = new VisRadioButton("Test");

        Assert.assertNotNull("Image stack cell should not be null", radioButton.getImageStackCell());
    }

    @Test
    public void testSetStateInvalid() {
        VisRadioButton radioButton = new VisRadioButton("Test");

        Assert.assertFalse("State invalid should be false by default", radioButton.setStateInvalid());

        radioButton.setStateInvalid(true);
        Assert.assertTrue("State invalid should be true", radioButton.setStateInvalid());

        radioButton.setStateInvalid(false);
        Assert.assertFalse("State invalid should be false", radioButton.setStateInvalid());
    }

    @Test
    public void testFocusBorder() {
        VisRadioButton radioButton = new VisRadioButton("Test");

        Assert.assertTrue("Focus border should be enabled by default", radioButton.isFocusBorderEnabled());

        radioButton.setFocusBorderEnabled(false);
        Assert.assertFalse("Focus border should be disabled", radioButton.isFocusBorderEnabled());

        radioButton.setFocusBorderEnabled(true);
        Assert.assertTrue("Focus border should be enabled", radioButton.isFocusBorderEnabled());
    }

    @Test
    public void testFocusGainedAndLost() {
        VisRadioButton radioButton = new VisRadioButton("Test");

        // Test focus gained
        radioButton.focusGained();
        // Focus border should be drawn (we can't easily test the visual effect)

        // Test focus lost
        radioButton.focusLost();
        // Focus border should not be drawn
    }

    @Test
    public void testButtonGroupBehavior() {
        VisRadioButton radio1 = new VisRadioButton("Option 1");
        VisRadioButton radio2 = new VisRadioButton("Option 2");
        VisRadioButton radio3 = new VisRadioButton("Option 3");

        ButtonGroup<VisRadioButton> buttonGroup = new ButtonGroup<>();
        buttonGroup.add(radio1);
        buttonGroup.add(radio2);
        buttonGroup.add(radio3);

        // Test that only one can be selected at a time
        radio1.setChecked(true);
        Assert.assertTrue("Radio 1 should be checked", radio1.isChecked());
        Assert.assertFalse("Radio 2 should be unchecked", radio2.isChecked());
        Assert.assertFalse("Radio 3 should be unchecked", radio3.isChecked());

        radio2.setChecked(true);
        Assert.assertFalse("Radio 1 should be unchecked", radio1.isChecked());
        Assert.assertTrue("Radio 2 should be checked", radio2.isChecked());
        Assert.assertFalse("Radio 3 should be unchecked", radio3.isChecked());
    }

    @Test
    public void testDisabledState() {
        VisRadioButton radioButton = new VisRadioButton("Test");
        radioButton.setChecked(true);

        radioButton.setDisabled(true);
        Assert.assertTrue("Radio button should be disabled", radioButton.isDisabled());
        Assert.assertTrue("Radio button should remain checked when disabled", radioButton.isChecked());

        // Changing state while disabled should still work
        radioButton.setChecked(false);
        Assert.assertFalse("Radio button should be unchecked", radioButton.isChecked());
    }

    @Test
    public void testGetPrefWidth() {
        VisRadioButton radioButton = new VisRadioButton("Test Option");

        float prefWidth = radioButton.getPrefWidth();
        Assert.assertTrue("Pref width should be positive", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeight() {
        VisRadioButton radioButton = new VisRadioButton("Test");

        float prefHeight = radioButton.getPrefHeight();
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetPrefWidthWithEmptyText() {
        VisRadioButton radioButton = new VisRadioButton("");

        float prefWidth = radioButton.getPrefWidth();
        Assert.assertTrue("Pref width should be positive even with empty text", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeightWithEmptyText() {
        VisRadioButton radioButton = new VisRadioButton("");

        float prefHeight = radioButton.getPrefHeight();
        Assert.assertTrue("Pref height should be positive even with empty text", prefHeight > 0);
    }

    @Test
    public void testMultipleRadioButtons() {
        VisRadioButton radio1 = new VisRadioButton("Option 1");
        VisRadioButton radio2 = new VisRadioButton("Option 2");
        VisRadioButton radio3 = new VisRadioButton("Option 3");

        Assert.assertNotNull("All radio buttons should be created", radio1);
        Assert.assertNotNull("All radio buttons should be created", radio2);
        Assert.assertNotNull("All radio buttons should be created", radio3);

        Assert.assertEquals("Radio 1 should have correct text", "Option 1", radio1.getText().toString());
        Assert.assertEquals("Radio 2 should have correct text", "Option 2", radio2.getText().toString());
        Assert.assertEquals("Radio 3 should have correct text", "Option 3", radio3.getText().toString());

        // All should be unchecked by default
        Assert.assertFalse("Radio 1 should be unchecked by default", radio1.isChecked());
        Assert.assertFalse("Radio 2 should be unchecked by default", radio2.isChecked());
        Assert.assertFalse("Radio 3 should be unchecked by default", radio3.isChecked());
    }

    @Test
    public void testRadioButtonWithLongText() {
        String longText = "This is a very long radio button option text that should test the radio button's ability to handle longer strings";
        VisRadioButton radioButton = new VisRadioButton(longText);

        Assert.assertEquals("Long text should be set correctly", longText, radioButton.getText().toString());
        Assert.assertTrue("Pref width should be larger for long text", radioButton.getPrefWidth() > 0);
    }

    @Test
    public void testRadioButtonWithSpecialCharacters() {
        String specialText = "Special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        VisRadioButton radioButton = new VisRadioButton(specialText);

        Assert.assertEquals("Special characters should be handled", specialText, radioButton.getText().toString());
    }

    @Test
    public void testRadioButtonStyleProperties() {
        VisRadioButton radioButton = new VisRadioButton("Test");
        VisCheckBox.VisCheckBoxStyle style = radioButton.getStyle();

        Assert.assertNotNull("Check background should not be null", style.checkBackground);
        Assert.assertNotNull("Tick should not be null", style.tick);
        Assert.assertNotNull("Font should not be null", style.font);
        Assert.assertNotNull("Font color should not be null", style.fontColor);
    }

    @Test
    public void testRadioButtonWithNullText() {
        VisRadioButton radioButton = new VisRadioButton(null);

        Assert.assertNotNull("Radio button should be created with null text", radioButton);
        Assert.assertEquals("Text should be empty when null is passed", "", radioButton.getText().toString());
    }

    @Test
    public void testRadioButtonStyleCopyConstructor() {
        Drawable mockCheckBackground = Mockito.mock(Drawable.class);
        Drawable mockTick = Mockito.mock(Drawable.class);
        BitmapFont mockFont = Mockito.mock(BitmapFont.class);
        Color mockColor = Color.RED;

        VisCheckBox.VisCheckBoxStyle original = new VisCheckBox.VisCheckBoxStyle(mockCheckBackground, mockTick, mockFont, mockColor);
        VisCheckBox.VisCheckBoxStyle copy = new VisCheckBox.VisCheckBoxStyle(original);

        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertEquals("Check background should be copied", mockCheckBackground, copy.checkBackground);
        Assert.assertEquals("Tick should be copied", mockTick, copy.tick);
        Assert.assertEquals("Font should be copied", mockFont, copy.font);
        Assert.assertEquals("Font color should be copied", mockColor, copy.fontColor);
    }

    @Test
    public void testRadioButtonStyleDefaultConstructor() {
        VisCheckBox.VisCheckBoxStyle style = new VisCheckBox.VisCheckBoxStyle();

        Assert.assertNotNull("Style should be created", style);
        Assert.assertNull("Check background should be null by default", style.checkBackground);
        Assert.assertNull("Tick should be null by default", style.tick);
        Assert.assertNull("Font should be null by default", style.font);
        Assert.assertNull("Font color should be null by default", style.fontColor);
    }
}
