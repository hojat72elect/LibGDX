package com.badlogic.gdx.utils.async

/**
 * Info : This interface was moved to Kerman game engine.
 *
 * Task to be submitted to an [AsyncExecutor], returning a result of type T.
 */
interface AsyncTask<T> {
    @Throws(Exception::class)
    fun call(): T
}
