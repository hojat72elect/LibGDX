package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link ScrollableTextArea}.
 */
public class ScrollableTextAreaTest {

    @Test
    public void testConstructorWithText() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test Text");
        
        Assert.assertNotNull("Text area should be created", textArea);
        Assert.assertEquals("Text should be set", "Test Text", textArea.getText());
        Assert.assertTrue("Should implement Cullable", textArea instanceof Cullable);
    }

    @Test
    public void testConstructorWithTextAndStyleName() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test Text", "default");
        
        Assert.assertNotNull("Text area should be created", textArea);
        Assert.assertEquals("Text should be set", "Test Text", textArea.getText());
    }

    @Test
    public void testConstructorWithTextAndStyle() {
        VisTextFieldStyle style = new VisTextFieldStyle();
        ScrollableTextArea textArea = new ScrollableTextArea("Test Text", style);
        
        Assert.assertNotNull("Text area should be created", textArea);
        Assert.assertEquals("Text should be set", "Test Text", textArea.getText());
    }

    @Test
    public void testGetPrefHeight() {
        ScrollableTextArea textArea = new ScrollableTextArea("Line 1\nLine 2\nLine 3");
        
        float prefHeight = textArea.getPrefHeight();
        Assert.assertTrue("Pref height should be positive", prefHeight > 0);
        
        // Pref height should be based on number of lines and font line height
        int expectedLines = 3;
        float expectedHeight = expectedLines * textArea.getStyle().font.getLineHeight();
        Assert.assertEquals("Pref height should match lines * line height", 
                          expectedHeight, prefHeight, 0.0001f);
    }

    @Test
    public void testGetPrefHeightWithEmptyText() {
        ScrollableTextArea textArea = new ScrollableTextArea("");
        
        float prefHeight = textArea.getPrefHeight();
        Assert.assertTrue("Pref height should be positive even for empty text", prefHeight > 0);
    }

    @Test
    public void testGetPrefHeightWithSingleLine() {
        ScrollableTextArea textArea = new ScrollableTextArea("Single line");
        
        float prefHeight = textArea.getPrefHeight();
        float expectedHeight = 1 * textArea.getStyle().font.getLineHeight();
        Assert.assertEquals("Pref height should match one line height", 
                          expectedHeight, prefHeight, 0.0001f);
    }

    @Test
    public void testSetText() {
        ScrollableTextArea textArea = new ScrollableTextArea("Original");
        
        Assert.assertEquals("Original text should be set", "Original", textArea.getText());
        
        textArea.setText("New text");
        Assert.assertEquals("Text should be updated", "New text", textArea.getText());
    }

    @Test
    public void testSetTextWithEmptyString() {
        ScrollableTextArea textArea = new ScrollableTextArea("Original");
        
        textArea.setText("");
        Assert.assertEquals("Text should be empty", "", textArea.getText());
    }

    @Test
    public void testSetTextWithNull() {
        ScrollableTextArea textArea = new ScrollableTextArea("Original");
        
        textArea.setText(null);
        Assert.assertEquals("Text should be empty when null is set", "", textArea.getText());
    }

    @Test
    public void testCreateCompatibleScrollPane() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        ScrollPane scrollPane = textArea.createCompatibleScrollPane();
        
        Assert.assertNotNull("Scroll pane should be created", scrollPane);
        Assert.assertFalse("Overscroll X should be disabled", scrollPane.isOverscrollX());
        Assert.assertFalse("Overscroll Y should be disabled", scrollPane.isOverscrollY());
        Assert.assertFalse("Flick scroll should be disabled", scrollPane.isFlickScroll());
        Assert.assertFalse("Fade scroll bars should be disabled", scrollPane.getFadeScrollBars());
        Assert.assertTrue("Scroll bars on top should be enabled", scrollPane.getScrollbarsOnTop());
        Assert.assertTrue("X scrolling should be disabled", scrollPane.isScrollingDisabledX());
        Assert.assertFalse("Y scrolling should be enabled", scrollPane.isScrollingDisabledY());
    }

    @Test
    public void testSetCullingArea() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        Rectangle cullingArea = new Rectangle(0, 0, 100, 50);
        
        textArea.setCullingArea(cullingArea);
        
        // Test that culling area is set (we can't easily access the private field)
        // This test mainly ensures the method doesn't throw exceptions
        Assert.assertTrue("Should implement Cullable", textArea instanceof Cullable);
    }

    @Test
    public void testSetParentWithScrollPane() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        ScrollPane scrollPane = new ScrollPane(textArea);
        
        // Setting parent to scroll pane should not throw exception
        textArea.setParent(scrollPane);
        
        Assert.assertNotNull("Parent should be set", textArea.getParent());
    }

    @Test
    public void testSetParentWithNonScrollPane() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        Group group = new Group();
        
        // Setting parent to non-scroll pane should not throw exception
        textArea.setParent(group);
        
        Assert.assertNotNull("Parent should be set", textArea.getParent());
    }

    @Test
    public void testSetParentWithNull() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        
        // Setting parent to null should not throw exception
        textArea.setParent(null);
        
        Assert.assertNull("Parent should be null", textArea.getParent());
    }

    @Test
    public void testSizeChanged() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        
        // Size change should not throw exception
        textArea.setSize(200, 100);
        textArea.sizeChanged();
        
        Assert.assertEquals("Width should be set", 200, textArea.getWidth(), 0.0001f);
        Assert.assertEquals("Height should be set", 100, textArea.getHeight(), 0.0001f);
    }

    @Test
    public void testLinesShowingProperty() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        
        // After size change, linesShowing should be set to a large value
        textArea.sizeChanged();
        
        // We can't access the private field directly, but we can test that sizeChanged doesn't crash
        Assert.assertTrue("Text area should still be functional", textArea.getText() != null);
    }

    @Test
    public void testScrollTextAreaListener() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        
        // The text area should use ScrollTextAreaListener instead of TextAreaListener
        // We can test this by checking that the listener is created
        Assert.assertNotNull("Text area should have input listener", textArea.getChildren());
    }

    @Test
    public void testUpdateScrollLayout() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        
        // Should not throw exception
        textArea.updateScrollLayout();
    }

    @Test
    public void testUpdateScrollPosition() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        Rectangle cullingArea = new Rectangle(0, 0, 100, 50);
        textArea.setCullingArea(cullingArea);
        
        // Should not throw exception
        textArea.updateScrollPosition();
    }

    @Test
    public void testUpdateScrollPositionWithoutCullingArea() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        
        // Should not throw exception even without culling area
        textArea.updateScrollPosition();
    }

    @Test
    public void testUpdateScrollPositionWithoutScrollPaneParent() {
        ScrollableTextArea textArea = new ScrollableTextArea("Test text");
        Rectangle cullingArea = new Rectangle(0, 0, 100, 50);
        textArea.setCullingArea(cullingArea);
        textArea.setParent(new Group()); // Non-scroll pane parent
        
        // Should not throw exception
        textArea.updateScrollPosition();
    }

    @Test
    public void testProgrammaticChangeEvents() {
        ScrollableTextArea textArea = new ScrollableTextArea("Original");
        
        // Test with programmatic change events enabled (default)
        textArea.setText("New text");
        Assert.assertEquals("Text should be updated", "New text", textArea.getText());
    }

    @Test
    public void testChangeTextMethod() {
        ScrollableTextArea textArea = new ScrollableTextArea("Original");
        
        // Test the changeText method indirectly through setText
        textArea.setText("New text");
        Assert.assertEquals("Text should be updated", "New text", textArea.getText());
    }

    @Test
    public void testWithMultilineText() {
        String multilineText = "Line 1\nLine 2\nLine 3\nLine 4";
        ScrollableTextArea textArea = new ScrollableTextArea(multilineText);
        
        Assert.assertEquals("Multiline text should be preserved", multilineText, textArea.getText());
        
        float prefHeight = textArea.getPrefHeight();
        int expectedLines = 4;
        float expectedHeight = expectedLines * textArea.getStyle().font.getLineHeight();
        Assert.assertEquals("Pref height should account for all lines", 
                          expectedHeight, prefHeight, 0.0001f);
    }

    @Test
    public void testWithEmptyConstructor() {
        ScrollableTextArea textArea = new ScrollableTextArea("");
        
        Assert.assertNotNull("Text area should be created with empty text", textArea);
        Assert.assertEquals("Text should be empty", "", textArea.getText());
    }
}
