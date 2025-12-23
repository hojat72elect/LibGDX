package com.badlogic.gdx.ai.tests.steer.scene2d;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

/**
 * An {@link InputProcessor} that allows you to manually move a {@link SteeringActor}.
 *
 * @autor davebaol
 */
public class Scene2dTargetInputProcessor extends InputAdapter {
    protected SteeringActor target;

    public Scene2dTargetInputProcessor(SteeringActor target) {
        this.target = target;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        setTargetPosition(screenX, screenY);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        setTargetPosition(screenX, screenY);
        return true;
    }

    protected void setTargetPosition(int screenX, int screenY) {
        Vector2 pos = target.getPosition();
        target.getStage().screenToStageCoordinates(pos.set(screenX, screenY));
        target.getParent().stageToLocalCoordinates(pos);
        target.setPosition(pos.x, pos.y, Align.center);
    }
}
