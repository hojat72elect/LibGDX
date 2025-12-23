package com.badlogic.gdx.ai;

public class DefaultTimepiece implements Timepiece {

    private float time;
    private float deltaTime;
    private final float maxDeltaTime;

    public DefaultTimepiece() {
        this(Float.POSITIVE_INFINITY);
    }

    public DefaultTimepiece(float maxDeltaTime) {
        this.time = 0f;
        this.deltaTime = 0f;
        this.maxDeltaTime = maxDeltaTime;
    }

    @Override
    public float getTime() {
        return time;
    }

    @Override
    public float getDeltaTime() {
        return deltaTime;
    }

    @Override
    public void update(float deltaTime) {
        this.deltaTime = (deltaTime > maxDeltaTime ? maxDeltaTime : deltaTime);
        this.time += this.deltaTime;
    }
}
