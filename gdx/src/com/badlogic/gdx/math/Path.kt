package com.badlogic.gdx.math

/**
 * Interface that specifies a path of type T within the window 0.0<=t<=1.0.
 */
interface Path<T> {

    fun derivativeAt(out: T, t: Float): T

    /**
     * @return The value of the path at t where 0<=t<=1
     */
    fun valueAt(out: T, t: Float): T

    /**
     * @return The approximated value (between 0 and 1) on the path which is closest to the specified value. Note that the
     * implementation of this method might be optimized for speed against precision, see [.locate] for a more
     * precise (but more intensive) method.
     */
    fun approximate(v: T): Float

    /**
     * @return The precise location (between 0 and 1) on the path which is closest to the specified value. Note that the
     * implementation of this method might be CPU intensive, see [.approximate] for a faster (but less
     * precise) method.
     */
    fun locate(v: T): Float

    /**
     * @param samples The amount of divisions used to approximate length. Higher values will produce more precise results, but
     * will be more CPU intensive.
     * @return An approximated length of the spline through sampling the curve and accumulating the euclidean distances between the
     * sample points.
     */
    fun approxLength(samples: Int): Float
}
