package com.badlogic.gdx.ai.pfa;

/**
 * A {@code HierarchicalGraph} is a multilevel graph that can be traversed by a {@link HierarchicalPathFinder} at any level of its
 * hierarchy.
 *
 * @param <N> Type of node
 */
public interface HierarchicalGraph<N> extends Graph<N> {

    /**
     * Returns the number of levels in this hierarchical graph.
     */
    int getLevelCount();

    /**
     * Switches the graph into the given level so all future calls to the {@link #getConnections(Object) getConnections} methods
     * act as if the graph was just a simple, non-hierarchical graph at that level.
     *
     * @param level the level to set
     */
    void setLevel(int level);

    /**
     * Converts the node at the input level into a node at the output level.
     *
     * @param inputLevel  the input level
     * @param node        the node at the input level
     * @param outputLevel the output level
     * @return the node at the output level.
     */
    N convertNodeBetweenLevels(int inputLevel, N node, int outputLevel);
}
