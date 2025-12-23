package com.badlogic.gdx.ai.tests.pfa.tests.tiled;

import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;

/**
 * Graph interface representing a generic tiled map.
 *
 * @param <N> Type of node, either flat or hierarchical, extending the {@link TiledNode} class
 */
public interface TiledGraph<N extends TiledNode<N>> extends IndexedGraph<N> {

    void init(int roomCount, int roomMinSize, int roomMaxSize, int squashIterations);

    N getNode(int x, int y);

    N getNode(int index);
}
