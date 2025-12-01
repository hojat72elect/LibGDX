
package com.badlogic.gdx.physics.box2d;

/** Contact impulses for reporting. Impulses are used instead of forces because sub-step forces may approach infinity for rigid
 * body collisions. These match up one-to-one with the contact points in b2Manifold.
 * @author mzechner */
public class ContactImpulse {
	org.jbox2d.callbacks.ContactImpulse impulse;
	float[] tmp = new float[2];
	final float[] normalImpulses = new float[2];
	final float[] tangentImpulses = new float[2];

	ContactImpulse () {
	}

	public float[] getNormalImpulses () {
        if (impulse.count >= 0) System.arraycopy(impulse.normalImpulses, 0, normalImpulses, 0, impulse.count);
		return normalImpulses;
	}

	public float[] getTangentImpulses () {
        if (impulse.count >= 0) System.arraycopy(impulse.tangentImpulses, 0, tangentImpulses, 0, impulse.count);
		return tangentImpulses;
	}

	public int getCount () {
		return impulse.count;
	}
}
