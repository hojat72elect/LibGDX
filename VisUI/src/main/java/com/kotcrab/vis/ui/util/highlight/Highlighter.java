package com.kotcrab.vis.ui.util.highlight;

import com.badlogic.gdx.graphics.Color;
import com.kotcrab.vis.ui.widget.HighlightTextArea;

/**
 * Highlighter aggregates multiple {@link HighlightRule} into single collection. Highlighter is used by {@link HighlightTextArea}
 * to get information about which parts of text should be highlighted. If you need GWT compatibility, you need to use {@link BaseHighlighter}.
 *
 * @see BaseHighlighter
 * @since 1.1.2
 */
public class Highlighter extends BaseHighlighter {
    /**
     * Adds regex based highlighter rule.
     */
    public void regex(Color color, String regex) {
        addRule(new RegexHighlightRule(color, regex));
    }
}
