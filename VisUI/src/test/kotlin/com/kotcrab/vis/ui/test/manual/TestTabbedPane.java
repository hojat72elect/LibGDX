package com.kotcrab.vis.ui.test.manual;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane.TabbedPaneStyle;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

public class TestTabbedPane extends VisWindow {

    public TestTabbedPane(boolean vertical) {
        super("tabbed pane");

        TableUtils.setSpacingDefaults(this);

        setResizable(true);
        addCloseButton();
        closeOnEscape();

        final VisTable container = new VisTable();

        TabbedPaneStyle style = VisUI.getSkin().get(vertical ? "vertical" : "default", TabbedPaneStyle.class);
        TabbedPane tabbedPane = new TabbedPane(style);
        tabbedPane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab(Tab tab) {
                container.clearChildren();
                container.add(tab.getContentTable()).expand().fill();
            }
        });

        if (style.vertical) {
            top();
            defaults().top();
            add(tabbedPane.getTable()).growY();
            add(container).expand().fill();
        } else {
            add(tabbedPane.getTable()).expandX().fillX();
            row();
            add(container).expand().fill();
        }

        tabbedPane.add(new TestTab("tab1"));
        tabbedPane.add(new TestTab("tab2"));
        tabbedPane.add(new TestTab("tab3"));
        tabbedPane.add(new TestTab("tab4"));
        tabbedPane.add(new TestTab("tab5"));
        tabbedPane.add(new TestTab("tab6"));
        tabbedPane.add(new TestTab("tab7"));
        tabbedPane.add(new TestTab("tab8"));

        Tab tab = new TestTab("tab9");
        tabbedPane.add(tab);
        tabbedPane.disableTab(tab, true);

//		debugAll();
        setSize(300, 200);
        centerWindow();
    }

    private class TestTab extends Tab {
        private final String title;
        private final Table content;

        public TestTab(String title) {
            super(false, true);
            this.title = title;

            content = new VisTable();
            content.add(new VisLabel(title));
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public Table getContentTable() {
            return content;
        }
    }
}
