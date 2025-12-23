package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.math.MathUtils;

public final class UniformFloatDistribution extends FloatDistribution {

    private final float low;
    private final float high;

    public UniformFloatDistribution(float high) {
        this(0, high);
    }

    public UniformFloatDistribution(float low, float high) {
        this.low = low;
        this.high = high;
    }

    public float nextFloat() {
        return MathUtils.random(low, high);
    }

    public float getLow() {
        return low;
    }

    public float getHigh() {
        return high;
    }
}
