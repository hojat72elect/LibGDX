package com.badlogic.gdx.scenes.scene2d.actions;

/**
 * Info : This abstract class was moved to Kerman game engine.
 * <p>
 * Base class for actions that transition over time using the percent complete since the last frame.
 */
abstract public class RelativeTemporalAction extends TemporalAction {
    private float lastPercent;

    protected void begin() {
        lastPercent = 0;
    }

    protected void update(float percent) {
        updateRelative(percent - lastPercent);
        lastPercent = percent;
    }

    abstract protected void updateRelative(float percentDelta);
}
