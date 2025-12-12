package com.badlogic.gdx.net

import com.badlogic.gdx.Net
import com.badlogic.gdx.Net.Protocol
import com.badlogic.gdx.utils.Disposable
import java.io.InputStream
import java.io.OutputStream

/**
 * A client socket that talks to a server socket via some [Protocol]. See
 * [Net.newClientSocket] and [Net.newServerSocket].
 * A socket has an [InputStream] used to send data to the other end of the connection, and an [OutputStream] to
 * receive data from the other end of the connection.
 * A socket needs to be disposed if it is no longer used. Disposing also closes the connection.
 */
interface Socket : Disposable {

    /**
     * @return whether the socket is connected
     */
    fun isConnected(): Boolean

    /**
     * @return the [InputStream] used to read data from the other end of the connection.
     */
    fun getInputStream(): InputStream

    /**
     * @return the [OutputStream] used to write data to the other end of the connection.
     */
    fun getOutputStream(): OutputStream

    /**
     * @return the RemoteAddress of the Socket as String
     */
    fun getRemoteAddress(): String
}
