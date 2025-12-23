package com.badlogic.gdx.ai.utils.random;

public final class ConstantDoubleDistribution extends DoubleDistribution {

    public static final ConstantDoubleDistribution NEGATIVE_ONE = new ConstantDoubleDistribution(-1);
    public static final ConstantDoubleDistribution ZERO = new ConstantDoubleDistribution(0);
    public static final ConstantDoubleDistribution ONE = new ConstantDoubleDistribution(1);

    private final double value;

    public ConstantDoubleDistribution(double value) {
        this.value = value;
    }

    @Override
    public double nextDouble() {
        return value;
    }

    public double getValue() {
        return value;
    }
}
