package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * Info : This class was moved to Kerman game engine.
 * <p>
 * Sets the actor's {@link Actor#setTouchable(Touchable) touchability}.
 */
public class TouchableAction extends Action {
    private Touchable touchable;

    public boolean act(float delta) {
        target.setTouchable(touchable);
        return true;
    }

    public Touchable getTouchable() {
        return touchable;
    }

    public void setTouchable(Touchable touchable) {
        this.touchable = touchable;
    }
}
