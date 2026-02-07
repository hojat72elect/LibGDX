package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link VisLabel}.
 */
public class VisLabelTest {

    @Test
    public void testDefaultConstructor() {
        VisLabel label = new VisLabel();
        
        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Default text should be empty", "", label.getText().toString());
        Assert.assertNotNull("Style should not be null", label.getStyle());
    }

    @Test
    public void testConstructorWithText() {
        VisLabel label = new VisLabel("Test Text");
        
        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertNotNull("Style should not be null", label.getStyle());
    }

    @Test
    public void testConstructorWithTextAndColor() {
        Color testColor = Color.RED;
        VisLabel label = new VisLabel("Test Text", testColor);
        
        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertEquals("Color should be set", testColor, label.getColor());
    }

    @Test
    public void testConstructorWithTextAndAlignment() {
        VisLabel label = new VisLabel("Test Text", Label.Align.CENTER);
        
        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertEquals("Alignment should be set", Label.Align.CENTER, label.getLabelAlign());
    }

    @Test
    public void testConstructorWithTextAndStyle() {
        Label.LabelStyle mockStyle = Mockito.mock(Label.LabelStyle.class);
        VisLabel label = new VisLabel("Test Text", mockStyle);
        
        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertSame("Style should be set", mockStyle, label.getStyle());
    }

    @Test
    public void testConstructorWithTextAndStyleName() {
        VisLabel label = new VisLabel("Test Text", "default");
        
        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertNotNull("Style should not be null", label.getStyle());
    }

    @Test
    public void testConstructorWithTextFontNameAndColor() {
        VisLabel label = new VisLabel("Test Text", "default-font", Color.BLUE);
        
        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertEquals("Color should be set", Color.BLUE, label.getColor());
    }

    @Test
    public void testConstructorWithTextFontNameAndColorName() {
        VisLabel label = new VisLabel("Test Text", "default-font", "white");
        
        Assert.assertNotNull("Label should be created", label);
        Assert.assertEquals("Text should be set", "Test Text", label.getText().toString());
        Assert.assertNotNull("Color should be set", label.getColor());
    }

    @Test
    public void testSetText() {
        VisLabel label = new VisLabel();
        label.setText("New Text");
        
        Assert.assertEquals("Text should be updated", "New Text", label.getText().toString());
    }

    @Test
    public void testSetTextWithNull() {
        VisLabel label = new VisLabel("Original");
        label.setText(null);
        
        Assert.assertEquals("Text should be empty when null is set", "", label.getText().toString());
    }

    @Test
    public void testSetColor() {
        VisLabel label = new VisLabel();
        Color testColor = Color.GREEN;
        label.setColor(testColor);
        
        Assert.assertEquals("Color should be set", testColor, label.getColor());
    }

    @Test
    public void testSetAlignment() {
        VisLabel label = new VisLabel("Test");
        label.setAlignment(Label.Align.RIGHT);
        
        Assert.assertEquals("Alignment should be set", Label.Align.RIGHT, label.getLabelAlign());
    }

    @Test
    public void testSetWrap() {
        VisLabel label = new VisLabel("Test Text");
        label.setWrap(true);
        
        Assert.assertTrue("Wrap should be enabled", label.getWrap());
    }

    @Test
    public void testGetPrefWidth() {
        VisLabel label = new VisLabel("Test Text");
        float prefWidth = label.getPrefWidth();
        
        Assert.assertTrue("Pref width should be positive", prefWidth > 0);
    }

    @Test
    public void testGetPrefHeight() {
        VisLabel label = new VisLabel("Test Text");
        float prefHeight = label.getPrefHeight();
        
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testGetPrefWidthWithEmptyText() {
        VisLabel label = new VisLabel("");
        float prefWidth = label.getPrefWidth();
        
        Assert.assertTrue("Pref width should be non-negative", prefWidth >= 0);
    }

    @Test
    public void testGetPrefHeightWithEmptyText() {
        VisLabel label = new VisLabel("");
        float prefHeight = label.getPrefHeight();
        
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
    }

    @Test
    public void testLabelInheritance() {
        VisLabel label = new VisLabel();
        
        Assert.assertTrue("VisLabel should extend Label", label instanceof Label);
    }

    @Test
    public void testMultipleLabels() {
        VisLabel label1 = new VisLabel("Label 1");
        VisLabel label2 = new VisLabel("Label 2", Color.RED);
        VisLabel label3 = new VisLabel("Label 3", Label.Align.CENTER);
        
        Assert.assertNotNull("All labels should be created", label1);
        Assert.assertNotNull("All labels should be created", label2);
        Assert.assertNotNull("All labels should be created", label3);
        
        Assert.assertEquals("Label 1 should have correct text", "Label 1", label1.getText().toString());
        Assert.assertEquals("Label 2 should have correct text", "Label 2", label2.getText().toString());
        Assert.assertEquals("Label 3 should have correct text", "Label 3", label3.getText().toString());
        
        Assert.assertEquals("Label 2 should have red color", Color.RED, label2.getColor());
        Assert.assertEquals("Label 3 should have center alignment", Label.Align.CENTER, label3.getLabelAlign());
    }

    @Test
    public void testLabelWithLongText() {
        String longText = "This is a very long text that should test the label's ability to handle longer strings";
        VisLabel label = new VisLabel(longText);
        
        Assert.assertEquals("Long text should be set correctly", longText, label.getText().toString());
        Assert.assertTrue("Pref width should be larger for long text", label.getPrefWidth() > 0);
    }

    @Test
    public void testLabelWithSpecialCharacters() {
        String specialText = "Special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        VisLabel label = new VisLabel(specialText);
        
        Assert.assertEquals("Special characters should be handled", specialText, label.getText().toString());
    }
}
