package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link ListViewStyle}.
 */
public class ListViewStyleTest {

    @Test
    public void testDefaultConstructor() {
        ListViewStyle style = new ListViewStyle();
        
        Assert.assertNotNull("Style should be created", style);
        Assert.assertNull("Scroll pane style should be null by default", style.scrollPaneStyle);
    }

    @Test
    public void testCopyConstructor() {
        ListViewStyle original = new ListViewStyle();
        original.scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        
        ListViewStyle copy = new ListViewStyle(original);
        
        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertNotNull("Copied scroll pane style should not be null", copy.scrollPaneStyle);
        Assert.assertNotSame("Scroll pane style should be a new instance", 
                           original.scrollPaneStyle, copy.scrollPaneStyle);
    }

    @Test
    public void testCopyConstructorWithNullStyle() {
        ListViewStyle original = new ListViewStyle();
        original.scrollPaneStyle = null;
        
        ListViewStyle copy = new ListViewStyle(original);
        
        Assert.assertNotNull("Copy should be created", copy);
        Assert.assertNull("Copied scroll pane style should be null", copy.scrollPaneStyle);
    }

    @Test
    public void testScrollPaneStyleField() {
        ListViewStyle style = new ListViewStyle();
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        
        style.scrollPaneStyle = scrollPaneStyle;
        
        Assert.assertSame("Scroll pane style should be set", scrollPaneStyle, style.scrollPaneStyle);
    }

    @Test
    public void testScrollPaneStyleCopy() {
        // Create a mock drawable for testing
        Drawable mockDrawable = Mockito.mock(Drawable.class);
        
        ListViewStyle original = new ListViewStyle();
        original.scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        original.scrollPaneStyle.background = mockDrawable;
        
        ListViewStyle copy = new ListViewStyle(original);
        
        Assert.assertEquals("Background should be copied", 
                          mockDrawable, copy.scrollPaneStyle.background);
    }

    @Test
    public void testStyleIndependence() {
        Drawable mockDrawable1 = Mockito.mock(Drawable.class);
        Drawable mockDrawable2 = Mockito.mock(Drawable.class);
        
        ListViewStyle original = new ListViewStyle();
        original.scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        original.scrollPaneStyle.background = mockDrawable1;
        
        ListViewStyle copy = new ListViewStyle(original);
        copy.scrollPaneStyle.background = mockDrawable2;
        
        Assert.assertEquals("Original should remain unchanged", 
                          mockDrawable1, original.scrollPaneStyle.background);
        Assert.assertEquals("Copy should have new value", 
                          mockDrawable2, copy.scrollPaneStyle.background);
    }
}
