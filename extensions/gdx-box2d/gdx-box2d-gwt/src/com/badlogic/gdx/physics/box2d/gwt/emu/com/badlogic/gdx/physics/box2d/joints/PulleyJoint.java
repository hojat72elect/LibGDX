
package com.badlogic.gdx.physics.box2d.joints;

import org.jbox2d.common.Vec2;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;

/** The pulley joint is connected to two bodies and two fixed ground points. The pulley supports a ratio such that: length1 +
 * ratio * length2 <= constant Yes, the force transmitted is scaled by the ratio. The pulley also enforces a maximum length limit
 * on both sides. This is useful to prevent one side of the pulley hitting the top. */
public class PulleyJoint extends Joint {
	org.jbox2d.dynamics.joints.PulleyJoint joint;

	public PulleyJoint (World world, org.jbox2d.dynamics.joints.PulleyJoint joint) {
		super(world, joint);
		this.joint = joint;
	}

	/** Get the first ground anchor. */
	private final Vector2 groundAnchorA = new Vector2();

	public Vector2 getGroundAnchorA () {
		Vec2 g = joint.getGroundAnchorA();
		return groundAnchorA.set(g.x, g.y);
	}

	/** Get the second ground anchor. */
	private final Vector2 groundAnchorB = new Vector2();

	public Vector2 getGroundAnchorB () {
		Vec2 g = joint.getGroundAnchorB();
		return groundAnchorB.set(g.x, g.y);
	}

	/** Get the current length of the segment attached to body1. */
	public float getLength1 () {
		return joint.getLength1();
	}

	/** Get the current length of the segment attached to body2. */
	public float getLength2 () {
		return joint.getLength2();
	}

	/** Get the pulley ratio. */
	public float getRatio () {
		return joint.getRatio();
	}
}
