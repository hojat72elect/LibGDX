package com.badlogic.gdx.ai.steer;

import com.badlogic.gdx.ai.steer.Proximity.ProximityCallback;
import com.badlogic.gdx.math.Vector;

/**
 * {@code GroupBehavior} is the base class for the steering behaviors that take into consideration the agents in the game world
 * that are within the immediate area of the owner. This immediate area is defined by a {@link Proximity} that is in charge of
 * finding and processing the owner's neighbors through the given {@link ProximityCallback}.
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 */
public abstract class GroupBehavior<T extends Vector<T>> extends SteeringBehavior<T> {

    /**
     * The proximity decides which agents are considered neighbors.
     */
    protected Proximity<T> proximity;

    /**
     * Creates a GroupBehavior for the specified owner and proximity.
     *
     * @param owner     the owner of this behavior.
     * @param proximity the proximity to detect the owner's neighbors
     */
    public GroupBehavior(Steerable<T> owner, Proximity<T> proximity) {
        super(owner);
        this.proximity = proximity;
    }

    /**
     * Returns the proximity of this group behavior
     */
    public Proximity<T> getProximity() {
        return proximity;
    }

    /**
     * Sets the proximity of this group behavior
     *
     * @param proximity the proximity to set
     */
    public void setProximity(Proximity<T> proximity) {
        this.proximity = proximity;
    }
}
