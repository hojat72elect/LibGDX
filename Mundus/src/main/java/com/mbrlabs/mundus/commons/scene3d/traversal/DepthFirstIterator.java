package com.mbrlabs.mundus.commons.scene3d.traversal;

import com.mbrlabs.mundus.commons.scene3d.GameObject;

import java.util.Iterator;
import java.util.Stack;

public class DepthFirstIterator implements Iterator<GameObject> {

    private final Stack<GameObject> stack;

    public DepthFirstIterator(GameObject root) {
        stack = new Stack<GameObject>();
        stack.push(root);
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public GameObject next() {
        GameObject top = stack.pop();
        if (top.getChildren() != null) {
            for (GameObject child : top.getChildren()) {
                stack.push(child);
            }
        }

        return top;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
