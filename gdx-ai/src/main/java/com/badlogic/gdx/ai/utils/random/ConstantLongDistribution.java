package com.badlogic.gdx.ai.utils.random;

public final class ConstantLongDistribution extends LongDistribution {

    public static final ConstantLongDistribution NEGATIVE_ONE = new ConstantLongDistribution(-1);
    public static final ConstantLongDistribution ZERO = new ConstantLongDistribution(0);
    public static final ConstantLongDistribution ONE = new ConstantLongDistribution(1);

    private final long value;

    public ConstantLongDistribution(long value) {
        this.value = value;
    }

    @Override
    public long nextLong() {
        return value;
    }

    public long getValue() {
        return value;
    }
}
