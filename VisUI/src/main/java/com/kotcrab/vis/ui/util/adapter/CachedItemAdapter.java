package com.kotcrab.vis.ui.util.adapter;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Implementation of {@link ItemAdapter} that caches created views. Provides two methods that are called when new view
 * should be created and when old view should be updated (see {@link #createView(Object)} and {@link #updateView(Actor, Object)}).
 * Internal cache is not cleared automatically and obsolete entries must be removed manually.
 *
 * @since 1.0.0
 */
public abstract class CachedItemAdapter<ItemT, ViewT extends Actor> implements ItemAdapter<ItemT> {
    private final ObjectMap<ItemT, ViewT> views = new ObjectMap<ItemT, ViewT>();

    @Override
    public final ViewT getView(ItemT item) {
        ViewT view = views.get(item);

        if (view == null) {
            view = createView(item);
            if (view == null) throw new IllegalStateException("Returned view view can't be null");
            views.put(item, view);
        } else {
            updateView(view, item);
        }

        return view;
    }

    /**
     * @return internal views cache map
     */
    protected ObjectMap<ItemT, ViewT> getViews() {
        return views;
    }

    protected abstract ViewT createView(ItemT item);

    protected abstract void updateView(ViewT view, ItemT item);
}
