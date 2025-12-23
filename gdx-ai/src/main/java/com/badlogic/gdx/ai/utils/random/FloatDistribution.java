package com.badlogic.gdx.ai.utils.random;

public abstract class FloatDistribution implements Distribution {

    @Override
    public int nextInt() {
        return (int) nextFloat();
    }

    @Override
    public long nextLong() {
        return (long) nextFloat();
    }

    @Override
    public double nextDouble() {
        return nextFloat();
    }
}
