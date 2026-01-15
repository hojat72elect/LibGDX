package com.badlogic.gdx.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Info : This class was moved to Kerman game engine.
 * <p>
 * Removes an actor from the stage.
 */
public class RemoveActorAction extends Action {
    private boolean removed;

    public boolean act(float delta) {
        if (!removed) {
            removed = true;
            target.remove();
        }
        return true;
    }

    public void restart() {
        removed = false;
    }
}
