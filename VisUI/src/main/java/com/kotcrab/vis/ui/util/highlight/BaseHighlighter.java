package com.kotcrab.vis.ui.util.highlight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.HighlightTextArea;

/**
 * Highlighter aggregates multiple {@link HighlightRule} into single collection. Highlighter is used by {@link HighlightTextArea}
 * to get information about which parts of text should be highlighted. Compared to {@link Highlighter} this class is GWT compatible.
 *
 * @see Highlighter
 * @since 1.1.2
 */
public class BaseHighlighter {
    private final Array<HighlightRule> rules = new Array<HighlightRule>();

    /**
     * Adds highlighter rule. What is highlighted depends on rule implementation.
     */
    public void addRule(HighlightRule rule) {
        rules.add(rule);
    }

    /**
     * Adds word based highlighter rule. Note that for most uses, word based rules are not sophisticated enough - for example
     * using regex rule for programming language keywords detection is far more robust.
     *
     * @see WordHighlightRule
     */
    public void word(Color color, String word) {
        addRule(new WordHighlightRule(color, word));
    }

    /**
     * Adds word based highlighter rule. Utility method allowing to add many words at once.
     *
     * @see #word(Color, String)
     * @see WordHighlightRule
     */
    public void word(Color color, String... words) {
        for (String word : words) {
            addRule(new WordHighlightRule(color, word));
        }
    }

    /**
     * Process all rules in this highlighter.
     *
     * @param highlights current highlights, new highlights can be added to this list however it should not be modified in any other ways
     */
    public void process(HighlightTextArea textArea, Array<Highlight> highlights) {
        for (HighlightRule rule : rules) {
            rule.process(textArea, highlights);
        }
    }
}
