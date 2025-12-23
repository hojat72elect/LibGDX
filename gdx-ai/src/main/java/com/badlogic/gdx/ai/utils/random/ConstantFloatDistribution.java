package com.badlogic.gdx.ai.utils.random;

public final class ConstantFloatDistribution extends FloatDistribution {

    public static final ConstantFloatDistribution NEGATIVE_ONE = new ConstantFloatDistribution(-1);
    public static final ConstantFloatDistribution ZERO = new ConstantFloatDistribution(0);
    public static final ConstantFloatDistribution ONE = new ConstantFloatDistribution(1);
    public static final ConstantFloatDistribution ZERO_POINT_FIVE = new ConstantFloatDistribution(.5f);

    private final float value;

    public ConstantFloatDistribution(float value) {
        this.value = value;
    }

    @Override
    public float nextFloat() {
        return value;
    }

    public float getValue() {
        return value;
    }
}
