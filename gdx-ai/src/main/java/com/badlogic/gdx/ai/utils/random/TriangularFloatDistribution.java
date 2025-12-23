package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.math.MathUtils;

public final class TriangularFloatDistribution extends FloatDistribution {

    private final float low;
    private final float high;
    private final float mode;

    public TriangularFloatDistribution(float high) {
        this(-high, high);
    }

    public TriangularFloatDistribution(float low, float high) {
        this(low, high, (low + high) * .5f);
    }

    public TriangularFloatDistribution(float low, float high, float mode) {
        this.low = low;
        this.high = high;
        this.mode = mode;
    }

    @Override
    public float nextFloat() {
        if (-low == high && mode == 0) return MathUtils.randomTriangular(high); // It's faster
        return MathUtils.randomTriangular(low, high, mode);
    }

    public float getLow() {
        return low;
    }

    public float getHigh() {
        return high;
    }

    public float getMode() {
        return mode;
    }
}
