package com.badlogic.gdx.utils.async

/**
 * Task to be submitted to an [AsyncExecutor], returning a result of type T.
 */
interface AsyncTask<T> {
    @Throws(Exception::class)
    fun call(): T
}
