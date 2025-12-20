package com.kotcrab.vis.ui.util.adapter;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Built-in adapter implementation for {@link ArrayList}.
 *
 * @since 1.0.0
 */
public abstract class ArrayListAdapter<ItemT, ViewT extends Actor> extends AbstractListAdapter<ItemT, ViewT> {
    private final ArrayList<ItemT> array;

    public ArrayListAdapter(ArrayList<ItemT> array) {
        this.array = array;
    }

    @Override
    public Iterable<ItemT> iterable() {
        return array;
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public int indexOf(ItemT item) {
        return array.indexOf(item);
    }

    @Override
    public void add(ItemT element) {
        array.add(element);
        itemAdded(element);
    }

    @Override
    public ItemT get(int index) {
        return array.get(index);
    }

    @Override
    protected void sort(Comparator<ItemT> comparator) {
        Collections.sort(array, comparator);
    }

    // Delegates

    public ItemT set(int index, ItemT element) {
        ItemT res = array.set(index, element);
        itemsChanged();
        return res;
    }

    public void add(int index, ItemT element) {
        array.add(index, element);
        itemAdded(element);
    }

    public ItemT remove(int index) {
        ItemT res = array.remove(index);
        if (res != null) itemRemoved(res);
        return res;
    }

    public boolean remove(ItemT item) {
        boolean res = array.remove(item);
        if (res) itemRemoved(item);
        return res;
    }

    public void clear() {
        array.clear();
        itemsChanged();
    }

    public boolean addAll(Collection<? extends ItemT> c) {
        boolean res = array.addAll(c);
        itemsChanged();
        return res;
    }

    public boolean addAll(int index, Collection<? extends ItemT> c) {
        boolean res = array.addAll(index, c);
        itemsChanged();
        return res;
    }

    public boolean removeAll(Collection<?> c) {
        boolean res = array.removeAll(c);
        itemsChanged();
        return res;
    }
}
