package com.badlogic.gdx.ai.utils.random;

public abstract class DoubleDistribution implements Distribution {

    @Override
    public int nextInt() {
        return (int) nextDouble();
    }

    @Override
    public long nextLong() {
        return (long) nextDouble();
    }

    @Override
    public float nextFloat() {
        return (float) nextDouble();
    }
}
