package com.badlogic.gdx.ai.utils.random;

public abstract class IntegerDistribution implements Distribution {

    @Override
    public long nextLong() {
        return nextInt();
    }

    @Override
    public float nextFloat() {
        return (float) nextInt();
    }

    @Override
    public double nextDouble() {
        return nextInt();
    }
}
