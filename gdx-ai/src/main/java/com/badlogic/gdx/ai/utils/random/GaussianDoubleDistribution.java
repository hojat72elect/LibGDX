package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.math.MathUtils;

public final class GaussianDoubleDistribution extends DoubleDistribution {

    public static final GaussianDoubleDistribution STANDARD_NORMAL = new GaussianDoubleDistribution(0, 1);

    private final double mean;
    private final double standardDeviation;

    public GaussianDoubleDistribution(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    @Override
    public double nextDouble() {
        return mean + MathUtils.random.nextGaussian() * standardDeviation;
    }

    public double getMean() {
        return mean;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }
}
