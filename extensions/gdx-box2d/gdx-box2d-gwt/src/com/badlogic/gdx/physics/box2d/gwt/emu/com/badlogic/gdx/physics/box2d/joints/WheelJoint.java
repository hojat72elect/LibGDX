
package com.badlogic.gdx.physics.box2d.joints;

import org.jbox2d.common.Vec2;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;

/** A wheel joint. This joint provides two degrees of freedom: translation along an axis fixed in body1 and rotation in the plane.
 * You can use a joint limit to restrict the range of motion and a joint motor to drive the rotation or to model rotational
 * friction. This joint is designed for vehicle suspensions. */
public class WheelJoint extends Joint {
	org.jbox2d.dynamics.joints.WheelJoint joint;

	Vector2 localAnchorA = new Vector2();
	Vector2 localAnchorB = new Vector2();
	Vector2 localAxisA = new Vector2();

	public WheelJoint (World world, org.jbox2d.dynamics.joints.WheelJoint joint) {
		super(world, joint);
		this.joint = joint;
	}

	public Vector2 getLocalAnchorA () {
		Vec2 localAnchor = joint.getLocalAnchorA();
		localAnchorA.set(localAnchor.x, localAnchor.y);
		return localAnchorA;
	}

	public Vector2 getLocalAnchorB () {
		Vec2 localAnchor = joint.getLocalAnchorB();
		localAnchorB.set(localAnchor.x, localAnchor.y);
		return localAnchorB;
	}

	public Vector2 getLocalAxisA () {
		Vec2 localAxis = joint.getLocalAxisA();
		localAxisA.set(localAxis.x, localAxis.y);
		return localAxisA;
	}

	/** Get the current joint translation, usually in meters. */
	public float getJointTranslation () {
		return joint.getJointTranslation();
	}

	/** Get the current joint translation speed, usually in meters per second. */
	public float getJointSpeed () {
		return joint.getJointSpeed();
	}

	/** Is the joint motor enabled? */
	public boolean isMotorEnabled () {
		return joint.isMotorEnabled();
	}

	/** Enable/disable the joint motor. */
	public void enableMotor (boolean flag) {
		joint.enableMotor(flag);
	}

	/** Set the motor speed, usually in radians per second. */
	public void setMotorSpeed (float speed) {
		joint.setMotorSpeed(speed);
	}

	/** Get the motor speed, usually in radians per second. */
	public float getMotorSpeed () {
		return joint.getMotorSpeed();
	}

	/** Set/Get the maximum motor force, usually in N-m. */
	public void setMaxMotorTorque (float torque) {
		joint.setMaxMotorTorque(torque);
	}

	public float getMaxMotorTorque () {
		return joint.getMaxMotorTorque();
	}

	/** Get the current motor torque given the inverse time step, usually in N-m. */
	public float getMotorTorque (float invDt) {
		return joint.getMotorTorque(invDt);
	}

	/** Set/Get the spring frequency in hertz. Setting the frequency to zero disables the spring. */
	public void setSpringFrequencyHz (float hz) {
		joint.setSpringFrequencyHz(hz);
	}

	public float getSpringFrequencyHz () {
		return joint.getSpringFrequencyHz();
	}

	/** Set/Get the spring damping ratio */
	public void setSpringDampingRatio (float ratio) {
		joint.setSpringDampingRatio(ratio);
	}

	public float getSpringDampingRatio () {
		return joint.getSpringDampingRatio();
	}

}
