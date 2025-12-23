package com.badlogic.gdx.ai.steer.behaviors;

import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

/**
 * {@code Flee} behavior does the opposite of {@link Seek}. It produces a linear steering force that moves the agent away from a
 * target position.
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 *  */
public class Flee<T extends Vector<T>> extends Seek<T> {

    /**
     * Creates a {@code Flee} behavior for the specified owner.
     *
     * @param owner the owner of this behavior.
     */
    public Flee(Steerable<T> owner) {
        this(owner, null);
    }

    /**
     * Creates a {@code Flee} behavior for the specified owner and target.
     *
     * @param owner  the owner of this behavior
     * @param target the target agent of this behavior.
     */
    public Flee(Steerable<T> owner, Location<T> target) {
        super(owner, target);
    }

    @Override
    protected SteeringAcceleration<T> calculateRealSteering(SteeringAcceleration<T> steering) {
        // We just do the opposite of seek, i.e. (owner.getPosition() - target.getPosition())
        // instead of (target.getPosition() - owner.getPosition())
        steering.linear.set(owner.getPosition()).sub(target.getPosition()).nor().scl(getActualLimiter().getMaxLinearAcceleration());

        // No angular acceleration
        steering.angular = 0;

        // Output steering acceleration
        return steering;
    }

    //
    // Setters overridden in order to fix the correct return type for chaining
    //

    @Override
    public Flee<T> setOwner(Steerable<T> owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public Flee<T> setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Sets the limiter of this steering behavior. The given limiter must at least take care of the maximum linear acceleration.
     *
     * @return this behavior for chaining.
     */
    @Override
    public Flee<T> setLimiter(Limiter limiter) {
        this.limiter = limiter;
        return this;
    }

    @Override
    public Flee<T> setTarget(Location<T> target) {
        this.target = target;
        return this;
    }
}
