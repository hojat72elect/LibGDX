package com.badlogic.gdx.ai.steer.proximities;

import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector;

/**
 * {@code ProximityBase} is the base class for any concrete proximity based on an iterable collection of agents.
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 */
public abstract class ProximityBase<T extends Vector<T>> implements Proximity<T> {

    /**
     * The owner of  this proximity.
     */
    protected Steerable<T> owner;

    /**
     * The collection of the agents handled by this proximity.
     * <p>
     * Note that, being this field of type {@code Iterable}, you can either use java or libgdx collections. See
     * https://github.com/libgdx/gdx-ai/issues/65
     */
    protected Iterable<? extends Steerable<T>> agents;

    /**
     * Creates a {@code ProximityBase} for the specified owner and list of agents.
     *
     * @param owner  the owner of this proximity
     * @param agents the list of agents
     */
    public ProximityBase(Steerable<T> owner, Iterable<? extends Steerable<T>> agents) {
        this.owner = owner;
        this.agents = agents;
    }

    @Override
    public Steerable<T> getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Steerable<T> owner) {
        this.owner = owner;
    }

    /**
     * Returns the the agents that represent potential neighbors.
     */
    public Iterable<? extends Steerable<T>> getAgents() {
        return agents;
    }

    /**
     * Sets the agents that represent potential neighbors.
     */
    public void setAgents(Iterable<Steerable<T>> agents) {
        this.agents = agents;
    }
}
