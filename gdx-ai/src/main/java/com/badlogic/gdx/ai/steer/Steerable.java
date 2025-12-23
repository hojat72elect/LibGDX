package com.badlogic.gdx.ai.steer;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

/**
 * A {@code Steerable} is a {@link Location} that gives access to the character's data required by steering system.
 * <p>
 * Notice that there is nothing to connect the direction that a Steerable is moving and the direction it is facing. For
 * instance, a character can be oriented along the x-axis but be traveling directly along the y-axis.
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 */
public interface Steerable<T extends Vector<T>> extends Location<T>, Limiter {

    /**
     * Returns the vector indicating the linear velocity of this Steerable.
     */
    T getLinearVelocity();

    /**
     * Returns the float value indicating the the angular velocity in radians of this Steerable.
     */
    float getAngularVelocity();

    /**
     * Returns the bounding radius of this Steerable.
     */
    float getBoundingRadius();

    /**
     * Returns {@code true} if this Steerable is tagged; {@code false} otherwise.
     */
    boolean isTagged();

    /**
     * Tag/untag this Steerable. This is a generic flag utilized in a variety of ways.
     *
     * @param tagged the boolean value to set
     */
    void setTagged(boolean tagged);
}
