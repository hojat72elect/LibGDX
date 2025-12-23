package com.badlogic.gdx.ai.utils.random;

public final class TriangularLongDistribution extends LongDistribution {

    private final long low;
    private final long high;
    private final double mode;

    public TriangularLongDistribution(long high) {
        this(-high, high);
    }

    public TriangularLongDistribution(long low, long high) {
        this(low, high, (low + high) * .5);
    }

    public TriangularLongDistribution(long low, long high, double mode) {
        this.low = low;
        this.high = high;
        this.mode = mode;
    }

    @Override
    public long nextLong() {
        double r;
        if (-low == high && mode == 0)
            r = TriangularDoubleDistribution.randomTriangular(high); // It's faster
        else
            r = TriangularDoubleDistribution.randomTriangular(low, high, mode);
        return Math.round(r);
    }

    public long getLow() {
        return low;
    }

    public long getHigh() {
        return high;
    }

    public double getMode() {
        return mode;
    }
}
