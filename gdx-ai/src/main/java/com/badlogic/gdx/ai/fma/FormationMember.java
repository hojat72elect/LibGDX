package com.badlogic.gdx.ai.fma;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector;

/**
 * Game characters coordinated by a {@link Formation} must implement this interface. Any {@code FormationMember} has a target
 * location which is the place where it should be in order to stay in formation. This target location is calculated by the
 * formation itself.
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 */
public interface FormationMember<T extends Vector<T>> {

    /**
     * Returns the target location of this formation member.
     */
    Location<T> getTargetLocation();
}
