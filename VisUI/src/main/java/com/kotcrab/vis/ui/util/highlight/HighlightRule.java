package com.kotcrab.vis.ui.util.highlight;

import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.HighlightTextArea;

/**
 *  @since 1.1.2
 */
public interface HighlightRule {
    /**
     * Process this rule. This method should detect matches in text area text, create {@link Highlight} instances and add them to provided
     * highlights array.
     *
     * @param textArea   text area
     * @param highlights current highlights, new highlights can be added to this list however it should not be modified in any other ways
     */
    void process(HighlightTextArea textArea, Array<Highlight> highlights);
}
