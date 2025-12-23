package com.badlogic.gdx.ai.pfa;

import com.badlogic.gdx.utils.Array;

/**
 * A graph is a collection of nodes, each one having a collection of outgoing {@link Connection connections}.
 *
 * @param <N> Type of node
 */
public interface Graph<N> {

    /**
     * Returns the connections outgoing from the given node.
     *
     * @param fromNode the node whose outgoing connections will be returned
     * @return the array of connections outgoing from the given node.
     */
    Array<Connection<N>> getConnections(N fromNode);
}
