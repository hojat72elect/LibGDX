package com.badlogic.gdx.ai.pfa.indexed;

import com.badlogic.gdx.ai.pfa.Graph;

/**
 * A graph for the {@link IndexedAStarPathFinder}.
 *
 * @param <N> Type of node
 */
public interface IndexedGraph<N> extends Graph<N> {

    /**
     * Returns the unique index of the given node.
     *
     * @param node the node whose index will be returned
     * @return the unique index of the given node.
     */
    int getIndex(N node);

    /**
     * Returns the number of nodes in this graph.
     */
    int getNodeCount();
}
