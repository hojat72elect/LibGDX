package com.kotcrab.vis.ui.util.adapter;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * Very simple default implementation of adapter for {@link ListView}. Uses {@link Object#toString()} to create text
 * view for item.
 */
public class SimpleListAdapter<ItemT> extends ArrayAdapter<ItemT, VisTable> {
    private final SimpleListAdapterStyle style;

    public SimpleListAdapter(Array<ItemT> array) {
        this(array, "default");
    }

    public SimpleListAdapter(Array<ItemT> array, String styleName) {
        this(array, VisUI.getSkin().get(styleName, SimpleListAdapterStyle.class));
    }

    public SimpleListAdapter(Array<ItemT> array, SimpleListAdapterStyle style) {
        super(array);
        this.style = style;
    }

    @Override
    protected VisTable createView(ItemT item) {
        VisTable table = new VisTable();
        table.left();
        table.add(new VisLabel(item.toString()));
        return table;
    }

    @Override
    protected void selectView(VisTable view) {
        view.setBackground(style.selection);
    }

    @Override
    protected void deselectView(VisTable view) {
        view.setBackground(style.background);
    }

    public static class SimpleListAdapterStyle {
        public Drawable background;
        public Drawable selection;

        public SimpleListAdapterStyle() {
        }

        public SimpleListAdapterStyle(Drawable background, Drawable selection) {
            this.background = background;
            this.selection = selection;
        }

        public SimpleListAdapterStyle(SimpleListAdapterStyle style) {
            this.background = style.background;
            this.selection = style.selection;
        }
    }
}
