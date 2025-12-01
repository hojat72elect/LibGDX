
package com.badlogic.gdx.physics.box2d.joints;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointDef;

public class MotorJointDef extends JointDef {

	public MotorJointDef () {
		type = JointType.MotorJoint;
	}

	/** Initialize the bodies and offsets using the current transforms. */
	public void initialize (Body body1, Body body2) {
		this.bodyA = body1;
		this.bodyB = body2;
		this.linearOffset.set(bodyA.getLocalPoint(bodyB.getPosition()));
		this.angularOffset = bodyB.getAngle() - bodyA.getAngle();
	}

	/** Position of bodyB minus the position of bodyA, in bodyA's frame, in meters. */
	public final Vector2 linearOffset = new Vector2();

	/** The bodyB angle minus bodyA angle in radians. */
	public float angularOffset = 0.0f;

	/** The maximum motor force in N. */
	public float maxForce = 1.0f;

	/** The maximum motor torque in N-m. */
	public float maxTorque = 1.0f;

	/** Position correction factor in the range [0,1]. */
	public float correctionFactor = 0.3f;

	@Override
	public org.jbox2d.dynamics.joints.JointDef toJBox2d () {
		org.jbox2d.dynamics.joints.MotorJointDef jd = new org.jbox2d.dynamics.joints.MotorJointDef();
		jd.bodyA = bodyA.body;
		jd.bodyB = bodyB.body;
		jd.collideConnected = collideConnected;
		jd.linearOffset.set(linearOffset.x, linearOffset.y);
		jd.angularOffset = angularOffset;
		jd.maxForce = maxForce;
		jd.maxTorque = maxTorque;
		jd.type = org.jbox2d.dynamics.joints.JointType.MOTOR;
		return jd;
	}

}
