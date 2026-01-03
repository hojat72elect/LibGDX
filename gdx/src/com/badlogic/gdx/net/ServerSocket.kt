package com.badlogic.gdx.net

import com.badlogic.gdx.Net.Protocol
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.GdxRuntimeException

/**
 * Info : Moved this interface to Kerman game engine.
 *
 * A server socket that accepts new incoming connections, returning [Socket] instances. The [.accept]
 * method should preferably be called in a separate thread as it is blocking.
 */
interface ServerSocket : Disposable {

    /**
     * @return the Protocol used by this socket
     */
    fun getProtocol(): Protocol

    /**
     * Accepts a new incoming connection from a client [Socket]. The given hints will be applied to the accepted socket.
     * Blocking, call on a separate thread.
     *
     * @param hints additional [SocketHints] applied to the accepted [Socket]. Input null to use the default setting
     * provided by the system.
     * @return the accepted [Socket]
     * @throws GdxRuntimeException in case an error occurred
     */
    fun accept(hints: SocketHints): Socket
}
