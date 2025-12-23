package com.badlogic.gdx.ai.steer.utils;

import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector;

/**
 * A {@code RayConfiguration} is a collection of rays typically used for collision avoidance.
 *
 * @param <T> Type of vector, either 2D or 3D, implementing the {@link Vector} interface
 */
public interface RayConfiguration<T extends Vector<T>> {
    Ray<T>[] updateRays();
}
