package com.kotcrab.vis.ui.test.manual;

import com.badlogic.gdx.graphics.Color;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.highlight.Highlighter;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TestHighlightTextArea extends VisWindow {

    public TestHighlightTextArea() {
        super("highlight textarea");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        setResizable(true);
        addCloseButton();
        closeOnEscape();
        addVisWidgets();

        setSize(280, 380);
        centerWindow();
    }

    private void addVisWidgets() {
        HighlightTextArea textArea = new HighlightTextArea("private class Foo {\n" +
                "  int foo;\n" +
                "  float bar;\n" +
                "  String foobar;\n" +
                "}");
        Highlighter highlighter = new Highlighter();
        //it is much more reliable to use regex for keyword detection
        highlighter.regex(Color.valueOf("66CCB3"), "\\b(class|private|protected|public|if|else|void|for|while|continue|break)\\b");
        highlighter.regex(Color.valueOf("BED6FF"), "\\b(int|float|boolean|short|long|char)\\b");
        highlighter.regex(Color.valueOf("EFC090"), "\\b(foo|bar)\\b");
        highlighter.regex(Color.valueOf("75715E"), "/\\*(?:.|[\\n\\r])*?\\*/"); //block comments (/* comment */)
        textArea.setHighlighter(highlighter);
        add(textArea.createCompatibleScrollPane()).grow();
    }
}
