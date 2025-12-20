package com.kotcrab.vis.ui.test.manual;

import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.MultiSplitPane;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TestMultiSplitPane extends VisWindow {
    private final boolean vertical = false;

    public TestMultiSplitPane() {
        super("multisplitpane");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        addVisWidgets();

        setSize(300, 150);
        centerWindow();
    }

    private void addVisWidgets() {
        VisLabel label = new VisLabel("Label #1");
        VisLabel label2 = new VisLabel("Label #2");
        VisLabel label3 = new VisLabel("Label #3");

        MultiSplitPane splitPane = new MultiSplitPane(vertical);
        splitPane.setWidgets(label, label2, label3);
        add(splitPane).fill().expand();
    }
}
