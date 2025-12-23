package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.math.MathUtils;

public final class UniformDoubleDistribution extends DoubleDistribution {

    private final double low;
    private final double high;

    public UniformDoubleDistribution(double high) {
        this(0, high);
    }

    public UniformDoubleDistribution(double low, double high) {
        this.low = low;
        this.high = high;
    }

    public double nextDouble() {
        return low + MathUtils.random.nextDouble() * (high - low);
    }

    public double getLow() {
        return low;
    }

    public double getHigh() {
        return high;
    }
}
