package com.kotcrab.vis.ui.util.highlight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link BaseHighlighter}.
 */
public class BaseHighlighterTest {

    private BaseHighlighter highlighter;

    @Mock
    private HighlightRule mockRule1;

    @Mock
    private HighlightRule mockRule2;

    @Mock
    private HighlightTextArea mockTextArea;

    private Array<Highlight> highlights;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        highlighter = new BaseHighlighter();
        highlights = new Array<>();
    }

    @Test
    public void testAddRule() {
        // Test adding a single rule
        highlighter.addRule(mockRule1);

        // Verify the rule was added by checking process method calls it
        highlighter.process(mockTextArea, highlights);
        verify(mockRule1).process(mockTextArea, highlights);
    }

    @Test
    public void testAddMultipleRules() {
        // Test adding multiple rules
        highlighter.addRule(mockRule1);
        highlighter.addRule(mockRule2);

        // Verify both rules are processed
        highlighter.process(mockTextArea, highlights);
        verify(mockRule1).process(mockTextArea, highlights);
        verify(mockRule2).process(mockTextArea, highlights);
    }

    @Test
    public void testWordSingle() {
        // Test adding a single word rule
        Color testColor = Color.RED;
        String testWord = "test";

        highlighter.word(testColor, testWord);

        // Process and verify the word rule was added
        when(mockTextArea.getText()).thenReturn("This is a test string");
        highlighter.process(mockTextArea, highlights);

        assertEquals("Should have one highlight", 1, highlights.size);
        Highlight highlight = highlights.first();
        assertEquals("Color should match", testColor, highlight.color());
        assertEquals("Start position should be correct", 10, highlight.start());
        assertEquals("End position should be correct", 14, highlight.end());
    }

    @Test
    public void testWordMultiple() {
        // Test adding multiple word rules at once
        Color testColor = Color.BLUE;
        String[] testWords = {"public", "class", "void"};

        highlighter.word(testColor, testWords);

        // Process with text containing all words
        when(mockTextArea.getText()).thenReturn("public class Test { void method(); }");
        highlighter.process(mockTextArea, highlights);

        assertEquals("Should have three highlights", 3, highlights.size);

        // Verify all highlights have the correct color
        for (Highlight highlight : highlights) {
            assertEquals("All highlights should have the same color", testColor, highlight.color());
        }
    }

    @Test
    public void testWordMultipleWithVarargs() {
        // Test varargs functionality
        Color testColor = Color.GREEN;

        highlighter.word(testColor, "if", "else", "for", "while");

        when(mockTextArea.getText()).thenReturn("if (condition) { for (int i = 0; i < 10; i++) { if (i == 5) { break; } else { continue; } } while (true); }");
        highlighter.process(mockTextArea, highlights);

        assertTrue("Should have multiple highlights", highlights.size >= 4);

        // Verify all highlights have the correct color
        for (Highlight highlight : highlights) {
            assertEquals("All highlights should have the same color", testColor, highlight.color());
        }
    }

    @Test
    public void testProcessWithEmptyRules() {
        // Test processing when no rules have been added
        highlighter.process(mockTextArea, highlights);

        assertTrue("Highlights should be empty when no rules", highlights.isEmpty());
        verifyNoInteractions(mockRule1, mockRule2);
    }

    @Test
    public void testProcessWithNullHighlights() {
        // Test that process method handles null highlights array gracefully
        try {
            highlighter.process(mockTextArea, null);
            // Should not throw exception
            assertTrue("Should handle null highlights gracefully", true);
        } catch (Exception e) {
            fail("Should not throw exception with null highlights: " + e.getMessage());
        }
    }

    @Test
    public void testProcessWithEmptyHighlights() {
        // Test processing with empty highlights array
        highlighter.addRule(mockRule1);

        highlighter.process(mockTextArea, highlights);

        verify(mockRule1).process(mockTextArea, highlights);
        assertTrue("Highlights should remain empty if rule doesn't add any", highlights.isEmpty());
    }

    @Test
    public void testWordRuleWithEmptyText() {
        // Test word rule with empty text
        highlighter.word(Color.YELLOW, "test");

        when(mockTextArea.getText()).thenReturn("");
        highlighter.process(mockTextArea, highlights);

        assertTrue("Should have no highlights for empty text", highlights.isEmpty());
    }

    @Test
    public void testWordRuleWithNoMatches() {
        // Test word rule when word is not found in text
        highlighter.word(Color.CYAN, "nonexistent");

        when(mockTextArea.getText()).thenReturn("This text does not contain the target word");
        highlighter.process(mockTextArea, highlights);

        assertTrue("Should have no highlights when word not found", highlights.isEmpty());
    }

    @Test
    public void testWordRuleWithMultipleOccurrences() {
        // Test word rule with multiple occurrences of the same word
        highlighter.word(Color.MAGENTA, "test");

        when(mockTextArea.getText()).thenReturn("test test test");
        highlighter.process(mockTextArea, highlights);

        assertEquals("Should have three highlights for three occurrences", 3, highlights.size);

        // Verify positions
        assertEquals("First highlight start", 0, highlights.get(0).start());
        assertEquals("First highlight end", 4, highlights.get(0).end());
        assertEquals("Second highlight start", 5, highlights.get(1).start());
        assertEquals("Second highlight end", 9, highlights.get(1).end());
        assertEquals("Third highlight start", 10, highlights.get(2).start());
        assertEquals("Third highlight end", 14, highlights.get(2).end());
    }

    @Test
    public void testWordRuleWithOverlappingWords() {
        // Test word rule with overlapping words
        highlighter.word(Color.ORANGE, "test");
        highlighter.word(Color.PINK, "testing");

        when(mockTextArea.getText()).thenReturn("testing");
        highlighter.process(mockTextArea, highlights);

        assertEquals("Should have two highlights for overlapping words", 2, highlights.size);
    }

    @Test
    public void testRuleProcessingOrder() {
        // Test that rules are processed in the order they were added
        highlighter.addRule(mockRule1);
        highlighter.addRule(mockRule2);

        highlighter.process(mockTextArea, highlights);

        // Verify order of processing
        var inOrder = inOrder(mockRule1, mockRule2);
        inOrder.verify(mockRule1).process(mockTextArea, highlights);
        inOrder.verify(mockRule2).process(mockTextArea, highlights);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWordRuleWithNullColor() {
        // Test that word rule throws exception for null color
        highlighter.word(null, "test");

        when(mockTextArea.getText()).thenReturn("test");
        highlighter.process(mockTextArea, highlights);
    }

    @Test(expected = NullPointerException.class)
    public void testWordRuleWithNullWord() {
        // Test that word rule throws exception for null word
        highlighter.word(Color.RED, (String) null);

        when(mockTextArea.getText()).thenReturn("test");
        highlighter.process(mockTextArea, highlights);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWordRuleWithEmptyWord() {
        // Test word rule with empty word - should throw IllegalArgumentException
        // when trying to create Highlight with start >= end
        highlighter.word(Color.RED, "");

        when(mockTextArea.getText()).thenReturn("test");
        highlighter.process(mockTextArea, highlights);
    }

    @Test
    public void testMultipleWordRulesSameColor() {
        // Test multiple word rules with the same color
        Color testColor = Color.PURPLE;
        highlighter.word(testColor, "public");
        highlighter.word(testColor, "private");
        highlighter.word(testColor, "protected");

        when(mockTextArea.getText()).thenReturn("public class Test { private int value; protected String name; }");
        highlighter.process(mockTextArea, highlights);

        assertEquals("Should have three highlights", 3, highlights.size);

        // Verify all highlights have the same color
        for (Highlight highlight : highlights) {
            assertEquals("All highlights should have the same color", testColor, highlight.color());
        }
    }

    @Test
    public void testMultipleWordRulesDifferentColors() {
        // Test multiple word rules with different colors
        highlighter.word(Color.RED, "error");
        highlighter.word(Color.GREEN, "success");
        highlighter.word(Color.BLUE, "info");

        when(mockTextArea.getText()).thenReturn("error: operation failed, success: retry worked, info: processing complete");
        highlighter.process(mockTextArea, highlights);

        assertEquals("Should have three highlights", 3, highlights.size);

        // Verify each highlight has the correct color
        boolean foundRed = false, foundGreen = false, foundBlue = false;
        for (Highlight highlight : highlights) {
            if (highlight.color() == Color.RED) foundRed = true;
            if (highlight.color() == Color.GREEN) foundGreen = true;
            if (highlight.color() == Color.BLUE) foundBlue = true;
        }

        assertTrue("Should have red highlight", foundRed);
        assertTrue("Should have green highlight", foundGreen);
        assertTrue("Should have blue highlight", foundBlue);
    }
}
