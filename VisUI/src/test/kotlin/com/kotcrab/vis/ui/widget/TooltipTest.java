package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link Tooltip}.
 */
public class TooltipTest {

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
        Actor mockTarget = Mockito.mock(Actor.class);
        Tooltip.Builder builder = new Tooltip.Builder("Test").target(mockTarget);
        Tooltip tooltip = builder.build();
        
        Assert.assertNotNull("Tooltip should be created", tooltip);
        Assert.assertSame("Target should be set", mockTarget, tooltip.getTarget());
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

    @Test(expected = IllegalArgumentException.class)
    public void testBuilderWithZeroWidth() {
        Tooltip.Builder builder = new Tooltip.Builder("Test");
        builder.width(0f);
    }

    @Test
    public void testGetAndSetTarget() {
        Tooltip tooltip = new Tooltip();
        Actor mockTarget = Mockito.mock(Actor.class);
        
        Assert.assertNull("Target should be null initially", tooltip.getTarget());
        
        tooltip.setTarget(mockTarget);
        Assert.assertSame("Target should be set", mockTarget, tooltip.getTarget());
        
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
        Actor mockTarget = Mockito.mock(Actor.class);
        tooltip.setTarget(mockTarget);
        
        // Should not throw exception
        tooltip.attach();
        
        // Test that attaching again throws exception
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
        Actor mockTarget = Mockito.mock(Actor.class);
        tooltip.setTarget(mockTarget);
        tooltip.attach();
        
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
        Actor mockTarget = Mockito.mock(Actor.class);
        Tooltip tooltip = new Tooltip();
        tooltip.setTarget(mockTarget);
        tooltip.attach();
        
        Tooltip.removeTooltip(mockTarget);
        
        // Should not throw exception - the tooltip should be removed from the target
    }

    @Test
    public void testRemoveTooltipWithoutTooltip() {
        Actor mockTarget = Mockito.mock(Actor.class);
        
        // Should not throw exception
        Tooltip.removeTooltip(mockTarget);
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
        Actor mockTarget = Mockito.mock(Actor.class);
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Tooltip.TooltipStyle style = new Tooltip.TooltipStyle(mockBackground);
        
        Tooltip tooltip = new Tooltip.Builder("Test")
            .target(mockTarget)
            .style(style)
            .width(150f)
            .build();
        
        Assert.assertNotNull("Tooltip should be created", tooltip);
        Assert.assertSame("Target should be set", mockTarget, tooltip.getTarget());
        Assert.assertNotNull("Content should not be null", tooltip.getContent());
    }

    @Test
    public void testMultipleTooltips() {
        Tooltip tooltip1 = new Tooltip("Tooltip 1");
        Tooltip tooltip2 = new Tooltip("Tooltip 2");
        Actor target1 = Mockito.mock(Actor.class);
        Actor target2 = Mockito.mock(Actor.class);
        
        tooltip1.setTarget(target1);
        tooltip2.setTarget(target2);
        
        tooltip1.attach();
        tooltip2.attach();
        
        Assert.assertSame("Tooltip 1 should have correct target", target1, tooltip1.getTarget());
        Assert.assertSame("Tooltip 2 should have correct target", target2, tooltip2.getTarget());
    }
}
