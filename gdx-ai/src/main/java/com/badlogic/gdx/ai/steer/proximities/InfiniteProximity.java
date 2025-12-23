package com.badlogic.gdx.ai.steer.proximities;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector;

/**
 * {@code InfiniteProximity} is likely the simplest type of Proximity one can imagine. All the agents contained in the specified
 * list are considered neighbors of the owner, excluded the owner itself (if it is part of the list).
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 */
public class InfiniteProximity<T extends Vector<T>> extends ProximityBase<T> {

    /**
     * Creates a {@code InfiniteProximity} for the specified owner and list of agents.
     *
     * @param owner  the owner of this proximity
     * @param agents the list of agents
     */
    public InfiniteProximity(Steerable<T> owner, Iterable<? extends Steerable<T>> agents) {
        super(owner, agents);
    }

    @Override
    public int findNeighbors(ProximityCallback<T> callback) {
        int neighborCount = 0;
        for (Steerable<T> currentAgent : agents) {
            // Make sure the agent being examined isn't the owner
            if (currentAgent != owner) {
                if (callback.reportNeighbor(currentAgent)) {
                    neighborCount++;
                }
            }
        }

        return neighborCount;
    }
}
