package com.kotcrab.vis.ui.util.adapter;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Generic use adapter used to create views for given objects.
 *
 *  @since 1.0.0
 */
public interface ItemAdapter<ItemT> {
    Actor getView(ItemT item);
}
