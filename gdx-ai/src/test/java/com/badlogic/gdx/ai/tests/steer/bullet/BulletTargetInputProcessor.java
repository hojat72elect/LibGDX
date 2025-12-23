package com.badlogic.gdx.ai.tests.steer.bullet;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * An {@link InputProcessor} that allows you to manually move a {@link SteeringBulletEntity}.
 */
public class BulletTargetInputProcessor extends InputAdapter {

    private static final Collision<Vector3> output = new Collision<Vector3>(new Vector3(), new Vector3());
    private static final Vector3 rayFrom = new Vector3();
    private static final Vector3 rayTo = new Vector3();
    private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);
    SteeringBulletEntity target;
    Viewport viewport;
    btCollisionWorld world;
    Vector3 offset;
    boolean moveTarget = false;

    public BulletTargetInputProcessor(SteeringBulletEntity target, Vector3 offset, Viewport viewport, btCollisionWorld world) {
        this.target = target;
        this.viewport = viewport;
        this.world = world;
        this.offset = offset;
    }

    public boolean keyDown(int keycode) {
        if (keycode == Keys.SPACE) {
            moveTarget = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.SPACE) {
            moveTarget = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        setTargetPosition(screenX, screenY);
        return moveTarget;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        setTargetPosition(screenX, screenY);
        return moveTarget;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return setTargetPosition(screenX, screenY);
    }

    private boolean setTargetPosition(int screenX, int screenY) {
        if (moveTarget) {
            Ray pickRay = viewport.getPickRay(screenX, screenY);
            btCollisionObject body = rayTest(output, pickRay);

            if (body != null && body.userData != null && body.userData.equals("ground")) {
                target.transform.setToTranslation(output.point.add(offset));
                target.body.setWorldTransform(target.transform);
            }
            return true;
        }
        return false;
    }

    private btCollisionObject rayTest(Collision<Vector3> output, Ray ray) {
        rayFrom.set(ray.origin);
        // 500 meters max from the origin
        rayTo.set(ray.direction).scl(500f).add(rayFrom);

        // we reuse the ClosestRayResultCallback, thus we need to reset its
        // values
        callback.setCollisionObject(null);
        callback.setClosestHitFraction(1f);
        callback.setRayFromWorld(rayFrom);
        callback.setRayToWorld(rayTo);

        world.rayTest(rayFrom, rayTo, callback);

        if (callback.hasHit()) {
            callback.getHitPointWorld(output.point);
            callback.getHitNormalWorld(output.normal);
            return callback.getCollisionObject();
        }

        return null;
    }
}
