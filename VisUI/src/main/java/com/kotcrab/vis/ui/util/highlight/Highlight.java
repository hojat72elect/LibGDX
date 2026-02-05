package com.kotcrab.vis.ui.util.highlight;

import com.badlogic.gdx.graphics.Color;

/**
 * Represents single highlight.
 *
 * @since 1.1.2
 */
public record Highlight(Color color, int start, int end) implements Comparable<Highlight> {
    public Highlight {
        if (color == null) throw new IllegalArgumentException("color can't be null");
        if (start >= end) throw new IllegalArgumentException("start can't be >= end: " + start + " >= " + end);
    }

    @Override
    public int compareTo(Highlight o) {
        return start() - o.start();
    }
}
