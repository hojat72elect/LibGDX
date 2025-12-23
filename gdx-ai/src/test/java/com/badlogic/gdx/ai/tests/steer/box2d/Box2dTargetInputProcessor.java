package com.badlogic.gdx.ai.tests.steer.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.tests.steer.scene2d.SteeringActor;
import com.badlogic.gdx.math.Vector2;

/**
 * An {@link InputProcessor} that allows you to manually move a {@link SteeringActor}.
 *
 * @autor davebaol
 */
public class Box2dTargetInputProcessor extends InputAdapter {
    protected Box2dSteeringEntity target;

    public Box2dTargetInputProcessor(Box2dSteeringEntity target) {
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
        screenY = Gdx.graphics.getHeight() - screenY;
        pos.x = Box2dSteeringTest.pixelsToMeters(screenX);
        pos.y = Box2dSteeringTest.pixelsToMeters(screenY);
        target.getBody().setTransform(pos, target.body.getAngle());
    }
}
