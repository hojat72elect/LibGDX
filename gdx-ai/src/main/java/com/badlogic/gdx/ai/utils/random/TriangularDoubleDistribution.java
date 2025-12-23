package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.math.MathUtils;

public final class TriangularDoubleDistribution extends DoubleDistribution {

    private final double low;
    private final double high;
    private final double mode;

    public TriangularDoubleDistribution(double high) {
        this(-high, high);
    }

    public TriangularDoubleDistribution(double low, double high) {
        this(low, high, (low + high) * .5);
    }

    public TriangularDoubleDistribution(double low, double high, double mode) {
        this.low = low;
        this.high = high;
        this.mode = mode;
    }

    /**
     * Returns a triangularly distributed random number between {@code -high} (exclusive) and {@code high} (exclusive), where values
     * around zero are more likely.
     * <p>
     * This is an optimized version of {@link #randomTriangular(float, float, float) randomTriangular(-high, high, 0)}
     *
     * @param high the upper limit
     */
    static double randomTriangular(double high) {
        return (MathUtils.random.nextDouble() - MathUtils.random.nextDouble()) * high;
    }

    /**
     * Returns a triangularly distributed random number between {@code low} (inclusive) and {@code high} (exclusive), where values
     * around {@code mode} are more likely.
     *
     * @param low  the lower limit
     * @param high the upper limit
     * @param mode the point around which the values are more likely
     */
    static double randomTriangular(double low, double high, double mode) {
        double u = MathUtils.random.nextDouble();
        double d = high - low;
        if (u <= (mode - low) / d) return low + Math.sqrt(u * d * (mode - low));
        return high - Math.sqrt((1 - u) * d * (high - mode));
    }

    @Override
    public double nextDouble() {
        if (-low == high && mode == 0) return randomTriangular(high); // It's faster
        return randomTriangular(low, high, mode);
    }

    public double getLow() {
        return low;
    }

    public double getHigh() {
        return high;
    }

    public double getMode() {
        return mode;
    }
}
