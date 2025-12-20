package com.kotcrab.vis.ui.util.highlight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.HighlightTextArea;

/**
 * Highlighter rule using {@link String#indexOf(String)} to detect text matches.
 *
 * @since 1.1.2
 */
public class WordHighlightRule implements HighlightRule {
    private final Color color;
    private final String word;

    public WordHighlightRule(Color color, String word) {
        this.color = color;
        this.word = word;
    }

    @Override
    public void process(HighlightTextArea textArea, Array<Highlight> highlights) {
        String text = textArea.getText();
        int index = text.indexOf(word);
        while (index >= 0) {
            highlights.add(new Highlight(color, index, index += word.length()));
            index = text.indexOf(word, index);
        }
    }
}
