package com.mbrlabs.mundus.commons.scene3d.traversal;

import com.badlogic.gdx.utils.Queue;
import com.mbrlabs.mundus.commons.scene3d.GameObject;

import java.util.Iterator;

public class BreadthFirstIterator implements Iterator<GameObject> {

    private final Queue<GameObject> queue;

    public BreadthFirstIterator(GameObject root) {
        queue = new Queue<GameObject>();
        queue.addLast(root);
    }

    @Override
    public boolean hasNext() {
        return queue.size > 0;
    }

    @Override
    public GameObject next() {
        GameObject first = queue.removeFirst();
        if (first.getChildren() != null) {
            for (GameObject go : first.getChildren()) {
                queue.addLast(go);
            }
        }

        return first;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
