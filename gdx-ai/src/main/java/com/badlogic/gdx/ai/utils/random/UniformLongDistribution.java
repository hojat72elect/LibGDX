package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.math.MathUtils;

public final class UniformLongDistribution extends LongDistribution {

    private final long low;
    private final long high;

    public UniformLongDistribution(long high) {
        this(0, high);
    }

    public UniformLongDistribution(long low, long high) {
        this.low = low;
        this.high = high;
    }

    @Override
    public long nextLong() {
        return low + (long) (MathUtils.random.nextDouble() * (high - low));
    }

    public long getLow() {
        return low;
    }

    public long getHigh() {
        return high;
    }
}
