package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link Separator}.
 */
public class SeparatorTest {

    @Test
    public void testDefaultConstructor() {
        Separator separator = new Separator();
        
        Assert.assertNotNull("Separator should be created", separator);
        Assert.assertNotNull("Style should not be null", separator.getStyle());
    }

    @Test
    public void testConstructorWithStyleName() {
        Separator separator = new Separator("default");
        
        Assert.assertNotNull("Separator should be created", separator);
        Assert.assertNotNull("Style should not be null", separator.getStyle());
    }

    @Test
    public void testConstructorWithStyle() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 2);
        
        Separator separator = new Separator(style);
        
        Assert.assertNotNull("Separator should be created", separator);
        Assert.assertSame("Style should be set", style, separator.getStyle());
    }

    @Test
    public void testGetPrefHeight() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 5);
        Separator separator = new Separator(style);
        
        float prefHeight = separator.getPrefHeight();
        
        Assert.assertEquals("Pref height should equal style thickness", 5f, prefHeight, 0.0001f);
    }

    @Test
    public void testGetPrefWidth() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 3);
        Separator separator = new Separator(style);
        
        float prefWidth = separator.getPrefWidth();
        
        Assert.assertEquals("Pref width should equal style thickness", 3f, prefWidth, 0.0001f);
    }

    @Test
    public void testGetPrefHeightWithDefaultStyle() {
        Separator separator = new Separator();
        
        float prefHeight = separator.getPrefHeight();
        
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetPrefWidthWithDefaultStyle() {
        Separator separator = new Separator();
        
        float prefWidth = separator.getPrefWidth();
        
        Assert.assertTrue("Pref width should be positive", prefWidth > 0);
    }

    @Test
    public void testGetStyle() {
        Separator separator = new Separator();
        
        Separator.SeparatorStyle style = separator.getStyle();
        
        Assert.assertNotNull("Style should not be null", style);
    }

    @Test
    public void testSeparatorStyleCopyConstructor() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        
        Separator.SeparatorStyle original = new Separator.SeparatorStyle(mockBackground, 4);
        Separator.SeparatorStyle copy = new Separator.SeparatorStyle(original);
        
        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertEquals("Background should be copied", mockBackground, copy.background);
        Assert.assertEquals("Thickness should be copied", 4, copy.thickness);
    }

    @Test
    public void testSeparatorStyleConstructorWithDrawableAndThickness() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 7);
        
        Assert.assertNotNull("Style should be created", style);
        Assert.assertEquals("Background should be set", mockBackground, style.background);
        Assert.assertEquals("Thickness should be set", 7, style.thickness);
    }

    @Test
    public void testSeparatorStyleDefaultConstructor() {
        Separator.SeparatorStyle style = new Separator.SeparatorStyle();
        
        Assert.assertNotNull("Style should be created", style);
        Assert.assertNull("Background should be null by default", style.background);
        Assert.assertEquals("Thickness should be 0 by default", 0, style.thickness);
    }

    @Test
    public void testDraw() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 3);
        Separator separator = new Separator(style);
        separator.setSize(100, 3);
        
        Batch mockBatch = Mockito.mock(Batch.class);
        
        // Should not throw exception
        separator.draw(mockBatch, 1.0f);
        
        // Verify that background draw was called
        Mockito.verify(mockBackground).draw(mockBatch, 0f, 0f, 100f, 3f);
    }

    @Test
    public void testDrawWithParentAlpha() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 2);
        Separator separator = new Separator(style);
        separator.setSize(50, 2);
        
        Batch mockBatch = Mockito.mock(Batch.class);
        
        separator.draw(mockBatch, 0.5f);
        
        // Verify that background draw was called with correct dimensions
        Mockito.verify(mockBackground).draw(mockBatch, 0f, 0f, 50f, 2f);
    }

    @Test
    public void testDrawWithNullBackground() {
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(null, 2);
        Separator separator = new Separator(style);
        separator.setSize(50, 2);
        
        Batch mockBatch = Mockito.mock(Batch.class);
        
        // Should not throw exception even with null background
        try {
            separator.draw(mockBatch, 1.0f);
        } catch (NullPointerException e) {
            Assert.fail("Should not throw NullPointerException with null background");
        }
    }

    @Test
    public void testSeparatorWithZeroThickness() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 0);
        Separator separator = new Separator(style);
        
        Assert.assertEquals("Pref height should be 0", 0f, separator.getPrefHeight(), 0.0001f);
        Assert.assertEquals("Pref width should be 0", 0f, separator.getPrefWidth(), 0.0001f);
    }

    @Test
    public void testSeparatorWithNegativeThickness() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, -1);
        Separator separator = new Separator(style);
        
        Assert.assertEquals("Pref height should be negative", -1f, separator.getPrefHeight(), 0.0001f);
        Assert.assertEquals("Pref width should be negative", -1f, separator.getPrefWidth(), 0.0001f);
    }

    @Test
    public void testSeparatorWithLargeThickness() {
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(mockBackground, 100);
        Separator separator = new Separator(style);
        
        Assert.assertEquals("Pref height should be 100", 100f, separator.getPrefHeight(), 0.0001f);
        Assert.assertEquals("Pref width should be 100", 100f, separator.getPrefWidth(), 0.0001f);
    }

    @Test
    public void testStyleIndependence() {
        Drawable mockBackground1 = Mockito.mock(Drawable.class);
        Drawable mockBackground2 = Mockito.mock(Drawable.class);
        
        Separator.SeparatorStyle original = new Separator.SeparatorStyle(mockBackground1, 5);
        Separator.SeparatorStyle copy = new Separator.SeparatorStyle(original);
        copy.background = mockBackground2;
        copy.thickness = 10;
        
        Assert.assertEquals("Original background should remain unchanged", mockBackground1, original.background);
        Assert.assertEquals("Original thickness should remain unchanged", 5, original.thickness);
        Assert.assertEquals("Copy background should be updated", mockBackground2, copy.background);
        Assert.assertEquals("Copy thickness should be updated", 10, copy.thickness);
    }

    @Test
    public void testSeparatorInheritance() {
        Separator separator = new Separator();
        
        // Test that Separator extends Widget
        Assert.assertTrue("Separator should extend Widget", 
                        separator instanceof com.badlogic.gdx.scenes.scene2d.ui.Widget);
    }

    @Test
    public void testSeparatorStyleWithNullBackground() {
        Separator.SeparatorStyle style = new Separator.SeparatorStyle(null, 5);
        
        Assert.assertNotNull("Style should be created with null background", style);
        Assert.assertNull("Background should be null", style.background);
        Assert.assertEquals("Thickness should be set", 5, style.thickness);
    }

    @Test
    public void testMultipleSeparators() {
        Separator separator1 = new Separator();
        Separator separator2 = new Separator("default");
        Drawable mockBackground = Mockito.mock(Drawable.class);
        Separator.SeparatorStyle customStyle = new Separator.SeparatorStyle(mockBackground, 8);
        Separator separator3 = new Separator(customStyle);
        
        Assert.assertNotNull("All separators should be created", separator1);
        Assert.assertNotNull("All separators should be created", separator2);
        Assert.assertNotNull("All separators should be created", separator3);
        
        Assert.assertNotSame("Separators should have different style instances", 
                           separator1.getStyle(), separator2.getStyle());
        Assert.assertSame("Custom style should be set", customStyle, separator3.getStyle());
    }
}
