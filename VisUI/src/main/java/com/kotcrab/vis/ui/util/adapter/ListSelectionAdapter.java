package com.kotcrab.vis.ui.util.adapter;

import com.kotcrab.vis.ui.util.adapter.AbstractListAdapter.ListSelectionListener;

/**
 * Empty {@link ListSelectionListener} implementation.
 */
public class ListSelectionAdapter<ItemT, ViewT> implements ListSelectionListener<ItemT, ViewT> {
    @Override
    public void selected(ItemT item, ViewT view) {

    }

    @Override
    public void deselected(ItemT item, ViewT view) {

    }
}
