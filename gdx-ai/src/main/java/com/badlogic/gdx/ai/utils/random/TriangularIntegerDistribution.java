package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.math.MathUtils;

public final class TriangularIntegerDistribution extends IntegerDistribution {

    private final int low;
    private final int high;
    private final float mode;

    public TriangularIntegerDistribution(int high) {
        this(-high, high);
    }

    public TriangularIntegerDistribution(int low, int high) {
        this(low, high, (low + high) * .5f);
    }

    public TriangularIntegerDistribution(int low, int high, float mode) {
        this.low = low;
        this.high = high;
        this.mode = mode;
    }

    @Override
    public int nextInt() {
        float r;
        if (-low == high && mode == 0)
            r = MathUtils.randomTriangular(high); // It's faster
        else
            r = MathUtils.randomTriangular(low, high, mode);
        return Math.round(r);
    }

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }

    public float getMode() {
        return mode;
    }
}
