package com.kotcrab.vis.ui.util.highlight;

import com.badlogic.gdx.graphics.Color;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link Highlight}.
 */
public class HighlightTest {

    @Test
    public void testValidHighlightCreation() {
        Color color = Color.RED;
        int start = 5;
        int end = 10;
        
        Highlight highlight = new Highlight(color, start, end);
        
        assertEquals("Color should match", color, highlight.color());
        assertEquals("Start should match", start, highlight.start());
        assertEquals("End should match", end, highlight.end());
    }

    @Test
    public void testHighlightWithZeroLength() {
        Color color = Color.BLUE;
        int start = 0;
        int end = 0;
        
        try {
            new Highlight(color, start, end);
            fail("Should throw IllegalArgumentException for zero-length highlight");
        } catch (IllegalArgumentException e) {
            assertTrue("Error message should mention start >= end", 
                      e.getMessage().contains("start can't be >= end"));
        }
    }

    @Test
    public void testHighlightWithNegativeLength() {
        Color color = Color.GREEN;
        int start = 10;
        int end = 5;
        
        try {
            new Highlight(color, start, end);
            fail("Should throw IllegalArgumentException for negative-length highlight");
        } catch (IllegalArgumentException e) {
            assertTrue("Error message should mention start >= end", 
                      e.getMessage().contains("start can't be >= end"));
            assertTrue("Error message should include values", 
                      e.getMessage().contains("10 >= 5"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHighlightWithNullColor() {
        new Highlight(null, 0, 5);
    }

    @Test
    public void testNullColorErrorMessage() {
        try {
            new Highlight(null, 0, 5);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message should be specific", 
                        "color can't be null", e.getMessage());
        }
    }

    @Test
    public void testCompareTo() {
        Color color = Color.YELLOW;
        
        Highlight highlight1 = new Highlight(color, 0, 5);
        Highlight highlight2 = new Highlight(color, 10, 15);
        Highlight highlight3 = new Highlight(color, 5, 10);
        
        assertTrue("highlight1 should be less than highlight2", 
                  highlight1.compareTo(highlight2) < 0);
        assertTrue("highlight2 should be greater than highlight1", 
                  highlight2.compareTo(highlight1) > 0);
        assertTrue("highlight1 should be less than highlight3", 
                  highlight1.compareTo(highlight3) < 0);
        assertTrue("highlight3 should be greater than highlight1", 
                  highlight3.compareTo(highlight1) > 0);
        assertEquals("highlight1 compared to itself should be 0", 
                    0, highlight1.compareTo(highlight1));
    }

    @Test
    public void testCompareToWithSameStart() {
        Color color = Color.CYAN;
        
        Highlight highlight1 = new Highlight(color, 5, 10);
        Highlight highlight2 = new Highlight(color, 5, 15);
        
        assertEquals("Highlights with same start should be equal", 
                    0, highlight1.compareTo(highlight2));
        assertEquals("Highlights with same start should be equal (reverse)", 
                    0, highlight2.compareTo(highlight1));
    }

    @Test
    public void testHighlightWithAdjacentRanges() {
        Color color = Color.MAGENTA;
        
        Highlight highlight1 = new Highlight(color, 0, 5);
        Highlight highlight2 = new Highlight(color, 5, 10);
        
        assertTrue("Adjacent highlights should be comparable", 
                  highlight1.compareTo(highlight2) < 0);
        assertTrue("Adjacent highlights should be comparable (reverse)", 
                  highlight2.compareTo(highlight1) > 0);
    }

    @Test
    public void testHighlightWithLargeValues() {
        Color color = Color.ORANGE;
        int start = Integer.MAX_VALUE - 10;
        int end = Integer.MAX_VALUE;
        
        Highlight highlight = new Highlight(color, start, end);
        
        assertEquals("Large start value should be preserved", start, highlight.start());
        assertEquals("Large end value should be preserved", end, highlight.end());
        assertEquals("Color should be preserved", color, highlight.color());
    }

    @Test
    public void testHighlightWithNegativeStart() {
        Color color = Color.PINK;
        int start = -5;
        int end = 5;
        
        Highlight highlight = new Highlight(color, start, end);
        
        assertEquals("Negative start should be allowed", start, highlight.start());
        assertEquals("End should be preserved", end, highlight.end());
        assertEquals("Color should be preserved", color, highlight.color());
    }

    @Test
    public void testHighlightWithSingleCharacter() {
        Color color = Color.WHITE;
        int start = 7;
        int end = 8;
        
        Highlight highlight = new Highlight(color, start, end);
        
        assertEquals("Single character highlight should work", start, highlight.start());
        assertEquals("Single character highlight should work", end, highlight.end());
        assertEquals("Color should be preserved", color, highlight.color());
    }

    @Test
    public void testRecordEqualsAndHashCode() {
        Color color1 = Color.RED;
        Color color2 = Color.BLUE;
        
        Highlight highlight1a = new Highlight(color1, 0, 5);
        Highlight highlight1b = new Highlight(color1, 0, 5);
        Highlight highlight2 = new Highlight(color2, 0, 5);
        Highlight highlight3 = new Highlight(color1, 1, 5);
        
        assertEquals("Equal highlights should be equal", highlight1a, highlight1b);
        assertEquals("Equal highlights should have same hash code", 
                    highlight1a.hashCode(), highlight1b.hashCode());
        
        assertNotEquals("Different colors should not be equal", highlight1a, highlight2);
        assertNotEquals("Different starts should not be equal", highlight1a, highlight3);
        
        assertNotEquals("Should not be equal to null", highlight1a, null);
        assertNotEquals("Should not be equal to different type", highlight1a, "string");
    }

    @Test
    public void testRecordToString() {
        Color color = Color.RED;
        Highlight highlight = new Highlight(color, 0, 5);
        
        String toString = highlight.toString();
        assertNotNull("toString should not be null", toString);
        assertTrue("toString should contain class name", toString.contains("Highlight"));
        assertTrue("toString should contain color info", toString.contains("color"));
        assertTrue("toString should contain start", toString.contains("start"));
        assertTrue("toString should contain end", toString.contains("end"));
    }
}
