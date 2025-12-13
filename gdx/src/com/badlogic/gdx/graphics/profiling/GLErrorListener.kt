package com.badlogic.gdx.graphics.profiling

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.GdxRuntimeException

/**
 * Listener for GL errors detected by [GLProfiler].
 */
interface GLErrorListener {
    /**
     * Put your error logging code here.
     * @see GLInterceptor.resolveErrorNumber
     */
    fun onError(error: Int)

    companion object {
        /**
         * Listener that will log using Gdx.app.error GL error name and GL function.
         */
        @JvmField
        val LOGGING_LISTENER: GLErrorListener = object : GLErrorListener {
            override fun onError(error: Int) {
                var place: String? = null
                try {
                    val stack = Thread.currentThread().stackTrace
                    for (i in stack.indices) {
                        if ("check" == stack[i].methodName) {
                            if (i + 1 < stack.size) {
                                val glMethod = stack[i + 1]
                                place = glMethod.methodName
                            }
                            break
                        }
                    }
                } catch (_: Exception) {
                }

                if (place != null) {
                    Gdx.app.error("GLProfiler", "Error " + GLInterceptor.resolveErrorNumber(error) + " from " + place)
                } else {
                    Gdx.app.error("GLProfiler", "Error " + GLInterceptor.resolveErrorNumber(error) + " at: ", Exception())
                    // This will capture current stack trace for logging, if possible
                }
            }
        }

        // Basic implementations
        /**
         * Listener that will throw a GdxRuntimeException with error name.
         */
        @JvmField
        val THROWING_LISTENER: GLErrorListener = object : GLErrorListener {
            override fun onError(error: Int) {
                throw GdxRuntimeException("GLProfiler: Got GL error " + GLInterceptor.resolveErrorNumber(error))
            }
        }
    }
}
