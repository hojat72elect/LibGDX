package com.badlogic.gdx.ai.utils.random;

import com.badlogic.gdx.math.MathUtils;

public final class GaussianFloatDistribution extends FloatDistribution {

    public static final GaussianFloatDistribution STANDARD_NORMAL = new GaussianFloatDistribution(0, 1);

    private final float mean;
    private final float standardDeviation;

    public GaussianFloatDistribution(float mean, float standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    @Override
    public float nextFloat() {
        return mean + (float) MathUtils.random.nextGaussian() * standardDeviation;
    }

    public float getMean() {
        return mean;
    }

    public float getStandardDeviation() {
        return standardDeviation;
    }
}
