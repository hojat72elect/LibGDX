package com.badlogic.gdx.ai.utils.random;

public abstract class LongDistribution implements Distribution {

    @Override
    public int nextInt() {
        return (int) nextLong();
    }

    @Override
    public float nextFloat() {
        return (float) nextLong();
    }

    @Override
    public double nextDouble() {
        return (double) nextLong();
    }
}
