package com.kotcrab.vis.ui.widget;

import static org.mockito.Mockito.when;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Clipboard;
import com.kotcrab.vis.ui.VisUI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TooltipTest {

    @Mock
    private Clipboard mockClipboard;
    @Mock
    private Application mockApplication;
    @Mock
    private Files mockFiles;
    @Mock
    private Input mockInput;
    @Mock
    private Graphics mockGraphics;

    private BitmapFont testFont;
    private Drawable testDrawable;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock Gdx application
        Gdx.app = mockApplication;
        Gdx.files = mockFiles;
        Gdx.input = mockInput;
        Gdx.graphics = mockGraphics;
        when(mockApplication.getClipboard()).thenReturn(mockClipboard);

        // Setup essential Gdx.graphics mocks
        when(mockGraphics.getWidth()).thenReturn(800);
        when(mockGraphics.getHeight()).thenReturn(600);
        when(mockGraphics.getDeltaTime()).thenReturn(0.016f);

        // Create test font
        testFont = newTestFont();
        testFont.setColor(Color.WHITE);

        // Create test drawable
        testDrawable = Mockito.mock(Drawable.class);
        when(testDrawable.getMinWidth()).thenReturn(10f);
        when(testDrawable.getMinHeight()).thenReturn(10f);

        // Load VisUI for testing
        if (!VisUI.isLoaded()) {
            VisUI.setSkipGdxVersionCheck(true);
            Skin testSkin = createMinimalSkin();
            VisUI.load(testSkin);
        }
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

        // Add Sizes (required by VisTable which Tooltip extends)
        com.kotcrab.vis.ui.Sizes sizes = new com.kotcrab.vis.ui.Sizes();
        sizes.scaleFactor = 1f;
        sizes.spacingTop = 2f;
        sizes.spacingBottom = 2f;
        sizes.spacingRight = 2f;
        sizes.spacingLeft = 2f;
        sizes.buttonBarSpacing = 5f;
        sizes.menuItemIconSize = 22f;
        sizes.borderSize = 1f;
        sizes.spinnerButtonHeight = 12f;
        sizes.spinnerFieldSize = 50f;
        skin.add("default", sizes);

        // Add VisLabel style (required for Tooltip text content)
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = testFont;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // Add VisLabel specific style
        VisLabel.LabelStyle visLabelStyle = new VisLabel.LabelStyle();
        visLabelStyle.font = testFont;
        visLabelStyle.fontColor = Color.WHITE;
        skin.add("default", visLabelStyle, VisLabel.LabelStyle.class);

        // Add Tooltip style
        Tooltip.TooltipStyle tooltipStyle = new Tooltip.TooltipStyle();
        tooltipStyle.background = testDrawable;
        skin.add("default", tooltipStyle);

        return skin;
    }

    private static BitmapFont newTestFont() {
        com.badlogic.gdx.graphics.Texture mockTexture = Mockito.mock(com.badlogic.gdx.graphics.Texture.class);
        when(mockTexture.getWidth()).thenReturn(1);
        when(mockTexture.getHeight()).thenReturn(1);

        com.badlogic.gdx.graphics.g2d.TextureRegion mockRegion = Mockito
                .mock(com.badlogic.gdx.graphics.g2d.TextureRegion.class);
        when(mockRegion.getTexture()).thenReturn(mockTexture);

        BitmapFont.BitmapFontData fontData = new BitmapFont.BitmapFontData() {
            @Override
            public boolean hasGlyph(char ch) {
                return true;
            }
        };

        return new BitmapFont(fontData, com.badlogic.gdx.utils.Array.with(mockRegion), true);
    }

    @Test
    public void testDefaultConstructor() {
        Tooltip tooltip = new Tooltip();

        Assert.assertNotNull("Tooltip should be created", tooltip);
        Assert.assertNull("Target should be null by default", tooltip.getTarget());
        Assert.assertNull("Content should be null by default", tooltip.getContent());
        Assert.assertEquals("Default fade time should be set",
                Tooltip.DEFAULT_FADE_TIME, tooltip.getFadeTime(), 0.0001f);
        Assert.assertEquals("Default appear delay time should be set",
                Tooltip.DEFAULT_APPEAR_DELAY_TIME, tooltip.getAppearDelayTime(), 0.0001f);
    }

    @Test
    public void testConstructorWithStyleName() {
        Tooltip tooltip = new Tooltip("default");

        Assert.assertNotNull("Tooltip should be created", tooltip);
        Assert.assertNull("Target should be null by default", tooltip.getTarget());
        Assert.assertNull("Content should be null by default", tooltip.getContent());
    }

    @Test
    public void testConstructorWithStyle() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Tooltip.TooltipStyle style = new Tooltip.TooltipStyle(mockBackground);

        Tooltip tooltip = new Tooltip(style);

        Assert.assertNotNull("Tooltip should be created", tooltip);
        Assert.assertNull("Target should be null by default", tooltip.getTarget());
        Assert.assertNull("Content should be null by default", tooltip.getContent());
    }

    @Test
    public void testBuilderWithText() {
        Tooltip.Builder builder = new Tooltip.Builder("Test Tooltip");
        Tooltip tooltip = builder.build();

        Assert.assertNotNull("Tooltip should be created", tooltip);
        Assert.assertNotNull("Content should not be null", tooltip.getContent());
        Assert.assertTrue("Content should be a VisLabel", tooltip.getContent() instanceof VisLabel);
        Assert.assertEquals("Text should be set", "Test Tooltip",
                ((VisLabel) tooltip.getContent()).getText().toString());
    }

    @Test
    public void testBuilderWithTextAndAlignment() {
        Tooltip.Builder builder = new Tooltip.Builder("Test Tooltip", com.badlogic.gdx.utils.Align.center);
        Tooltip tooltip = builder.build();

        Assert.assertNotNull("Tooltip should be created", tooltip);
        Assert.assertTrue("Content should be a VisLabel", tooltip.getContent() instanceof VisLabel);

        VisLabel label = (VisLabel) tooltip.getContent();
        Assert.assertEquals("Text should be set", "Test Tooltip", label.getText().toString());
        Assert.assertEquals("Alignment should be set", com.badlogic.gdx.utils.Align.center, label.getLabelAlign());
    }

    @Test
    public void testBuilderWithActor() {
        Actor mockActor = Mockito.mock(Actor.class);
        Tooltip.Builder builder = new Tooltip.Builder(mockActor);
        Tooltip tooltip = builder.build();

        Assert.assertNotNull("Tooltip should be created", tooltip);
        Assert.assertSame("Content should be the provided actor", mockActor, tooltip.getContent());
    }

    @Test
    public void testBuilderWithTarget() {
        // Use real Actor - mock Actor doesn't have getListeners() working properly
        Actor target = new Actor();
        Tooltip.Builder builder = new Tooltip.Builder("Test").target(target);
        Tooltip tooltip = builder.build();

        Assert.assertNotNull("Tooltip should be created", tooltip);
        Assert.assertSame("Target should be set", target, tooltip.getTarget());
    }

    @Test
    public void testBuilderWithStyle() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Tooltip.TooltipStyle style = new Tooltip.TooltipStyle(mockBackground);
        Tooltip.Builder builder = new Tooltip.Builder("Test").style(style);
        Tooltip tooltip = builder.build();

        Assert.assertNotNull("Tooltip should be created", tooltip);
        // Style is applied internally, we just test it doesn't crash
    }

    @Test
    public void testBuilderWithStyleName() {
        Tooltip.Builder builder = new Tooltip.Builder("Test").style("default");
        Tooltip tooltip = builder.build();

        Assert.assertNotNull("Tooltip should be created", tooltip);
        // Style is applied internally, we just test it doesn't crash
    }

    @Test
    public void testBuilderWithWidth() {
        Tooltip.Builder builder = new Tooltip.Builder("Test").width(100f);
        Tooltip tooltip = builder.build();

        Assert.assertNotNull("Tooltip should be created", tooltip);
        Assert.assertTrue("Content should be a VisLabel", tooltip.getContent() instanceof VisLabel);

        VisLabel label = (VisLabel) tooltip.getContent();
        Assert.assertTrue("Label should have wrap enabled", label.getWrap());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithNegativeWidth() {
        Tooltip.Builder builder = new Tooltip.Builder("Test");
        builder.width(-10f);
    }

    @Test
    public void testBuilderWithZeroWidth() {
        // Zero width is valid (implementation only rejects width < 0)
        Tooltip.Builder builder = new Tooltip.Builder("Test");
        builder.width(0f);
        // Should not throw exception
    }

    @Test
    public void testGetAndSetTarget() {
        Tooltip tooltip = new Tooltip();
        // Use real Actor - mock Actor doesn't have getListeners() working properly
        Actor target = new Actor();

        Assert.assertNull("Target should be null initially", tooltip.getTarget());

        tooltip.setTarget(target);
        Assert.assertSame("Target should be set", target, tooltip.getTarget());

        tooltip.setTarget(null);
        Assert.assertNull("Target should be null after removal", tooltip.getTarget());
    }

    @Test
    public void testGetAndSetContent() {
        Tooltip tooltip = new Tooltip();
        Actor mockContent = Mockito.mock(Actor.class);

        Assert.assertNull("Content should be null initially", tooltip.getContent());

        tooltip.setContent(mockContent);
        Assert.assertSame("Content should be set", mockContent, tooltip.getContent());

        tooltip.setContent(null);
        Assert.assertNull("Content should be null after removal", tooltip.getContent());
    }

    @Test
    public void testGetAndSetText() {
        Tooltip tooltip = new Tooltip();

        tooltip.setText("Test Text");
        Assert.assertNotNull("Content should not be null", tooltip.getContent());
        Assert.assertTrue("Content should be a VisLabel", tooltip.getContent() instanceof VisLabel);
        Assert.assertEquals("Text should be set", "Test Text",
                ((VisLabel) tooltip.getContent()).getText().toString());

        tooltip.setText("New Text");
        Assert.assertEquals("Text should be updated", "New Text",
                ((VisLabel) tooltip.getContent()).getText().toString());
    }

    @Test
    public void testSetTextWithExistingLabel() {
        Tooltip tooltip = new Tooltip();
        VisLabel originalLabel = new VisLabel("Original");
        tooltip.setContent(originalLabel);

        tooltip.setText("New Text");

        Assert.assertSame("Should reuse existing label", originalLabel, tooltip.getContent());
        Assert.assertEquals("Text should be updated", "New Text", originalLabel.getText().toString());
    }

    @Test
    public void testGetAndSetFadeTime() {
        Tooltip tooltip = new Tooltip();

        Assert.assertEquals("Default fade time should be set",
                Tooltip.DEFAULT_FADE_TIME, tooltip.getFadeTime(), 0.0001f);

        tooltip.setFadeTime(0.5f);
        Assert.assertEquals("Fade time should be updated", 0.5f, tooltip.getFadeTime(), 0.0001f);
    }

    @Test
    public void testGetAndSetAppearDelayTime() {
        Tooltip tooltip = new Tooltip();

        Assert.assertEquals("Default appear delay time should be set",
                Tooltip.DEFAULT_APPEAR_DELAY_TIME, tooltip.getAppearDelayTime(), 0.0001f);

        tooltip.setAppearDelayTime(1.0f);
        Assert.assertEquals("Appear delay time should be updated", 1.0f, tooltip.getAppearDelayTime(), 0.0001f);
    }

    @Test
    public void testGetAndSetMouseMoveFadeOut() {
        Tooltip tooltip = new Tooltip();

        Assert.assertEquals("Default mouse move fade out should match static field",
                Tooltip.MOUSE_MOVED_FADEOUT, tooltip.isMouseMoveFadeOut());

        tooltip.setMouseMoveFadeOut(true);
        Assert.assertTrue("Mouse move fade out should be true", tooltip.isMouseMoveFadeOut());

        tooltip.setMouseMoveFadeOut(false);
        Assert.assertFalse("Mouse move fade out should be false", tooltip.isMouseMoveFadeOut());
    }

    @Test
    public void testAttach() {
        Tooltip tooltip = new Tooltip();
        // Use real Actor - mock Actor doesn't have getListeners() working properly
        Actor target = new Actor();
        // setTarget() internally calls attach(), so we set target directly to test
        // attach() separately

        // First verify that attach() does nothing if no target is set
        tooltip.attach(); // Should not throw

        // Now set target and verify it auto-attaches
        tooltip.setTarget(target);

        // Test that attaching again throws exception (already attached by setTarget)
        try {
            tooltip.attach();
            Assert.fail("Should throw exception when attaching to same target twice");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testAttachWithoutTarget() {
        Tooltip tooltip = new Tooltip();

        // Should not throw exception
        tooltip.attach();
    }

    @Test
    public void testDetach() {
        Tooltip tooltip = new Tooltip();
        // Use real Actor - mock Actor doesn't have getListeners() working properly
        Actor target = new Actor();
        tooltip.setTarget(target); // setTarget() already calls attach() internally

        // Should not throw exception
        tooltip.detach();
    }

    @Test
    public void testDetachWithoutTarget() {
        Tooltip tooltip = new Tooltip();

        // Should not throw exception
        tooltip.detach();
    }

    @Test
    public void testRemoveTooltip() {
        // Use real Actor - mock Actor doesn't have getListeners() working properly
        Actor target = new Actor();
        Tooltip tooltip = new Tooltip();
        tooltip.setTarget(target); // setTarget() already calls attach() internally

        Tooltip.removeTooltip(target);

        // Should not throw exception - the tooltip should be removed from the target
    }

    @Test
    public void testRemoveTooltipWithoutTooltip() {
        // Use real Actor - mock Actor doesn't have getListeners() working properly
        Actor target = new Actor();

        // Should not throw exception
        Tooltip.removeTooltip(target);
    }

    @Test
    public void testGetPosition() {
        Tooltip tooltip = new Tooltip();

        tooltip.setPosition(10.5f, 20.7f);

        Assert.assertEquals("X position should be rounded to int", 10f, tooltip.getX(), 0.0001f);
        Assert.assertEquals("Y position should be rounded to int", 20f, tooltip.getY(), 0.0001f);
    }

    @Test
    public void testGetContentCell() {
        Tooltip tooltip = new Tooltip();
        Actor mockContent = Mockito.mock(Actor.class);
        tooltip.setContent(mockContent);

        Assert.assertNotNull("Content cell should not be null", tooltip.getContentCell());
    }

    @Test
    public void testTooltipStyleCopyConstructor() {
        Drawable mockBackground = Mockito.mock(Drawable.class);

        Tooltip.TooltipStyle original = new Tooltip.TooltipStyle(mockBackground);
        Tooltip.TooltipStyle copy = new Tooltip.TooltipStyle(original);

        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertEquals("Background should be copied", mockBackground, copy.background);
    }

    @Test
    public void testTooltipStyleConstructorWithDrawable() {
        Drawable mockBackground = Mockito.mock(Drawable.class);

        Tooltip.TooltipStyle style = new Tooltip.TooltipStyle(mockBackground);

        Assert.assertNotNull("Style should be created", style);
        Assert.assertEquals("Background should be set", mockBackground, style.background);
    }

    @Test
    public void testTooltipStyleDefaultConstructor() {
        Tooltip.TooltipStyle style = new Tooltip.TooltipStyle();

        Assert.assertNotNull("Style should be created", style);
        Assert.assertNull("Background should be null by default", style.background);
    }

    @Test
    public void testStaticFields() {
        Assert.assertTrue("Default fade time should be positive", Tooltip.DEFAULT_FADE_TIME > 0);
        Assert.assertTrue("Default appear delay time should be positive", Tooltip.DEFAULT_APPEAR_DELAY_TIME > 0);
    }

    @Test
    public void testTooltipInheritance() {
        Tooltip tooltip = new Tooltip();

        // Test that Tooltip extends VisTable
        Assert.assertTrue("Tooltip should extend VisTable",
                tooltip instanceof VisTable);
    }

    @Test
    public void testBuilderChaining() {
        // Use real Actor - mock Actor doesn't have getListeners() working properly
        Actor target = new Actor();
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Tooltip.TooltipStyle style = new Tooltip.TooltipStyle(mockBackground);

        Tooltip tooltip = new Tooltip.Builder("Test")
                .target(target)
                .style(style)
                .width(150f)
                .build();

        Assert.assertNotNull("Tooltip should be created", tooltip);
        Assert.assertSame("Target should be set", target, tooltip.getTarget());
        Assert.assertNotNull("Content should not be null", tooltip.getContent());
    }

    @Test
    public void testMultipleTooltips() {
        // Use Tooltip.Builder to set content text - Tooltip(String) constructor takes
        // style name, not text content
        Tooltip tooltip1 = new Tooltip.Builder("Tooltip 1").build();
        Tooltip tooltip2 = new Tooltip.Builder("Tooltip 2").build();
        // Use real Actor - mock Actor doesn't have getListeners() working properly
        Actor target1 = new Actor();
        Actor target2 = new Actor();

        tooltip1.setTarget(target1); // setTarget() already calls attach() internally
        tooltip2.setTarget(target2); // setTarget() already calls attach() internally

        Assert.assertSame("Tooltip 1 should have correct target", target1, tooltip1.getTarget());
        Assert.assertSame("Tooltip 2 should have correct target", target2, tooltip2.getTarget());
    }
}
