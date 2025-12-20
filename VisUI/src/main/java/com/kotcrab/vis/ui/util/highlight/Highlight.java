package com.kotcrab.vis.ui.util.highlight;

import com.badlogic.gdx.graphics.Color;

/**
 * Represents single highlight.
 *
 * @since 1.1.2
 */
public class Highlight implements Comparable<Highlight> {
    private final Color color;
    private final int start;
    private final int end;

    public Highlight(Color color, int start, int end) {
        if (color == null) throw new IllegalArgumentException("color can't be null");
        if (start >= end) throw new IllegalArgumentException("start can't be >= end: " + start + " >= " + end);
        this.color = color;
        this.start = start;
        this.end = end;
    }

    public Color getColor() {
        return color;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public int compareTo(Highlight o) {
        return getStart() - o.getStart();
    }
}
