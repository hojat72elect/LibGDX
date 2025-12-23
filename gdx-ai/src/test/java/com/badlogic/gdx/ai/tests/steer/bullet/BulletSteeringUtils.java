package com.badlogic.gdx.ai.tests.steer.bullet;

import com.badlogic.gdx.math.Vector3;

public final class BulletSteeringUtils {

    private BulletSteeringUtils() {
    }

    public static float vectorToAngle(Vector3 vector) {
// return (float)Math.atan2(vector.z, vector.x);
        return (float) Math.atan2(-vector.z, vector.x);
    }

    public static Vector3 angleToVector(Vector3 outVector, float angle) {
// outVector.set(MathUtils.cos(angle), 0f, MathUtils.sin(angle));
        outVector.z = -(float) Math.sin(angle);
        outVector.y = 0;
        outVector.x = (float) Math.cos(angle);
        return outVector;
    }
}
