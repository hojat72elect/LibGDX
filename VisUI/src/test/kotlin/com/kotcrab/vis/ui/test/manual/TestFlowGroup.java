package com.kotcrab.vis.ui.test.manual;

import com.kotcrab.vis.ui.layout.FlowGroup;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisWindow;

public class TestFlowGroup extends VisWindow {
    public TestFlowGroup() {
        super("flow groups");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        setResizable(true);
        addCloseButton();
        closeOnEscape();

        FlowGroup group = new FlowGroup(true, 2);

        String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi luctus magna sit amet tellus egestas tincidunt. " +
                "Morbi tempus eleifend dictum. Nunc ex nisl, dignissim eget gravida vel, rutrum a nibh. Fusce congue odio ac elit " +
                "rhoncus rutrum. Donec nec lectus leo. Phasellus et consectetur ante. Cras vel consectetur mauris, sed semper lectus. ";
        String[] parts = lorem.split(" ");
        for (String part : parts) {
            group.addActor(new VisLabel(part));
        }

        VisScrollPane scrollPane = new VisScrollPane(group);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(false);
        scrollPane.setOverscroll(false, false);
        scrollPane.setScrollingDisabled(!group.isVertical(), group.isVertical());
        add(scrollPane).grow();

        setSize(300, 150);
        centerWindow();
    }
}
