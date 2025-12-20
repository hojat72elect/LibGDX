package com.kotcrab.vis.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

public class ListViewStyle {
    public ScrollPane.ScrollPaneStyle scrollPaneStyle;

    public ListViewStyle() {
    }

    public ListViewStyle(ListViewStyle style) {
        if (style.scrollPaneStyle != null) this.scrollPaneStyle = new ScrollPane.ScrollPaneStyle(style.scrollPaneStyle);
    }
}
