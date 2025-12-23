package com.badlogic.gdx.ai.tests.steer.bullet;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector3;

public class BulletLocation implements Location<Vector3> {

    Vector3 position;
    float orientation;

    public BulletLocation() {
        this.position = new Vector3();
        this.orientation = 0;
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public Location<Vector3> newLocation() {
        return new BulletLocation();
    }

    @Override
    public float vectorToAngle(Vector3 vector) {
        return BulletSteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector3 angleToVector(Vector3 outVector, float angle) {
        return BulletSteeringUtils.angleToVector(outVector, angle);
    }
}
