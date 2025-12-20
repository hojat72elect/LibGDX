package com.kotcrab.vis.ui.util.highlight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.HighlightTextArea;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Highlighter rule using regex to detect text matches. Regexes and thus this rule can't be used on GWT.
 *
 *  @since 1.1.2
 */
public class RegexHighlightRule implements HighlightRule {
    private final Color color;
    private final Pattern pattern;

    public RegexHighlightRule(Color color, String regex) {
        this.color = color;
        pattern = Pattern.compile(regex);
    }

    @Override
    public void process(HighlightTextArea textArea, Array<Highlight> highlights) {
        Matcher matcher = pattern.matcher(textArea.getText());
        while (matcher.find()) {
            highlights.add(new Highlight(color, matcher.start(), matcher.end()));
        }
    }
}
