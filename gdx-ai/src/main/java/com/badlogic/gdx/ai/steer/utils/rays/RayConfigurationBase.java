package com.badlogic.gdx.ai.steer.utils.rays;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.utils.RayConfiguration;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector;

/**
 * {@code RayConfigurationBase} is the base class for concrete ray configurations having a fixed number of rays.
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 */
public abstract class RayConfigurationBase<T extends Vector<T>> implements RayConfiguration<T> {

    protected Steerable<T> owner;
    protected Ray<T>[] rays;

    /**
     * Creates a {@code RayConfigurationBase} for the given owner and the specified number of rays.
     *
     * @param owner   the owner of this configuration
     * @param numRays the number of rays used by this configuration
     */
    @SuppressWarnings("unchecked")
    public RayConfigurationBase(Steerable<T> owner, int numRays) {
        this.owner = owner;
        this.rays = new Ray[numRays];
        for (int i = 0; i < numRays; i++)
            this.rays[i] = new Ray<T>(owner.getPosition().cpy().setZero(), owner.getPosition().cpy().setZero());
    }

    /**
     * Returns the owner of this configuration.
     */
    public Steerable<T> getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this configuration.
     */
    public void setOwner(Steerable<T> owner) {
        this.owner = owner;
    }

    /**
     * Returns the rays of this configuration.
     */
    public Ray<T>[] getRays() {
        return rays;
    }

    /**
     * Sets the rays of this configuration.
     */
    public void setRays(Ray<T>[] rays) {
        this.rays = rays;
    }
}
